package com.partha.reactiveapp01;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;


public class Ex01FluxAppTest {
	
	private static final Logger logger = LoggerFactory.getLogger(Ex01FluxAppTest.class);
	
	/**
	 * this shows how to create a flux with some values. 
	 * however the data in flux can be accessed only after subscribing to it .
	 */
	@Test
	public void test1() {
		logger.info("FluxAppTest.test1() :: started");
		Flux<String> flux = Flux.just("Spring","SpringBoot","ReactiveSpring");	
		flux.subscribe( System.out::println );
		logger.info("FluxAppTest.test1() :: completed");
	}
	
	
	/**
	 * this method shows how to add log to the flux to monitor the flux events 
	 */
	@Test
	public void test2() {
		logger.info("FluxAppTest.test2() :: started");
		Flux<String> flux = Flux.just("Spring","SpringBoot","ReactiveSpring")
				.log();	
		flux.subscribe( System.out::println );
		logger.info("FluxAppTest.test2() :: completed");
	}
	
	
	/**
	 * this method shows how to attach exception to a flux. lets say we want to mimic 
	 * a scenario where a flux emits three data and then an exception arises.
	 * 
	 *  it is to be noted when there is an exception then onComplete() is not triggered in the end .
	 *  rather onError() is triggered
	 */
	@Test
	public void test3() {
		logger.info("FluxAppTest.test3() :: started");
		Flux<String> flux = Flux.just("Spring","SpringBoot","ReactiveSpring")
				.concatWith(Flux.error(new RuntimeException("some exception")))
				.log();	
		flux.subscribe( System.out::println );
		logger.info("FluxAppTest.test3() :: completed");
	}
	
	
	/**
	 * this method shows another overriden form of subscribe method which takes an additional labmda which 
	 * gets triggered if there is an error
	 */
	@Test
	public void test4() {
		logger.info("FluxAppTest.test4() :: started");
		Flux<String> flux = Flux.just("Spring","SpringBoot","ReactiveSpring")
				.concatWith(Flux.error(new RuntimeException("some exception")));
		flux.subscribe( System.out::println , e -> System.out.println("there is some error"));
		logger.info("FluxAppTest.test4() :: completed");
	}
	
	/**
	 * this is to show that once a flux emits an exception after that it cant emit any more data
	 * it will just call the onError() and exit. this is why the below code doesnt print 'SpringSecurity'
	 */
	@Test
	public void test5() {
		logger.info("FluxAppTest.test5() :: started");
		Flux<String> flux = Flux.just("Spring","SpringBoot","ReactiveSpring")
				.concatWith(Flux.error(new RuntimeException("some exception")))
				.concatWith(Flux.just("SpringSecurity"));
				//.log();	
		flux.subscribe( System.out::println ,  e -> System.out.println("there is some error"));
		logger.info("FluxAppTest.test5() :: completed");
	}
	
	

	
	/**
	 * there is another overrriden form of the subscribe method which takes 3 arguments 
	 * where the 3rd argument is a lambda which is triggered only after successfull completion of the flux
	 * i.e. after triggered just after completion of onComplete() 
	 * 
	 * i.e here the below code will print 'all data fetched' . however if we uncomment the  concatWith exception
	 * then it will invoke the second lambda argument and print 'there is some error'
	 */
	@Test
	public void test6() {
		logger.info("FluxAppTest.test6() :: started");
		Flux<String> flux = Flux.just("Spring","SpringBoot","ReactiveSpring")
				//.concatWith(Flux.error(new RuntimeException("some exception")));
				.concatWith(Flux.just("SpringSecurity"));
				//.log();	
		flux.subscribe( System.out::println , 
				e -> System.out.println("there is some error") ,
				() -> System.out.println("all data fetched"));
		logger.info("FluxAppTest.test6() :: completed");
	}
	
	
	//writing test cases for flux
	
	
	
	/**
	 * 1.this method shows how to test a flux
	 * 2.it is to be noted that for the StepVerifier the subscription to flux happens via .verifyComplete()
	 *   if we comment .verifyComplete() then the subscription will not happen and the flux will not emit data.
	 */
	@Test
	public void test7() {
		logger.info("FluxAppTest.test7() :: started");
		Flux<String> flux = Flux.just("Spring","SpringBoot","ReactiveSpring")
				//.concatWith(Flux.error(new RuntimeException("some exception")));
				.concatWith(Flux.just("SpringSecurity"))
				.log();	
		
		StepVerifier.create(flux)
					.expectNext("Spring")
					.expectNext("SpringBoot")
					.expectNext("ReactiveSpring")
					.expectNext("SpringSecurity")
					.verifyComplete();
					
		logger.info("FluxAppTest.test7() :: completed");					
	}
	
	
	/**
	 * 1.testing for exception
	 * 2.it is to be noted that in case of error here we dont use .verifyComplete() rather we use .verify()
	 * 
	 */
	@Test
	public void test8() {
		logger.info("FluxAppTest.test8() :: started");
		Flux<String> flux = Flux.just("Spring","SpringBoot","ReactiveSpring")
				.concatWith(Flux.error(new RuntimeException("some exception")))
				.log();	
		
		StepVerifier.create(flux)
					.expectNext("Spring")
					.expectNext("SpringBoot")
					.expectNext("ReactiveSpring")
					.expectError(RuntimeException.class)
					.verify();
					
		logger.info("FluxAppTest.test8() :: completed");					
	}
	
	
	
	/**
	 * 1.testing for exception message
	 * 2.it is to be noted that in case of error here we dont use .verifyComplete() rather we use .verify()
	 */
	@Test
	public void test9() {
		logger.info("FluxAppTest.test9() :: started");
		Flux<String> flux = Flux.just("Spring","SpringBoot","ReactiveSpring")
				.concatWith(Flux.error(new RuntimeException("some exception")));
				//.log();	
		
		StepVerifier.create(flux)
					.expectNext("Spring")
					.expectNext("SpringBoot")
					.expectNext("ReactiveSpring")
					.expectErrorMessage("some exception")
					.verify();
				
		//however it is to be kept in mind that .expectErrorMessage() and .expectError() cant be used together				
		logger.info("FluxAppTest.test9() :: completed");					
	}
	
	/**
	 * 1.if we dont want to verify individual items rather we want to verify the total count of items in flux
	 *   then this is done in the below way
	 */
	@Test
	public void test10() {
		logger.info("FluxAppTest.test10() :: started");
		Flux<String> flux = Flux.just("Spring","SpringBoot","ReactiveSpring");
					
		
		StepVerifier.create(flux)
					.expectNextCount(3)
					.verifyComplete();
				
		logger.info("FluxAppTest.test10() :: completed");					
	}



}
