package com.aurine.cloudx.dashboard.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.dashboard.constant.ServiceNameEnum;
import com.aurine.cloudx.dashboard.constant.VersionEnum;
import com.aurine.cloudx.dashboard.entity.ProjectBuildingPersonTotalMView;
import com.aurine.cloudx.dashboard.exception.DashboardErrorEnum;
import com.aurine.cloudx.dashboard.exception.DashboardException;
import com.aurine.cloudx.dashboard.mapper.ProjectBuildingPersonTotalMapper;
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
 * @description: 楼栋人员统计
 * @author : Qiu <qiujb@miligc.com>
 * @date : 2021 08 16 11:24
 * @Copyright:
 */

@Service
public class BuildingPersonTotalServiceV1 extends AbstractDashboardService {

    @Resource
    private ProjectBuildingPersonTotalMapper projectBuildingPersonTotalMapper;

    @Override
    public JSONObject getData(DashboardRequestVo request) {
        if (StringUtils.isEmpty(request.getProjectId()) || StringUtils.isEmpty(request.getBuildingId())) {
            throw new DashboardException(DashboardErrorEnum.ARGUMENT_INVALID);
        }
        boolean whole = request.getStorey() == null || request.getStorey() <= 0;
        List<ProjectBuildingPersonTotalMView> ptList = super.getListByProjectIdAndBuildingId(projectBuildingPersonTotalMapper, ProjectBuildingPersonTotalMView.class, request.getProjectId(), request.getBuildingId(), request.getStorey());
        if (CollUtil.isEmpty(ptList)) throw new DashboardException(DashboardErrorEnum.EMPTY_RESULT);

        VO vo = new VO();
        Long[] household = initArray(ptList.size());
        Long[] visitor = initArray(ptList.size());
        Long[] male = initArray(ptList.size());
        Long[] female = initArray(ptList.size());
        Long[] children = initArray(ptList.size());
        Long[] adult = initArray(ptList.size());
        Long[] old = initArray(ptList.size());

        for (int i = 0; i < ptList.size(); i++) {
            ProjectBuildingPersonTotalMView pt = ptList.get(i);
            int index = i;
            if (whole && pt.getStorey() != null && pt.getStorey() > 0) {
                index = pt.getStorey() - 1;
            }
            household[index] = pt.getCntTotal();
            visitor[index] = pt.getCntVisitor();
            male[index] = pt.getCntMale();
            female[index] = pt.getCntFemale();
            children[index] = pt.getCntChildren();
            adult[index] = pt.getCntAdult();
            old[index] = pt.getCntOld();
            vo.setAllHousehold(vo.getAllHousehold() + pt.getCntTotal());
            vo.setAllVisitor(vo.getAllVisitor() + pt.getCntVisitor());
            vo.setAllMale(vo.getAllMale() + pt.getCntMale());
            vo.setAllFemale(vo.getAllFemale() + pt.getCntFemale());
            vo.setAllChildren(vo.getAllChildren() + pt.getCntChildren());
            vo.setAllAdult(vo.getAllAdult() + pt.getCntAdult());
            vo.setAllOld(vo.getAllOld() + pt.getCntOld());
        }
        vo.setHousehold(household);
        vo.setVisitor(visitor);
        vo.setMale(male);
        vo.setFemale(female);
        vo.setChildren(children);
        vo.setAdult(adult);
        vo.setOld(old);

        return JSONUtil.toJSONObject(vo);
    }

    @Data
    private class VO {
        /**
         * 整楼住户数量
         */
        @ApiModelProperty(value = "整楼住户数量")
        private Long allHousehold = 0L;

        /**
         * 住户数量
         */
        @ApiModelProperty(value = "住户数量")
        private Long[] household;

        /**
         * 整楼访客数量
         */
        @ApiModelProperty(value = "整楼访客数量")
        private Long allVisitor = 0L;

        /**
         * 访客数量
         */
        @ApiModelProperty(value = "访客数量")
        private Long[] visitor;

        /**
         * 整楼男性数量
         */
        @ApiModelProperty(value = "整楼男性数量")
        private Long allMale = 0L;

        /**
         * 男性数量
         */
        @ApiModelProperty(value = "男性数量")
        private Long[] male;

        /**
         * 整楼女性数量
         */
        @ApiModelProperty(value = "整楼女性数量")
        private Long allFemale = 0L;

        /**
         * 女性数量
         */
        @ApiModelProperty(value = "女性数量")
        private Long[] female;

        /**
         * 整楼未成年数量
         */
        @ApiModelProperty(value = "整楼未成年数量")
        private Long allChildren = 0L;

        /**
         * 未成年数量
         */
        @ApiModelProperty(value = "未成年数量")
        private Long[] children;

        /**
         * 整楼成年人数量
         */
        @ApiModelProperty(value = "整楼成年人数量")
        private Long allAdult = 0L;

        /**
         * 成年人数量
         */
        @ApiModelProperty(value = "成年人数量")
        private Long[] adult;

        /**
         * 整楼老年人数量
         */
        @ApiModelProperty(value = "整楼老年人数量")
        private Long allOld = 0L;

        /**
         * 老年人数量
         */
        @ApiModelProperty(value = "老年人数量")
        private Long[] old;
    }

    /**
     * 获取服务名称
     *
     * @return
     */
    @Override
    public String getServiceName() {
        return ServiceNameEnum.BUILDING_PERSON_TOTAL.serviceName;
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
