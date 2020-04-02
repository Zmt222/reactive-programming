package com.partha.reactiveapp02;

import java.time.Duration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class MyRestController {
	
	private static final Logger logger = LoggerFactory.getLogger(MyRestController.class);
	
	@GetMapping(value="/flux")
	public Flux<Integer> getFlux(){
		logger.info("MyRestController.flux() :: start");
		return Flux.just(1,2,3,4,5)
				//.delayElements(Duration.ofSeconds(1))
				.log();
	}
	
	
	@GetMapping(value="/fluxStream",produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
	public Flux<Integer> getFluxStream(){
		logger.info("MyRestController.getFluxStream() :: start");
		return Flux.just(1,2,3,4,5)
				.delayElements(Duration.ofSeconds(1))
				.log();
	}
	
	@GetMapping(value="/mono")
	public Mono<Integer> getMono(){
		logger.info("MyRestController.getMono() :: start");
		return Mono.just(1)
				.log();
	}


}
