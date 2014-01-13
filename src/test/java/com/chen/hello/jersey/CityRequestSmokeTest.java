package com.chen.hello.jersey;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.sun.jersey.api.client.ClientResponse;





public class CityRequestSmokeTest extends JerseyTestNG{

	@Test(description="IT test")
	public void testIT() throws JSONException{
		ClientResponse clientResponse = null;
		clientResponse=getWebResource("/San_Diego").get(ClientResponse.class);
		
		JSONObject retJson = clientResponse.getEntity(JSONObject.class);
		Assert.assertEquals(retJson.getString("areaTotal"), "964.506");
	
	}
}