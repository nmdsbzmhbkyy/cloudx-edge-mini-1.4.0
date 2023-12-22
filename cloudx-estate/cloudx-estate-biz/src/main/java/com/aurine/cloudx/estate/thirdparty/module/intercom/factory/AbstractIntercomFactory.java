package com.aurine.cloudx.estate.thirdparty.module.intercom.factory;

import com.aurine.cloudx.estate.thirdparty.module.intercom.platform.IntercomService;

/**
 * <p>云对讲抽象工厂</p>
 * @ClassName: AbstractIntercomFactory
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-11-12 17:21
 * @Copyright:
 */
public abstract class AbstractIntercomFactory {

    /**
     * 获取云对讲 接口实例
     *
     * @return
     */
    public abstract IntercomService getIntercomService();


}
