package com.partha.reactiveapp06.controllers;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.partha.reactiveapp06.documents.Item;
import com.partha.reactiveapp06.initializers.ItemDataInitializer;
import com.partha.reactiveapp06.repositories.ItemReactiveRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;


@SpringBootTest
@DirtiesContext
@ExtendWith(SpringExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
@AutoConfigureWebTestClient
@ComponentScan(basePackages = "com.partha.reactiveapp06",
	//includeFilters = @Filter(type = FilterType.REGEX, pattern="com.concretepage.*.*Util"),
	 excludeFilters = @Filter(type = FilterType.ASSIGNABLE_TYPE, classes = ItemDataInitializer.class)) 
class ItemControllerTest {
	
	private static final Logger logger = LoggerFactory.getLogger(ItemControllerTest.class);
	
	@Autowired
	private WebTestClient webClient;
	
	@Autowired
	private ItemReactiveRepository itemRepository;
	
	List<Item> items = Arrays.asList(
			new Item(null,"Samsung TV",400.0),
			new Item(null,"LG TV",300.0),
			new Item(null,"Apple Watch",50.0),
			new Item("ABC","Bose Headphones",40.0),
			new Item("ABCD","BOAT Headphones",20.0)
			);
	
	@BeforeAll
	public void setup() {
		logger.info("ItemControllerTest.setup() :: start");	
		Mono<Void> mono = this.itemRepository.deleteAll().log();
		mono.subscribe(item ->{},
				e -> {},
				() -> {
					 this.itemRepository.saveAll(this.items).blockLast();
				});
		
//		itemRepository.deleteAll()
//						.thenMany(Flux.fromIterable(this.items))
//						.flatMap(itemRepository::save)
//						.doOnNext(item ->{
//							logger.info("inserted item is:"+item.toString());
//						})
//						.blockLast();
		logger.info("ItemControllerTest.setup() :: end");
	}
	


	@Test
	@Order(value = 1)
	void getAllItemsVerify() {
		logger.info("ItemControllerTest.getAllItemsVerify() :: start");
		 this.webClient.get().uri("/items")
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isOk()
				.expectHeader().contentType(MediaType.APPLICATION_JSON)
				.expectBodyList(Item.class)
				//.hasSize(5); //not checking on size since its all non blocking and insert and delete also changes the count at the same time
				.consumeWith(response -> {response.getResponseBody().stream()
					.forEach(item ->{
						assertNotNull(item.getId());
					});});
				
				
				
		//this.itemRepository.findAll().count().subscribe(a -> System.out.println("total record-count :"+a));
		logger.info("ItemControllerTest.getAllItemsVerify() :: end");
	}
	
	@Test
	@Order(value = 2)
	public void getOneItem(){
		logger.info("ItemControllerTest.getOneItem() :: start");
		this.webClient.get().uri("/items/ABCD")
			.exchange()
			.expectStatus().isOk()
			.expectBody()
			.jsonPath("$.price").isEqualTo(20.00)
			.jsonPath("$.description").isEqualTo("BOAT Headphones");
		logger.info("ItemControllerTest.getOneItem() :: end");
	}
	
	 @Test
	    public void getOneItem_notFound(){
		 logger.info("ItemControllerTest.getOneItem_notFound() :: start");
	        this.webClient.get()
	        .uri("/items/DEF")
	                .exchange()
	                .expectStatus().isNotFound();
	        logger.info("ItemControllerTest.getOneItem_notFound() :: end");
	    }

	
		@Test
		@Order(value = 3)
		void insertVerify() {		
//			//approach1
//			 Flux<Item> responseBody = this.webClient.post().uri("/items")
//			 .bodyValue(new Item("ABCDE", "Videocon TV", 200.00))
//					.accept(MediaType.APPLICATION_JSON)
//					.exchange()
//					.expectStatus().isOk()
//					.expectHeader().contentType(MediaType.APPLICATION_JSON)
//					.returnResult(Item.class)
//					.getResponseBody();
//			 
//			 StepVerifier.create(responseBody)
//			 .expectSubscription()
//			 .expectNextMatches(item -> item.getDescription().equals("Videocon TV"))
//			 .verifyComplete();	

			
//			//approach2
			this.webClient.post().uri("/items")
				 .body(Mono.just(new Item("ABCDE", "Videocon TV", 200.00)),Item.class)
				 .exchange()
		        .expectStatus().isCreated()
		        .expectBody()
		        .jsonPath("$.id").isNotEmpty()
		        .jsonPath("$.description").isEqualTo("Videocon TV")
		        .jsonPath("$.price").isEqualTo(200.00);
		}

	
	
	@Test
	@Order(value = 4)
	void deleteVerify() {	
		  this.webClient.delete().uri("/items/ABC")
          .accept(MediaType.APPLICATION_JSON)
          .exchange()
          .expectStatus().isOk()
          .expectBody(Void.class);
	}
	
	@Test
	@Order(value = 5)
	void updateVerify() {	
		 Flux<Item> responseBody = this.webClient.put().uri("/items/ABC")
		 .bodyValue(new Item("ABC", "Bose Headphones", 45.00))
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isOk()
				.expectHeader().contentType(MediaType.APPLICATION_JSON)
				.returnResult(Item.class)
				.getResponseBody();
		 
		 StepVerifier.create(responseBody)
		 .expectSubscription()
		 .expectNextMatches(item -> (item.getDescription().equals("Bose Headphones") && item.getPrice()==45.00))
		 .verifyComplete();		 
	}
	
	

}
