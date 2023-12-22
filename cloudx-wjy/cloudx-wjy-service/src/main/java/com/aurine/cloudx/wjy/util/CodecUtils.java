package com.aurine.cloudx.wjy.util;

import org.apache.commons.lang3.RandomStringUtils;

import java.security.MessageDigest;
import java.util.UUID;

public class CodecUtils {

	public final static String MD5(String str) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'a', 'b', 'c', 'd', 'e', 'f' };
		try {
			byte[] btInput = str.getBytes();
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			mdInst.update(btInput);
			byte[] md = mdInst.digest();
			int j = md.length;
			char strs[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				strs[k++] = hexDigits[byte0 >>> 4 & 0xf];
				strs[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(strs);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public final static String MD5Upper(String str) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'A', 'B', 'C', 'D', 'E', 'F' };
		try {
			byte[] btInput = str.getBytes();
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			mdInst.update(btInput);
			byte[] md = mdInst.digest();
			int j = md.length;
			char strs[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				strs[k++] = hexDigits[byte0 >>> 4 & 0xf];
				strs[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(strs);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String getMD516(String source)
	{
		String md532 = MD5(source);
		String md516 = null;

		if (md532 != null)
		{
			md516 = md532.substring(8, 24);
		}

		return md516;
	}

	/**
	 * 生成UUID
	 * @return
	 */
	public static String getUUID() {  
		String idstr = UUID.randomUUID().toString().replace("-", "");
		return idstr.toUpperCase();
	}

	public static String getFileName(){
		return String.valueOf(System.currentTimeMillis())+ RandomStringUtils.randomNumeric(3);
	}	
}
