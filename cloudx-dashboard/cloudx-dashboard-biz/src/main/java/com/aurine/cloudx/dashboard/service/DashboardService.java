package com.aurine.cloudx.dashboard.service;

import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.dashboard.vo.DashboardRequestVo;

/**
 * @description:
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2021-03-24
 * @Copyright:
 */
public interface DashboardService {

    /**
     * 获取数据
     *
     * @param request
     * @return
     */
    JSONObject getData(DashboardRequestVo request) throws Exception;

    /**
     * 获取服务名称
     *
     * @return
     */
    String getServiceName();

    /**
     * 获取版本号
     *
     * @return
     */
    String getVersion();


}
