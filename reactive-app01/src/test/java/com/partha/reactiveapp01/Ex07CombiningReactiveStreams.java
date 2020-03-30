package com.partha.reactiveapp01;

import java.time.Duration;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

class Ex07CombiningReactiveStreams {
	
	private static final Logger logger = LoggerFactory.getLogger(Ex07CombiningReactiveStreams.class);

	
	/**
	 * this test method shows how to combine flux
	 */
	@Test
	void test1() {
		logger.info("Ex07CombiningReactiveStreams.test1() :: start");
		Flux<String> flux1 = Flux.just("A","B","C");
		Flux<String> flux2 = Flux.just("D","E","F");
		
		Flux<String> mergedFlux = Flux.merge(flux1,flux2);
		
		StepVerifier.create(mergedFlux.log())
					.expectNextCount(6) //because for each element we are going to get two elements
					.verifyComplete();
		logger.info("Ex07CombiningReactiveStreams.test1() :: end");
	}
	
	
	
	/**
	 * this test method shows how to combine flux when the elements are 
	 * emitted with delay.
	 * 
	 * it is to be noted that in this case the order is not preserved
	 */
	@Test
	void test2() {
		logger.info("Ex07CombiningReactiveStreams.test2() :: start");
		Flux<String> flux1 = Flux.just("A","B","C").delayElements(Duration.ofSeconds(1));
		Flux<String> flux2 = Flux.just("D","E","F").delayElements(Duration.ofSeconds(1));
		
		Flux<String> mergedFlux = Flux.merge(flux1,flux2);
		
		StepVerifier.create(mergedFlux.log())
					.expectNextCount(6) //because for each element we are going to get two elements
					.verifyComplete();
		logger.info("Ex07CombiningReactiveStreams.test2() :: end");
	}
	
	
	/**
	 * if we want to preserve the order then instead of merge we have to use concat
	 */
	@Test
	void test3() {
		logger.info("Ex07CombiningReactiveStreams.test3() :: start");
		Flux<String> flux1 = Flux.just("A","B","C").delayElements(Duration.ofSeconds(1));
		Flux<String> flux2 = Flux.just("D","E","F").delayElements(Duration.ofSeconds(1));
		
		Flux<String> mergedFlux = Flux.concat(flux1,flux2);
		
		StepVerifier.create(mergedFlux.log())
					.expectNextCount(6) //because for each element we are going to get two elements
					.verifyComplete();
		logger.info("Ex07CombiningReactiveStreams.test3() :: end");
	}
	

}
