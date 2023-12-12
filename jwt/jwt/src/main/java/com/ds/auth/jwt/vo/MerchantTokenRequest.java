package com.ds.auth.jwt.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class MerchantTokenRequest {
	

	   private String aud;
	   private long exp;
	   private long nbf;
	   private long iat;
	   private String jti;
	   private String mid;
	   private String mbn;
	   private int mcc;
	 

}
