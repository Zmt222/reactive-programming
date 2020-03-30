package com.partha.reactiveapp01;

import java.time.Duration;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

class Ex08ExceptionHandlingInReactiveStreams {
	
	private static final Logger logger = LoggerFactory.getLogger(Ex08ExceptionHandlingInReactiveStreams.class);

	
	/**
	 * 1.this test method shows the events that occur on error. 
	 * 2.it also shows that once an error occurs after that the flux doesnt emit any value
	 */
	@Test
	void test1() {
		logger.info("Ex08ExceptionHandlingInReactiveStreams.test1() :: start");
		Flux<String> flux = Flux.just("A","B","C")
				.concatWith(Flux.error(new RuntimeException("some exception")))
				.concatWith(Flux.just("D","E"));
		
		
		StepVerifier.create(flux.log())
					.expectSubscription()
					.expectNext("A","B","C")
					.expectError(RuntimeException.class)
					.verify();
		logger.info("Ex08ExceptionHandlingInReactiveStreams.test1() :: end");
	}
	
	
	/**
	 * 1.however if on occurance of error we want to perform any task then that can be 
	 * done using onErrorResume()
	 * 
	 * 2.note the events in this case. here elements D and E doesnt get added because of runtime expection. 
	 * and then it resumes from the onErrorResume()
	 * 
	 * 3.on the subcriber end the onError() is not triggered . Rather onComplete() is triggered just like a happy path
	 */
	@Test
	void test2() {
		logger.info("Ex08ExceptionHandlingInReactiveStreams.test2() :: start");
		Flux<String> flux = Flux.just("A","B","C")
				.concatWith(Flux.error(new RuntimeException("some exception")))
				.concatWith(Flux.just("D","E"))
				.onErrorResume( e -> {
					System.out.println("Exception is :"+e);
					return Flux.just("F","G");
				});
		
		
		StepVerifier.create(flux.log())
					.expectSubscription()
					.expectNext("A","B","C")
					//.expectError(RuntimeException.class)
					.expectNext("F","G")
					.verifyComplete();
		logger.info("Ex08ExceptionHandlingInReactiveStreams.test2() :: end");
	}
	
	
	/**
	 * 1. the onErrorReturn() is similar to onErrorResume() . the difference is that it is 
	 * used to return only one value / default-value
	 */
	@Test
	void test3() {
		logger.info("Ex08ExceptionHandlingInReactiveStreams.test3() :: start");
		Flux<String> flux = Flux.just("A","B","C")
				.concatWith(Flux.error(new RuntimeException("some exception")))
				.concatWith(Flux.just("D","E"))
				.onErrorReturn("F");
			
		StepVerifier.create(flux.log())
					.expectSubscription()
					.expectNext("A","B","C")
					.expectNext("F")
					.verifyComplete();
		logger.info("Ex08ExceptionHandlingInReactiveStreams.test3() :: end");
	}
	
	
	
	/**
	 * 1.earlier we were catching the exception and handling it so that the flux resumes normally
	 * 2.here we will see how to transform an exception before throwing it 
	 */
	@Test
	void test4() {
		logger.info("Ex08ExceptionHandlingInReactiveStreams.test4() :: start");
		Flux<String> flux = Flux.just("A","B","C")
				.concatWith(Flux.error(new RuntimeException("some exception")))
				.concatWith(Flux.just("D","E"))
				.onErrorMap( e -> new CustomException(e));
			
		StepVerifier.create(flux.log())
					.expectSubscription()
					.expectNext("A","B","C")
					.expectError(CustomException.class)
					.verify();
		logger.info("Ex08ExceptionHandlingInReactiveStreams.test4() :: end");
	}
	
	
	/**
	 * 1.this method shows how to retry on error
	 */
	@Test
	void test5() {
		logger.info("Ex08ExceptionHandlingInReactiveStreams.test5() :: start");
		Flux<String> flux = Flux.just("A","B","C")
				.concatWith(Flux.error(new RuntimeException("some exception")))
				.concatWith(Flux.just("D","E"))
				.retry(2)
				.onErrorMap( e -> new CustomException(e))
				;
			
		StepVerifier.create(flux.log())
					.expectSubscription()
					.expectNext("A","B","C") // for the original flow
					.expectNext("A","B","C") // for the first retry
					.expectNext("A","B","C") // for the second retry
					.expectError(CustomException.class)
					.verify();
		logger.info("Ex08ExceptionHandlingInReactiveStreams.test5() :: end");
	}
	
	
	/**
	 * 1.this method shows how to retry on error with BackOff
	 * 2.it is also to be noted that test case fails for 
	 */
	@Test
	void test6() {
		logger.info("Ex08ExceptionHandlingInReactiveStreams.test6() :: start");
		Flux<String> flux = Flux.just("A","B","C")
				.concatWith(Flux.error(new RuntimeException("some exception")))
				.concatWith(Flux.just("D","E"))		
				.onErrorMap( e -> new CustomException(e))
				.retryBackoff(2, Duration.ofSeconds(5));
			
		StepVerifier.create(flux.log())
					.expectSubscription()
					.expectNext("A","B","C") // for the original flow
					.expectNext("A","B","C") // for the first retry
					.expectNext("A","B","C") // for the second retry
					//.expectError(CustomException.class)
					.expectError(IllegalStateException.class)
					.verify();
		logger.info("Ex08ExceptionHandlingInReactiveStreams.test6() :: end");
	}
	
	
	

}
