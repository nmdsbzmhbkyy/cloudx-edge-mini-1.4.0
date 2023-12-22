package com.aurine.cloudx.dashboard.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.dashboard.constant.ServiceNameEnum;
import com.aurine.cloudx.dashboard.constant.VersionEnum;
import com.aurine.cloudx.dashboard.entity.ProjectPersonAgeGenderMview;
import com.aurine.cloudx.dashboard.entity.ProjectPersonCarMview;
import com.aurine.cloudx.dashboard.entity.ProjectPersonFaceMview;
import com.aurine.cloudx.dashboard.exception.DashboardErrorEnum;
import com.aurine.cloudx.dashboard.exception.DashboardException;
import com.aurine.cloudx.dashboard.mapper.ProjectPersonCarMapper;
import com.aurine.cloudx.dashboard.mapper.ProjectPersonFaceMapper;
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
 * @description: 车辆持有率
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2021-03-25
 * @Copyright:
 */
@Service
public class PersonCarServiceV1 extends AbstractDashboardService {
    @Resource
    private ProjectPersonCarMapper projectPersonCarMapper;

    /**
     * 获取数据
     *
     * @param request
     * @return
     */
    @Override
    public JSONObject getData(DashboardRequestVo request) {
        List<ProjectPersonCarMview> poList = super.getListByProjectId(projectPersonCarMapper, ProjectPersonCarMview.class, request.getProjectIdArray());
        if (CollUtil.isEmpty(poList)) throw new DashboardException(DashboardErrorEnum.EMPTY_RESULT);

        VO vo = new VO();
        for (ProjectPersonCarMview po : poList) {
            vo.setTotal(po.getTotal() + vo.getTotal());
            vo.setHaveCar(po.getHaveCar() + vo.getHaveCar());
            vo.setNoHaveCar(po.getNotHaveCar() + vo.getNoHaveCar());
        }


        return JSONUtil.toJSONObject(vo);
    }

    @Data
    private class VO {
        @ApiModelProperty(value = "总人数")
        private Long total = 0L;
        @ApiModelProperty(value = "有车人数")
        private Long haveCar = 0L;
        @ApiModelProperty(value = "无车人数")
        private Long noHaveCar = 0L;

    }

    /**
     * 获取服务名称
     *
     * @return
     */
    @Override
    public String getServiceName() {
        return ServiceNameEnum.PERSON_CAR.serviceName;
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
