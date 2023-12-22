package com.aurine.cloudx.dashboard.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.dashboard.constant.ServiceNameEnum;
import com.aurine.cloudx.dashboard.constant.VersionEnum;
import com.aurine.cloudx.dashboard.entity.ProjectBuildingDeviceListMView;
import com.aurine.cloudx.dashboard.exception.DashboardErrorEnum;
import com.aurine.cloudx.dashboard.exception.DashboardException;
import com.aurine.cloudx.dashboard.mapper.ProjectBuildingDeviceListMapper;
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
 * @description: 楼栋告警统计7日
 * @author : Qiu <qiujb@miligc.com>
 * @date : 2021 08 18 17:12
 * @Copyright:
 */

@Service
public class BuildingDeviceListServiceV1 extends AbstractDashboardService {

    @Resource
    private ProjectBuildingDeviceListMapper projectBuildingDeviceListMapper;

    @Override
    public JSONObject getData(DashboardRequestVo request) throws Exception {
        if (StringUtils.isEmpty(request.getProjectId()) || StringUtils.isEmpty(request.getBuildingId())) {
            throw new DashboardException(DashboardErrorEnum.ARGUMENT_INVALID);
        }
        List<ProjectBuildingDeviceListMView> dlList = super.getListByProjectIdAndBuildingId(projectBuildingDeviceListMapper, ProjectBuildingDeviceListMView.class, request.getProjectId(), request.getBuildingId(), request.getStorey());
        if (CollUtil.isEmpty(dlList)) throw new DashboardException(DashboardErrorEnum.EMPTY_RESULT);

        VO vo = new VO();
        vo.setTotal((long) dlList.size());
        vo.setList(dlList);

        return JSONUtil.toJSONObject(vo);
    }

    @Data
    private class VO {
        /**
         * 设备总数
         */
        @ApiModelProperty(value = "设备总数")
        private Long total = 0L;

        /**
         * 设备列表
         */
        @ApiModelProperty(value = "设备列表")
        private List<ProjectBuildingDeviceListMView> list;
    }

    @Override
    public String getServiceName() {
        return ServiceNameEnum.BUILDING_DEVICE_LIST.serviceName;
    }

    @Override
    public String getVersion() {
        return VersionEnum.V1.number;
    }
}
