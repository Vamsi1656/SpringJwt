package com.ds.auth.jwt.crypto;

import java.io.IOException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import org.apache.commons.codec.binary.Base64;

public class RSACrypto {

    public static ECPublicKey getECPublicKey(String pemPublicKeyFilepath) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException{
    	final KeyFactory keyPairGenerator = KeyFactory.getInstance("EC"); // EC is ECDSA in Java
    	return (ECPublicKey) keyPairGenerator.generatePublic(new X509EncodedKeySpec(Base64.decodeBase64(removeEncapsulationBoundaries(pemPublicKeyFilepath))));
    }
    
    public static ECPrivateKey getECPrivateKey(String pemPrivateKeyFilepath) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException{
    	final KeyFactory keyPairGenerator = KeyFactory.getInstance("EC"); // EC is ECDSA in Java
    	return (ECPrivateKey) keyPairGenerator.generatePrivate(new PKCS8EncodedKeySpec(Base64.decodeBase64(removeEncapsulationBoundaries(pemPrivateKeyFilepath))));
    }
    
    private static String removeEncapsulationBoundaries(String key) {
        return key.replaceAll("\n", "")
                .replaceAll(" ", "")
                .replaceAll("-{5}[a-zA-Z]*-{5}", "");
   }




}
