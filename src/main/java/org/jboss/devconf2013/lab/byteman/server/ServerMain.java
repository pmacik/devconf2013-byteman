package org.jboss.devconf2013.lab.byteman.server;

import java.io.IOException;

public class ServerMain {

	public static void main(String[] args) throws IOException,
			InterruptedException {
		int port = 0;
		try {
			port = Integer.valueOf(args[0]);
		} catch (Exception e) {
			System.out.println("Usage: ServerMain <port>");
		}
		Server s = new Server(port);
		Thread serverThread = new Thread(s);
		serverThread.start();
		System.out.println("Starting server. Hit ENTER to stop.");
		while (!System.console().reader().ready()) {
			Thread.sleep(500);
		}
		s.stopRunning();
		serverThread.join();
	}
}
