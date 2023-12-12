package com.ds.auth.jwt.exceptions;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ExceptionMessages {

    private ExceptionMessages(){}

    public enum ExceptionMessagesEnum {
  		
        INVALID_APPLICATION_NAME_EXCEPTION("Invalid Application for Authentication",ExceptionCodes.INVALID_APPLICATION_NAME_EXCEPTION_CODE),
		JWT_TOKEN_BLOCKED_EXCEPTION("User is Blocked,Please contact Support team",ExceptionCodes.JWT_TOKEN_BLOCKED_EXCEPTION_CODE),
		INVALID_JWT_TOKEN_EXCEPTION("Invalid Request,Please contact Support team",ExceptionCodes.INVALID_JWT_TOKEN_EXCEPTION_CODE),
		INVALID_JWT_HEADER_EXCEPTION("Invalid Request,Please contact Support team",ExceptionCodes.INVALID_JWT_ID_EXCEPTION_CODE),
		UNAUTHORIZED_USER_JWT_TOKEN_EXCEPTION("Unauthorized User Access. Please login again",ExceptionCodes.UNAUTHORIZED_USER_ACCESS_EXCEPTION_CODE),
		JWT_TOKEN_EXPIRED_EXCEPTION("Jwt Token Expired",ExceptionCodes.TOKEN_EXPIRED_EXCEPTION_CODE),
    	NO_REQUEST_FOUND("Exception while parsing request.Reqeust is 'NULL'",ExceptionCodes.NO_REQEUST_FOUND_CODE),
        JSON_PARSE_EXCEPTION("Exception while parsing request",ExceptionCodes.JSON_PARSING_EXCEPTION_CODE),
        JSON_MAPPING_EXCEPTION("Exception while mapping request to object",ExceptionCodes.JSON_MAPPING_EXCEPTION_CODE),
        MANDATORY_DATA_EXCEPTION("Exception while mandatory data is missing",ExceptionCodes.MANDATORY_DATA_EXCEPTION_CODE),
        INPUTSTREAM_EXCEPTION_IO("Exception while reading data from provided request",ExceptionCodes.REQUEST_INPUTSTREAM_IO_EXCEPTION_CODE);
		
        private String message;
        private String code;
        private static Map<String, String> map = Collections.unmodifiableMap(initializeMapFromEnum());
        
        ExceptionMessagesEnum(String message, String code){
            this.message = message;
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public String getCode() {
            return code;
        }

        public static String getMessageByCode(String errorCode){
            if(map.containsKey(errorCode)) {
                return map.get(errorCode);
            }
            return null;
        }

        private static HashMap<String, String> initializeMapFromEnum() {
            HashMap<String, String> enumMap = new HashMap<>();
            for(ExceptionMessagesEnum e : ExceptionMessagesEnum.values()){
                enumMap.put(e.getCode(), e.getMessage());
            }            
            return enumMap;
        }

     }
}
