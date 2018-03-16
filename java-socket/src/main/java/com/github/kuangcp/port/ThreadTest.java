package com.github.kuangcp.port;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ThreadTest {
	public static void main(String[] wq) {
		ServerSocket serverSocket = null;
		Socket socket = null;
		OutputStream os = null;
		InputStream is = null;
		try {
			serverSocket = new ServerSocket(1000);
			socket = serverSocket.accept();
			LogicThread t = new LogicThread(socket);
			t.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
