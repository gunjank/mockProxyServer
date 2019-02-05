package com.bh.api.proxy.gateway;

import org.junit.Test;

public class MockModellerTester {

	@Test
	public void test_mockModeller() throws Exception {
		
		String st = "dsdsds2322";
		System.out.println(st.matches("([A-Za-z]+[0-9]|[0-9]+[A-Za-z])[A-Za-z0-9]*"));
		
		String email = "abcaddd.com";
		System.out.println(email.matches("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$"));
	
		String phoneNumber = "123-456-7890";
		System.out.println(phoneNumber.toString().matches("((\\(\\d{3}\\) ?)|(\\d{3}-))?\\d{3}-\\d{4}"));
		
		String ssn = "123-45-7896";
		System.out.println(ssn.toString().matches("((\\(\\d{3}\\) ?)|(\\d{3}-))?\\d{2}-\\d{4}"));
		
		}
}
