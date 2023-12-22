package com.aurine.cloudx.dashboard.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.dashboard.constant.ServiceNameEnum;
import com.aurine.cloudx.dashboard.constant.VersionEnum;
import com.aurine.cloudx.dashboard.entity.ProjectBuildingEmployerTotalMView;
import com.aurine.cloudx.dashboard.exception.DashboardErrorEnum;
import com.aurine.cloudx.dashboard.exception.DashboardException;
import com.aurine.cloudx.dashboard.mapper.ProjectBuildingEmployerTotalMapper;
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
 * @description: 楼栋房屋统计
 * @author : Qiu <qiujb@miligc.com>
 * @date : 2021 08 17 10:39
 * @Copyright:
 */

@Service
public class BuildingEmployerTotalServiceV1 extends AbstractDashboardService {

    @Resource
    private ProjectBuildingEmployerTotalMapper projectBuildingEmployerTotalMapper;

    @Override
    public JSONObject getData(DashboardRequestVo request) {
        if (StringUtils.isEmpty(request.getProjectId()) || StringUtils.isEmpty(request.getBuildingId())) {
            throw new DashboardException(DashboardErrorEnum.ARGUMENT_INVALID);
        }
        boolean whole = request.getStorey() == null || request.getStorey() <= 0;
        List<ProjectBuildingEmployerTotalMView> etList = super.getListByProjectIdAndBuildingId(projectBuildingEmployerTotalMapper, ProjectBuildingEmployerTotalMView.class, request.getProjectId(), request.getBuildingId(), request.getStorey());
        if (CollUtil.isEmpty(etList)) throw new DashboardException(DashboardErrorEnum.EMPTY_RESULT);

        VO vo = new VO();
        Long[] total = initArray(etList.size());
        Long[] privateType = initArray(etList.size());
        Long[] groupType = initArray(etList.size());
        Long[] otherType = initArray(etList.size());

        for (int i = 0; i < etList.size(); i++) {
            ProjectBuildingEmployerTotalMView et = etList.get(i);
            int index = i;
            if (whole && et.getStorey() != null && et.getStorey() > 0) {
                index = et.getStorey() - 1;
            }
            total[index] = et.getCntTotal();
            privateType[index] = et.getCntPrivate();
            groupType[index] = et.getCntGroup();
            otherType[index] = et.getCntOther();
            vo.setAllPrivateType(vo.getAllPrivateType() + et.getCntPrivate());
            vo.setAllGroupType(vo.getAllGroupType() + et.getCntGroup());
            vo.setAllOtherType(vo.getAllOtherType() + et.getCntOther());
            vo.setAllTotal(vo.getAllTotal() + et.getCntTotal());
        }
        vo.setTotal(total);
        vo.setPrivateType(privateType);
        vo.setGroupType(groupType);
        vo.setOtherType(otherType);

        return JSONUtil.toJSONObject(vo);
    }

    @Data
    private class VO {
        /**
         * 总数
         */
        @ApiModelProperty(value = "总数")
        private Long allTotal = 0L;

        /**
         * 总数
         */
        @ApiModelProperty(value = "私有类型经济总数")
        private Long allPrivateType = 0L;

        /**
         * 总数
         */
        @ApiModelProperty(value = "集体全资类型经济总数")
        private Long allGroupType = 0L;

        /**
         * 总数
         */
        @ApiModelProperty(value = "其他类型经济总数")
        private Long allOtherType = 0L;

        /**
         * 当前楼层的总数
         */
        @ApiModelProperty(value = "当前楼层的总数")
        private Long[] total;

        /**
         * 私有类型经济
         */
        @ApiModelProperty(value = "私有类型经济")
        private Long[] privateType;

        /**
         * 集体全资类型经济
         */
        @ApiModelProperty(value = "集体全资类型经济")
        private Long[] groupType;

        /**
         * 其他类型经济
         */
        @ApiModelProperty(value = "其他类型经济")
        private Long[] otherType;
    }

    @Override
    public String getServiceName() {
        return ServiceNameEnum.BUILDING_EMPLOYER_TOTAL.serviceName;
    }

    @Override
    public String getVersion() {
        return VersionEnum.V1.number;
    }
}
