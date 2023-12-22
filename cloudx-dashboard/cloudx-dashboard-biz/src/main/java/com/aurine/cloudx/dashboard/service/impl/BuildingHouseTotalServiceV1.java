package com.aurine.cloudx.dashboard.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.dashboard.constant.ServiceNameEnum;
import com.aurine.cloudx.dashboard.constant.VersionEnum;
import com.aurine.cloudx.dashboard.entity.ProjectBuildingHouseTotalMView;
import com.aurine.cloudx.dashboard.exception.DashboardErrorEnum;
import com.aurine.cloudx.dashboard.exception.DashboardException;
import com.aurine.cloudx.dashboard.mapper.ProjectBuildingHouseTotalMapper;
import com.aurine.cloudx.dashboard.service.AbstractDashboardService;
import com.aurine.cloudx.dashboard.util.JSONUtil;
import com.aurine.cloudx.dashboard.vo.DashboardRequestVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 * @version 1
 * @description: 楼栋房屋统计
 * @author : Qiu <qiujb@miligc.com>
 * @date : 2021 08 17 9:38
 * @Copyright:
 */

@Service
public class BuildingHouseTotalServiceV1 extends AbstractDashboardService {

    @Resource
    private ProjectBuildingHouseTotalMapper projectBuildingHouseTotalMapper;

    @Override
    public JSONObject getData(DashboardRequestVo request) {
        if (StringUtils.isEmpty(request.getProjectId()) || StringUtils.isEmpty(request.getBuildingId())) {
            throw new DashboardException(DashboardErrorEnum.ARGUMENT_INVALID);
        }
        boolean whole = request.getStorey() == null || request.getStorey() <= 0;
        List<ProjectBuildingHouseTotalMView> htList = super.getListByProjectIdAndBuildingId(projectBuildingHouseTotalMapper, ProjectBuildingHouseTotalMView.class, request.getProjectId(), request.getBuildingId(), request.getStorey());
        if (CollUtil.isEmpty(htList)) throw new DashboardException(DashboardErrorEnum.EMPTY_RESULT);

        VO vo = new VO();
        Long[] total = initArray(htList.size());
        Long[] owner = initArray(htList.size());
        Long[] family = initArray(htList.size());
        Long[] rent = initArray(htList.size());
        Long[] houseTotal = initArray(htList.size());
        Long[] houseRent = initArray(htList.size());
        Long[] houseSelf = initArray(htList.size());
        Long[] houseIdle = initArray(htList.size());
        Double[] houseRentalRate = initDoubleArray(htList.size());

        for (int i = 0; i < htList.size(); i++) {
            ProjectBuildingHouseTotalMView ht = htList.get(i);
            int index = i;
            if (whole && ht.getStorey() != null && ht.getStorey() > 0) {
                index = ht.getStorey() - 1;
            }
            total[index] = ht.getCntTotal();
            owner[index] = ht.getCntOwner();
            family[index] = ht.getCntFamily();
            rent[index] = ht.getCntRent();
            houseTotal[index] = ht.getCntHouseTotal();
            houseRent[index] = ht.getCntHouseRent();
            houseSelf[index] = ht.getCntHouseSelf();
            houseIdle[index] = ht.getCntHouseIdle();
            if (ht.getCntHouseTotal() != null && ht.getCntHouseTotal() > 0) {
                double rate = Double.valueOf(ht.getCntHouseRent() + "") / Double.valueOf(ht.getCntHouseTotal() + "");
                rate = new BigDecimal(rate).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                houseRentalRate[index] = rate;
            }

            vo.setAllTotal(vo.getAllTotal() + ht.getCntTotal());
            vo.setAllOwner(vo.getAllOwner() + ht.getCntOwner());
            vo.setAllFamily(vo.getAllFamily() + ht.getCntFamily());
            vo.setAllRent(vo.getAllRent() + ht.getCntRent());
            vo.setAllHouseTotal(vo.getAllHouseTotal() + ht.getCntHouseTotal());
            vo.setAllHouseRent(vo.getAllHouseRent() + ht.getCntHouseRent());
            vo.setAllHouseSelf(vo.getAllHouseSelf() + ht.getCntHouseSelf());
            vo.setAllHouseIdle(vo.getAllHouseIdle() + ht.getCntHouseIdle());
        }
        vo.setTotal(total);
        vo.setOwner(owner);
        vo.setFamily(family);
        vo.setRent(rent);
        vo.setHouseTotal(houseTotal);
        vo.setHouseRent(houseRent);
        vo.setHouseSelf(houseSelf);
        vo.setHouseIdle(houseIdle);
        vo.setHouseRentalRate(houseRentalRate);
        if (vo.getAllHouseTotal() != null && vo.getAllHouseTotal() > 0) {
            double rate = Double.valueOf(vo.getAllHouseRent() + "") / Double.valueOf(vo.getAllHouseTotal() + "");
            rate = new BigDecimal(rate).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            vo.setAllHouseRentalRate(rate);
        }

        return JSONUtil.toJSONObject(vo);
    }

    @Data
    private class VO {
        /**
         * 整楼住户总数
         */
        @ApiModelProperty(value = "整楼住户总数")
        private Long allTotal = 0L;

        /**
         * 整楼业主数
         */
        @ApiModelProperty(value = "整楼业主数")
        private Long allOwner = 0L;

        /**
         * 整楼家属数
         */
        @ApiModelProperty(value = "整楼家属数")
        private Long allFamily = 0L;

        /**
         * 整楼租客数
         */
        @ApiModelProperty(value = "整楼租客数")
        private Long allRent = 0L;

        /**
         * 整楼房间总数
         */
        @ApiModelProperty(value = "整楼房间总数")
        private Long allHouseTotal = 0L;

        /**
         * 整楼出租屋数
         */
        @ApiModelProperty(value = "整楼出租屋数")
        private Long allHouseRent = 0L;

        /**
         * 整楼自住房数
         */
        @ApiModelProperty(value = "整楼自住房数")
        private Long allHouseSelf = 0L;

        /**
         * 整楼空置房数
         */
        @ApiModelProperty(value = "整楼空置房数")
        private Long allHouseIdle = 0L;

        /**
         * 整楼的出租率
         */
        @ApiModelProperty(value = "整楼的出租率")
        private Double allHouseRentalRate = 0.0D;

        /**
         * 当前楼层的住户总数
         */
        @ApiModelProperty(value = "当前楼层的住户总数")
        private Long[] total;

        /**
         * 业主数
         */
        @ApiModelProperty(value = "业主数")
        private Long[] owner;

        /**
         * 家属数
         */
        @ApiModelProperty(value = "家属数")
        private Long[] family;

        /**
         * 租客数
         */
        @ApiModelProperty(value = "租客数")
        private Long[] rent;

        /**
         * 当前楼层的房间总数
         */
        @ApiModelProperty(value = "当前楼层的房间总数")
        private Long[] houseTotal;

        /**
         * 出租屋数
         */
        @ApiModelProperty(value = "出租屋数")
        private Long[] houseRent;

        /**
         * 自住房数
         */
        @ApiModelProperty(value = "自住房数")
        private Long[] houseSelf;

        /**
         * 空置房数
         */
        @ApiModelProperty(value = "空置房数")
        private Long[] houseIdle;

        /**
         * 当前楼层的出租率
         */
        @ApiModelProperty(value = "当前楼层的出租率")
        private Double[] houseRentalRate;
    }

    @Override
    public String getServiceName() {
        return ServiceNameEnum.BUILDING_HOUSE_TOTAL.serviceName;
    }

    @Override
    public String getVersion() {
        return VersionEnum.V1.number;
    }
}
