package com.ds.auth.jwt.utils;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.ds.auth.jwt.exceptions.ExceptionMessages.ExceptionMessagesEnum;
import com.ds.auth.jwt.exceptions.RequestProcessException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
@Component
public class JsonUtil {
   
    private JsonUtil() {}

    public static final Logger logger = LoggerFactory.getLogger(JsonUtil.class);

    public static ObjectMapper getObjectMapper() {
		return new ObjectMapper();
	}

    public static Object getObjectFromRequest(HttpServletRequest request, Class<?> clasz)
			throws RequestProcessException {
		if (ObjectValidationUtil.isNull(request)) {
			logger.info(ExceptionMessagesEnum.NO_REQUEST_FOUND.getMessage());
			throw new RequestProcessException(ExceptionMessagesEnum.NO_REQUEST_FOUND.getMessage(),
					ExceptionMessagesEnum.NO_REQUEST_FOUND.getCode());
		}
		try {
			return getObjectMapper().readValue(request.getInputStream(), clasz);
		} catch (JsonParseException e) {
			logger.debug(ExceptionMessagesEnum.JSON_PARSE_EXCEPTION.getMessage());
			throw new RequestProcessException(ExceptionMessagesEnum.JSON_PARSE_EXCEPTION.getMessage(),
					ExceptionMessagesEnum.JSON_PARSE_EXCEPTION.getCode());
		} catch (JsonMappingException e) {
			logger.debug(ExceptionMessagesEnum.JSON_MAPPING_EXCEPTION.getMessage());
			throw new RequestProcessException(ExceptionMessagesEnum.JSON_MAPPING_EXCEPTION.getMessage(),
					ExceptionMessagesEnum.JSON_MAPPING_EXCEPTION.getCode());
		} catch (IOException e) {
			logger.debug(ExceptionMessagesEnum.INPUTSTREAM_EXCEPTION_IO.getMessage());
			throw new RequestProcessException(ExceptionMessagesEnum.INPUTSTREAM_EXCEPTION_IO.getMessage(),
					ExceptionMessagesEnum.INPUTSTREAM_EXCEPTION_IO.getCode());
		}
	}
    
    public static String getJsonStringFromObject(Object object) {
    	
    	try {
			return new ObjectMapper().writeValueAsString(object);
		} catch (JsonProcessingException e) {
			
			e.printStackTrace();
		}
		return null;
    	
    }

}
