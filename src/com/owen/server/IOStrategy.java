package com.owen.server;

import java.net.Socket;
/**
 * ���ڴ���Socket��������Ľӿ�
 * @author Administrator
 *
 */
public interface IOStrategy {
	void service(Socket socket);
}
