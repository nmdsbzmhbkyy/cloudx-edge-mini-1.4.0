package com.aurine.cloudx.dashboard.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.dashboard.constant.ServiceNameEnum;
import com.aurine.cloudx.dashboard.constant.VersionEnum;
import com.aurine.cloudx.dashboard.entity.ProjectAlarmDay07Mview;
import com.aurine.cloudx.dashboard.entity.ProjectAlarmMview;
import com.aurine.cloudx.dashboard.entity.ProjectInspectDay07Mview;
import com.aurine.cloudx.dashboard.entity.ProjectInspectMview;
import com.aurine.cloudx.dashboard.exception.DashboardErrorEnum;
import com.aurine.cloudx.dashboard.exception.DashboardException;
import com.aurine.cloudx.dashboard.mapper.ProjectAlarmDay07Mapper;
import com.aurine.cloudx.dashboard.mapper.ProjectAlarmMapper;
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
 * @description:报警记录信息
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2021-03-25
 * @Copyright:
 */
@Service
public class AlarmRecordServiceV1 extends AbstractDashboardService {
    @Resource
    private ProjectAlarmMapper projectAlarmMapper;

    @Resource
    private ProjectAlarmDay07Mapper projectAlarmDay07Mapper;

    /**
     * 获取数据
     *
     * @param request
     * @return
     */
    @Override
    public JSONObject getData(DashboardRequestVo request) {
        List<ProjectAlarmMview> poList = super.getListByProjectId(projectAlarmMapper, ProjectAlarmMview.class, request.getProjectIdArray());

        if (CollUtil.isEmpty(poList)) throw new DashboardException(DashboardErrorEnum.EMPTY_RESULT);

        List<ProjectAlarmDay07Mview> dayList = super.getListByProjectId(projectAlarmDay07Mapper, ProjectAlarmDay07Mview.class, request.getProjectIdArray());
        if (CollUtil.isEmpty(dayList)) throw new DashboardException(DashboardErrorEnum.EMPTY_RESULT);


        VO vo = new VO();

        for (ProjectAlarmMview po : poList) {
            vo.setInProcess(po.getInProcess() + vo.getInProcess());
            vo.setNotProcess(po.getNotProcess() + vo.getNotProcess());
            vo.setProcessed(po.getProcessed() + vo.getProcessed());
        }

        Long[] day = initArray(7);
        for (ProjectAlarmDay07Mview po : dayList) {
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

        return JSONUtil.toJSONObject(vo);
    }

    @Data
    private class VO {

        /**
         * 待巡检
         */
        @ApiModelProperty(value = "待巡检")
        private Long notProcess = 0L;
        /**
         * 巡检中
         */
        @ApiModelProperty(value = "巡检中")
        private Long inProcess = 0L;

        /**
         * 已巡检
         */
        @ApiModelProperty(value = "已巡检")
        private Long processed = 0L;


        private Long[] day;

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
        return ServiceNameEnum.ALARM_RECODE.serviceName;
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
