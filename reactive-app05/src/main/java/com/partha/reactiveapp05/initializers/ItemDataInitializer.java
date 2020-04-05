package com.partha.reactiveapp05.initializers;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.partha.reactiveapp05.documents.Item;
import com.partha.reactiveapp05.repositories.ItemReactiveRepository;

import reactor.core.publisher.Flux;

@Component
public class ItemDataInitializer implements CommandLineRunner {
	
	private static final Logger logger = LoggerFactory.getLogger(ItemDataInitializer.class);
	
	List<Item> items = Arrays.asList(
			new Item(null,"Samsung TV",400.0),
			new Item(null,"LG TV",300.0),
			new Item(null,"Apple Watch",50.0),
			new Item("ABC","Bose Headphones",40.0),
			new Item("ABCD","BOAT Headphones",20.0)
			);
	
	@Autowired
	private ItemReactiveRepository itemRepository;

	@Override
	public void run(String... args) throws Exception {
		initialDataSetup();
	}
	
	public void initialDataSetup() {
		this.itemRepository.deleteAll()
		.thenMany(Flux.fromIterable(this.items))
		.flatMap(itemRepository::save)
		.thenMany(itemRepository.findAll())
		.subscribe(item -> {
			logger.info("inserted object: "+ item.toString());
		});
		
		
		this.itemRepository.saveAll(this.items);
	}

}
