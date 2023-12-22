package com.aurine.cloudx.dashboard.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.dashboard.config.TestConfig;
import com.aurine.cloudx.dashboard.constant.ServiceNameEnum;
import com.aurine.cloudx.dashboard.constant.VersionEnum;
import com.aurine.cloudx.dashboard.entity.ProjectAlarmDay07Mview;
import com.aurine.cloudx.dashboard.entity.ProjectAlarmMview;
import com.aurine.cloudx.dashboard.exception.DashboardErrorEnum;
import com.aurine.cloudx.dashboard.exception.DashboardException;
import com.aurine.cloudx.dashboard.mapper.ProjectAlarmDay07Mapper;
import com.aurine.cloudx.dashboard.mapper.ProjectAlarmMapper;
import com.aurine.cloudx.dashboard.service.AbstractDashboardService;
import com.aurine.cloudx.dashboard.util.JSONUtil;
import com.aurine.cloudx.dashboard.vo.DashboardRequestVo;
import com.aurine.cloudx.dashboard.vo.Result;
import com.aurine.cloudx.dashboard.websocket.WebSocketUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @version 1
 * @description:测试用
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2021-03-25
 * @Copyright:
 */
@Service
public class TestParkingInServiceV1 extends AbstractDashboardService {
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
//
//        /**
//         * "plateNumber": "闽A 52482",
//         *         "projectId": "1000000277",
//         *         "enterTime": "2020-08-13 08:44:21",
//         *         "enterGateName": "入口车道1",
//         *         "enterPicUrl": "http://xxxxx.com/xxxx.jpg",
//         * "enterDeviceId": "0000"
//         */
//        dataJson.put("enterTime", DateUtil.now());
//
//        dataJson.put("enterGateName", "入口车道1");
//        dataJson.put("enterPicUrl", "http://sfm-img.oss-cn-shenzhen.aliyuncs.com/dd54d72a-3dad-4d3e-b8ab-b0fcfc9f549e20210416090414c?Expires=2565219861&OSSAccessKeyId=LTAIIux1rFeEycax&Signature=HxnOYiB3ojFr%2BfnBYvgIqej4Kbo%3D");
//        dataJson.put("plateNumber", "闽A 68G6T");
//        dataJson.put("projectId", request.getProjectIdArray()[0]);
//        dataJson.put("enterDeviceId", "bfbe1cd3903b5dd5e053be00a8c04b3c");

        JSONObject vo = testConfig.getParkingIn(request.getParamsData().getString("id"));
        if(vo == null) throw  new DashboardException(DashboardErrorEnum.EMPTY_RESULT);

        vo.put("enterTime", DateUtil.now());
        vo.put("mock", true);
        vo.put("fileName", request.getParamsData().getString("fileName"));

        WebSocketUtil.sendMessgae(Result.ok(JSONUtil.toJSONObject(vo), request.getProjectIdArray()[0], "parkEntranceHisIn", "1"), request.getProjectIdArray()[0], "parkEntranceHisIn", "1");

        return JSONUtil.toJSONObject(vo);
    }


    /**
     * 获取服务名称
     *
     * @return
     */
    @Override
    public String getServiceName() {
        return ServiceNameEnum.TEST_PARKING_IN.serviceName;
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
