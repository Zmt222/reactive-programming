package com.partha.reactiveapp06.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.partha.reactiveapp06.documents.Item;
import com.partha.reactiveapp06.repositories.ItemReactiveRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class ItemController {
	
	@Autowired
	private ItemReactiveRepository itemRepository;
	
	@GetMapping(value="/items")
	public Flux<Item> getAllItems(){
		return itemRepository.findAll();
	}
	
 	@GetMapping("/items/{id}")
   public Mono<ResponseEntity<Item>> getOneItem(@PathVariable String id){
        return this.itemRepository.findById(id)
                .map((item) -> new ResponseEntity<>(item, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
	
	@PostMapping(value = "/items")
	@ResponseStatus(HttpStatus.CREATED)
	public Mono<Item> createItem(@RequestBody Item item){
		return itemRepository.save(item);
	}
	
	 //id and item to be updated in the req = path variable and request body - completed
    // using the id get the item from database - completed
    // updated the item retrieved with the value from the request body - completed
    // save the item - completed
    //return the saved item - completed
	@PutMapping(value = "/items/{id}")
	public Mono<ResponseEntity<Item>> updateItem(@PathVariable String id,
			@RequestBody Item item){
		
		return this.itemRepository.findById(id)
				.flatMap(currentItem -> {
					currentItem.setPrice(item.getPrice());
                  currentItem.setDescription(item.getDescription());
                  return itemRepository.save(currentItem);
				})
              .map(updatedItem -> new ResponseEntity<>(updatedItem, HttpStatus.OK))
              .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));

	}
	
	
	@DeleteMapping(value = "/items/{itemId}")
	public Mono<Void> deleteItem(@PathVariable String itemId){
		return itemRepository.deleteById(itemId);
	}

}
