package com.aurine.cloudx.dashboard.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.dashboard.constant.ServiceNameEnum;
import com.aurine.cloudx.dashboard.constant.VersionEnum;
import com.aurine.cloudx.dashboard.entity.ProjectDataMview;
import com.aurine.cloudx.dashboard.exception.DashboardErrorEnum;
import com.aurine.cloudx.dashboard.exception.DashboardException;
import com.aurine.cloudx.dashboard.mapper.ProjectDataMapper;
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
 * @description:框架统计信息
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2021-03-25
 * @Copyright:
 */
@Service
public class FrameTotalServiceV1 extends AbstractDashboardService {
    @Resource
    private ProjectDataMapper projectDataMapper;

    /**
     * 获取数据
     *
     * @param request
     * @return
     */
    @Override
    public JSONObject getData(DashboardRequestVo request) {
        request.getProjectIdArray();
        List<ProjectDataMview> poList = super.getListByProjectId(projectDataMapper, ProjectDataMview.class, request.getProjectIdArray());

        if (CollUtil.isEmpty(poList)) throw new DashboardException(DashboardErrorEnum.EMPTY_RESULT);

        VO vo = new VO();

        for (ProjectDataMview po : poList) {
            vo.setBuildingNum(po.getBuildingNum() + vo.getBuildingNum());
            vo.setHouseNum(po.getHouseNum() + vo.getHouseNum());
            vo.setOccupiedHouseNum((po.getHouseNum() - po.getHouseIdleNum()) + vo.getOccupiedHouseNum());
            vo.setIdleHouseNum(po.getHouseIdleNum() + vo.getIdleHouseNum());

            vo.setParkingPlaceNum(po.getPlaceNum() + vo.getParkingPlaceNum());
            vo.setDeviceNum(po.getDeviceNum() + vo.getDeviceNum());
            vo.setCarNum(po.getCarNum() + vo.getCarNum());
            vo.setPersonNum(po.getPersonNum() + vo.getPersonNum());
        }


        return JSONUtil.toJSONObject(vo);
    }

    @Data
    private class VO {


        @ApiModelProperty(value = "房屋总数")
        private Long houseNum = 0L;
        @ApiModelProperty(value = "入住房屋数")
        private Long occupiedHouseNum = 0L;
        @ApiModelProperty(value = "空房数")
        private Long idleHouseNum = 0L;
        @ApiModelProperty(value = "楼栋总数")
        private Long buildingNum = 0L;
        @ApiModelProperty(value = "车位数")
        private Long parkingPlaceNum = 0L;
        @ApiModelProperty(value = "设备数")
        private Long deviceNum = 0L;
        @ApiModelProperty(value = "车辆数")
        private Long carNum = 0L;
        @ApiModelProperty(value = "人口数")
        private Long personNum = 0L;


    }

    /**
     * 获取服务名称
     *
     * @return
     */
    @Override
    public String getServiceName() {
        return ServiceNameEnum.FRAME_TOTAL.serviceName;
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
