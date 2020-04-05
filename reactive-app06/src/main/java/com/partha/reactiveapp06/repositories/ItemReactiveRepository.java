package com.partha.reactiveapp06.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.partha.reactiveapp06.documents.Item;

import reactor.core.publisher.Flux;

@Repository
public interface ItemReactiveRepository extends ReactiveMongoRepository<Item, String>{

	//this is a custom read method doesnt require implementation since it follow the method naming convention
	public Flux<Item> findByDescription(String description);
}
