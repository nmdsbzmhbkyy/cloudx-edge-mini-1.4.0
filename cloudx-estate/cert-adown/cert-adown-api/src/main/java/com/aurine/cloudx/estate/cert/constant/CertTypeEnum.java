package com.aurine.cloudx.estate.cert.constant;


import lombok.AllArgsConstructor;
import lombok.Getter;


/**
 * @description: 凭证类型
 * @author: wangwei
 * @date: 2021/12/14 15:14
 **/
@Getter
@AllArgsConstructor
public enum CertTypeEnum {
	CARD("0"),
	FACE("1"),
	/**
	 * 未定义类型
	 */
	UNDEFINED("-1");


	private final String code;

	/**
	 * @param type
	 * @return
	 */
	public static CertTypeEnum getEnum(String type) {
		for (CertTypeEnum value : CertTypeEnum.values()) {
			if (value.code.equals(type)) {
				return value;
			}
		}
		return UNDEFINED;
	}


}
