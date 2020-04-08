package com.partha.reactiveapp8.documents;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Document(collection = "itemCapped")
@NoArgsConstructor
@AllArgsConstructor
public class ItemCapped {
	
	@Id
	private String id;
	
	private String description;
	
	private Double price;

}