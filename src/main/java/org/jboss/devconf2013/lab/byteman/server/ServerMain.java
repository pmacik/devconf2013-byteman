package org.jboss.devconf2013.lab.byteman.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.charset.Charset;

public class ServerMain {

	public static void main(String[] args) throws IOException, InterruptedException {
		int port = 0;
		try {
			port = Integer.valueOf(args[0]);
		} catch (Exception e) {
			System.out.println("Usage: ServerMain <port>");
		}
		ServerSocket sc = new ServerSocket(port);
		sc.setSoTimeout(1000);
		System.out.printf("Server listening at %d. Hit Enter to stop.\n", port);
		while (!System.console().reader().ready()) {
			try {
				Socket s = sc.accept();
				BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream(), Charset.defaultCharset()));
				String line = new StringBuffer(br.readLine()).reverse().toString();
				String[] strs = line.split(" ");
				for (int i = 0; i < strs.length; i++) {
					Thread.sleep(1000);
					s.getOutputStream().write((strs[i] + " ").getBytes(Charset.defaultCharset()));
				}
				s.close();
			} catch (SocketTimeoutException ste) {
				// this is expected
			}
		}
		sc.close();
	}

}
