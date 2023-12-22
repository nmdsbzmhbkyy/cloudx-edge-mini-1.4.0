package com.aurine.cloudx.dashboard.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.dashboard.constant.ServiceNameEnum;
import com.aurine.cloudx.dashboard.constant.VersionEnum;
import com.aurine.cloudx.dashboard.entity.ProjectHouseTypeMview;
import com.aurine.cloudx.dashboard.entity.ProjectPersonCarMview;
import com.aurine.cloudx.dashboard.entity.ProjectPersonFaceMview;
import com.aurine.cloudx.dashboard.exception.DashboardErrorEnum;
import com.aurine.cloudx.dashboard.exception.DashboardException;
import com.aurine.cloudx.dashboard.mapper.ProjectHouseTypeMapper;
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
 * @description: 面部识别比例
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2021-03-25
 * @Copyright:
 */
@Service
public class PersonFaceServiceV1 extends AbstractDashboardService {
    @Resource
    private ProjectPersonFaceMapper projectPersonFaceMapper;

    /**
     * 获取数据
     *
     * @param request
     * @return
     */
    @Override
    public JSONObject getData(DashboardRequestVo request) {
        List<ProjectPersonFaceMview> poList = super.getListByProjectId(projectPersonFaceMapper, ProjectPersonFaceMview.class, request.getProjectIdArray());

        if (CollUtil.isEmpty(poList)) throw new DashboardException(DashboardErrorEnum.EMPTY_RESULT);

        VO vo = new VO();

        for (ProjectPersonFaceMview po : poList) {
            vo.setTotal(po.getTotal() + vo.getTotal());
            vo.setHaveFace(po.getPersonFace() + vo.getHaveFace());
        }

        vo.setNoHaveFace(vo.getTotal() - vo.getHaveFace());

        return JSONUtil.toJSONObject(vo);
    }

    @Data
    private class VO {
        @ApiModelProperty(value = "总人数")
        private Long total = 0L;
        @ApiModelProperty(value = "持有人数")
        private Long haveFace = 0L;
        @ApiModelProperty(value = "无面部信息人数")
        private Long noHaveFace = 0L;

    }

    /**
     * 获取服务名称
     *
     * @return
     */
    @Override
    public String getServiceName() {
        return ServiceNameEnum.PERSON_FACE.serviceName;
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
