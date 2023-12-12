package com.example.demo.service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component

public class JwtService {

	
	public String generateJwtToken(String username) {
		Map<String,Object> claims=new HashMap();
		return createToken(claims,username);
	}
	
	private String createToken(Map<String,Object> claims,String username) {
		return Jwts.builder()
				.setClaims(claims)
				.setSubject(username)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis()+1000*60*30))
				.signWith(getSignKey(),SignatureAlgorithm.HS256).compact();
	}

	private Key getSignKey() {
		byte[] keyBytes=Decoders.BASE64.decode("3A841236424272BD69BA757BAE814");
		return Keys.hmacShaKeyFor(keyBytes);
	}
	
}
