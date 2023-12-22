package com.aurine.cloudx.dashboard.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.dashboard.constant.ServiceNameEnum;
import com.aurine.cloudx.dashboard.constant.VersionEnum;
import com.aurine.cloudx.dashboard.exception.DashboardErrorEnum;
import com.aurine.cloudx.dashboard.exception.DashboardException;
import com.aurine.cloudx.dashboard.mapper.DeviceDataMapper;
import com.aurine.cloudx.dashboard.mapper.ProjectDeviceInfoMapper;
import com.aurine.cloudx.dashboard.service.AbstractDashboardService;
import com.aurine.cloudx.dashboard.util.JSONUtil;
import com.aurine.cloudx.dashboard.vo.DashboardRequestVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @version 1
 * @description: 设备总览数据
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2021-04-09
 * @Copyright:
 */
@Service
public class DeviceOverviewServiceV1 extends AbstractDashboardService {
    @Resource
    private DeviceDataMapper deviceDataMapper;
    @Resource
    private ProjectDeviceInfoMapper projectDeviceInfoMapper;


    /**
     * 获取数据
     *
     * @param request
     * @return
     */
    @Override
    public JSONObject getData(DashboardRequestVo request) {
        //参数检查
        if (ArrayUtil.isEmpty(request.getProjectIdArray())) {
            throw new DashboardException(DashboardErrorEnum.ARGUMENT_INVALID);
        }


        List<LinkedHashMap<String, Object>> countDataMapList = deviceDataMapper.getDataCountOverview(request.getProjectIdArray()[0]);
        List<LinkedHashMap<String, Object>> typeDataMapList = deviceDataMapper.getDataTypeOverview(request.getProjectIdArray()[0]);

        if (CollUtil.isEmpty(typeDataMapList)) throw new DashboardException(DashboardErrorEnum.EMPTY_RESULT);
        if (CollUtil.isEmpty(countDataMapList)) throw new DashboardException(DashboardErrorEnum.EMPTY_RESULT);


        VO vo = new VO();
        vo.setDeviceTypeList(typeDataMapList);
        vo.setStateCount(countDataMapList);

        return JSONUtil.toJSONObject(vo);


    }

    @Data
    private class VO {
        private List stateCount;
        private List deviceTypeList;

    }

    /**
     * 获取服务名称
     *
     * @return
     */
    @Override
    public String getServiceName() {
        return ServiceNameEnum.DEVICE_OVERVIEW.serviceName;
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
