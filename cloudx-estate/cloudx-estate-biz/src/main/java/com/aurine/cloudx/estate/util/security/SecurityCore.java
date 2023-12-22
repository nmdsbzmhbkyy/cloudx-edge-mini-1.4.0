package com.aurine.cloudx.estate.util.security;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


public class SecurityCore {
    /*
     * 加密用的Key 可以用26个字母和数字组成 此处使用AES-128-CBC加密模式，key需要为16位。
     */
    private String sKey        = "eimKGGm!B3Fn47TD";
    private String ivParameter = "3zcI#vOkxL8uJLZ8";
    private static SecurityCore instance = null;

    private SecurityCore() {

    }

    public static SecurityCore getInstance() {
        if (instance == null) {
            instance = new SecurityCore();
        }
        return instance;
    }

    public static String webSafeBase64StringEncoding(byte[] sSrc,boolean padded) throws Exception {
        String encodeString= Base64.encodeBase64String(sSrc);// 此处使用BASE64做转码。

        //websafe base64
        encodeString=encodeString.replace("+","-");
        encodeString=encodeString.replace("/","_");

        //nopadding base64
        if (!padded) {
            if (encodeString.endsWith("=")) {
                encodeString = encodeString.substring(0, encodeString.length() - 1);
                if (encodeString.endsWith("=")) {
                    encodeString = encodeString.substring(0, encodeString.length() - 1);
                }
            }
        }
        return encodeString;
    }

    public static byte[] webSafeBase64StringDecoding(String sSrc) throws Exception {
        //websafe base64
        sSrc=sSrc.replace("-","+");
        sSrc=sSrc.replace("_","/");

        return Base64.decodeBase64(sSrc);
    }

    public static String base64StringEncoding(byte[] sSrc,boolean padded) throws Exception {
        String encodeString= Base64.encodeBase64String(sSrc);// 此处使用BASE64做转码。

        //nopadding base64
        if (!padded) {
            if (encodeString.endsWith("=")) {
                encodeString = encodeString.substring(0, encodeString.length() - 1);
                if (encodeString.endsWith("=")) {
                    encodeString = encodeString.substring(0, encodeString.length() - 1);
                }
            }
        }
        return encodeString;
    }

    public static byte[] base64StringDecoding(String sSrc) throws Exception {
        return Base64.decodeBase64(sSrc);
    }

    public static byte[] AES128CBCStringEncoding(String encData ,String secretKey,String vector) throws Exception {

        if(secretKey == null) {
            return null;
        }
        if(secretKey.length() != 16) {
            return null;
        }
        if (vector != null && vector.length() != 16) {
            return null;
        }
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        byte[] raw = secretKey.getBytes();
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        IvParameterSpec iv = new IvParameterSpec(vector.getBytes());// 使用CBC模式，需要一个向量iv，可增加加密算法的强度
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
        byte[] encrypted = cipher.doFinal(encData.getBytes("utf-8"));

        return encrypted;
    }

    public static String AES128CBCStringDecoding(byte[] sSrc,String key,String ivs) throws Exception {
        try {
            if(key == null) {
                return null;
            }
            if(key.length() != 16) {
                return null;
            }
            if (ivs != null && ivs.length() != 16) {
                return null;
            }
            byte[] raw = key.getBytes("ASCII");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec iv = new IvParameterSpec(ivs.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] original = cipher.doFinal(sSrc);
            String originalString = new String(original, "utf-8");
            return originalString;
        } catch (Exception ex) {
            return null;
        }
    }


    // 加密
    public String encrypt(String sSrc) throws Exception {
        try {

            //System.out.println(new String(AES128CBCStringEncoding(sSrc,sKey,ivParameter)));
            String encodeString=base64StringEncoding(AES128CBCStringEncoding(sSrc,sKey,ivParameter),false);

            return encodeString;
        } catch (Exception ex) {
            return null;
        }
    }

    // 解密
    public String decrypt(String sSrc) throws Exception {
        try {
            String decodeString=AES128CBCStringDecoding(base64StringDecoding(sSrc),sKey,ivParameter);
            return decodeString;
        } catch (Exception ex) {
            return null;
        }
    }

//    //test
//    public static void main(String[] args) throws Exception {
//        // 需要加密的字串
//        String cSrc = "123";
//
//        // 加密
//        long lStart = System.currentTimeMillis();
//        String enString = SecurityCore.getInstance().encrypt(cSrc);
//        System.out.println("加密后的字串是：" + enString);
//
//        long lUseTime = System.currentTimeMillis() - lStart;
//        System.out.println("加密耗时：" + lUseTime + "毫秒");
//        // 解密
//        lStart = System.currentTimeMillis();
//        String DeString = SecurityCore.getInstance().decrypt(enString);
//        System.out.println("解密后的字串是：" + DeString);
//        lUseTime = System.currentTimeMillis() - lStart;
//        System.out.println("解密耗时：" + lUseTime + "毫秒");
//    }
}
