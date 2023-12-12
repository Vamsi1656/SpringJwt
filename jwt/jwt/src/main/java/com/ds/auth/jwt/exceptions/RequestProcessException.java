package com.ds.auth.jwt.exceptions;

public class RequestProcessException extends Exception{

    private static final long serialVersionUID = -4983478204357190254L;
	private final String message;
	private final String code;
	
	public RequestProcessException(String message,String code) {
		super(message);
		this.message = message;
		this.code = code;
	}
	
	@Override
	public String getLocalizedMessage() {
		return message;
	}
	

	public String getCode() {
		return code;
	}
    
}
