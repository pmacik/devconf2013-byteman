package org.jboss.devconf2013.lab.byteman;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.jboss.byteman.contrib.bmunit.BMNGRunner;
import org.jboss.byteman.contrib.bmunit.BMRule;
import org.jboss.byteman.contrib.bmunit.BMScript;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * The testsuite for Byteman related testing.
 * 
 * @author Pavel Macík <pmacik@redhat.com>
 */
public class BytemanTest extends BMNGRunner {

	/**
	 * The simple class for demonstration.
	 */
	private BMClass clazz = new BMClass();

	/**
	 * Test for demonstrating annotation based Byteman rule.
	 */
	@BMRule(name = "ruleAnnotation", targetClass = "org.jboss.devconf2013.lab.byteman.BMClass", targetMethod = "doSomething", condition = "true", targetLocation = "AT ENTRY", action = "throw new java.io.IOException(\"ruleAnnotation\")")
	@Test(enabled = true)
	public void bmRuleAnnotationTest() {
		try {
			clazz.doSomething();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		Assert.fail("The java.io.IOException should be thrown at this point.");
	}

	/**
	 * Test for demonstrating Byteman rule stored in a file.
	 */
	@BMScript(value = "BytemanTest-bmRuleFileTest.btm")
	@Test(enabled = true)
	public void bmRuleFileTest() {
		try {
			clazz.doSomethingElse();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		}
		Assert.fail("The java.io.FileNotFoundException should be thrown at this point.");
	}

	/**
	 * Test the actual function of {@link BMClass#doSomething()} method.
	 */
	@Test(enabled = true)
	public void doSomethingTest() {
		try {
			clazz.doSomething();
		} catch (IOException e) {
			e.printStackTrace();
			Assert.fail("The java.io.IOException should not be thrown at this point.");
		}
	}

	/**
	 * Test the actual function of {@link BMClass#doSomethingElse()} method.
	 */
	@Test(enabled = true)
	public void doSomethingElseTest() {
		try {
			clazz.doSomethingElse();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			Assert.fail("The java.io.FileNotFoundException should not be thrown at this point.");
		}
	}
}
