package com.aurine.cloudx.estate.cert.exception.code;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

/**
 * @description: 异常编码
 * @author: wangwei
 * @date: 2021/12/16 18:22
 **/
@AllArgsConstructor
@Getter
public enum ErrorCode {


	HTTP_OUT_OF_TIME(100301, HandleType.TO_DB, "连接超时"),
	HTTP_NOT_AUTH(100401, HandleType.TO_DB, "访问未被授权"),
	HTTP_URL_NOT_FOUND(100404, HandleType.TO_DB, "访问地址不存在"),
	HTTP_ERROR(100999, HandleType.TO_DB, "访问发生异常"),

	CONFIG_NO_CONFIG(200100, HandleType.TO_DB, "未找到APP配置"),
	NO_OBJECT(400100, HandleType.TO_CONSOLE, "未找到对应请求实体"),//通过reqId，在数据库和缓存中均为找到对应的数据，认为是脏数据
	/**
	 * 未知错误
	 */
	UNKNOWN(999999, HandleType.TO_DB, "未知错误");


	private int code;
	private HandleType handleType;
	private String desc;


	public static ErrorCode getErrorCode(int code) {
		for (ErrorCode errorCode : ErrorCode.values()) {
			if (errorCode.code == code) {
				return errorCode;
			}
		}
		return UNKNOWN;
	}
}
