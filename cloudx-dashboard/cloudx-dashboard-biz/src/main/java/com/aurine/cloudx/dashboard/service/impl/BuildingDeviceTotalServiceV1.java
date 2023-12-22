package com.aurine.cloudx.dashboard.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.dashboard.constant.ServiceNameEnum;
import com.aurine.cloudx.dashboard.constant.VersionEnum;
import com.aurine.cloudx.dashboard.entity.ProjectBuildingDeviceTotalMView;
import com.aurine.cloudx.dashboard.exception.DashboardErrorEnum;
import com.aurine.cloudx.dashboard.exception.DashboardException;
import com.aurine.cloudx.dashboard.mapper.ProjectBuildingDeviceTotalMapper;
import com.aurine.cloudx.dashboard.service.AbstractDashboardService;
import com.aurine.cloudx.dashboard.util.JSONUtil;
import com.aurine.cloudx.dashboard.vo.DashboardRequestVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @version 1
 * @description: 楼栋设备统计
 * @author : Qiu <qiujb@miligc.com>
 * @date : 2021 08 18 15:14
 * @Copyright:
 */

@Service
public class BuildingDeviceTotalServiceV1 extends AbstractDashboardService {

    @Resource
    private BuildingAlarmTotalServiceV1 buildingAlarmTotalServiceV1;

    @Resource
    private ProjectBuildingDeviceTotalMapper projectBuildingDeviceTotalMapper;

    @Override
    public JSONObject getData(DashboardRequestVo request) {
        if (StringUtils.isEmpty(request.getProjectId()) || StringUtils.isEmpty(request.getBuildingId())) {
            throw new DashboardException(DashboardErrorEnum.ARGUMENT_INVALID);
        }
        List<ProjectBuildingDeviceTotalMView> dtList = super.getListByProjectIdAndBuildingId(projectBuildingDeviceTotalMapper, ProjectBuildingDeviceTotalMView.class, request.getProjectId(), request.getBuildingId(), request.getStorey());
        if (CollUtil.isEmpty(dtList)) throw new DashboardException(DashboardErrorEnum.EMPTY_RESULT);

        VO vo = new VO();
        int axisNumber = 7;

        JSONObject data = buildingAlarmTotalServiceV1.getData(request);
        if (data == null) {
            throw new DashboardException(DashboardErrorEnum.EMPTY_RESULT);
        }
        Long[] online = initArray(dtList.size());
        Long[] offline = initArray(dtList.size());
        Long[] totalAlarm = initArray(dtList.size());

        for (int i = 0; i < dtList.size(); i++) {
            ProjectBuildingDeviceTotalMView dt = dtList.get(i);
            int index = i;
            online[index] = dt.getCntOnline();
            offline[index] = dt.getCntOffline();
            vo.setAllOnline(vo.getAllOnline() + dt.getCntOnline());
            vo.setAllOffline(vo.getAllOffline() + dt.getCntOffline());
            vo.setAllTotal(vo.getAllTotal() + dt.getCntTotal());
        }
        vo.setOnline(online);
        vo.setOffline(offline);
        vo.setDAxis(initAxis(axisNumber, DAY, false, -1));

        JSONObject voJSON = JSONUtil.toJSONObject(vo);
        voJSON.put("alarm", data.get("alarm"));
        voJSON.put("allAlarm", data.get("allAlarm"));
        voJSON.put("totalAlarm", data.get("totalAlarm"));
        voJSON.put("allTotal", data.get("allTotal"));

        return voJSON;
    }

    @Data
    private class VO {
        /**
         * 整楼设备总数
         */
        @ApiModelProperty(value = "整楼设备总数")
        private Long allTotal = 0L;

        /**
         * 整楼设备在线数
         */
        @ApiModelProperty(value = "整楼设备在线数")
        private Long allOnline = 0L;

        /**
         * 整楼设备离线数
         */
        @ApiModelProperty(value = "整楼设备离线数")
        private Long allOffline = 0L;

        /**
         * 在线数
         */
        @ApiModelProperty(value = "在线数")
        private Long[] online;

        /**
         * 离线数
         */
        @ApiModelProperty(value = "离线数")
        private Long[] offline;

        /**
         * 天
         */
        @ApiModelProperty(value = "天")
        private String[] dAxis;
    }

    @Override
    public String getServiceName() {
        return ServiceNameEnum.BUILDING_DEVICE_TOTAL.serviceName;
    }

    @Override
    public String getVersion() {
        return VersionEnum.V1.number;
    }
}
