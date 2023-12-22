package com.aurine.cloudx.dashboard.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.dashboard.constant.ServiceNameEnum;
import com.aurine.cloudx.dashboard.constant.VersionEnum;
import com.aurine.cloudx.dashboard.entity.ProjectPersonCarMview;
import com.aurine.cloudx.dashboard.entity.ProjectPersonRegionMview;
import com.aurine.cloudx.dashboard.entity.ProjectRepairRecordDay7Mview;
import com.aurine.cloudx.dashboard.exception.DashboardErrorEnum;
import com.aurine.cloudx.dashboard.exception.DashboardException;
import com.aurine.cloudx.dashboard.mapper.ProjectPersonRegionMapper;
import com.aurine.cloudx.dashboard.mapper.ProjectRepairRecordDay7Mapper;
import com.aurine.cloudx.dashboard.service.AbstractDashboardService;
import com.aurine.cloudx.dashboard.util.JSONUtil;
import com.aurine.cloudx.dashboard.vo.DashboardRequestVo;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @version 1
 * @description: 人员户籍分布
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2021-03-30
 * @Copyright:
 */
@Service
public class PersonRegionServiceV1 extends AbstractDashboardService {
    @Resource
    private ProjectPersonRegionMapper projectPersonRegionMapper;


    /**
     * 获取数据
     *
     * @param request
     * @return
     */
    @Override
    public JSONObject getData(DashboardRequestVo request) {
        List<ProjectPersonRegionMview> poList = super.getListByProjectId(projectPersonRegionMapper, ProjectPersonRegionMview.class, request.getProjectIdArray());

        if (CollUtil.isEmpty(poList)) throw new DashboardException(DashboardErrorEnum.EMPTY_RESULT);

        VO vo = new VO();
        for (ProjectPersonRegionMview po : poList) {
            vo.setOutProvince(po.getOutProvince() + vo.getOutProvince());
            vo.setInProvince(po.getInProvince() + vo.getInProvince());
            vo.setInCity(po.getInCity() + vo.getInCity());
            vo.setUnknown(po.getUnknown() + vo.getUnknown());
        }


        return JSONUtil.toJSONObject(vo);
    }

    @Data
    private class VO {
        /**
         * 外省人数
         */
        @ApiModelProperty("外省人数")
        private Long outProvince = 0L;

        /**
         * 本省人数
         */
        @ApiModelProperty("本省人数")
        private Long inProvince = 0L;

        /**
         * 本市人数
         */
        @ApiModelProperty("本市人数")
        private Long inCity = 0L;

        /**
         * 未知地区人数
         */
        @ApiModelProperty("未知地区人数")
        private Long unknown = 0L;


    }

    /**
     * 获取服务名称
     *
     * @return
     */
    @Override
    public String getServiceName() {
        return ServiceNameEnum.PERSON_REGION.serviceName;
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
