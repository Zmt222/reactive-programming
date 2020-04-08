package com.partha.reactiveapp8.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.partha.reactiveapp8.documents.ItemCapped;
import com.partha.reactiveapp8.repositories.ItemCappedReactiveRepository;

import reactor.core.publisher.Flux;

@RestController
public class StreamController {
	
	@Autowired
	private ItemCappedReactiveRepository repository;
	
	@GetMapping(value="/stream/items",produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
	public Flux<ItemCapped> getItemStreams(){
		return this.repository.findItemsBy();
	}

}
