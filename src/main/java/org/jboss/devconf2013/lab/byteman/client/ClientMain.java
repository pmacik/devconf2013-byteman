package org.jboss.devconf2013.lab.byteman.client;

import java.io.IOException;

public class ClientMain {

	public static void main(String... args) throws IOException,
			InterruptedException {
		int port = 0;
		String message = "";
		try {
			port = Integer.valueOf(args[0]);
			message = args[1];
		} catch (Exception e) {
			System.out.println("Usage: ClientMain <port> <message>");
		}
		Client c = new Client();
		c.openSocket(port);
		c.sendMessage(message + "\n");

		c.receiveResponse();
		System.out.printf("Response: %s\n", c.getResponse());
		c.close();
	}

}
