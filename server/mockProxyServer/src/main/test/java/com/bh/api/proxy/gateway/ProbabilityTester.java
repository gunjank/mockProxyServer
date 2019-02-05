package com.bh.api.proxy.gateway;

import java.util.Random;

import org.junit.Test;

public class ProbabilityTester {

	@Test
	public void test_jsonReader() throws Exception {
		int withDelay = 0;
		int withOutDelay = 0;
		for(int i =0; i<1000;i++) {
			boolean b = new Random().nextInt(100) < 70;
			if(b) {
				withDelay++;
			}else {
				withOutDelay++;
			}
			
		}
		System.out.println("withDelay: "+ withDelay + "   | withOutDelay: " + withOutDelay);
	}
	
}
