package com.partha.reactiveapp8.initializers;

import java.time.Duration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Component;

import com.partha.reactiveapp8.documents.ItemCapped;
import com.partha.reactiveapp8.repositories.ItemCappedReactiveRepository;

import reactor.core.publisher.Flux;

@Component
public class ItemDataInitializer implements CommandLineRunner {
	
	private static final Logger logger = LoggerFactory.getLogger(ItemDataInitializer.class);
	
	
	@Autowired
	private MongoOperations mongoOperations;
	
	@Autowired
	private ItemCappedReactiveRepository itemCappedReactiveRepository;

	@Override
	public void run(String... args) throws Exception {
		createdCappedCollection();
		dataSetupForCappedCollection();
	}
	

	
	
	private void createdCappedCollection() {
		mongoOperations.dropCollection(ItemCapped.class);
		mongoOperations.createCollection(ItemCapped.class,
				CollectionOptions.empty().maxDocuments(20).size(50000).capped());
	}
	
	public void dataSetupForCappedCollection() {
		Flux<ItemCapped> cappedItemFlux = Flux.interval(Duration.ofSeconds(1))
		.map(i -> new ItemCapped(null, "randomItem"+i, (100.00+i)));
		
		itemCappedReactiveRepository
			.insert(cappedItemFlux)
			.subscribe(item -> {
				System.out.println("item inserted from command line runner : "+item);
			});
	}

}
