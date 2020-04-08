package com.partha.reactiveapp8.routers;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.partha.reactiveapp8.handlers.ItemHandler;

@Configuration
public class ItemRouter {
	
	@Bean
	public RouterFunction<ServerResponse> itemStreamRoute(ItemHandler itemHandler){
		return RouterFunctions.route(GET("/functionalStream/items").and(accept(MediaType.APPLICATION_JSON)), itemHandler::getItemsStream);	
		//return RouterFunctions.route(GET("/functionalStream/items").and(accept(MediaType.APPLICATION_JSON)),itemHandler::getItemsStream);
				
	}

}
