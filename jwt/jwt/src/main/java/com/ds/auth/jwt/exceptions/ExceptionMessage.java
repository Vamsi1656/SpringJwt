package com.ds.auth.jwt.exceptions;

import java.io.Serializable;

import lombok.Data;

@Data
public class ExceptionMessage implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String code;
	private String message;
}
