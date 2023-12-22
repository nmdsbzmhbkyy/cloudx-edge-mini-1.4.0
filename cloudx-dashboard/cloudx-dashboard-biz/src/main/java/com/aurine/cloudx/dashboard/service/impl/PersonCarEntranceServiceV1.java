package com.aurine.cloudx.dashboard.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.dashboard.constant.ServiceNameEnum;
import com.aurine.cloudx.dashboard.constant.VersionEnum;
import com.aurine.cloudx.dashboard.entity.ProjectCarEntranceThisDayMview;
import com.aurine.cloudx.dashboard.entity.ProjectHouseholderMview;
import com.aurine.cloudx.dashboard.entity.ProjectPersonEntranceThisDayMview;
import com.aurine.cloudx.dashboard.exception.DashboardErrorEnum;
import com.aurine.cloudx.dashboard.exception.DashboardException;
import com.aurine.cloudx.dashboard.mapper.ProjectCarEntranceThisDayMapper;
import com.aurine.cloudx.dashboard.mapper.ProjectHouseholderMapper;
import com.aurine.cloudx.dashboard.mapper.ProjectPersonEntranceThisDayMapper;
import com.aurine.cloudx.dashboard.service.AbstractDashboardService;
import com.aurine.cloudx.dashboard.util.JSONUtil;
import com.aurine.cloudx.dashboard.vo.DashboardRequestVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @version 1
 * @description: 人员车辆当日的通行情况
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2021-03-25
 * @Copyright:
 */
@Service
public class PersonCarEntranceServiceV1 extends AbstractDashboardService {
    @Resource
    private ProjectPersonEntranceThisDayMapper projectPersonEntranceThisDayMapper;

    @Resource
    private ProjectCarEntranceThisDayMapper projectCarEntranceThisDayMapper;

    /**
     * 获取数据
     *
     * @param request
     * @return
     */
    @Override
    public JSONObject getData(DashboardRequestVo request) {
        List<ProjectPersonEntranceThisDayMview> personList = super.getListByProjectId(projectPersonEntranceThisDayMapper, ProjectPersonEntranceThisDayMview.class, request.getProjectIdArray());
        List<ProjectCarEntranceThisDayMview> carList = super.getListByProjectId(projectCarEntranceThisDayMapper, ProjectCarEntranceThisDayMview.class, request.getProjectIdArray());

        if (CollUtil.isEmpty(personList)) throw new DashboardException(DashboardErrorEnum.EMPTY_RESULT);
        if (CollUtil.isEmpty(carList)) throw new DashboardException(DashboardErrorEnum.EMPTY_RESULT);

        VO vo = new VO();
        for (ProjectPersonEntranceThisDayMview po : personList) {
            vo.setOwner(vo.getOwner() + po.getOwner());
            vo.setFamily(vo.getFamily() + po.getFamily());
            vo.setRent(vo.getRent() + po.getRent());
            vo.setTotalPerson(vo.getTotalCar() + po.getTotal());
            vo.setVisitor(vo.getVisitor() + po.getVisitor());
            vo.setStaff(vo.getStaff() + po.getStaff());
        }
        for (ProjectCarEntranceThisDayMview po : carList) {
            vo.setTotalCar(vo.getTotalCar() + po.getTotal());
        }

        return JSONUtil.toJSONObject(vo);
    }

    @Data
    private class VO {
        @ApiModelProperty(value = "今日通行总人数")
        private Long totalPerson = 0L;

        @ApiModelProperty(value = "今日通行总车数")
        private Long totalCar = 0L;

        @ApiModelProperty(value = "业主人数")
        private Long owner = 0L;
        @ApiModelProperty(value = "家属人数")
        private Long family = 0L;
        @ApiModelProperty(value = "租客人数")
        private Long rent = 0L;
        @ApiModelProperty(value = "访客人数")
        private Long visitor = 0L;
        @ApiModelProperty(value = "员工")
        private Long staff = 0L;

    }

    /**
     * 获取服务名称
     *
     * @return
     */
    @Override
    public String getServiceName() {
        return ServiceNameEnum.PERSON_CAR_ENTRANCE.serviceName;
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
