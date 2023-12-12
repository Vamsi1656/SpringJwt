package com.ds.auth.jwt.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ObjectValidationUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(ObjectValidationUtil.class);

    public static boolean isNull(Object obj) {
		return obj == null;
	}
}
