package com.ds.auth.jwt;

import java.io.IOException;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.ECDSAKeyProvider;
import com.ds.auth.jwt.constants.Constants;
import com.ds.auth.jwt.crypto.RSACrypto;
import com.ds.auth.jwt.exceptions.InvalidApplicationException;
import com.ds.auth.jwt.exceptions.RequestProcessException;
import com.ds.auth.jwt.exceptions.TokenExpiryException;
import com.ds.auth.jwt.interfaces.IJwtHandler;
import com.ds.auth.jwt.utils.FileUtil;
import com.ds.auth.jwt.utils.JsonUtil;
import com.ds.auth.jwt.vo.JwtResponse;
import com.ds.auth.jwt.vo.JwtTokenGenerateRequest;
import com.ds.auth.jwt.vo.MerchantTokenRequest;

@Component
public class AbstractJwtHandler implements IJwtHandler {

	@Value("${jwt.token.expiry.hours}")
	int tokenExpiryHours;

	@Value("${jwt.token.expiry.minutes}")
	int tokenExpiryMinutes;

	@Value("${taptopay.jwt.sign.privatekey}")
	String privateKeyPath;

	@Value("${taptopay.jwt.sign.publickey}")
	String publicKeyPath;

	private static final Logger logger = LoggerFactory.getLogger(AbstractJwtHandler.class);

	String publicKeyPemFile;
	String privatekeyPemFile;

	public String generateToken(JwtTokenGenerateRequest request) throws InvalidApplicationException{
		logger.info("Jwt Token generation Process started jwtRequest: {}", request.toString());

		UUID uuid = UUID.randomUUID();

		final Date createdDate = new Date();
		long expiryMillis = 0;

		if (tokenExpiryHours > 0)
			expiryMillis = createdDate.getTime() + tokenExpiryHours * 60 * 60 * 1000;

		if (tokenExpiryMinutes > 0)
			expiryMillis = createdDate.getTime() + tokenExpiryMinutes * 60 * 1000;

		final Date expirationDate = new Date(expiryMillis);
		
		Algorithm algorithm = Algorithm.ECDSA256(getRsaPublicKey(), getRsaPrivateKey()); // == ES256

		Map<String, Object> headerMap = new HashMap<String, Object>();
		headerMap.put(Constants.HEADER_FEILD_SIGNATURE_ALGORITHM, Constants.ECDSA_256);
		headerMap.put(Constants.HEADER_FEILD_TOKEN_KEY_ID, Constants.Token_Key_ID);
		headerMap.put(Constants.HEADER_FEILD_TOKEN_TYPE, Constants.TYPE);

		String jwtToken = JWT.create().withHeader(headerMap).withAudience(request.getAud())
				.withExpiresAt(expirationDate).withNotBefore(createdDate).withIssuedAt(createdDate)
				.withJWTId(uuid.toString()).withClaim(Constants.MERCHANT_ID, request.getMid())
				.withClaim(Constants.MERCHANT_BANNER_NAME, request.getMbn())
				.withClaim(Constants.MERCHANT_CATEGORY_CODE, request.getMcc())
				.withClaim(Constants.TERMINAL_PROFILE_ID, request.getTpid()).sign(algorithm);
		
		JwtResponse jwtResponse = new JwtResponse();
		jwtResponse.setToken(jwtToken);
		jwtResponse.setJti(uuid.toString());
		return JsonUtil.getJsonStringFromObject(jwtResponse);
	}

	@Override
	public String generatePaymentCardReaderToken(JwtTokenGenerateRequest request) throws InvalidApplicationException{
		return generateToken(request);
	}

	@Override
	public JwtResponse verifyJwtToken(String jwtToken, boolean refreshToken)
			throws InvalidApplicationException, TokenExpiryException {
		JwtResponse jwtResponse = verifyToken(jwtToken, getRsaPublicKey(), refreshToken);

		return jwtResponse;
	}

	public JwtResponse verifyToken(String jwtToken, ECPublicKey ecPublicKey, boolean refreshToken)
			throws InvalidApplicationException, TokenExpiryException {
		Map<String, Object> headerMap = new HashMap<String, Object>();
		headerMap.put(Constants.HEADER_FEILD_SIGNATURE_ALGORITHM, Constants.ECDSA_256);
		headerMap.put(Constants.HEADER_FEILD_TOKEN_KEY_ID, Constants.Token_Key_ID);
		headerMap.put(Constants.HEADER_FEILD_TOKEN_TYPE, Constants.TYPE);

		JwtResponse jwtResponse = new JwtResponse();
		try {

			ECPublicKey publicKey = RSACrypto.getECPublicKey(publicKeyPemFile);
			final Algorithm algorithm = Algorithm.ECDSA256(new ECDSAKeyProvider() {

				@Override
				public ECPublicKey getPublicKeyById(String keyId) {
					return publicKey;
				}

				@Override
				public String getPrivateKeyId() {
					throw new UnsupportedOperationException();
				}

				@Override
				public ECPrivateKey getPrivateKey() {
					throw new UnsupportedOperationException();
				}
			});

			DecodedJWT decodedJWT = JWT.require(algorithm).build().verify(jwtToken);

			jwtResponse.setExp(decodedJWT.getClaim(Constants.EXPIRATION).asLong());
			jwtResponse.setIat(decodedJWT.getClaim(Constants.ISSUED_AT_TIME).asLong());
			jwtResponse.setJti(decodedJWT.getClaim(Constants.JWT_ID).asString());
			jwtResponse.setMid(decodedJWT.getClaim(Constants.MERCHANT_ID).asString());
			jwtResponse.setMbn(decodedJWT.getClaim(Constants.MERCHANT_BANNER_NAME).asString());
			jwtResponse.setMcc(decodedJWT.getClaim(Constants.MERCHANT_CATEGORY_CODE).asInt());
			jwtResponse.setTpid(decodedJWT.getClaim(Constants.TERMINAL_PROFILE_ID).asString());
			jwtResponse.setAas(decodedJWT.getClaim(Constants.ALLOWED_APP_BUNDLE_IDS).asArray(String.class));
			jwtResponse.setAvs(decodedJWT.getClaim(Constants.ALLOWED_CARD_READER_IDS).asArray(String.class));
			jwtResponse.setMaa(decodedJWT.getClaim(Constants.MERCHANT_ALLOWED_ACTIONS).asArray(String.class));
			jwtResponse.setResponseCode("200");

		} catch (Exception ex) {

			if (ex.getMessage().contains("The Token has expired")) {
				logger.error(ex.getMessage());
				throw new TokenExpiryException("401", "Token has expired");
			} else {
				throw new InvalidApplicationException("500", "Internal Server error");
			}
		}
		return jwtResponse;
	}

	public String refreshJwtToken(JwtTokenGenerateRequest jwtTokenGenerateRequest, String jwtToken,
			boolean refreshToken) throws InvalidApplicationException, RequestProcessException {

		JwtResponse jwtResponse;
		try {
			jwtResponse = verifyJwtToken(jwtToken, refreshToken);
		} catch (InvalidApplicationException e) {
			e.printStackTrace();
		} catch (TokenExpiryException e) {
			e.printStackTrace();
			if (refreshToken) {
				return generateToken(jwtTokenGenerateRequest);
			}
		}
		return "";
	}

	@Override
	public String merchantJwtToken(MerchantTokenRequest request) throws InvalidApplicationException{

		JwtTokenGenerateRequest generateRequest = new JwtTokenGenerateRequest();
		generateRequest.setAud(request.getAud());
		generateRequest.setExp(request.getExp());
		generateRequest.setNbf(request.getNbf());
		generateRequest.setIat(request.getIat());
		generateRequest.setJti(request.getJti());
		generateRequest.setMid(request.getMid());
		generateRequest.setMbn(request.getMbn());
		generateRequest.setMcc(request.getMcc());

		return generateToken(generateRequest);
	}

	public ECPublicKey getRsaPublicKey() {

		ECPublicKey rsaPublicKey = null;
		try {
			try {
				publicKeyPemFile = FileUtil.getFileFromResourceAsStream(getClass().getClassLoader(),publicKeyPath);

			} catch (IOException e) {

				e.printStackTrace();
			}
			rsaPublicKey = RSACrypto.getECPublicKey(publicKeyPemFile);

		} catch (Exception e) {

			e.printStackTrace();
		}
		return rsaPublicKey;
	}

	public ECPrivateKey getRsaPrivateKey() {

		ECPrivateKey rsaPrivateKey = null;
		try {
			try {
				privatekeyPemFile = FileUtil.getFileFromResourceAsStream(getClass().getClassLoader(),privateKeyPath);
			} catch (IOException e) {

				e.printStackTrace();
			}
			rsaPrivateKey = RSACrypto.getECPrivateKey(privatekeyPemFile);
		} catch (Exception e) {

			e.printStackTrace();
		}
		return rsaPrivateKey;
	}

	

}
