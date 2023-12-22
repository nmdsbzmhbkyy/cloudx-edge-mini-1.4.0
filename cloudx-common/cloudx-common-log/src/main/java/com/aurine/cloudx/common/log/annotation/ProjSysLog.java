package com.aurine.cloudx.common.log.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 操作日志注解
 * @ClassName:  ProjSysLog   
 * @author: 林港 <ling@aurine.cn>
 * @date:   2020年6月22日 上午10:33:02      
 * @Copyright:
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ProjSysLog {

	/**
	 * 描述
	 *
	 * @return {String}
	 */
	String value();
}
