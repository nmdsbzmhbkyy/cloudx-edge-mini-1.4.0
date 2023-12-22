package com.aurine.cloudx.estate.cert.exception.code;


import lombok.AllArgsConstructor;
import lombok.Getter;


/**
 * @description: 异常处理类型
 * @author: wangwei
 * @date: 2021/12/17 15:41
 **/
@AllArgsConstructor
@Getter
public enum HandleType {


	TO_DB(1),
	TO_CONSOLE(2);

	private int code;


}
