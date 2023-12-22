package com.aurine.cloudx.dashboard.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.dashboard.constant.ServiceNameEnum;
import com.aurine.cloudx.dashboard.constant.VersionEnum;
import com.aurine.cloudx.dashboard.entity.ProjectHouseTypeMview;
import com.aurine.cloudx.dashboard.exception.DashboardErrorEnum;
import com.aurine.cloudx.dashboard.exception.DashboardException;
import com.aurine.cloudx.dashboard.mapper.ProjectHouseTypeMapper;
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
 * @description: 房屋类型占比
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2021-03-25
 * @Copyright:
 */
@Service
public class HouseTypeServiceV1 extends AbstractDashboardService {
    @Resource
    private ProjectHouseTypeMapper projectHouseTypeMapper;

    /**
     * 获取数据
     *
     * @param request
     * @return
     */
    @Override
    public JSONObject getData(DashboardRequestVo request) {
        List<ProjectHouseTypeMview> poList = super.getListByProjectId(projectHouseTypeMapper, ProjectHouseTypeMview.class, request.getProjectIdArray());

        if (CollUtil.isEmpty(poList)) throw new DashboardException(DashboardErrorEnum.EMPTY_RESULT);

        VO vo = new VO();
        for (ProjectHouseTypeMview po : poList) {
            vo.setTotal(po.getCntTotal() + vo.getTotal());
            vo.setOwner(po.getCntOwner() + vo.getOwner());
            vo.setRent(po.getCntRent() + vo.getRent());
            vo.setIdle(po.getCntIdle() + vo.getIdle());
        }

        return JSONUtil.toJSONObject(vo);
    }

    @Data
    private class VO {
        @ApiModelProperty(value = "总房数")
        private Long total = 0L;
        @ApiModelProperty(value = "自住房数")
        private Long owner = 0L;
        @ApiModelProperty(value = "空置数")
        private Long idle = 0L;
        @ApiModelProperty(value = "出租屋数")
        private Long rent = 0L;
    }

    /**
     * 获取服务名称
     *
     * @return
     */
    @Override
    public String getServiceName() {
        return ServiceNameEnum.HOUSE_TYPE.serviceName;
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
