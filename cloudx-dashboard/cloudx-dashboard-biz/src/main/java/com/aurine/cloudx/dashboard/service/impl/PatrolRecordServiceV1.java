package com.aurine.cloudx.dashboard.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.dashboard.constant.ServiceNameEnum;
import com.aurine.cloudx.dashboard.constant.VersionEnum;
import com.aurine.cloudx.dashboard.entity.ProjectInspectMview;
import com.aurine.cloudx.dashboard.entity.ProjectPatrolMview;
import com.aurine.cloudx.dashboard.exception.DashboardErrorEnum;
import com.aurine.cloudx.dashboard.exception.DashboardException;
import com.aurine.cloudx.dashboard.mapper.ProjectInspectMapper;
import com.aurine.cloudx.dashboard.mapper.ProjectPatrolMapper;
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
 * @description:巡更统计信息
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2021-03-25
 * @Copyright:
 */
@Service
public class PatrolRecordServiceV1 extends AbstractDashboardService {
    @Resource
    private ProjectPatrolMapper projectPatrolMapper;

    /**
     * 获取数据
     *
     * @param request
     * @return
     */
    @Override
    public JSONObject getData(DashboardRequestVo request) {
        request.getProjectIdArray();
        List<ProjectPatrolMview> poList = super.getListByProjectId(projectPatrolMapper, ProjectPatrolMview.class, request.getProjectIdArray());

        if (CollUtil.isEmpty(poList)) throw new DashboardException(DashboardErrorEnum.EMPTY_RESULT);

        VO vo = new VO();

        for (ProjectPatrolMview po : poList) {
            vo.setToDo(po.getToDo() + vo.getToDo());
            vo.setComplete(po.getComplete() + vo.getComplete());
            vo.setDoing(po.getDoing() + vo.getDoing());
            vo.setExpired(po.getExpired() + vo.getExpired());
        }


        return JSONUtil.toJSONObject(vo);
    }

    @Data
    private class VO {

        /**
         * 待巡更
         */
        @ApiModelProperty(value = "待巡更")
        private Long toDo = 0L;
        /**
         * 巡更中
         */
        @ApiModelProperty(value = "巡更中")
        private Long doing = 0L;

        /**
         * 已巡更
         */
        @ApiModelProperty(value = "已巡更")
        private Long complete = 0L;

        /**
         * 已过期
         */
        @ApiModelProperty(value = "已过期")
        private Long expired = 0L;

    }

    /**
     * 获取服务名称
     *
     * @return
     */
    @Override
    public String getServiceName() {
        return ServiceNameEnum.PATROL_RECORD.serviceName;
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
