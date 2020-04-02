package com.partha.reactiveapp03.handlers;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@RunWith(SpringRunner.class)
//@WebFluxTest
@SpringBootTest
@AutoConfigureWebTestClient
class MyHandler1Test {
	
	@Autowired
	WebTestClient client;

	@Test
	void fluxVerify() {
		Flux<Integer> flux = client.get().uri("/functional/flux")
								.accept(MediaType.APPLICATION_JSON_UTF8)
								.exchange()
								.expectStatus().isOk()
								.returnResult(Integer.class)
								.getResponseBody();
		
		StepVerifier.create(flux)
				.expectSubscription()
				.expectNext(1,2,3,4)
				.verifyComplete();
		
	}
	
	
	
	@Test
	void monoVerify() {
		Flux<Integer> flux = client.get().uri("/functional/mono")
								.accept(MediaType.APPLICATION_JSON_UTF8)
								.exchange()
								.expectStatus().isOk()
								.returnResult(Integer.class)
								.getResponseBody();
		
		StepVerifier.create(flux)
				.expectSubscription()
				.expectNext(1)
				.verifyComplete();
		
	}


}
