package com.owen.client;
/**
 * ������ļ������Ľӿ�
 * @author Administrator
 *
 */
public interface FileOperation {
	//�ϴ��ļ�������һ��uuid
	String update(String fileName);
	//�����ļ�,����uuid������
	boolean download(String uuid);
	//ɾ���ļ�
	boolean delete(String uuid);
}
