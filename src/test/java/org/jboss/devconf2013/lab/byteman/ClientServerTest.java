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

public class ClientServerTest extends BMNGRunner {

	private ExecutorService es = null;
	private static final String REQUEST_MESSAGE = "Hello World!";
	private static final String RESPONSE_MESSAGE = "!dlroW olleH ";

	@BeforeTest
	public void beforeTest() {
		es = Executors.newSingleThreadExecutor();
	}

	@AfterTest
	public void afterTest() {
		es.shutdown();
	}

	@Test(enabled = true)
	public void clientTest() throws Exception {
		Server server = new Server(5050);
		es.submit(server);

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

	@Test(enabled = true)
	public void clientFixedTest() throws Exception {
		Server server = new Server(5050);
		es.submit(server);

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

	@Test(enabled = true)
	@BMScript(value = "ClientServerTest-clientBMTest.btm")
	public void clientBMTest() throws Exception {
		Server server = new Server(5050);
		es.submit(server);

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
