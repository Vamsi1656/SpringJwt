package com.ds.auth.jwt.exceptions;

public class TokenExpiryException extends Exception {

	private static final long serialVersionUID = 1L;
	private String message;
	private String code;

	public TokenExpiryException() {
		super();
	}

	public TokenExpiryException(String message) {
		super(message);
	}

	public TokenExpiryException(String code, String message) {
		super(message);
		this.message = message;
		this.code = code;
	}

	public String getMessage() {

		return message;
	}

	public String getCode() {
		return code;
	}
}
