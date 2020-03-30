package com.partha.reactiveapp01;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

class Ex05TransformingReactiveStreams {
	
	private static final Logger logger = LoggerFactory.getLogger(Ex05TransformingReactiveStreams.class);

	/**
	 * this method shows how to transform reactive-streams.
	 * this is similar to how we use map to transform java-streams
	 */
	@Test
	void test1() {
		logger.info("Ex05TransformingReactiveStreams.test1() :: start");
		List<String> list = Arrays.asList(new String[] {"SpringCore","SpringSecurity","SpringReactor"});
		Flux<String> flux = Flux.fromIterable(list)
				.map(s -> s.toUpperCase());
		
		StepVerifier.create(flux.log())
					.expectNext("SPRINGCORE","SPRINGSECURITY","SPRINGREACTOR")
					.verifyComplete();
		logger.info("Ex05TransformingReactiveStreams.test1() :: end");
	}
	
	

}
