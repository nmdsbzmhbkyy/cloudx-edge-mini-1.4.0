package com.aurine.cloudx.estate.thirdparty.business.aop;

import java.lang.annotation.*;

/**
 * 用于标记第三方业务对接方法实现类
 * 如希望当前实现方法被自动调用，需要使用本注解进行标记
 *
 *
 * @description:
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-12-11
 * @Copyright:
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface ThirdPartyBusinessApiEnable {

}
