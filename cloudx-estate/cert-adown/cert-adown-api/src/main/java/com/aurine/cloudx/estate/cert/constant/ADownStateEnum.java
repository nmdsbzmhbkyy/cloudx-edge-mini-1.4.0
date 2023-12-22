package com.aurine.cloudx.estate.cert.constant;


import lombok.AllArgsConstructor;
import lombok.Getter;


/**
 * @description: 下发状态枚举
 * @author: wangwei
 * @date: 2021/12/14 14:23
 **/
@Getter
@AllArgsConstructor
public enum ADownStateEnum {

	WAITING("0", "待下载"),
	QUEUE("1", "等待队列中"),//等待队列中的任务，再重启后需要重新抓取到队列
	DOWNING("2", "下载中"),
	SUCCESS("3", "已下载"),
	FAIL("4", "下载失败"),
	OUT_OF_TIME("5", "超时"),
	ERROR("9", "异常");

	private final String code;
	private final String desc;

	/**
	 * @param code
	 * @return
	 */
	public static ADownStateEnum getEnum(String code) {
		for (ADownStateEnum value : ADownStateEnum.values()) {
			if (value.code.equals(code)) {
				return value;
			}
		}
		return ERROR;
	}


}
