package org.jboss.devconf2013.lab.byteman;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * The simple class for Byteman demonstration.
 * 
 * @author Pavel Macík <pmacik@redhat.com>
 */
public class BMClass {
	public void doSomething() throws IOException {
		System.out.println("I have done something!");
	}

	public void doSomethingElse() throws FileNotFoundException {
		System.out.println("I have done something else!");
	}
}
