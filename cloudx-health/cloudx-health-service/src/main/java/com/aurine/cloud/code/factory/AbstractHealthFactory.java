package com.aurine.cloud.code.factory;

import com.aurine.cloud.code.platform.PassWayHealthService;

/***
 *
 * 区分不同地区健康码工厂
 * @ClassName: AbstractHealthFactory
 * @author: yz <wangwei@aurine.cn>
 */
public abstract class AbstractHealthFactory {

    /**
     * 获取不同健康码验证实例
     * @param versionNumberPlaceName 版本名
     * @return
     */
    public abstract PassWayHealthService getPassWayHealthService(String versionNumberPlaceName);


}
