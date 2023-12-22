package com.aurine.cloudx.estate.cert.util;


/**
 * @description:UUID工具包
 * @author: wangwei
 * @date: 2021/12/15 17:37
 **/
public class UUID {


	/**
	 * 生成32位UUID 不带 "-"
	 *
	 * @return
	 */
	public static String randomUUID() {
		return java.util.UUID.randomUUID().toString().replace("-", "");
	}

}
