package com.owen.storage;

import java.io.*;
import java.net.Socket;
import java.util.*;

import com.owen.server.FileServer;
import com.owen.server.IOStrategy;
import com.owen.utils.MyUtils;


public class FileProtocol implements IOStrategy {
	public static ThreadLocal tl = new ThreadLocal();
	@Override
	public void service(Socket socket) {
		try {

			Map<String, Object> session = new HashMap<String, Object>();
			tl.set(session);

			InputStream in = socket.getInputStream();
			OutputStream out = socket.getOutputStream();

			BufferedInputStream bis = new BufferedInputStream(in);
			BufferedOutputStream bos = new BufferedOutputStream(out);

			DataInputStream dis = new DataInputStream(bis);
			DataOutputStream dos = new DataOutputStream(bos);

			int command = 0;
			int len = 0;
			byte[] buffer = null;
			String file_id = null;
			String nodeFolder1 = null;
			String nodeFolder2 = null;
			File file = null;
			File backupFile = null;
			FileInputStream fis2 = null;
			BufferedOutputStream bos2 = null;
			FileOutputStream fos2 = null;
			BufferedInputStream bis2 = null;

			while (true) {

				command = dis.readInt();
				switch (command) {
				case 0:
					break;
				case 1: // send file from client to server
					try {
						file_id = dis.readUTF(); // ����ļ���uuid
						nodeFolder1 = dis.readUTF();//����ļ��������·��
						nodeFolder2 = dis.readUTF();//����ļ�����ı���·��
						
						file = new File(nodeFolder1, file_id);
						backupFile = new File(nodeFolder2,file_id);
						fos2 = new FileOutputStream(file);
						bos2 = new BufferedOutputStream(fos2);
						session.put("file_id", file_id);
						session.put("file", file);
						session.put("bos", bos2);
						session.put("fos", fos2);

						tl.set(session);

						dos.writeInt(0); // answer the request
						dos.flush();
					} catch (Exception ex) {
						dos.writeInt(-1);
						dos.flush();
					}
					break;
				case 2: // client send file data to server
					try {
						
						len = dis.readInt();
						System.out.println("ÿ�ν��ܵ��ļ�����--->"+len);
						buffer = new byte[len];
						dis.readFully(buffer);

						bos2.write(buffer);
						bos2.flush();

						dos.writeInt(0);
						dos.flush();
						//���б���
						copy(file,backupFile);
						//���´洢����ʣ������
						String nodeproperties1 = "nodes\\"+file.getParentFile().getName()+".properties";
						MyUtils.setLastVolume(nodeproperties1, len);
						String nodeproperties2 = "nodes\\"+backupFile.getParentFile().getName()+".properties";
						MyUtils.setLastVolume(nodeproperties2, len);
					} catch (Exception ex) {
						dos.write(-1);
						dos.flush();
					}
					break;
				case 3: // client send end-of-file to server.
					try {
						fos2.close();
						bos2.close();
						session.remove("fos");
						session.remove("bos");
						
						dos.writeInt(0);
						dos.flush();
					} catch (Exception ex) {
						dos.writeInt(-1);
						dos.flush();
					}
					break;
				case 4: // get file from server
					//��ȡ�ļ�uuid
					file_id = dis.readUTF();
					System.out.println("++++++++++"+file_id);
					// ��ȡ�ļ�·��
					String file_folder = dis.readUTF();
					System.out.println("+++++++++"+file_folder);
					file = new File(file_folder, file_id);
					System.out.println("====�ļ����ڣ�"+file.exists());
					if (file.exists()) {
						fis2 = new FileInputStream(file);
						bis2 = new BufferedInputStream(fis2);
						session.put("fis", fis2);
						session.put("bis", bis2);
						dos.writeInt(0);
						dos.flush();
					} else {
						dos.writeInt(-1);
						dos.flush();
					}
					break;
				case 5: // get file data from server
					len = dis.readInt(); // file data buffer length
					buffer = new byte[len];
					while(true){
						len = bis2.read(buffer);
						dos.writeInt(len);
						if(len != -1){
							byte[] bd = new byte[len];
							System.arraycopy(buffer, 0, bd, 0, len);
							dos.write(bd);
						}else{
							break;
						}
					}
					bis2.close();
					fis2.close();
					session.remove("bis");
					session.remove("fis");
					dos.flush();
					break;
				case 6: // delete file from server
					try {
						//����ļ�uuid
						file_id = dis.readUTF();
						//����ļ������洢��Ŀ¼
						String fileFolder1 = dis.readUTF();
						//����ļ��ı��ݽ���Ŀ¼
						String fileFolder2 = dis.readUTF();
						long length = 0;
						File file1 = new File(fileFolder1, file_id);
						if (file1.exists()) {
							length = file1.length();
							file1.delete();
						}
						//���´洢����ʣ������
						String nodeproperties1 = "nodes\\"+file1.getParentFile().getName()+".properties";
						MyUtils.setLastVolume(nodeproperties1, -length);
						
						File file2 = new File(fileFolder2,file_id);
						if (file2.exists()) {
							file2.delete();
						}
						//���´洢����ʣ������
						String nodeproperties2 = "nodes\\"+file2.getParentFile().getName()+".properties";
						MyUtils.setLastVolume(nodeproperties2, -length);
						dos.writeInt(0);
						dos.flush();
					} catch (Exception ex) {
						dos.writeInt(-1);
						dos.flush();
					}
					break;
				}
			}
		} catch (Exception e) {
			// 关掉文件资源，关掉网络资�?
			// 从Thread Local 中取数据，关掉文件等资源
			Map<String, Object> session = (Map<String, Object>) tl.get();
			if (session.containsKey("fis")) {
				FileInputStream fis = (FileInputStream) session.get("fis");
				try {
					fis.close();
				} catch (Exception ex) {
				}
				session.remove("fis");
			}

			if (session.containsKey("fos")) {
				FileOutputStream fos = (FileOutputStream) session.get("fos");
				try {
					fos.close();
				} catch (Exception ex) {
				}
				session.remove("fos");
			}

		}

	}

	/**
	 * ʵ���ļ������ķ���
	 * @param from
	 * @param to
	 * @throws IOException
	 */
	private void copy(File from, File to) throws IOException {
		FileInputStream fis = new FileInputStream(from);
		BufferedInputStream bis = new BufferedInputStream(fis);
		FileOutputStream fos = new FileOutputStream(to);
		BufferedOutputStream bos = new BufferedOutputStream(fos);
		int r = 0;
		byte[] buffer = new byte[1024 * 16];
		while ((r = bis.read(buffer)) != -1) {
			bos.write(buffer, 0, r);
		}
		bos.close();
		fos.close();
		bis.close();
		fis.close();
	}

}
