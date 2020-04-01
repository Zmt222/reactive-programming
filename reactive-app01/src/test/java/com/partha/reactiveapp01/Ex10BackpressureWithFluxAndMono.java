package com.partha.reactiveapp01;

import java.time.Duration;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

class Ex10BackpressureWithFluxAndMono {
	
	private static final Logger logger = LoggerFactory.getLogger(Ex10BackpressureWithFluxAndMono.class);

	@Test
	void test1() {
		logger.info("Ex10BackpressureWithFluxAndMono.test1() :: start");
		Flux<Integer> flux = Flux.range(1, 10)  //starts from zero and keeps emiting
				.delayElements(Duration.ofMillis(1000)); 	
		
		StepVerifier.create(flux.log())
					.expectSubscription()
					.thenRequest(1)
					.expectNext(1)
					.thenRequest(1)
					.expectNext(2)
					.thenCancel()
					.verify();
		logger.info("Ex10BackpressureWithFluxAndMono.test1() :: end");
	}
	
	@Test
	void test2() {
		logger.info("Ex10BackpressureWithFluxAndMono.test2() :: start");
		Flux<Integer> flux = Flux.range(1, 10)  //starts from zero and keeps emiting
				.log(); 	
		
		flux.subscribe( 
				element -> System.out.println("element is : "+element) , //consumer
				e -> System.out.println("Exception is : " +e), //error consumer
				() -> System.out.println("Done"), //complete consumer
				( subscription -> subscription.request(2)));  //backpressure consumer
		
		logger.info("Ex10BackpressureWithFluxAndMono.test2() :: end");
	}
	
	@Test
	void test3() {
		logger.info("Ex10BackpressureWithFluxAndMono.test3() :: start");
		Flux<Integer> flux = Flux.range(1, 10)  //starts from zero and keeps emiting
				.log(); 	
		
		flux.subscribe( 
				element -> System.out.println("element is : "+element) , //consumer
				e -> System.out.println("Exception is : " +e), //error consumer
				() -> System.out.println("Done"), //complete consumer
				( subscription -> subscription.cancel()));  //backpressure consumer
		
		logger.info("Ex10BackpressureWithFluxAndMono.test3() :: end");
	}
	
	@Test
	void test4() {
		logger.info("Ex10BackpressureWithFluxAndMono.test4() :: start");
		Flux<Integer> flux = Flux.range(1, 10)  //starts from zero and keeps emiting
				.log(); 	
		
		flux.subscribe(new BaseSubscriber<Integer>() {
			@Override
			protected void hookOnNext(Integer value) {
				request(1);
				System.out.println("value received is :"+value);
				if(value==4) {
					cancel();
				}
			}
		});  
		
		logger.info("Ex10BackpressureWithFluxAndMono.test4() :: end");
	}
	

}
