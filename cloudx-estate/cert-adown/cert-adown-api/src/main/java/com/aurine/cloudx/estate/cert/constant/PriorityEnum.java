package com.aurine.cloudx.estate.cert.constant;


import lombok.AllArgsConstructor;
import lombok.Getter;




/**
 * @description: 优先级枚举
 * @author: wangwei
 * @date: 2021/12/14 14:01
 **/
@Getter
@AllArgsConstructor
public enum PriorityEnum {
	/**
	 * 超驰级，最优先任务
	 */
	OVERRIDE(0),
	/**
	 * 第一优先 通常为用户添加凭证后的操作
	 */
	FIRST_PRIORITY(1),
	/**
	 *第二优先 通常为添加设备，替换设备等操作后产生的下载请求
	 */
	SECOND_PRIORITY(2),
	/**
	 * 第三优先 通常为重新下发流程的优先度
	 */
	THIRD_PRIORITY(3);


	private final Integer priority;


}
