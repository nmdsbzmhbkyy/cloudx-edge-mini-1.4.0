package com.aurine.cloudx.estate.util.security;

import org.bouncycastle.crypto.digests.SM3Digest;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;

import java.io.UnsupportedEncodingException;
import java.security.Security;

public class Sm3Util {
    private static final String ENCODING = "iso-8859-1";
    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public static String encrypt(String paramStr){
        String resultHexString = "";
        try {

            byte[] srcData = paramStr.getBytes(ENCODING);
            byte[] resultHash = hash(srcData);
            resultHexString = ByteUtils.toHexString(resultHash);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return  resultHexString;
    }

    public static byte[] hash(byte[] srcData){
        SM3Digest digest = new SM3Digest();
        digest.update(srcData, 0, srcData.length);
        byte[] hash = new byte[digest.getDigestSize()];
        digest.doFinal(hash, 0);
        return hash;
    }

//    public static void main(String[] args) {
//        System.out.println(encrypt("abc"));
//    }
}
