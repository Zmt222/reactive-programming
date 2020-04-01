package com.partha.reactiveapp01;

import java.time.Duration;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;

class Ex11HotAndColdReactiveStreams {

	private static final Logger logger = LoggerFactory.getLogger(Ex11HotAndColdReactiveStreams.class);

//	/**
//	 * this method shows how a cold-publisher behaves
//	 */
//	@Test
//	void test1() {
//		logger.info("Ex10BackpressureWithFluxAndMono.test1() :: start");
//
//		try {
//			Flux<String> flux = Flux.just("A","B","C","D","E","F")
//					.delayElements(Duration.ofSeconds(1))
//					.log();
//
//			flux.subscribe(element -> System.out.println("subscriber1 : "+element));			
//			Thread.sleep(3000);
//
//			flux.subscribe(element -> System.out.println("subscriber2 : "+element));
//			Thread.sleep(4000);
//
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//		logger.info("Ex10BackpressureWithFluxAndMono.test1() :: end");
//	}
	
	
	
	@Test
	void test2() {
		logger.info("Ex10BackpressureWithFluxAndMono.test2() :: start");

		try {
			Flux<String> flux = Flux.just("A","B","C","D","E","F")
					.delayElements(Duration.ofSeconds(1))
					.log();
			
			ConnectableFlux<String> connectableFlux = flux.publish();
			
			//now make it behave like a hot-publisher we have fire the below command
			connectableFlux.connect();
			
			//ones connect() is called after that any number of subscribers can connect to the flux
			//and it will behave as a hot-publisher
			connectableFlux.subscribe(element -> System.out.println("subscriber1 : "+element));			
			Thread.sleep(3000);

			connectableFlux.subscribe(element -> System.out.println("subscriber2 : "+element)); 
			//doesnt emit element from the beginning but from the moment it subscribes (unlike cold-publisher)
			Thread.sleep(4000);
			

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		logger.info("Ex10BackpressureWithFluxAndMono.test2() :: end");
	}




}
