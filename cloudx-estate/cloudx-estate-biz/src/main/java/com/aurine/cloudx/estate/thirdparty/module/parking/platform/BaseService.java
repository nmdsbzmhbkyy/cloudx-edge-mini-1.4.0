package com.aurine.cloudx.estate.thirdparty.module.parking.platform;

/**
 * @ClassName: RemoteBaseService
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020/6/23 9:54
 * @Copyright:
 */
public interface BaseService {
    /**
     * 获取版本
     *
     * @return
     */
    public String getVersion();

    /**
     * 获取平台类型
     *
     * @return
     */
    public String getPlatform();
}
