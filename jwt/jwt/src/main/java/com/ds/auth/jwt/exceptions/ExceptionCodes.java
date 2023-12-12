package com.ds.auth.jwt.exceptions;

public class ExceptionCodes {
    private ExceptionCodes(){
        throw new IllegalStateException("Cannot create instance for " + ExceptionCodes.class.getName() + " class");
    }

    public static final String TOKEN_EXPIRED_EXCEPTION_CODE = "ERR_100";
    public static final String PLATFORM_NOT_SUPPORTED_EXCEPTION_CODE = "ERR_101";
    public static final String INVALID_APPLICATION_NAME_EXCEPTION_CODE = "ERR_102";
    public static final String JWT_TOKEN_BLOCKED_EXCEPTION_CODE = "ERR_103";
    public static final String INVALID_JWT_TOKEN_EXCEPTION_CODE = "ERR_104";
    public static final String UNAUTHORIZED_USER_ACCESS_EXCEPTION_CODE = "ERR_105";
    public static final String INVALID_JWT_ID_EXCEPTION_CODE = "ERR_400";

    // Request Exception codes
	public static final String NO_REQEUST_FOUND_CODE = "ERR_201";

    // JSON Exception codes
	public static final String JSON_PARSING_EXCEPTION_CODE = "ERR_202";
	public static final String JSON_MAPPING_EXCEPTION_CODE = "ERR_203";

    	// InputStream Exception codes
	public static final String REQUEST_INPUTSTREAM_IO_EXCEPTION_CODE = "ERR_204";
	public static final String MANDATORY_DATA_EXCEPTION_CODE = "ERR_205";
}
