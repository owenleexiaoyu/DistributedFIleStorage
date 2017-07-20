package com.owen.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Properties;

import com.owen.server.FileServer;
import com.owen.utils.MyUtils;

public class FileClient implements FileOperation{
	RemoteFileOperation fileOperation;
	private Socket socket;//�ͻ��˵��׽���
	private DataInputStream dis;//Socket��������
	private DataOutputStream dos;//socket�������
	
	public FileClient() {
		//��ȡFileServer�������ļ�����ȡFileServer��IP��ַ�Ͷ˿ں�
		Properties properties = MyUtils.getProperty("fileserver.properties");
		String fileServerIP = properties.getProperty("fileServerIP");
		int fileServerPort = Integer.parseInt(properties.getProperty("fileServerPort"));
		//������FileServer��Socket����
		try {
			socket = new Socket(fileServerIP,fileServerPort);
			//��ȡSocket���������������
			if(socket != null){
				dis = new DataInputStream(socket.getInputStream());
				dos = new DataOutputStream(socket.getOutputStream());
			}			
		} catch (ConnectException e) {
			System.out.println("������δ����");
		}catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * �ͻ����ϴ��ļ���RemoteFileOperation����ýڵ�����ļ�����
	 */
	@Override
	public String update(String fileName) {
		//���������û������ֱ���ϴ�ʧ��
		if(socket ==null){
			return null;
		}
		File f = new File(fileName);
		String uuid = null;
		String tempId = null;
		try {
			dos.writeInt(1);
			dos.writeUTF(f.getName());
			dos.writeLong(f.length());
			dos.flush();
			if(!dis.readBoolean()){
				System.out.println(dis.readUTF());
				return null;
			}
			//��ȡ���ļ������洢���ͱ��ݽ���IP��ַ�Ͷ˿ں�,�ļ���
			String address1 = dis.readUTF();
			int port1 = dis.readInt();
			String nodefolder1 = dis.readUTF();
			System.out.println(address1+" "+port1+" "+nodefolder1);
			String address2 = dis.readUTF();
			int port2 = dis.readInt();
			String nodefolder2 = dis.readUTF();
			uuid = dis.readUTF();
			tempId = uuid;
			System.out.println(address2+" "+port2+" "+nodefolder2);
			//����һ��RemoteFileOperation����
			fileOperation = new RemoteFileOperation(address1, port1);
			//����fileOperation��update�������ӿͻ������㷢���ļ�����
			uuid = fileOperation.update(fileName+"@"+uuid+"@"+nodefolder1+"@"+nodefolder2);
			//�ϴ�ʧ��,֪ͨ������ɾ����¼
			if(uuid == null){
				dos.writeInt(4);
				dos.writeUTF(tempId);
				System.out.println("�ϴ�ʧ��,֪ͨ������ɾ����¼");
			}
			fileOperation = null;
		} catch (SocketException e) {
			System.out.println("������δ��������ֹ���");
		}catch (IOException e) {
			e.printStackTrace();
		}
		return uuid;
	}
	/**
	 * �ͻ���Ҫ�������ļ�����FileServer�л�ȡ���洢���ͱ��ݽ�����Ϣ
	 * �����洢��������ļ���������洢���ʧЧ����ӱ��ݽ������
	 */
	@Override
	public boolean download(String uuid) {
		//���������û������ֱ������ʧ��
		if(socket ==null){
			return false;
		}
		//�ļ�������
		String fileName;
		//�ļ��Ľ����Ϣ
		String address1 = null;
		String address2 = null;
		int port1 = 0;
		int port2 = 0;
		String nodefolder1 = null;
		String nodefolder2 = null;
		try {
			//��FileServer�������ص�ָ��
			dos.writeInt(2);
			//�����ļ���uuid
			dos.writeUTF(uuid);
			dos.flush();
			//�ļ�����
			if(dis.readBoolean()){
				//��ȡ���ļ�����ʵ����
				fileName = dis.readUTF();
				//��ȡ���ļ������洢���ͱ��ݽ���IP��ַ�Ͷ˿ں�,�ļ���
				address1 = dis.readUTF();
				port1 = dis.readInt();
				nodefolder1 = dis.readUTF();
				System.out.println(address1+" "+port1+" "+nodefolder1);
				address2 = dis.readUTF();
				port2 = dis.readInt();
				nodefolder2 = dis.readUTF();
				System.out.println(address2+" "+port2+" "+nodefolder2);
				boolean success = false;			
				//���ӵ��ļ������洢���
				fileOperation = new RemoteFileOperation(address1, port1);
				//����fileOperation��download����
				success = fileOperation.download(uuid+"@"+fileName+"@"+nodefolder1);
				fileOperation = null;

				if(success){
					return true;
				}
				//���ڵ�����ʧ��,���ӵ����ݽ���������
				else{
					//���ӵ��ļ��ı��ݴ洢���
					fileOperation = new RemoteFileOperation(address2, port2);
					//����fileOperation��download����
					success = fileOperation.download(uuid+"@"+fileName+"@"+nodefolder2);
					fileOperation = null;
					return success;
				}
			}else{
				System.out.println(dis.readUTF());
				return false;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	/**
	 * �ͻ�������ɾ������
	 */
	@Override
	public boolean delete(String uuid) {
		//���������δ������ֱ��ɾ��ʧ��
		if(socket ==null){
			return false;
		}
		// �ļ������洢�����Ϣ��������洢�ڵ�ɾ��ʧ���˾�ֱ��ʧ��
		String address1 = null;
		int port1 = 0;
		String nodefolder1 = null;
		String nodefolder2 = null;
		try {
			// ��FileServer����ɾ����ָ��
			dos.writeInt(3);
			// �����ļ���uuid
			dos.writeUTF(uuid);
			dos.flush();
			// �ļ�����
			if (dis.readBoolean()) {
				// ��ȡ���ļ������洢���ͱ��ݽ���IP��ַ�Ͷ˿ں�,�ļ���
				address1 = dis.readUTF();
				port1 = dis.readInt();
				nodefolder1 = dis.readUTF();
				nodefolder2 = dis.readUTF();
				System.out.println(address1 + " " + port1 + " " + nodefolder1+" "+nodefolder2);
				boolean success = false;
				// ���ӵ��ļ������洢���
				fileOperation = new RemoteFileOperation(address1, port1);
				// ����fileOperation��download����
				success = fileOperation.delete(uuid + "@" + nodefolder1+"@"+nodefolder2);
				fileOperation = null;
				return success;
			} else {
				//��ӡ����ԭ��
				System.out.println(dis.readUTF());
				return false;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

}
