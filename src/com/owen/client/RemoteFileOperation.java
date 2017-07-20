package com.owen.client;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.Key;
import java.util.Properties;

import com.owen.utils.Tool;
import com.owen.utils.ZipUtils;

/**
 * �ͻ�����洢���������ݴ������
 * @author Administrator
 *
 */
public class RemoteFileOperation implements FileOperation{
	private String host;//����������Ϣ
	private int port;//���Ķ˿ں�
	private Socket socket;//�ͻ��˵�Socket
	private DataInputStream dis;
	private DataOutputStream dos;
	BufferedInputStream bis;
	public RemoteFileOperation(String host,int port){
		try {
			socket = new Socket(host, port);
			if(socket != null){
				dis = new DataInputStream(socket.getInputStream());
				dos = new DataOutputStream(socket.getOutputStream());
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (ConnectException e) {
			System.out.println("�洢�ڵ�û��������ֹ���");
		} catch (IOException e) {
			e.printStackTrace();
		}
	} 
	@Override
	public String update(String fileName) {
		if(socket == null){
			return null;
		}
		String[] str = fileName.split("@");
		//��ȡuuid
		String uuid = str[1];
		//��ԭʼ�ļ�����ѹ��
		File zipFile = new File("E:\\"+uuid+".zip");
//		System.out.println("====>"+zipFile.getAbsolutePath());
		ZipUtils.zip(str[0], zipFile.getAbsolutePath());
		//��ȡ�����ļ���
		String nodeFolder1 = str[2];
		String nodeFolder2 = str[3];
		//2.��ѹ������ļ������ϴ�
		byte [] buffer = new byte[1024 * 1024 *2];
		
		//�ϴ������
		try {
			bis = new BufferedInputStream(new FileInputStream(zipFile));
			//���㷢���ϴ��ļ���ָ��
			dos.writeInt(1);
			//����uuid
			dos.writeUTF(uuid);
			dos.writeUTF(nodeFolder1);
			dos.writeUTF(nodeFolder2);
			dos.flush();
			//��ʾ���Է����ļ����ȼ�������
			
			if(dis.readInt() == 0){
				//ѭ������ָ����ȣ�������
				int len = 0;
				while((len = bis.read(buffer)) != -1){
					dos.writeInt(2);
					dos.writeInt(len);
//					System.out.println("ÿ�η����ļ��ĳ���---��"+len);
					dos.write(buffer, 0, buffer.length);
					dos.flush();
				}
				//�����ļ����ݳɹ�
				if(dis.readInt() == 0){
					//���ͽ���ָ��
					dos.writeInt(3);
					dos.flush();
				}
			}
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			//�ϴ��ɹ���ɾ��ѹ�������ļ�
			zipFile.delete();
			socket.close();
			socket = null;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return uuid;
	}

	@Override
	public boolean download(String uuid) {
		if(socket == null){
			return false;
		}
		boolean result = false;
		String [] str = uuid.split("@");
		//����������ļ�uuid���ļ�·��
		String file_id = str[0];
		String file_name = str[1];
		String file_folder = str[2];
		File f = new File("D:\\"+file_id+".zip");
		BufferedOutputStream bos =null;
		try {
			if(!f.exists()){
				f.createNewFile();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			//��������ָ��
			dos.writeInt(4);
			//����uuid���ļ�·��
			dos.writeUTF(file_id);
			dos.writeUTF(file_folder);
			dos.flush();
			//���������ļ�
			result = dis.readInt() == 0;
			
			if(result){
				//���������ļ����ݵ�ָ��
				dos.writeInt(5);
				//����һ��buffer�ĳ���
				dos.writeInt(1024 * 1024 * 2);
				dos.flush();
				bos = new BufferedOutputStream(new FileOutputStream(f));
				int len = 0;
				byte[] buffer = null;
				while((len = dis.readInt()) != -1){
//					System.out.println("�ͻ��˽����ļ�����===>"+len);
					buffer = new byte[len];
					dis.readFully(buffer);
					bos.write(buffer);
				}
				bos.flush();
				bos.close();
			}
			//��ɾ��
			else{
				return false;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		ZipUtils.unzip(f.getAbsolutePath(), f.getParent()+file_name);
		f.delete();
		try {
			socket.close();
			socket = null;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public boolean delete(String uuid) {
		if(socket == null){
			return false;
		}
		String [] str = uuid.split("@");
		
		boolean result = false;
		try {
			dos.writeInt(6);//����ɾ���ļ���ָ��
			dos.writeUTF(str[0]);//����Ҫɾ�����ļ���uuid
			dos.writeUTF(str[1]);//ݔ�����洢���ĸ�Ŀ¼
			dos.writeUTF(str[2]);//���뱸�ݽڵ�ĸ�Ŀ¼
			dos.flush();
			//�Ƿ���ڸ��ļ������������ʧ��
			result = dis.readInt() == 0;
			socket.close();
			socket = null;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

}
