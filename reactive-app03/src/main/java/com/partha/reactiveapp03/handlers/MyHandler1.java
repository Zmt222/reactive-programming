package com.partha.reactiveapp03.handlers;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class MyHandler1 {
	
	public Mono<ServerResponse> method1(ServerRequest request){
		return ServerResponse.ok()
					.contentType(MediaType.APPLICATION_JSON)
					.body(
							Flux.just(1,2,3,4)
							.log(), Integer.class
						);
	}
	
	
	public Mono<ServerResponse> method2(ServerRequest request){
		return ServerResponse.ok()
					.contentType(MediaType.APPLICATION_JSON)
					.body(
							Mono.just(1)
							.log(), Integer.class
						);
	}

}
