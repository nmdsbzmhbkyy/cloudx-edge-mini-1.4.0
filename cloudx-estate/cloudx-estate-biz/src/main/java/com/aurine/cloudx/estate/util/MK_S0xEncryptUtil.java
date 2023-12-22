package com.aurine.cloudx.estate.util;

public class MK_S0xEncryptUtil {
	public static byte[] xorEncode(byte[] data, String key) {
		// key，用于异或

		byte[] keyBytes = key.getBytes();

		byte[] encryptBytes = new byte[data.length];
		for (int i = 0; i < data.length; i++) {
			encryptBytes[i] = (byte) (data[i] ^ keyBytes[i % keyBytes.length]);
		}
		return encryptBytes;
	}
	
	public static byte[] xorEncode(byte[] data, byte[] keyBytes) {
		// key，用于异或

		byte[] encryptBytes = new byte[data.length];
		for (int i = 0; i < data.length; i++) {
			encryptBytes[i] = (byte) (data[i] ^ keyBytes[i % keyBytes.length]);
		}
		return encryptBytes;
	}

	public static byte[] andEncode(byte[] data, String key) {
		// key，用于与
		byte[] keyBytes = key.getBytes();

		byte[] encryptBytes = new byte[data.length];
		for (int i = 0; i < data.length; i++) {
			encryptBytes[i] = (byte) (data[i] & keyBytes[i % keyBytes.length]);
		}
		return encryptBytes;
	}

	public static byte[] andEncode(byte[] data, byte[] keyBytes) {
		// key，用于与

		byte[] encryptBytes = new byte[data.length];
		for (int i = 0; i < data.length; i++) {
			encryptBytes[i] = (byte) (data[i] & keyBytes[i % keyBytes.length]);
		}
		return encryptBytes;
	}

	public static byte[] notEncode(byte[] data) {

		byte[] encryptBytes = new byte[data.length];
		for (int i = 0; i < data.length; i++) {
			encryptBytes[i] = (byte) ~data[i];
		}
		return encryptBytes;

	}
	

	private static final char[] HEX_CHAR = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E',
			'F' };

	/**
	 * 16进制转btye[]
	 * 
	 * @param str
	 * @return
	 */
	public static byte[] toBytes(String str) {
		if (str == null || str.trim().equals("")) {
			return new byte[0];
		}
		byte[] bytes = new byte[str.length() / 2];
		for (int i = 0; i < str.length() / 2; i++) {
			String subStr = str.substring(i * 2, i * 2 + 2);
			bytes[i] = (byte) Integer.parseInt(subStr, 16);
		}
		return bytes;
	}

	/**
	 * 十进制转btye[]
	 * 
	 * @param str
	 * @return
	 */
	public static byte[] toBytes2(String str) {
		if (str == null || str.trim().equals("")) {
			return new byte[0];
		}
		byte[] bytes = new byte[str.length() / 2];
		for (int i = 0; i < str.length() / 2; i++) {
			String subStr = str.substring(i * 2, i * 2 + 2);
			bytes[i] = (byte) Integer.parseInt(subStr);
		}
		return bytes;
	}

	public static String bytesToHexFun2(byte[] bytes) {
		char[] buf = new char[bytes.length * 2];
		int index = 0;
		for (byte b : bytes) {
			// 利用位运算进行转换，可以看作方法一的变种
			buf[index++] = HEX_CHAR[b >>> 4 & 0xf];
			buf[index++] = HEX_CHAR[b & 0xf];
		}
		return new String(buf);
	}

	/**
	 * 字符串转换为Ascii，16进制
	 * 
	 * @param value
	 * @return
	 */
	public static String stringToHexAscii(String value) {
		StringBuffer sbu = new StringBuffer();
		char[] chars = value.toCharArray();
		for (int i = 0; i < chars.length; i++) {

			sbu.append(Integer.toHexString((int) chars[i]));

		}
		return sbu.toString();
	}

	/**
	 * 16进制Ascii转换为字符串
	 * 
	 * @param value
	 * @return
	 */
	public static String hexAsciiToString(String value) {
		StringBuffer sbu = new StringBuffer();
		int size = value.length() / 2;
		for (int i = 0; i < size; i++) {

			String val = value.substring(i * 2, (i + 1) * 2);
			sbu.append((char) Integer.parseInt(val, 16));
		}
		return sbu.toString();
	}


}
