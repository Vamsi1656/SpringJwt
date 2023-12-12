package com.ds.auth.jwt.exceptions;

public class InvalidApplicationException extends Exception{
    
	private static final long serialVersionUID = 1L;
	private final String code;
    private final String message;
    
       
    public InvalidApplicationException(String code, String message){
        this.code = code;
        this.message = message;
    }


    public String getCode() {
        return code;
    }
    @Override
    public String getLocalizedMessage() {
        return message;
    }

    

}
