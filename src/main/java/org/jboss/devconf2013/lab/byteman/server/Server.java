package org.jboss.devconf2013.lab.byteman.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.charset.Charset;

/**
 * Server library working as TCP echo server.
 * 
 * @author Pavel Macík <pmacik@redhat.com>
 */
public class Server implements Runnable {

	/**
	 * Port the server is listening on.
	 */
	private int port;

	/**
	 * The internal flag indicating whether the server is running.
	 */
	private boolean running;

	/**
	 * Main constructor.
	 */
	public Server(int port) {
		super();
		this.port = port;
	}

	/**
	 * Stop the server.
	 */
	public void stopRunning() {
		this.running = false;
	}

	/**
	 * Indicates whether the server is running-
	 * 
	 * @param <code>true</code> if the server is running, <code>false</code>
	 *        otherwise.
	 */
	public boolean isRunning() {
		return running;
	}

	/**
	 * The main method implementing the echo server.
	 */
	public void run() {
		running = true;
		ServerSocket sc = null;
		try {
			sc = new ServerSocket(port);

			sc.setSoTimeout(1000);
			System.out.printf("Server listening at %d.\n", port);
			while (running) {
				try {
					Socket s = sc.accept();
					BufferedReader br = new BufferedReader(
							new InputStreamReader(s.getInputStream(),
									Charset.defaultCharset()));
					String line = new StringBuffer(br.readLine()).reverse()
							.toString();
					String[] strs = line.split(" ");
					for (int i = 0; i < strs.length; i++) {
						Thread.sleep(1000);
						s.getOutputStream().write(
								(strs[i] + " ").getBytes(Charset
										.defaultCharset()));
					}
				} catch (SocketTimeoutException ste) {
					// this is expected
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (sc != null) {
				try {
					sc.close();
					System.out.println("Server exiting.");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
