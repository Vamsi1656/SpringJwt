package com.ds.auth.jwt.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = Include.NON_NULL)
public class JwtResponse {

	private String responseCode;
	private String responseMessage;
	private String token;
	private String aud;
	private long exp;
	private long nbf;
	private long iat;
	private String jti;
	private String tpid;
	private String mid;
	private String mbn;
	private int mcc;
	private String aas[];
	private String avs[];
	private String maa[];

}
