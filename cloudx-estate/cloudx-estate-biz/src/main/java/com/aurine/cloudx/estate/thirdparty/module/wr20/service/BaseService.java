package com.aurine.cloudx.estate.thirdparty.module.wr20.service;

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
     * 验证当前项目是否可以执行业务代码
     * @return
     */
    default  boolean useable(int projecrId){
        return false;
    }

}
