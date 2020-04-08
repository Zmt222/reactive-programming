package com.partha.reactiveapp06.routers;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.PUT;
import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

import com.partha.reactiveapp06.handlers.ItemHandler;

@Configuration
public class ItemRouter {
	
	@Bean
	public RouterFunction<ServerResponse> itemRoute(ItemHandler itemHandler){	
		return RouterFunctions.route(GET("/functional/items").and(accept(MediaType.APPLICATION_JSON)),itemHandler::getAllItems)
				.andRoute(GET("/functional/items/{id}").and(accept(MediaType.APPLICATION_JSON)), itemHandler::getOneItem)
				.andRoute(POST("/functional/items").and(accept(MediaType.APPLICATION_JSON)), itemHandler::createItem)
				.andRoute(DELETE("/functional/items/{id}").and(accept(MediaType.APPLICATION_JSON)), itemHandler::deleteItem)
				.andRoute(PUT("/functional/items/{id}").and(accept(MediaType.APPLICATION_JSON)), itemHandler::updateItem);
	}
	
	
	@Bean
	public RouterFunction<ServerResponse> exceptionRoute(ItemHandler itemHandler){	
		return RouterFunctions.route(GET("/functional/exception").and(accept(MediaType.APPLICATION_JSON)),itemHandler::createException);
				
	}

}
