package com.owen.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import com.owen.server.FileServer;
import com.owen.storage.SFile;
import com.owen.storage.StorageNode;

public class MyUtils {
	public static Properties getProperty(String propertiesPath){
		Properties fileServerProperties = new Properties();
		try {
			InputStream is = new FileInputStream(propertiesPath);
			fileServerProperties.load(is);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return fileServerProperties;
	}
	public static long getVolume(String maxVolumn){
		String num = "";
		long volumn = 0;
		if(maxVolumn!=null){
			for(int i = 0; i < maxVolumn.length(); i++){
				if(Character.isDigit(maxVolumn.charAt(i))){
					num += maxVolumn.charAt(i);
					volumn = Long.parseLong(num);
					continue;
				}
				
				switch(maxVolumn.charAt(i)){
					case 'B':
						break;
					case 'K':
						volumn = volumn * 1024;
						break;
					case 'M':
						volumn = volumn * 1024 * 1024;
						break;
					case 'G':
						volumn = volumn * 1024 * 1024 * 1024;
						break;
					default:
					
						break;
				}
			}
		}
		return volumn;
	}
	/**
	 * �޸������ļ��и�������ʣ������
	 * ���ϴ�һ���ļ��󣬾�Ҫ��ȥ����ļ��Ĵ�С
	 * ��ɾ��һ���ļ��󣬾�Ҫ��������ļ��Ĵ�С��size��ֵΪ��
	 * @param propertiesPath
	 * @param size
	 * @return
	 */
	public static String setLastVolume(String propertiesPath, long size){
		String lastStr = null;
		try {
			Properties p = new Properties();
			p.load(new FileInputStream(propertiesPath));
			long last = getVolume(p.getProperty("Last"));
			//��ȥ�ļ��Ĵ�С
			last -= size;
			lastStr = last+"B";
			p.setProperty("Last", lastStr);
			p.store(new FileOutputStream(propertiesPath), "update Last value");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return lastStr;
	}
	/**
	 * ͨ��uuid���ҷ��������Ƿ��и��ļ�,�еĻ����ظ��ļ�����Ϣ��û�з���null
	 * @param uuid
	 * @return
	 */
	public static SFile getFileFromServer(String uuid){
		if(FileServer.fileList == null){
			return null;
		}
		for(SFile sf : FileServer.fileList){
			if(sf.getUuid().equals(uuid)){
				return sf;
			}
		}
		return null;
	}
	/**
	 * ���FileServer�кͲ����˿���ͬ�Ľ��
	 * @param port
	 * @return
	 */
	public static StorageNode getNodeFromList(int port){
		if(FileServer.nodeList == null || FileServer.nodeList.size() == 0){
			return null;
		}
		for(StorageNode node : FileServer.nodeList){
			if(node.getNodePort() == port){
				return node;
			}
		}		
		return null;
	}
}
