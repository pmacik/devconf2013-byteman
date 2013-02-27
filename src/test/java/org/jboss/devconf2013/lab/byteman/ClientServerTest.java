package org.jboss.devconf2013.lab.byteman;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.jboss.byteman.contrib.bmunit.BMNGRunner;
import org.jboss.byteman.contrib.bmunit.BMScript;
import org.jboss.devconf2013.lab.byteman.client.Client;
import org.jboss.devconf2013.lab.byteman.server.Server;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * Testsuite for {@link Client} and {@link Server} interactions.
 * 
 * @author Pavel Macík <pmacik@redhat.com>
 */
public class ClientServerTest extends BMNGRunner {
	/**
	 * The executor service for server thread.
	 */
	private ExecutorService serverExecutorService = null;

	/**
	 * String constant.
	 */
	private static final String REQUEST_MESSAGE = "Hello World!";
	private static final String RESPONSE_MESSAGE = "!dlroW olleH ";

	/**
	 * The method executed before each test. It prepares the execution
	 * environment for server thread.
	 */
	@BeforeTest
	public void beforeTest() {
		serverExecutorService = Executors.newSingleThreadExecutor();
	}

	/**
	 * The method executed after each test. It clears the execution environment
	 * of the server.
	 */
	@AfterTest
	public void afterTest() {
		serverExecutorService.shutdown();
	}

	/**
	 * Test the wrong usage of the {@link Client} for connecting to the
	 * {@link Server}.
	 */
	@Test(enabled = true)
	public void clientTest() throws Exception {
		Server server = new Server(5050);
		serverExecutorService.submit(server);

		Thread.sleep(5000);

		Client c = new Client();
		c.openSocket(5050);
		c.sendMessage(REQUEST_MESSAGE + "\n");
		c.receiveResponse();
		String response = c.getResponse();
		System.out.printf(
				"request=[%s], expected response=[%s], actual response=[%s]\n",
				REQUEST_MESSAGE, RESPONSE_MESSAGE, response);
		Assert.assertNotEquals(response, RESPONSE_MESSAGE);
		c.close();
	}

	/**
	 * Test the proper usage of the {@link Client} for connecting to the
	 * {@link Server}.
	 */
	@Test(enabled = true)
	public void clientFixedTest() throws Exception {
		Server server = new Server(5050);
		serverExecutorService.submit(server);

		Client c = new Client();
		c.openSocket(5050);
		c.sendMessage(REQUEST_MESSAGE + "\n");
		System.out.print("Waiting for the response");
		while (!c.responseComplete()) {
			System.out.print(".");
			Thread.sleep(100);
		}
		System.out.println();
		c.receiveResponse();
		String response = c.getResponse();
		System.out.printf(
				"request=[%s], expected response=[%s], actual response=[%s]\n",
				REQUEST_MESSAGE, RESPONSE_MESSAGE, response);
		Assert.assertEquals(response, RESPONSE_MESSAGE);
		c.close();
	}

	/**
	 * Test the wrong usage of the {@link Client} for connecting to the
	 * {@link Server} with Byteman rule attached which reveal the actual
	 * problem. The implementation (code) of the method is identical to
	 * {@link #clientTest()} method but the bytecode is modified by Byteman at
	 * runtime.
	 */
	@Test(enabled = true)
	@BMScript(value = "ClientServerTest-clientBMTest.btm")
	public void clientBMTest() throws Exception {
		Server server = new Server(5050);
		serverExecutorService.submit(server);

		Thread.sleep(5000);

		Client c = new Client();
		c.openSocket(5050);
		c.sendMessage(REQUEST_MESSAGE + "\n");
		c.receiveResponse();
		String response = c.getResponse();
		System.out.printf(
				"request=[%s], expected response=[%s], actual response=[%s]\n",
				REQUEST_MESSAGE, RESPONSE_MESSAGE, response);
		Assert.assertEquals(response, RESPONSE_MESSAGE);
		c.close();
	}
}
