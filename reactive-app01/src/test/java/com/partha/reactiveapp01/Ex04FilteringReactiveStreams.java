package com.partha.reactiveapp01;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

class Ex04FilteringReactiveStreams {
	
	private static final Logger logger = LoggerFactory.getLogger(Ex04FilteringReactiveStreams.class);

	/**
	 * this method shows how to apply filters on reactive-streams.
	 * this is similar to how we apply filters on java-streams
	 */
	@Test
	void test1() {
		logger.info("Ex04FilteringReactiveStreams.test1() :: start");
		List<String> list = Arrays.asList(new String[] {"SpringCore","SpringSecurity","SpringReactor"});
		Flux<String> flux = Flux.fromIterable(list)
				.filter(text -> text.contains("act"));
		
		StepVerifier.create(flux.log())
					.expectNext("SpringReactor")
					.verifyComplete();
		logger.info("Ex04FilteringReactiveStreams.test1() :: end");
	}
	
	

}
