package com.partha.reactiveapp8.handlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.partha.reactiveapp8.documents.ItemCapped;
import com.partha.reactiveapp8.repositories.ItemCappedReactiveRepository;

import reactor.core.publisher.Mono;

@Component
public class ItemHandler {
	
	@Autowired
	private ItemCappedReactiveRepository itemCappedReactiveRepository;
	
	public Mono<ServerResponse> getItemsStream(ServerRequest request){
		return ServerResponse.ok()
						.contentType(MediaType.APPLICATION_STREAM_JSON)
						.body(this.itemCappedReactiveRepository.findItemsBy(),ItemCapped.class);
	}
	

}
