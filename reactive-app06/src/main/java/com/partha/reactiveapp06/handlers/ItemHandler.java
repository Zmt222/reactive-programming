package com.partha.reactiveapp06.handlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import java.net.URI;

import com.partha.reactiveapp06.documents.Item;
import com.partha.reactiveapp06.repositories.ItemReactiveRepository;

import static org.springframework.web.reactive.function.BodyInserters.fromObject;

import reactor.core.publisher.Mono;

@Component
public class ItemHandler {
	
	@Autowired
	ItemReactiveRepository itemRepository;
	
	public Mono<ServerResponse> notFound = ServerResponse.notFound().build();
	
	public Mono<ServerResponse> getAllItems(ServerRequest serverRequest){
		return ServerResponse.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(this.itemRepository.findAll(),Item.class);				
	}
	
	public Mono<ServerResponse> getOneItem(ServerRequest request){
		String id = request.pathVariable("id");
		Mono<Item> mono = this.itemRepository.findById(id);
		return mono.flatMap(item -> ServerResponse.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(fromObject(item)))
				.switchIfEmpty(notFound);
	}
	
	
	public Mono<ServerResponse> createItem(ServerRequest serverRequest){
		Mono<Item> itemToBeInserted = serverRequest.bodyToMono(Item.class);
		return itemToBeInserted.flatMap(item -> ServerResponse.created(URI.create(""))
				.contentType(MediaType.APPLICATION_JSON)
				.body(this.itemRepository.save(item),Item.class));		
	}
	
	
	public Mono<ServerResponse> deleteItem(ServerRequest serverRequest){
		String id = serverRequest.pathVariable("id");
		Mono<Void> deletedItem = this.itemRepository.deleteById(id);
		return ServerResponse.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(deletedItem, Void.class);
	}
	
	
	 public Mono<ServerResponse> updateItem(ServerRequest serverRequest) {
	        String id = serverRequest.pathVariable("id");
	        Mono<Item> updatedItem = serverRequest.bodyToMono(Item.class)
	                .flatMap((item) -> {

	                    Mono<Item> itemMono = this.itemRepository.findById(id)
	                            .flatMap(currentItem -> {
	                                currentItem.setDescription(item.getDescription());
	                                currentItem.setPrice(item.getPrice());
	                                return this.itemRepository.save(currentItem);

	                            });
	                    return itemMono;
	                });

	        return updatedItem.flatMap(item ->
	                ServerResponse.ok()
	                        .contentType(MediaType.APPLICATION_JSON)
	                        .body(fromObject(item)))
	                .switchIfEmpty(notFound);
	    }
	
	
	
	

}
