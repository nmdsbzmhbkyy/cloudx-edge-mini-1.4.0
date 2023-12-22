package com.aurine.cloudx.dashboard.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.dashboard.constant.ServiceNameEnum;
import com.aurine.cloudx.dashboard.constant.VersionEnum;
import com.aurine.cloudx.dashboard.entity.ProjectDataMview;
import com.aurine.cloudx.dashboard.entity.ProjectPersonFocusMview;
import com.aurine.cloudx.dashboard.exception.DashboardErrorEnum;
import com.aurine.cloudx.dashboard.exception.DashboardException;
import com.aurine.cloudx.dashboard.mapper.ProjectDataMapper;
import com.aurine.cloudx.dashboard.mapper.ProjectPersonFocusMapper;
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
 * @description:关注人群统计信息
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2021-03-25
 * @Copyright:
 */
@Service
public class PersonFocusServiceV1 extends AbstractDashboardService {
    @Resource
    private ProjectPersonFocusMapper projectPersonFocusMapper;

    /**
     * 获取数据
     *
     * @param request
     * @return
     */
    @Override
    public JSONObject getData(DashboardRequestVo request) {
        request.getProjectIdArray();
        List<ProjectPersonFocusMview> poList = super.getListByProjectId(projectPersonFocusMapper, ProjectPersonFocusMview.class, request.getProjectIdArray());

        if (CollUtil.isEmpty(poList)) throw new DashboardException(DashboardErrorEnum.EMPTY_RESULT);

        VO vo = new VO();

        for (ProjectPersonFocusMview po : poList) {
            vo.setOld(vo.getOld() + po.getKclr());
            vo.setChildren(vo.getChildren() + po.getLset());
            vo.setInjure(vo.getInjure() + po.getScry());
            vo.setMartyr(vo.getMartyr() + po.getLsjs());
        }


        return JSONUtil.toJSONObject(vo);
    }

    @Data
    private class VO {
        @ApiModelProperty(value = "老人")
        private Long old = 0L;
        @ApiModelProperty(value = "热痛")
        private Long children = 0L;
        @ApiModelProperty(value = "伤残人士")
        private Long injure = 0L;
        @ApiModelProperty(value = "烈士")
        private Long martyr = 0L;
    }

    /**
     * 获取服务名称
     *
     * @return
     */
    @Override
    public String getServiceName() {
        return ServiceNameEnum.PERSON_FOCUS.serviceName;
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
