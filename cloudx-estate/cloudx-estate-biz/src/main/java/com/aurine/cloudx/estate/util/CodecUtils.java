package com.aurine.cloudx.estate.util;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

import java.security.MessageDigest;
import java.util.UUID;

public class CodecUtils {

	public final static String MD5(String str) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
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

	public final static String MD5(byte[] str) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
		try {
			byte[] btInput = str;
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

	public static String getMD516(String source) {
		String md532 = MD5(source);
		String md516 = null;

		if (md532 != null) {
			md516 = md532.substring(8, 24);
		}

		return md516;
	}

	/**
	 * MD5加密，采用12位大写，第7位到19位。
	 * 
	 * @param source
	 * @return
	 */
	public static String getMD512_7_19(String source) {
		String md532 = MD5(source);
		String md516 = null;
		if (md532 != null) {
			md516 = md532.substring(7, 19);
		}

		return md516;
	}

	/**
	 * MD5加密，采用12位大写，第6位到12位。
	 * 
	 * @param source
	 * @return
	 */
	public static String getMD512_6_12(String source) {
		String md532 = MD5(source);
		String md516 = null;
		if (md532 != null) {
			md516 = md532.substring(6, 12);
		}

		return md516;
	}
}
