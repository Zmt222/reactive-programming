package com.partha.reactiveapp04.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.partha.reactiveapp04.documents.Item;

import reactor.core.publisher.Flux;

@Repository
public interface ItemReactiveRepository extends ReactiveMongoRepository<Item, String>{

	//this is a custom read method doesnt require implementation since it follow the method naming convention
	public Flux<Item> findByDescription(String description);
}
