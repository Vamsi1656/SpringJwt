package com.ds.auth.jwt.controllers;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ds.auth.jwt.AbstractJwtHandler;
import com.ds.auth.jwt.exceptions.ExceptionMessages.ExceptionMessagesEnum;
import com.ds.auth.jwt.exceptions.InvalidApplicationException;
import com.ds.auth.jwt.exceptions.PlatformNotSupportException;
import com.ds.auth.jwt.exceptions.RequestProcessException;
import com.ds.auth.jwt.exceptions.TokenExpiryException;
import com.ds.auth.jwt.utils.JsonUtil;
import com.ds.auth.jwt.vo.JwtResponse;
import com.ds.auth.jwt.vo.JwtTokenGenerateRequest;
import com.ds.auth.jwt.vo.MerchantTokenRequest;
import com.ds.auth.jwt.vo.Response;

@RestController
@RequestMapping(value = { "/jwt/", "/V1/jwt" })
public class JwtAuthenticationController {

	@Autowired
	private AbstractJwtHandler jwtHandler;
	
	
	public static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationController.class);

	@RequestMapping(value = "paymentcardtoken", method = RequestMethod.POST)
	public ResponseEntity<Response> generateJwtToken(HttpServletRequest request)
			throws RequestProcessException, PlatformNotSupportException, InvalidApplicationException {
		
		JwtTokenGenerateRequest jwtTokenGenerateRequest = (JwtTokenGenerateRequest) JsonUtil
				.getObjectFromRequest(request, JwtTokenGenerateRequest.class);
		logger.info("generateJwtToken method version; {} , RequestPacket: {}", "V0.1",
				jwtTokenGenerateRequest.toString());
	

		if(!StringUtils.isNotEmpty(jwtTokenGenerateRequest.getAud())) {
			throw new RequestProcessException("Audience is Mandatory data",
					ExceptionMessagesEnum.MANDATORY_DATA_EXCEPTION.getCode());
			}

		if(!StringUtils.isNotEmpty(jwtTokenGenerateRequest.getTpid())) {
			throw new RequestProcessException("Tpid is Mandatory data",
					ExceptionMessagesEnum.MANDATORY_DATA_EXCEPTION.getCode());
		}
		if(!StringUtils.isNotEmpty(jwtTokenGenerateRequest.getMid())) {
			throw new RequestProcessException("Mid is Mandatory data",
					ExceptionMessagesEnum.MANDATORY_DATA_EXCEPTION.getCode());
		}
		if(!StringUtils.isNotEmpty(jwtTokenGenerateRequest.getMbn())) {
			throw new RequestProcessException("Mbn is Mandatory data",
					ExceptionMessagesEnum.MANDATORY_DATA_EXCEPTION.getCode());
		}
		if((jwtTokenGenerateRequest.getMcc()>= 0)&&jwtTokenGenerateRequest.getMcc()<= 4) {
			throw new RequestProcessException("mcc is Mandatory data",
					ExceptionMessagesEnum.MANDATORY_DATA_EXCEPTION.getCode());
		}
		
		
		String jwtTokenResponse = jwtHandler.generateToken(jwtTokenGenerateRequest );
		logger.info("generateJwtToken method Version: {} ,ResponseData: {} ", "V2.0", jwtTokenResponse);
		Response response = new Response();
		response.setResponseCode("200");
		response.setResponseObject(jwtTokenResponse);
		response.setResponseMsg("JWT token successfully generated...");
		return ResponseEntity.ok().body(response);
	}

	private HttpHeaders getHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);
		return headers;
	}

	@RequestMapping(value = "verifyJwtToken", method = RequestMethod.POST)
	public ResponseEntity<Response> verifyJwtToken(HttpServletRequest request)
			throws RequestProcessException, PlatformNotSupportException, InvalidApplicationException {
		JwtResponse jwtResponse = new JwtResponse();
		String tokenHeader = request.getHeader("Authorization");
		
		logger.info("generateJwtToken method version; {} , RequestPacket: {}", "V0.1", tokenHeader);
		if (tokenHeader == null || tokenHeader.isEmpty()) {
			throw new RequestProcessException("Authorization token is required field",
					ExceptionMessagesEnum.INVALID_JWT_HEADER_EXCEPTION.getCode());
		}
		Response response = new Response();
			
				try {
					jwtResponse = jwtHandler.verifyJwtToken(tokenHeader, false);
					response.setResponseCode("200");
					response.setResponseObject(JsonUtil.getJsonStringFromObject(jwtResponse));
					response.setResponseMsg("JWT verify token successfully verified...");
				} catch (InvalidApplicationException | TokenExpiryException e) {
					e.printStackTrace();
					HttpHeaders headers = getHeaders();
					headers.set("TOKEN_REFRESH", "true");
					response.setResponseMsg(e.getMessage());
					response.setResponseCode("401");
					return ResponseEntity.ok().headers(headers).body(response);
				}
		logger.info("verifyingJwtToken method Version: {} ,ResponseData: {} ", "V2.0", jwtResponse);
	
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value = "refreshJwtToken", method = RequestMethod.POST)
	public ResponseEntity<Response> refreshJwtToken(HttpServletRequest request)
			throws RequestProcessException, PlatformNotSupportException, InvalidApplicationException, TokenExpiryException {
		
		String tokenHeader = request.getHeader("Authorization");
		if (tokenHeader == null || tokenHeader.isEmpty()) {
			throw new RequestProcessException("Authorization token is required field",
					ExceptionMessagesEnum.INVALID_JWT_HEADER_EXCEPTION.getCode());
		}	
		JwtTokenGenerateRequest refreshTokenRequest = (JwtTokenGenerateRequest) JsonUtil
				.getObjectFromRequest(request, JwtTokenGenerateRequest.class);
		logger.info("refreshJwtToken method version; {} , RequestPacket: {}", "V0.1",
				refreshTokenRequest.toString());
		
		String flag = request.getHeader("TOKEN_REFRESH");
		boolean refreshFlag = "true".equalsIgnoreCase(flag) ? true : false;
		logger.info("refreshJwtToken method Version: {} ,ResponseData: {} ", "V2.0", tokenHeader);
		String token = jwtHandler.refreshJwtToken(refreshTokenRequest,tokenHeader,refreshFlag);
		Response response = new Response();
		response.setResponseCode("200");
		response.setResponseObject(token);
		response.setResponseMsg("refreshJwtToken successfully generated...");
		return ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION, token).body(response);
		
	}
	

	@RequestMapping(value = "merchanttoken", method = RequestMethod.POST)
	public ResponseEntity<Response> merchantToken(HttpServletRequest request)
			throws RequestProcessException, PlatformNotSupportException, InvalidApplicationException {
		
		MerchantTokenRequest merchantTokenRequest = (MerchantTokenRequest) JsonUtil
				.getObjectFromRequest(request, MerchantTokenRequest.class);
		logger.info("generateJwtToken method version; {} , RequestPacket: {}", "V0.1",
				merchantTokenRequest.toString());
	
		String jwtTokenResponse = jwtHandler.merchantJwtToken(merchantTokenRequest );
		logger.info("generateJwtToken method Version: {} ,ResponseData: {} ", "V2.0", jwtTokenResponse);
		Response response = new Response();
		response.setResponseCode("200");
		response.setResponseObject(jwtTokenResponse);
		response.setResponseMsg("JWT token successfully generated...");
		return ResponseEntity.ok().body(response);
	}

	@GetMapping("/echo")
	public String echo() {
		return "Success !!!";
	}

	
}
