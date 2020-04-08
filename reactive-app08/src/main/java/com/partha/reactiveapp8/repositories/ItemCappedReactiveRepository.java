package com.partha.reactiveapp8.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.Tailable;
import org.springframework.stereotype.Repository;

import com.partha.reactiveapp8.documents.ItemCapped;

import reactor.core.publisher.Flux;

@Repository
public interface ItemCappedReactiveRepository extends ReactiveMongoRepository<ItemCapped, String>{

	@Tailable
	public Flux<ItemCapped> findItemsBy();
}
