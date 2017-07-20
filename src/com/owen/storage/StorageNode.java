package com.owen.storage;

import java.io.Serializable;

/**
 * �洢���
 * @author Administrator
 * @param <T>
 *
 */
public class StorageNode implements Comparable,Serializable{
	private String nodeName;//��������
	private String nodeIP;//����IP��ַ
	private int nodePort;//���Ķ˿ں�
	private String rootFolder;//���ļ���
	private long volume;//����
	private long last;//ʣ������
	private String fileServerIP;//�ļ���������IP��ַ
	private int fileServerPort;//�ļ��������Ķ˿ں�
	private boolean available;//�ýڵ������
	/**
	 * ���캯��
	 */
	public StorageNode(){}
	
	public StorageNode(String nodeName, String nodeIP, int nodePort, String rootFolder, long volume,long last) {
		super();
		this.nodeName = nodeName;
		this.nodeIP = nodeIP;
		this.nodePort = nodePort;
		this.rootFolder = rootFolder;
		this.volume = volume;
		this.last = last;
	}

	/**
	 * getter��setter����
	 * @return
	 */
	public String getNodeName() {
		return nodeName;
	}
	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}
	public String getNodeIP() {
		return nodeIP;
	}
	public void setNodeIP(String nodeIP) {
		this.nodeIP = nodeIP;
	}
	public int getNodePort() {
		return nodePort;
	}
	public void setNodePort(int nodePort) {
		this.nodePort = nodePort;
	}
	public String getRootFolder() {
		return rootFolder;
	}
	public void setRootFolder(String rootFolder) {
		this.rootFolder = rootFolder;
	}
	public long getVolume() {
		return volume;
	}
	public void setVolume(long volume) {
		this.volume = volume;
	}
	public String getFileServerIP() {
		return fileServerIP;
	}
	public void setFileServerIP(String fileServerIP) {
		this.fileServerIP = fileServerIP;
	}
	public int getFileServerPort() {
		return fileServerPort;
	}
	public void setFileServerPort(int fileServerPort) {
		this.fileServerPort = fileServerPort;
	}
	public long getLast() {
		return last;
	}
	public void setLast(long last) {
		this.last = last;
	}

	public boolean isAvailable() {
		return available;
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}

	/**
	 * ��дtoString����
	 */
	@Override
	public String toString() {
		return nodeName+" "+nodeIP+" "+nodePort+" "+
	rootFolder+" "+volume+ " " +last;
	}

	@Override
	public int compareTo(Object o) {
		StorageNode node = (StorageNode) o;
		return (int) (node.last-last);
	}
}
