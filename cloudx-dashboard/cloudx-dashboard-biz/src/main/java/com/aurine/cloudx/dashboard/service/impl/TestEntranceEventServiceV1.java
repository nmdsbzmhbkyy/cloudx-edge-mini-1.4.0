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
import lombok.Data;
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
public class TestEntranceEventServiceV1 extends AbstractDashboardService {
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

        JSONObject dataJson = new JSONObject();

        /**
         "personName": "张雯",
         "picUrl": "",
         "eventDesc": "使用远程开门",
         //         "createTime": "2021-03-26 14:46:25",
         //         "personType": "1",
         //         "projectId": "1000000456",
         "deviceName": "1栋01单元梯口机",
         "deviceId": "PDOQX276080299ICNB2J"

         */

//        dataJson.put("createTime", DateUtil.now());
//        dataJson.put("personType", "1");
//        dataJson.put("deviceId", "421e0e587c9d0f0fef8f0f1d0df98c1f");
//        dataJson.put("deviceName", "主入口人行门");
//        dataJson.put("projectId", "1000000456");
//        dataJson.put("eventDesc", "使用远程开门");
//        dataJson.put("picUrl", "https://icloudobs.aurine.cn:7003/eventserver/community/S1000000456/device/PYOQR276080299I6R81Q/PYOQR276080299I6R81Q_1618536187.jpg");
//        dataJson.put("personName", "李勇");


        JSONObject vo = testConfig.getEntranceEvent(request.getParamsData().getString("id"));
        if(vo == null) throw  new DashboardException(DashboardErrorEnum.EMPTY_RESULT);

        vo.put("createTime", DateUtil.now());

        WebSocketUtil.sendMessgae(Result.ok(JSONUtil.toJSONObject(vo), request.getProjectIdArray()[0], "entranceEvent", "1"), request.getProjectIdArray()[0], "entranceEvent", "1");
        return JSONUtil.toJSONObject(vo);
    }


    /**
     * 获取服务名称
     *
     * @return
     */
    @Override
    public String getServiceName() {
        return ServiceNameEnum.TEST_ENTRANCE.serviceName;
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
