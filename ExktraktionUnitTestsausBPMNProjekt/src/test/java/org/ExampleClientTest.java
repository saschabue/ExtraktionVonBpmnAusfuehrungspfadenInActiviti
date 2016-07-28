package org;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;

import development.ExampleClient;

public class ExampleClientTest extends StandardProcessIntegrationTest {

	@Test
	public void testClientMethod() {
		
		ExampleClient.main(null);
		String document = ExampleClient.getDocument();
		boolean istest = ExampleClient.isIstest();
		File or, ex;
		if(istest){
			or = new File(new PathsettingController().getOriginalTest()+document);
			ex = new File(new PathsettingController().getExtendedTest()+document);
		}else{
			or = new File(new PathsettingController().getOriginalProcesses()+document);
			ex = new File(new PathsettingController().getExtendedProcesses()+document);
		}
		Assert.assertTrue(or.exists());
		Assert.assertTrue(ex.exists());
	}
}
