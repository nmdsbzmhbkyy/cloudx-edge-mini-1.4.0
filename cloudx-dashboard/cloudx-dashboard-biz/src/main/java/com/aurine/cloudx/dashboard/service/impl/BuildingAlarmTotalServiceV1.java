package com.aurine.cloudx.dashboard.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.dashboard.constant.ServiceNameEnum;
import com.aurine.cloudx.dashboard.constant.VersionEnum;
import com.aurine.cloudx.dashboard.entity.ProjectBuildingAlarmD07MView;
import com.aurine.cloudx.dashboard.exception.DashboardErrorEnum;
import com.aurine.cloudx.dashboard.exception.DashboardException;
import com.aurine.cloudx.dashboard.mapper.ProjectBuildingAlarmD07Mapper;
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
 * @date : 2021 08 17 16:24
 * @Copyright:
 */

@Service
public class BuildingAlarmTotalServiceV1 extends AbstractDashboardService {

    @Resource
    private ProjectBuildingAlarmD07Mapper projectBuildingAlarmD07Mapper;

    @Override
    public JSONObject getData(DashboardRequestVo request) {
        if (StringUtils.isEmpty(request.getProjectId()) || StringUtils.isEmpty(request.getBuildingId())) {
            throw new DashboardException(DashboardErrorEnum.ARGUMENT_INVALID);
        }
        List<ProjectBuildingAlarmD07MView> atList = super.getListByProjectIdAndBuildingId(projectBuildingAlarmD07Mapper, ProjectBuildingAlarmD07MView.class, request.getProjectId(), request.getBuildingId(), request.getStorey());
        if (CollUtil.isEmpty(atList)) throw new DashboardException(DashboardErrorEnum.EMPTY_RESULT);

        VO vo = new VO();
        int axisNumber = 7;
        Long[] allAlarm = initArray(axisNumber);
        Long[] totalAlarm = initArray(atList.size());
        Long[] notProcess = initArray(atList.size());
        Long[] inProcess = initArray(atList.size());
        Long[] processed = initArray(atList.size());
        Long[][] alarm = initTwoDimensionalArray(atList.size(), axisNumber);

        for (int i = 0; i < atList.size(); i++) {
            ProjectBuildingAlarmD07MView at = atList.get(i);
            int index = i;
            alarm[index][0] = at.getCntD01();
            alarm[index][1] = at.getCntD02();
            alarm[index][2] = at.getCntD03();
            alarm[index][3] = at.getCntD04();
            alarm[index][4] = at.getCntD05();
            alarm[index][5] = at.getCntD06();
            alarm[index][6] = at.getCntD07();
            notProcess[index] = at.getCntNotProcess();
            inProcess[index] = at.getCntInProcess();
            processed[index] = at.getCntProcessed();
            totalAlarm[index] = at.getCntTotal();

            allAlarm[0] += at.getCntD01();
            allAlarm[1] += at.getCntD02();
            allAlarm[2] += at.getCntD03();
            allAlarm[3] += at.getCntD04();
            allAlarm[4] += at.getCntD05();
            allAlarm[5] += at.getCntD06();
            allAlarm[6] += at.getCntD07();
            
            vo.setAllNotProcess(vo.getAllNotProcess() + at.getCntNotProcess());
            vo.setAllInProcess(vo.getAllInProcess() + at.getCntInProcess());
            vo.setAllProcessed(vo.getAllProcessed() + at.getCntProcessed());
            vo.setAllTotal(vo.getAllTotal() + at.getCntTotal());
        }
        vo.setAllAlarm(allAlarm);
        vo.setAlarm(alarm);
        vo.setTotalAlarm(totalAlarm);
        vo.setNotProcess(notProcess);
        vo.setInProcess(inProcess);
        vo.setProcessed(processed);
        vo.setDAxis(initAxis(axisNumber, DAY, false, -1));

        return JSONUtil.toJSONObject(vo);
    }

    @Data
    private class VO {
        /**
         * 整楼告警数
         */
        @ApiModelProperty(value = "整楼告警数")
        private Long allTotal = 0L;

        /**
         * 当前楼层的总数
         */
        @ApiModelProperty(value = "整楼未处理告警数")
        private Long allNotProcess = 0L;

        /**
         * 整楼处理中告警数
         */
        @ApiModelProperty(value = "整楼处理中告警数")
        private Long allInProcess = 0L;

        /**
         * 整楼已处理告警数
         */
        @ApiModelProperty(value = "整楼已处理告警数")
        private Long allProcessed = 0L;

        /**
         * 整楼每天告警数
         */
        @ApiModelProperty(value = "整楼每天告警数")
        private Long[] allAlarm;

        /**
         * 未处理
         */
        @ApiModelProperty(value = "未处理")
        private Long[] notProcess;

        /**
         * 处理中
         */
        @ApiModelProperty(value = "处理中")
        private Long[] inProcess;

        /**
         * 已处理
         */
        @ApiModelProperty(value = "已处理")
        private Long[] processed;

        /**
         * 当前楼层告警数
         */
        @ApiModelProperty(value = "当前楼层告警数")
        private Long[] totalAlarm;

        /**
         * 当前楼层告警数
         */
        @ApiModelProperty(value = "当前楼层每天告警数")
        private Long[][] alarm;

        /**
         * 天
         */
        @ApiModelProperty(value = "天")
        private String[] dAxis;
    }

    @Override
    public String getServiceName() {
        return ServiceNameEnum.BUILDING_ALARM_TOTAL.serviceName;
    }

    @Override
    public String getVersion() {
        return VersionEnum.V1.number;
    }
}
