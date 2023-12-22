package com.aurine.cloudx.estate.util.security;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.Security;
import java.util.Base64;


public class AES128 {
	/**
	 * Aes128 加密
	 * 
	 * @author csy
	 * @date 2019年3月8日
	 */
	public static String encrypt(String sSrc,String charsetName) {
		String sKey = "eimKGGm!B3Fn47TD";
		String ivKey = "3zcI#vOkxL8uJLZ8";

		try {
			byte[] raw = sKey.getBytes();
			SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");// "算法/模式/补码方式"
			IvParameterSpec iv = new IvParameterSpec(ivKey.getBytes());// 使用CBC模式，需要一个向量iv，可增加加密算法的强度
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
			byte[] encrypted = cipher.doFinal(sSrc.getBytes(charsetName));
			String str = new String(Base64.getEncoder().encode(encrypted), charsetName);
			// new BASE64Encoder().encode(encrypted);
			str = str.replaceAll("\r\n", "");
			str = str.replaceAll("\n", "");
			str = str.replaceAll("\r", "");
//			System.out.println("AES-128-CBC 加密后 ==>> " + str);
			return str;// 此处使用BASE64做转码功能，同时能起到2次加密的作用。


		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String decrypt(String text,String charsetName) {
		try {
			String password = "eimKGGm!B3Fn47TD";
			byte[] cryptograph = text.getBytes();

			SecretKeySpec key = new SecretKeySpec(password.getBytes(charsetName), "AES");

			// 新增加密算法提供者
			Security.addProvider(new BouncyCastleProvider());

			// 创建一个密码对应，并指定提供者
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding", BouncyCastleProvider.PROVIDER_NAME);

			String ivStr = "3zcI#vOkxL8uJLZ8";
			// System.out.println("IV: " + ivStr);
			IvParameterSpec iv = new IvParameterSpec(ivStr.getBytes(charsetName));
			cipher.init(Cipher.DECRYPT_MODE, key, iv);
			System.out.println(new String(Base64.getDecoder().decode(cryptograph)));
			byte[] content = cipher.doFinal(Base64.getDecoder().decode(cryptograph));
			return new String(content, charsetName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
//	public static void main(String[] args) {
//
//		//System.out.println(Base64.getDecoder().decode("oa9uRZtbz4ZN7m148cyXJVQBAGr+uNH0nNXqoDSgZNPESR1Kjv3b7Kfpro3qx5i0"));
//		String a = decrypt("9d4HR8sqsU38ahjZi5C18OXPIneAiORHo+2661P6w6o=","iso-8859-1");
//		System.out.println(a);
//		byte[] bytes = new byte[0];
//		try {
//			bytes = a.getBytes("iso-8859-1");
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
//		System.out.println(bytes.length);
//		for (byte b:
//				bytes) {
//			System.out.print(Integer.toHexString(b&0xff)+",");
//		}
//
//	}

	public static byte[] hexStrToByteArray(String str)
	{
		if (str == null) {
			return null;
		}
		if (str.length() == 0) {
			return new byte[0];
		}
		byte[] byteArray = new byte[str.length() / 2];
		for (int i = 0; i < byteArray.length; i++){
			String subStr = str.substring(2 * i, 2 * i + 2);
			byteArray[i] = ((byte)Integer.parseInt(subStr, 16));
		}
		return byteArray;
	}
}
