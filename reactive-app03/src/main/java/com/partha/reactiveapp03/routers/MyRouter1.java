package com.partha.reactiveapp03.routers;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.partha.reactiveapp03.handlers.MyHandler1;

@Configuration
public class MyRouter1 {
	
	@Bean
	public RouterFunction<ServerResponse> route(MyHandler1 myHandler1){
		return RouterFunctions
				.route(GET("/functional/flux").and(accept(MediaType.APPLICATION_JSON)), myHandler1::method1)
				.andRoute(GET("/functional/mono").and(accept(MediaType.APPLICATION_JSON)), myHandler1::method2);
		
	}

}
