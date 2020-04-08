package com.partha.reactiveapp06.handlers;

import java.util.Map;

import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import reactor.core.publisher.Mono;

@Component
public class FunctionalWebExceptionHandler extends AbstractErrorWebExceptionHandler {

	public FunctionalWebExceptionHandler(ErrorAttributes errorAttributes, 
			ResourceProperties resourceProperties,
			ApplicationContext applicationContext,
			ServerCodecConfigurer serverCodecConfigurer) {
		//super(errorAttributes, resourceProperties, applicationContext);
		super(errorAttributes, new ResourceProperties(), applicationContext);
		super.setMessageWriters(serverCodecConfigurer.getWriters());
		super.setMessageReaders(serverCodecConfigurer.getReaders());
	}

	@Override
	protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
		return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
	}
	
	private Mono<ServerResponse> renderErrorResponse(ServerRequest serverRequest){
		Map<String, Object> errorAttributesMap = getErrorAttributes(serverRequest, false);
		//if we want we can log this map. this map is used actually to generate the default response
		return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.contentType(MediaType.APPLICATION_JSON)
				.body(BodyInserters.fromObject(errorAttributesMap.get("message")));	
	}

}
