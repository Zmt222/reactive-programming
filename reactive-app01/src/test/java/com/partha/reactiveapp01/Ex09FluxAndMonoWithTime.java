package com.partha.reactiveapp01;

import java.time.Duration;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

class Ex09FluxAndMonoWithTime {
	
	private static final Logger logger = LoggerFactory.getLogger(Ex09FluxAndMonoWithTime.class);

//	@Test
//	void test1() {
//		logger.info("Ex09FluxAndMonoWithTime.test1() :: start");
//		Flux<Long> infiniteFlux = Flux.interval(Duration.ofMillis(new Long(1000)))  //starts from zero and keeps emiting
//				.log(); 	
//		infiniteFlux.subscribe( element -> {logger.info("value is : "+element);});
//		logger.info("Ex09FluxAndMonoWithTime.test1() :: end");
//	}
//	
//	
//	@Test
//	void test2() {
//		logger.info("Ex09FluxAndMonoWithTime.test2() :: start");
//		Flux<Long> infiniteFlux = Flux.interval(Duration.ofMillis(new Long(1000)))  //starts from zero and keeps emiting
//				.log(); 	
//		infiniteFlux.subscribe( element -> {logger.info("value is : " +element);});
//		
//		try {
//			Thread.sleep(3000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//		logger.info("Ex09FluxAndMonoWithTime.test2() :: end");
//	}
//	
	
	
	@Test
	void test3() {
		logger.info("Ex09FluxAndMonoWithTime.test3() :: start");
		Flux<Long> infiniteFlux = Flux.interval(Duration.ofMillis(new Long(1000)))  //starts from zero and keeps emiting
				.take(3)
				.log(); 	
		
		StepVerifier.create(infiniteFlux)
					.expectSubscription()
					.expectNext(0L,1L,2L)
					.verifyComplete();

		logger.info("Ex09FluxAndMonoWithTime.test3() :: end");
	}
	
	
	

}
