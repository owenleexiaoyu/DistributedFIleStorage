package com.owen.server;

import java.net.Socket;

public class IOThread extends Thread{
	//������Э�����
	private IOStrategy ios;
	//Socket����
	private Socket socket;
	/**
	 * ���캯��������ʵ�ʵ�Э��
	 * @param ios
	 */
	public IOThread(IOStrategy ios){
		this.ios = ios;
	}
	
	/**
	 * ����Socket
	 * @param socket
	 */
	public synchronized void setSocket(Socket socket){
		this.socket = socket;
		//�����߳�
		notify();
	}
	
	/**
	 * �ж�һ���س��Ƿ��ǿ��е�
	 * @return
	 */
	public boolean isIdle(){
		return socket == null;
	}
	/**
	 * run�����о���ִ��Socket�ķ���
	 * ��run���������synchronized������Ϊ����wait�������ж�����
	 */
	@Override
	public synchronized void run() {
		while(true){
			try {
				//�����߳���������������ȴ���Socket���󴫽���ʱ��������
				wait();
				//ʹ��Э����ִ��Socket�ķ���
				ios.service(socket);
				//ִ����Ϻ��̻߳ص�����״̬
				socket = null;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
