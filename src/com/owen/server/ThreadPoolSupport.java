package com.owen.server;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ThreadPoolSupport implements IOStrategy{
	//�̳߳���������߳��б�
	private List threads = new ArrayList();
	//��ʼ����ʱ�򿪱ٶ���߳�
	private static final int THREAD_NUM = 50;
	//�̳߳�����������ɵ��̸߳���
	private static final int THREAD_MAX = 100;
	//Э�����
	private IOStrategy ios = null;
	/**
	 * ���캯��������紫��ʵ�ʵ�Э�����
	 * ���ڳ�ʼ��ʱ���ɶ���߳�
	 * @param ios
	 */
	public ThreadPoolSupport(IOStrategy ios) {
		this.ios = ios;
		for(int i = 0;i<THREAD_MAX;i++){
			IOThread t = new IOThread(ios);
			t.start();
			//�����̳߳���
			threads.add(t);
		}
		//�ȴ��̳߳��е��̶߳�����
		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void service(Socket socket) {
		IOThread t = null;
		//�Ƿ��ҵ������߳�
		boolean found = false;
		//�����̳߳��������̣߳��ҵ�һ�����е��߳�
		for(int i = 0;i<threads.size();i++){
			t = (IOThread) threads.get(i);
			if(t.isIdle()){
				found = true;
				break;
			}
		}
		//û���ҵ����е��̣߳����ҵ�ǰ�̵߳ĸ���û����������ƣ��򴴽��߳�
		if(!found && threads.size() < THREAD_MAX){
			t = new IOThread(ios);
			t.start();
			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			threads.add(t);
		}
		t.setSocket(socket);
	}
}
