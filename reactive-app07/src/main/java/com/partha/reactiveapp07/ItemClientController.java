package com.partha.reactiveapp07;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import com.partha.reactiveapp07.domain.Item;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class ItemClientController {
	
	private static final Logger logger = LoggerFactory.getLogger(ItemClientController.class);

	//creating webclient using factory method  of the webclient class where we pass the base url of 
	//the producer application
	WebClient webclient = WebClient.create("http://localhost:8080");
	
	@GetMapping(value="/client/retrieve")
	public Flux<Item> getAllItemsUsingRetrieve(){
		return webclient.get().uri("/items")
						.retrieve() //this is used to connect to the actual endpoint
						.bodyToFlux(Item.class)
						.log("items in client project retrive ");
	}
	
	
	@GetMapping(value="/client/exchange")
	public Flux<Item> getAllItemsUsingExchange(){
		return webclient.get().uri("/items")
						.exchange() //this is used to connect to the actual endpoint
						.flatMapMany(request -> {
							return request.bodyToFlux(Item.class);
						})
						.log("items in client project exchange");
	}
	
	/**
	 * this method shows how to pass path variable using webclient
	 * @return
	 */
	@GetMapping(value="/client/retrieve/{id}")
	public Mono<Item> getSingleUsingRetrieve(@PathVariable String id){
		return webclient.get().uri("/items/{id}",id)
						.retrieve() //this is used to connect to the actual endpoint
						.bodyToMono(Item.class)
						.log("item in client project retrive single item ");
	}
	
	@GetMapping(value="/client/exchange/{id}")
	public Mono<Item> getSingleItemUsingExchange(@PathVariable String id){
		return webclient.get().uri("/items/{id}",id)
						.exchange() //this is used to connect to the actual endpoint
						.flatMap(request -> {
							return request.bodyToMono(Item.class);
						})
						.log("items in client project exchange single item");
	}
	
	
	//making a post call using the client
	@PostMapping(value="/client/createItem")
	public Mono<Item> createItem(@RequestBody Item item){
		Mono<Item> requestMono = Mono.just(item);
		return webclient.post().uri("/items")
				.contentType(MediaType.APPLICATION_JSON)
				.body(requestMono, Item.class)
				.retrieve() //this is used to connect to the actual endpoint
				.bodyToMono(Item.class)
				.log("items in client project exchange single item");
	}
	
	
	//making a post call using the client
	@PutMapping(value="/client/updateItem/{id}")
	public Mono<Item> updateItem(@RequestBody Item item,@PathVariable String id){
		Mono<Item> requestMono = Mono.just(item);
		return webclient.put().uri("/items/{id}",id)
				.contentType(MediaType.APPLICATION_JSON)
				.body(requestMono, Item.class)
				.retrieve() //this is used to connect to the actual endpoint
				.bodyToMono(Item.class)
				.log("items in client project exchange single item");
	}
	
	
	//making a post call using the client
		@DeleteMapping(value="/client/deleteItem/{id}")
		public Mono<Void> deleteItem(@PathVariable String id){
			return webclient.delete().uri("/items/{id}",id)
					//.contentType(MediaType.APPLICATION_JSON)
					.retrieve() //this is used to connect to the actual endpoint
					.bodyToMono(Void.class)
					.log("items in client project exchange single item");
		}
	
}
