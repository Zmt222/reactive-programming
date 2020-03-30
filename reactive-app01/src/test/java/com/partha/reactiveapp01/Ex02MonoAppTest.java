package com.partha.reactiveapp01;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class Ex02MonoAppTest {
	
	private static final Logger logger = LoggerFactory.getLogger(Ex02MonoAppTest.class);
	
	/**
	 * creating and testing a mono
	 */
	@Test
	public void test1() {
		logger.info("MonoAppTest.test1() :: start");
		Mono mono = Mono.just("spring");
		
		StepVerifier.create(mono)
					.expectNext("spring")
					.verifyComplete();
		logger.info("MonoAppTest.test1() :: end");
	}
	
	/**
	 * testing error scenario
	 */
	@Test
	public void test2() {
		logger.info("MonoAppTest.test2() :: start");
		Mono mono = Mono.error(new RuntimeException("exception occured"));
		
		StepVerifier.create(mono)
					.expectError(RuntimeException.class)
					.verify();
		logger.info("MonoAppTest.test2() :: end");
	}

}
