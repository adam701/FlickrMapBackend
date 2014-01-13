package com.chen.hello.jersey;

import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;

import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.test.framework.JerseyTest;

public class JerseyTestNG {

	private static JerseyTest jerseyTest;

	
	@BeforeSuite
	public void init() {
		jerseyTest = new JerseyTest("com.chen.hello") {};
	}

	public WebResource resource() {
		return jerseyTest.resource();
	}

	@BeforeTest
	public void setUp() throws Exception {
		jerseyTest.setUp();
	}

	@AfterTest
	public void tearDown() throws Exception {
		jerseyTest.tearDown();
	}

	/**
	 * 
	 * @param jerseyResourcePath
	 * @return
	 */
	protected WebResource getWebResource(String jerseyResourcePath) {
		WebResource webResource = resource();
		webResource = webResource.path(jerseyResourcePath);//
		return webResource;
	}
}
