package com.aurine.cloudx.dashboard.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.dashboard.constant.ServiceNameEnum;
import com.aurine.cloudx.dashboard.constant.VersionEnum;
import com.aurine.cloudx.dashboard.entity.ProjectComplaintRecordDay7Mview;
import com.aurine.cloudx.dashboard.entity.ProjectPersonEntranceDay07Mview;
import com.aurine.cloudx.dashboard.entity.ProjectPersonEntranceH07Mview;
import com.aurine.cloudx.dashboard.exception.DashboardErrorEnum;
import com.aurine.cloudx.dashboard.exception.DashboardException;
import com.aurine.cloudx.dashboard.mapper.ProjectComplaintRecordDay07Mapper;
import com.aurine.cloudx.dashboard.mapper.ProjectPersonEntranceDay07Mapper;
import com.aurine.cloudx.dashboard.mapper.ProjectPersonEntranceH07Mapper;
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
 * @description: 居民出行趋势 每小时,每天
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2021-03-30
 * @Copyright:
 */
@Service
public class PersonEntranceServiceV1 extends AbstractDashboardService {
    @Resource
    private ProjectPersonEntranceH07Mapper projectPersonEntranceH07Mapper;
    @Resource
    private ProjectPersonEntranceDay07Mapper projectPersonEntranceDay07Mapper;


    /**
     * 获取数据
     *
     * @param request
     * @return
     */
    @Override
    public JSONObject getData(DashboardRequestVo request) {
        List<ProjectPersonEntranceH07Mview> hourList = super.getListByProjectId(projectPersonEntranceH07Mapper, ProjectPersonEntranceH07Mview.class, request.getProjectIdArray());
        List<ProjectPersonEntranceDay07Mview> dayList = super.getListByProjectId(projectPersonEntranceDay07Mapper, ProjectPersonEntranceDay07Mview.class, request.getProjectIdArray());

        if (CollUtil.isEmpty(hourList)) throw new DashboardException(DashboardErrorEnum.EMPTY_RESULT);
        if (CollUtil.isEmpty(dayList)) throw new DashboardException(DashboardErrorEnum.EMPTY_RESULT);

        VO vo = new VO();

        Long[] hour = initArray(7);
        for (ProjectPersonEntranceH07Mview po : hourList) {
            hour[0] += po.getH01();
            hour[1] += po.getH02();
            hour[2] += po.getH03();
            hour[3] += po.getH04();
            hour[4] += po.getH05();
            hour[5] += po.getH06();
            hour[6] += po.getH07();
        }
        vo.setHour(hour);

        Long[] day = initArray(7);
        for (ProjectPersonEntranceDay07Mview po : dayList) {
            day[0] += po.getDay01();
            day[1] += po.getDay02();
            day[2] += po.getDay03();
            day[3] += po.getDay04();
            day[4] += po.getDay05();
            day[5] += po.getDay06();
            day[6] += po.getDay07();
        }
        vo.setDay(day);

        vo.setDayAxis(initAxis(7, DAY, false, -1));
        vo.setHourAxis(initAxis(7, HOUR, false, -1));

        return JSONUtil.toJSONObject(vo);
    }

    @Data
    private class VO {
        @ApiModelProperty(value = "每小时出行趋势")
        private Long[] hour;
        @ApiModelProperty(value = "每天出行趋势")
        private Long[] day;

        @ApiModelProperty(value = "小时")
        private String[] hourAxis;
        @ApiModelProperty(value = "天")
        private String[] dayAxis;
    }

    /**
     * 获取服务名称
     *
     * @return
     */
    @Override
    public String getServiceName() {
        return ServiceNameEnum.PERSON_ENTRANCE.serviceName;
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
