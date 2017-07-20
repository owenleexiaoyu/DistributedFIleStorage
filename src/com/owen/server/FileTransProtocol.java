package com.owen.server;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;
import java.util.UUID;

import com.owen.storage.SFile;
import com.owen.storage.StorageNode;
import com.owen.utils.MyUtils;

/**
 * �������˶�����ļ�����Э��
 * ����������Socket�����Э��
 * @author Administrator
 *
 */
public class FileTransProtocol implements IOStrategy{
	
	@Override
	public void service(Socket socket) {
		try {
			//��ȡ��Socket�����������
			DataInputStream dis = new DataInputStream(socket.getInputStream());
			DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
			int command = 0;
			while (true) {
				command = dis.readInt(); // ʵ���ϣ�Э���������ֵ
				switch (command) { 
				case 1: //�ϴ��ļ������ؽ����Ϣ
					String fileName = dis.readUTF();
					long size = dis.readLong();
					System.out.println("���������յ��ϴ������ļ���Ϊ"+fileName+",��СΪ"+size);
					String uuid = UUID.randomUUID().toString();
					List<StorageNode> list = FileServer.getNodesForFile();
					
					//û�п��ýڵ����ýڵ������������
					if(list == null){
						dos.writeBoolean(false);
						dos.writeUTF("�ڵ���������");
						dos.flush();
					}else{
						StorageNode node1 = list.get(0);
						System.out.println("���͸�======="+node1.getNodeName());
						StorageNode node2 = list.get(1);
						System.out.println("���͸�======="+node2.getNodeName());
						SFile sf = new SFile(fileName,size,uuid,node1,node2);
						//���ļ��ŵ��б���
						FileServer.fileList.add(sf);
						dos.writeBoolean(true);
						//��ͻ��˷������洢�����Ϣ
						dos.writeUTF(node1.getNodeIP());
						dos.writeInt(node1.getNodePort());
						dos.writeUTF(node1.getRootFolder());
						//��ͻ��˷��ر��ݽ����Ϣ
						dos.writeUTF(node2.getNodeIP());
						dos.writeInt(node2.getNodePort());
						dos.writeUTF(node2.getRootFolder());
						//���ظ��ļ���uuid
						dos.writeUTF(uuid);
						dos.flush();
					}
					break;
				case 2: // �����ļ�
					//��ȡ�ļ���uuid
					String file_id = dis.readUTF();
					SFile sfile = MyUtils.getFileFromServer(file_id);
					//�ڷ����������ļ�
					if(sfile != null){
						dos.writeBoolean(true);
						StorageNode n1 = sfile.getStorageNode1();
						StorageNode n2 = sfile.getStorageNode2();
						//�����ļ�����
						dos.writeUTF(sfile.getFileName());
						//��ͻ��˷������洢�����Ϣ
						dos.writeUTF(n1.getNodeIP());
						dos.writeInt(n1.getNodePort());
						dos.writeUTF(n1.getRootFolder());
						//��ͻ��˷��ر��ݽ����Ϣ
						dos.writeUTF(n2.getNodeIP());
						dos.writeInt(n2.getNodePort());
						dos.writeUTF(n2.getRootFolder());
						dos.flush();
					}
					else{
						dos.writeBoolean(false);
						dos.writeUTF("��uuid��Ӧ���ļ�������");
					}
					break;
				case 3://ɾ���ļ�
					//��ȡ�ļ���uuid
					String f_id = dis.readUTF();
					SFile s = MyUtils.getFileFromServer(f_id);
					//�ڷ����������ļ�
					if(s != null){
						dos.writeBoolean(true);
						StorageNode n1 = s.getStorageNode1();
						StorageNode n2 = s.getStorageNode2();
						//��ͻ��˷������洢����IP��ַ�Ͷ˿ںţ����ڿͻ�������
						dos.writeUTF(n1.getNodeIP());
						dos.writeInt(n1.getNodePort());
						//��ͻ��˷����������ĸ�Ŀ¼
						dos.writeUTF(n1.getRootFolder());
						dos.writeUTF(n2.getRootFolder());
						dos.flush();
						//���ļ����б���ɾ��
						FileServer.fileList.remove(s);
					}
					else{
						dos.writeBoolean(false);
						dos.writeUTF("��uuid��Ӧ���ļ�������");
					}
					break;
				case 4://�ϴ�ʧ��,���ݿͻ�����ʱ�����uuidɾ��fileList�еļ�¼
					String tempId = dis.readUTF();
					SFile s_f = MyUtils.getFileFromServer(tempId);
					if(s_f != null){
						//���ļ����б���ɾ��
						FileServer.fileList.remove(s_f);
						System.out.println("���ļ��б���ɾ����ټ�¼");
					}
				}
			}
		} catch (SocketException e) {
			System.out.println("�ͻ��˶Ͽ����������֮�������");
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
