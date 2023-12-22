package com.aurine.cloudx.dashboard.service.impl;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.dashboard.config.TestConfig;
import com.aurine.cloudx.dashboard.constant.ServiceNameEnum;
import com.aurine.cloudx.dashboard.constant.VersionEnum;
import com.aurine.cloudx.dashboard.exception.DashboardErrorEnum;
import com.aurine.cloudx.dashboard.exception.DashboardException;
import com.aurine.cloudx.dashboard.service.AbstractDashboardService;
import com.aurine.cloudx.dashboard.util.JSONUtil;
import com.aurine.cloudx.dashboard.vo.DashboardRequestVo;
import com.aurine.cloudx.dashboard.vo.Result;
import com.aurine.cloudx.dashboard.websocket.WebSocketUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @version 1
 * @description:测试用
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2021-03-25
 * @Copyright:
 */
@Service
public class TestEntranceEventAlarmServiceV1 extends AbstractDashboardService {
    @Resource
    TestConfig testConfig;
    /**
     * 获取数据
     *
     * @param request
     * @return
     */
    @Override
    public JSONObject getData(DashboardRequestVo request) {

//        JSONObject dataJson = new JSONObject();

        /**
         "personName": "",
         "picUrl": "",
         "eventDesc": "防拆报警",
         "createTime": "2021-03-26 16:05:59",
         "personType": "",
         "deviceId": "0000",
         "deviceName": "A区一期-1栋01单元梯口机2.0（测试专用）",
         "projectId": "1000000696"


         */
//        dataJson.put("createTime", DateUtil.now());
//        dataJson.put("personType", "");
//        dataJson.put("deviceId", "a4c5658a9cd2976491f6e57ffb878828");
//        dataJson.put("deviceName", "1栋01单元梯口机");
//        dataJson.put("projectId", "1000000456");
//        dataJson.put("eventDesc", "防拆报警");
//        dataJson.put("picUrl", "https://icloudobs.aurine.cn:7003/eventserver/community/S1000000523/device/PFOQP27908029A2YMGRT/PFOQP27908029A2YMGRT_1617950321.jpg");
//        dataJson.put("personName", "");

        JSONObject vo = testConfig.getAlarmEvent(request.getParamsData().getString("id"));
        if(vo == null) throw  new DashboardException(DashboardErrorEnum.EMPTY_RESULT);

        vo.put("createTime", DateUtil.now());

        WebSocketUtil.sendMessgae(Result.ok(JSONUtil.toJSONObject(vo), request.getProjectIdArray()[0], "entranceAlarmEvent", "1"), request.getProjectIdArray()[0], "entranceAlarmEvent", "1");

        return JSONUtil.toJSONObject(vo);
    }


    /**
     * 获取服务名称
     *
     * @return
     */
    @Override
    public String getServiceName() {
        return ServiceNameEnum.TEST_ENTRANCE_ALARM.serviceName;
    }

    /**
     * 获取版本号
     *
     * @return
     */
    @Override
    public String getVersion() {
        return VersionEnum.V1.number;
    }
}
