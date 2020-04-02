package com.partha.reactiveapp02;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;


import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@RunWith(SpringRunner.class)
@WebFluxTest
class MyRestControllerTest {
	
	@Autowired
	private WebTestClient client; 
	//this corresponds to @TestRestTemplate which is the blocking version for spring-mvc

	@Test
	void fluxVerify() {
		Flux<Integer> flux = client.get().uri("/flux")
								.accept(MediaType.APPLICATION_JSON_UTF8)
								.exchange()
								.expectStatus().isOk()
								.returnResult(Integer.class)
								.getResponseBody();
		
		StepVerifier.create(flux)
				.expectSubscription()
				.expectNext(1,2,3,4,5)
				.verifyComplete();
		
	}
	
	
	@Test
	void fluxVerifySecondApproach() {
		 client.get().uri("/flux")
								.accept(MediaType.APPLICATION_JSON_UTF8)
								.exchange()
								.expectStatus().isOk()
								.expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
								.expectBodyList(Integer.class)
								.hasSize(5);
		
	}
	
	@Test
	void fluxVerifyThirdApproach() {
		
		List<Integer> expectedIntegerList =  Arrays.asList(1,2,3,4,5);
		
		 EntityExchangeResult<List<Integer>> actualIntegerList = client.get().uri("/flux")
								.accept(MediaType.APPLICATION_JSON_UTF8)
								.exchange()
								.expectStatus().isOk()
								.expectBodyList(Integer.class)
								.returnResult();
		 
		 assertEquals(expectedIntegerList, actualIntegerList.getResponseBody());
		
	}
	
	@Test
	void fluxVerifyFourthApproach() {
		
		List<Integer> expectedIntegerList =  Arrays.asList(1,2,3,4,5);
		
		client.get().uri("/flux")
								.accept(MediaType.APPLICATION_JSON_UTF8)
								.exchange()
								.expectStatus().isOk()
								.expectBodyList(Integer.class)
								.consumeWith(response ->{
									assertEquals(expectedIntegerList, response.getResponseBody());
								});
		
	}
	
	

	@Test
	void fluxStreamVerify() {
		Flux<Integer> responseBodyFlux = client.get().uri("/fluxStream")
				.accept(MediaType.APPLICATION_STREAM_JSON)
				.exchange()
				.expectStatus().isOk()
				.returnResult(Integer.class)
				.getResponseBody();

		StepVerifier.create(responseBodyFlux)
		.expectNext(1,2,3,4,5)
		.thenCancel()
		.verify();		
	}
	
	
	@Test
	void monoVerify() {
		 client.get().uri("/mono") //capture of
				.accept(MediaType.APPLICATION_JSON_UTF8)
				.exchange() //response spec
				.expectStatus().isOk()  //response spec
				.expectBody(Integer.class)
				.consumeWith( response -> {
					assertEquals(1, response.getResponseBody());
				});
	
	}



}
