package com.ds.auth.jwt.exceptions;

import lombok.Getter;

public class PlatformNotSupportException extends Exception{

    private String message;
	@Getter
	private String code;
	
	public PlatformNotSupportException(String code,String message) {
		this.code = code;
		this.message = message;
	}
	
	@Override
	public String getLocalizedMessage() {
		return message;
	}
    
}
