package com.aurine.cloudx.dashboard.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.dashboard.constant.ServiceNameEnum;
import com.aurine.cloudx.dashboard.constant.VersionEnum;
import com.aurine.cloudx.dashboard.entity.ProjectHouseholderMview;
import com.aurine.cloudx.dashboard.exception.DashboardErrorEnum;
import com.aurine.cloudx.dashboard.exception.DashboardException;
import com.aurine.cloudx.dashboard.mapper.ProjectHouseholderMapper;
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
 * @description:住户类型统计信息
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2021-03-25
 * @Copyright:
 */
@Service
public class HouseholderServiceV1 extends AbstractDashboardService {
    @Resource
    private ProjectHouseholderMapper projectHouseholderMapper;

    /**
     * 获取数据
     *
     * @param request
     * @return
     */
    @Override
    public JSONObject getData(DashboardRequestVo request) {
        List<ProjectHouseholderMview> poList = super.getListByProjectId(projectHouseholderMapper, ProjectHouseholderMview.class, request.getProjectIdArray());

        if (CollUtil.isEmpty(poList)) throw new DashboardException(DashboardErrorEnum.EMPTY_RESULT);

        VO vo = new VO();
        for (ProjectHouseholderMview po : poList) {
            vo.setTotal(po.getCntTotal() + vo.getTotal());
            vo.setOwner(po.getCntOwner() + vo.getOwner());
            vo.setFamily(po.getCntFamily() + vo.getFamily());
            vo.setRent(po.getCntRent() + vo.getRent());
        }

        vo.setOwnerScale(vo.getOwner() * 1.0 / vo.getTotal());
        vo.setFamilyScale(vo.getFamily() * 1.0 / vo.getTotal());
        vo.setRentScale(vo.getRent() * 1.0 / vo.getTotal());

        return JSONUtil.toJSONObject(vo);
    }

    @Data
    private class VO {
        @ApiModelProperty(value = "总人数")
        private Long total = 0L;
        @ApiModelProperty(value = "业主人数")
        private Long owner = 0L;
        @ApiModelProperty(value = "家属人数")
        private Long family = 0L;
        @ApiModelProperty(value = "租客人数")
        private Long rent = 0L;
        @ApiModelProperty(value = "业主百分比")
        private Double ownerScale;
        @ApiModelProperty(value = "家属百分比")
        private Double familyScale;
        @ApiModelProperty(value = "租客百分比")
        private Double rentScale;
    }

    /**
     * 获取服务名称
     *
     * @return
     */
    @Override
    public String getServiceName() {
        return ServiceNameEnum.HOUSEHOLDER.serviceName;
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
