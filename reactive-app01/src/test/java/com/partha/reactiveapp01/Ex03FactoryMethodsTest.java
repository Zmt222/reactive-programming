package com.partha.reactiveapp01;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class Ex03FactoryMethodsTest {
	
	private static final Logger logger = LoggerFactory.getLogger(Ex03FactoryMethodsTest.class);

	/**
	 * this method shows how to create a flux from an iterable
	 */
	@Test
	void test1() {
		logger.info("FactoryMethodsTest.test1() :: start");
		List<String> list = Arrays.asList(new String[] {"SpringCore","SpringSecurity","SpringReactor"});
		Flux<String> flux = Flux.fromIterable(list);
		
		StepVerifier.create(flux)
					.expectNext("SpringCore","SpringSecurity","SpringReactor")
					.verifyComplete();
		logger.info("FactoryMethodsTest.test1() :: end");
	}
	
	/**
	 * this method shows how to create a flux from an array
	 */
	@Test
	void test2() {
		logger.info("FactoryMethodsTest.test2() :: start");
		String[]  arr = new String[] {"SpringCore","SpringSecurity","SpringReactor"};
		Flux<String> flux = Flux.fromArray(arr);
		
		StepVerifier.create(flux)
					.expectNext("SpringCore","SpringSecurity","SpringReactor")
					.verifyComplete();
		logger.info("FactoryMethodsTest.test2() :: end");
	}
	
	
	/**
	 * this method shows how to create a flux from a stream
	 */
	@Test
	void test3() {
		logger.info("FactoryMethodsTest.test3() :: start");
		List<String> list = Arrays.asList(new String[] {"SpringCore","SpringSecurity","SpringReactor"});
		Flux<String> flux = Flux.fromStream(list.stream());
		
		StepVerifier.create(flux)
					.expectNext("SpringCore","SpringSecurity","SpringReactor")
					.verifyComplete();
		logger.info("FactoryMethodsTest.test3() :: end");
	}
	
	
	
	/**  factory methods for mono   **/
	
	/**
	 * this method shows how to create a mono using value or null
	 */
	@Test
	void test4() {
		logger.info("FactoryMethodsTest.test4() :: start");
		Mono mono = Mono.justOrEmpty(null);		
		StepVerifier.create(mono.log())
					.verifyComplete();
		logger.info("FactoryMethodsTest.test4() :: end");
	}
	
	
	/**
	 * this method shows how to create a mono using supplier
	 */
	@Test
	void test5() {
		logger.info("FactoryMethodsTest.test5() :: start");
		Supplier<String> stringSupplier = () -> "partha";
		Mono<String> mono = Mono.fromSupplier(stringSupplier);
		StepVerifier.create(mono.log())
					.expectNext("partha")
					.verifyComplete();
		
		logger.info("FactoryMethodsTest.test5() :: end");
	}

}
