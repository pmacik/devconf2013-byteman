package org.jboss.devconf2013.lab.byteman.client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.Charset;

/**
 * Client library to work with the TCP echo server.
 * 
 * 
 * @author Martin Večeřa <mvecera@redhat.com>
 *
 */
public class Client {

	private Socket socket;
	private int sentBytes = 0;
	private volatile byte[] responseData;
	
	/**
	 * Opens a connetion to the server at the specified port.
	 * 
	 * @param port TCP port number
	 * @throws UnknownHostException if the server cannot be accessed
	 * @throws IOException if any IO operation went wrong
	 */
	public void openSocket(int port) throws UnknownHostException, IOException {
		socket = new Socket("localhost", port);
	}

	/**
	 * Send a message to the server. Each message must be ended with a new line character. 
	 * The server won't process an unfinished message.
	 * 
	 * @param message a message to be sent to the server
	 * @throws IOException if any IO operation went wrong
	 */
	public void sendMessage(String message) throws IOException {
		byte[] data = message.getBytes(Charset.defaultCharset());
		sentBytes = data.length;
		socket.getOutputStream().write(data);
		System.out.printf("Sent message: %s", message);
	}

	private void startReceiveResponse(final byte[] data) throws IOException, InterruptedException {
		Thread t = new Thread(new Runnable() {

			public void run() {
				try {
					socket.getInputStream().read(data);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		});
		t.start();
		t.join();
	}
	
	/**
	 * Reads the response currently prepared at the server.
	 * 
	 * @throws IOException if any IO operation went wrong
	 * @throws InterruptedException if the read operation was aborted (by a signal for instance)
	 */
	public void receiveResponse() throws IOException, InterruptedException {
		responseData = new byte[sentBytes];
		startReceiveResponse(responseData);
	}
	
	/**
	 * Get the response read by the server.
	 * 
	 * @return the string representation of the response
	 */
	public String getResponse() {
		return new String(responseData, Charset.defaultCharset());
	}

	/**
	 * Closes the connection to the server.
	 * 
	 * @throws IOException if the socket cannot be closed
	 */
	public void close() throws IOException {
		socket.close();
	}

	/**
	 * Checks whether the response is already prepared by the server.
	 * 
	 * @return true if the complete response is ready
	 * @throws IOException if the status cannot be checked
	 */
	public boolean responseComplete() throws IOException {
		return socket.getInputStream().available() == sentBytes;
	}
	
}
