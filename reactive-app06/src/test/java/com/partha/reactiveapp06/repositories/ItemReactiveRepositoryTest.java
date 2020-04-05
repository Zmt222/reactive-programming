package com.partha.reactiveapp06.repositories;


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
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.partha.reactiveapp06.documents.Item;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@DataMongoTest
@ExtendWith(SpringExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
@DirtiesContext
//@SpringBootTest
class ItemReactiveRepositoryTest {
	
	private static final Logger logger = LoggerFactory.getLogger(ItemReactiveRepositoryTest.class);
	
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
		logger.info("ItemReactiveRepositoryTest.setup() :: start");
	
		itemRepository.deleteAll()
						.thenMany(Flux.fromIterable(this.items))
						.flatMap(itemRepository::save)
						.doOnNext(item ->{
							logger.info("inserted item is:"+item.toString());
						})
						.blockLast();
		logger.info("ItemReactiveRepositoryTest.setup() :: end");
	}
	
	

	@Test
	@Order(value = 1)
	void findAllVerify() {
		logger.info("ItemReactiveRepositoryTest.findAllVerify() :: start");
		StepVerifier.create(itemRepository.findAll())
				.expectSubscription()
				.expectNextCount(5)
				.verifyComplete();
		logger.info("ItemReactiveRepositoryTest.findAllVerify() :: end");	
	}
	
	@Test
	@Order(value = 2)
	void findByIdVerify() {
		logger.info("ItemReactiveRepositoryTest.findByIdVerify() :: start");
		StepVerifier.create(itemRepository.findById("ABC"))
				.expectSubscription()
				.expectNextMatches((item -> item.getDescription().equals("Bose Headphones")))
				.verifyComplete();
		logger.info("ItemReactiveRepositoryTest.findByIdVerify() :: end");	
	}
	
	
	@Test
	@Order(value = 3)
	void findByDescriptionVerify() {
		logger.info("ItemReactiveRepositoryTest.findByDescriptionVerify() :: start");
		StepVerifier.create(itemRepository.findByDescription("Samsung TV"))
				.expectSubscription()
				.expectNextCount(1)
				.verifyComplete();
		logger.info("ItemReactiveRepositoryTest.findByDescriptionVerify() :: end");	
	}
	
	
	@Test
	@Order(value = 4)
	void insertVerify() {
		logger.info("ItemReactiveRepositoryTest.insertVerify() :: start");
		Item item = Item.builder()
				.description("Amazon echo dot")
				.price(100.00)
				.build();
		StepVerifier.create(itemRepository.save(item))
				.expectSubscription()
				.expectNextMatches( i ->  i.getId()!=null && i.getDescription().equals("Amazon echo dot") && i.getPrice()==100.00)
				.verifyComplete();
		logger.info("ItemReactiveRepositoryTest.insertVerify() :: end");	
	}
	
	
	@Test
	@Order(value = 5)
	void updateVerify() {
		logger.info("ItemReactiveRepositoryTest.updateVerify() :: start");
		Double newPrice = 350.00;
		Flux<Item> updatedItem = this.itemRepository.findByDescription("LG TV")
		.map(item -> {
			 	item.setPrice(newPrice);
			 	return item;
		})
		.flatMap(item -> {
			return itemRepository.save(item);
		});
		
		
		StepVerifier.create(updatedItem)
				.expectSubscription()
				.expectNextMatches( i ->  i.getPrice()==350.00)
				.verifyComplete();
		logger.info("ItemReactiveRepositoryTest.updateVerify() :: end");	
	}
	
	
	@Test
	@Order(value = 6)
	void deleteByIdVerify() {
		logger.info("ItemReactiveRepositoryTest.deleteByIdVerify() :: start");
		
//		Mono<Void> mono = this.itemRepository.findById("ABC")
//			.map(Item :: getId)
//			.flatMap(id -> {
//				return itemRepository.deleteById(id);
//			});
		
		Mono<Void> mono = this.itemRepository.deleteById("ABCD");
		
		StepVerifier.create(mono)
		.expectSubscription()
		.verifyComplete();
		
		logger.info("ItemReactiveRepositoryTest.deleteByIdVerify() :: end");	
	}
	
	
	



}
