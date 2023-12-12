package com.ds.auth.jwt.utils;

import java.io.IOException;
import java.io.InputStream;

import com.nimbusds.jose.util.IOUtils;

public class FileUtil {
	
	public static String getFileFromResourceAsStream(ClassLoader classLoader,String fileName) throws IOException {
		// The class loader that loaded the class
//		ClassLoader classLoader;
		try {
//			classLoader = getClass().getClassLoader();
			InputStream inputStream = classLoader.getResourceAsStream(fileName);
			// the stream holding the file content
			if (inputStream == null) {
				throw new IllegalArgumentException("file not found! " + fileName);
			} else {
				return IOUtils.readInputStreamToString(inputStream);
			}
		} catch (Exception e) {
		
			e.printStackTrace();
		}
		return null;
		
	}

}
