package com.partha.reactiveapp01;

public class CustomException extends Throwable{

	private static final long serialVersionUID = 1L;
	private String message;
	
	public CustomException(Throwable e) {
		this.message = e.getMessage();
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
