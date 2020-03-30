package com.partha.reactiveapp01;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

class Ex06TransformingReactiveStreamsUsingFlatMap {
	
	private static final Logger logger = LoggerFactory.getLogger(Ex06TransformingReactiveStreamsUsingFlatMap.class);

	/**
	 * this method shows how to transform reactive-streams using flat-map.
	 * this is similar to how we use flat-map to transform java-streams
	 */
	@Test
	void test1() {
		logger.info("Ex06TransformingReactiveStreamsUsingFlatMap.test1() :: start");
		List<String> list = Arrays.asList(new String[] {"A","B","C","D"});
		Flux<String> flux = Flux.fromIterable(list)
				.flatMap(s -> {
					return Flux.fromIterable(convertToList(s));
				});
		
		StepVerifier.create(flux.log())
					.expectNextCount(8) //because for each element we are going to get two elements
					.verifyComplete();
		logger.info("Ex06TransformingReactiveStreamsUsingFlatMap.test1() :: end");
	}
	
	private List<String> convertToList(String s){
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return Arrays.asList(s,"new-value");
	}
	
	
	/**
	 * this method shows how to process the elements in parallel so that 
	 * we dont need to wait for 3 seconds
	 */
	@Test
	void test2() {
		logger.info("Ex06TransformingReactiveStreamsUsingFlatMap.test2() :: start");
		List<String> list = Arrays.asList(new String[] {"A","B","C","D"});
		Flux<String> flux = Flux.fromIterable(list)
				.window(2) // (A,B) (C,D)
				.flatMap( s -> {
					return s.map(this::convertToList).subscribeOn(Schedulers.parallel()) //Flux<List<String>>
								.flatMap(m -> Flux.fromIterable(m)); //Flux<String>
				})
				.log();
		
		StepVerifier.create(flux.log())
					.expectNextCount(8) //because for each element we are going to get two elements
					.verifyComplete();
		logger.info("Ex06TransformingReactiveStreamsUsingFlatMap.test2() :: end");
	}
	
	

}
