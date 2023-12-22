package com.aurine.cloudx.dashboard.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.dashboard.constant.ServiceNameEnum;
import com.aurine.cloudx.dashboard.constant.VersionEnum;
import com.aurine.cloudx.dashboard.entity.ProjectBuildingHouseInfoMView;
import com.aurine.cloudx.dashboard.exception.DashboardErrorEnum;
import com.aurine.cloudx.dashboard.exception.DashboardException;
import com.aurine.cloudx.dashboard.mapper.ProjectBuildingHouseInfoMapper;
import com.aurine.cloudx.dashboard.service.AbstractDashboardService;
import com.aurine.cloudx.dashboard.util.JSONUtil;
import com.aurine.cloudx.dashboard.vo.DashboardRequestVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @version 1
 * @description: 楼栋房屋信息
 * @author : Qiu <qiujb@miligc.com>
 * @date : 2021 08 17 9:02
 * @Copyright:
 */

@Service
public class BuildingHouseInfoServiceV1 extends AbstractDashboardService {

    @Resource
    private ProjectBuildingHouseInfoMapper projectBuildingHouseInfoMapper;

    @Override
    public JSONObject getData(DashboardRequestVo request) {
        if (StringUtils.isEmpty(request.getProjectId()) || StringUtils.isEmpty(request.getBuildingId())) {
            throw new DashboardException(DashboardErrorEnum.ARGUMENT_INVALID);
        }
        boolean whole = request.getStorey() == null || request.getStorey() <= 0;
        List<ProjectBuildingHouseInfoMView> hiList = super.getListByProjectIdAndBuildingId(projectBuildingHouseInfoMapper, ProjectBuildingHouseInfoMView.class, request.getProjectId(), request.getBuildingId(), request.getStorey());
        if (CollUtil.isEmpty(hiList)) throw new DashboardException(DashboardErrorEnum.EMPTY_RESULT);

        VO vo = new VO();
        Long[] person = initArray(hiList.size());
        Long[] house = initArray(hiList.size());
        Long[] unit = initArray(hiList.size());
        Long[] area = initArray(hiList.size());

        for (int i = 0; i < hiList.size(); i++) {
            ProjectBuildingHouseInfoMView hi = hiList.get(i);
            int index = i;
            if (whole && hi.getStorey() != null && hi.getStorey() > 0) {
                index = hi.getStorey() - 1;
            }
            person[index] = hi.getCntPerson();
            house[index] = hi.getCntHouse();
            unit[index] = hi.getCntUnit();
            area[index] = hi.getCntArea();
            vo.setAllPerson(vo.getAllPerson() + hi.getCntPerson());
            vo.setAllHouse(vo.getAllHouse() + hi.getCntHouse());
        }
        vo.setPerson(person);
        vo.setHouse(house);
        vo.setUnit(unit);
        vo.setArea(area);
        vo.setAllStorey(hiList.size());
        vo.setAllUnit(ArrayUtil.max(unit));
        vo.setAllArea(ArrayUtil.max(area));

        return JSONUtil.toJSONObject(vo);
    }

    @Data
    private class VO {
        /**
         * 楼层数
         */
        @ApiModelProperty(value = "楼层数")
        private Integer allStorey = 0;

        /**
         * 整楼人口数
         */
        @ApiModelProperty(value = "整楼人口数")
        private Long allPerson = 0L;

        /**
         * 整楼房屋数
         */
        @ApiModelProperty(value = "整楼房屋数")
        private Long allHouse = 0L;

        /**
         * 人口数
         */
        @ApiModelProperty(value = "人口数")
        private Long[] person;

        /**
         * 房屋数
         */
        @ApiModelProperty(value = "房屋数")
        private Long[] house;

        /**
         * 整楼单元数
         */
        @ApiModelProperty(value = "整楼单元数")
        private Long allUnit = 0L;

        /**
         * 单元数
         */
        @ApiModelProperty(value = "单元数")
        private Long[] unit;

        /**
         * 整楼面积
         */
        @ApiModelProperty(value = "整楼面积")
        private Long allArea = 0L;

        /**
         * 面积
         */
        @ApiModelProperty(value = "面积")
        private Long[] area;
    }

    @Override
    public String getServiceName() {
        return ServiceNameEnum.BUILDING_HOUSE_INFO.serviceName;
    }

    @Override
    public String getVersion() {
        return VersionEnum.V1.number;
    }
}
