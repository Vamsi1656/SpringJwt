package com.ds.auth.jwt.interfaces;

import com.ds.auth.jwt.exceptions.InvalidApplicationException;
import com.ds.auth.jwt.exceptions.TokenExpiryException;
import com.ds.auth.jwt.vo.JwtResponse;
import com.ds.auth.jwt.vo.JwtTokenGenerateRequest;
import com.ds.auth.jwt.vo.MerchantTokenRequest;

public interface IJwtHandler {
    
    public String generatePaymentCardReaderToken(JwtTokenGenerateRequest request) throws InvalidApplicationException;
    public JwtResponse verifyJwtToken(String token, boolean refreshToken) throws InvalidApplicationException, TokenExpiryException;
    
    public String merchantJwtToken(MerchantTokenRequest  request) throws InvalidApplicationException;
	
}
