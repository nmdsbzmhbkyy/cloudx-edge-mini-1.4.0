package com.aurine.cloudx.open.common.core.util.security;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

import java.security.MessageDigest;
import java.util.UUID;

public class MD5Util {

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

	/**
	 * 生成UUID
	 * 
	 * @return
	 */
	public static String getUUID() {
		String idstr = UUID.randomUUID().toString().replace("-", "");
		return idstr.toUpperCase();
	}

	public static String getFileName() {
		return String.valueOf(System.currentTimeMillis()) + RandomStringUtils.randomNumeric(3);
	}

	/**
	 * 生成socket流水号
	 * 
	 * @return
	 */
	public static String getSeqno() {
		return String.valueOf(System.currentTimeMillis()) + RandomStringUtils.randomNumeric(3);
	}

	private final static String SALT = "iot.2015";

	public static String getAppKey(String id) {
		return getMD516(SALT + id + "app");
	}

	public static String getAppSecret(String id, String key) {
		// return MD5(SALT+id+key+"app");
		return getUUID();
	}

	public static String getDevlopKey(String id) {
		return getMD516(SALT + id + "developer");
	}

	public static String getDevlopSecret(String id, String key) {
		// return MD5(SALT+id+key+"developer");
		return getUUID();
	}

	private final static String PASSWORD_SALT = "iot.2015";

	public static String password(String str) {
		if (StringUtils.isEmpty(str)) {
			throw new IllegalArgumentException();
		}
		return MD5(PASSWORD_SALT + str);
	}



	/**
	 * 用户私钥,16位
	 * 
	 * @return
	 */
	public static String getPrivateKey(String account) {
		String tmp_token = account + ";" + ";" + SALT;
		return MD5Util.getMD516(tmp_token);
	}

}
