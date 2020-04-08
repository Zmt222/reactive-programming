package com.partha.reactiveapp8.handlers;

import java.time.Duration;

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
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.partha.reactiveapp8.documents.ItemCapped;
import com.partha.reactiveapp8.initializers.ItemDataInitializer;
import com.partha.reactiveapp8.repositories.ItemCappedReactiveRepository;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@SpringBootTest
@DirtiesContext
@ExtendWith(SpringExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
@AutoConfigureWebTestClient
@ComponentScan(basePackages = "com.partha.reactiveapp08",
//includeFilters = @Filter(type = FilterType.REGEX, pattern="com.concretepage.*.*Util"),
excludeFilters = @Filter(type = FilterType.ASSIGNABLE_TYPE, classes = ItemDataInitializer.class)) 
class ItemHandlerTest {

	private static final Logger logger = LoggerFactory.getLogger(ItemHandlerTest.class);

	@Autowired
	private WebTestClient webClient;

	@Autowired
	private MongoOperations mongoOperations;

	@Autowired
	private ItemCappedReactiveRepository itemCappedReactiveRepository;


	@BeforeAll
	private void createdCappedCollection() {
		mongoOperations.dropCollection(ItemCapped.class);
		mongoOperations.createCollection(ItemCapped.class,
				CollectionOptions.empty().maxDocuments(20).size(50000).capped());

		Flux<ItemCapped> cappedItemFlux = Flux.interval(Duration.ofSeconds(1))
				.map(i -> new ItemCapped(null, "randomItem"+i, (100.00+i)))
				.take(5);

		itemCappedReactiveRepository
		.insert(cappedItemFlux)
		.doOnNext(item -> {
			System.out.println("item inserted from command line runner : "+item);
		})
		.blockLast();
	}
	
	 @Test
	 @Order(value=1)
	    public void testStreamAllItems(){

	        Flux<ItemCapped> itemCappedFlux = webClient.get().uri("/functionalStream/items")
	                .exchange()
	                .expectStatus().isOk()
	                .returnResult((ItemCapped.class))
	                .getResponseBody()
	                .take(5);

	        StepVerifier.create(itemCappedFlux)
	                .expectNextCount(5)
	                .thenCancel()
	                .verify();

	    }

}
