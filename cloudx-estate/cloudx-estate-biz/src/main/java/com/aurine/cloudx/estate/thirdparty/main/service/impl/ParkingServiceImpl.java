package com.aurine.cloudx.estate.thirdparty.main.service.impl;

import com.aurine.cloudx.estate.entity.ProjectEntryExitLane;
import com.aurine.cloudx.estate.entity.ProjectParkingInfo;
import com.aurine.cloudx.estate.service.*;
import com.aurine.cloudx.estate.thirdparty.main.entity.dto.EventDeviceParkingPassDTO;
import com.aurine.cloudx.estate.thirdparty.main.entity.dto.EventDeviceParkingPayDTO;
import com.aurine.cloudx.estate.thirdparty.main.service.ParkingService;
import com.aurine.cloudx.estate.thirdparty.module.parking.factory.ParkingFactoryProducer;
import com.aurine.cloudx.estate.util.ImgConvertUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @description:
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-08-06
 * @Copyright:
 */
@Service
public class ParkingServiceImpl implements ParkingService {
    @Resource
    ProjectParkingPlaceService projectParkingPlaceService;
    @Resource
    ProjectParkingInfoService projectParkingInfoService;
    @Resource
    ProjectCarInfoService projectCarInfoService;
    @Resource
    ProjectParkBillingRuleService projectParkBillingRuleService;
    @Resource
    ProjectPersonInfoService projectPersonInfoService;
    @Resource
    ProjectParkEntranceHisService projectParkEntranceHisService;
    @Resource
    ProjectEntryExitLaneService projectEntryExitLaneService;
    @Resource
    ImgConvertUtil imgConvertUtil;


    /**
     * 回调添加车辆入场纪录
     *
     * @param parkingPassDTO
     * @return
     */
    @Override
    public boolean enterCar(EventDeviceParkingPassDTO parkingPassDTO) {
        ProjectParkingInfo parkingInfo = projectParkingInfoService.getByThirdCode(parkingPassDTO.getThirdCode());

        if (parkingInfo != null) {
            boolean result = projectParkEntranceHisService.addEnter(parkingInfo.getParkId(), parkingPassDTO.getOrderNo(),
                    parkingPassDTO.getCarNo(), parkingPassDTO.getEnterDateTime(), parkingPassDTO.getGateName(), parkingPassDTO.getOperatorName());

            /**
             * 如果入场信息中已存在入场图片，直接进行保存
             */
            if (StringUtils.isNotEmpty(parkingPassDTO.getImgUrl())) {
                result = this.enterImg(parkingPassDTO);
            }
            return result;

        } else {
            //记录异常,未知车场
            return false;
        }
    }

    /**
     * 回调添加车辆出场纪录
     *
     * @param parkingPassDTO
     * @return
     */
    @Override
    public boolean outerCar(EventDeviceParkingPassDTO parkingPassDTO) {
        //调用业务
        ProjectParkingInfo parkingInfo = projectParkingInfoService.getByThirdCode(parkingPassDTO.getThirdCode());

        if (parkingInfo != null) {
            boolean result = projectParkEntranceHisService.addOuter(parkingInfo.getParkId(), parkingPassDTO.getOrderNo(),
                    parkingPassDTO.getOutDateTime(), parkingPassDTO.getGateName(), parkingPassDTO.getOperatorName());

            /**
             * 如果出场信息中已存在入场图片，直接进行保存
             */
            if (StringUtils.isNotEmpty(parkingPassDTO.getImgUrl())) {
                result = this.outerImg(parkingPassDTO);
            }
            return result;

        } else {
            //记录异常,未知车场
            return false;
        }
    }

    /**
     * 回调添加车辆入场图片
     *
     * @param parkingPassDTO
     * @return
     */
    @Override
    public boolean enterImg(EventDeviceParkingPassDTO parkingPassDTO) {

        //调用业务
        ProjectParkingInfo parkingInfo = projectParkingInfoService.getByThirdCode(parkingPassDTO.getThirdCode());

        if (parkingInfo != null) {
            return projectParkEntranceHisService.addEnterImg(parkingInfo.getParkId(), parkingPassDTO.getOrderNo(), imgConvertUtil.convertToLocalUrl(parkingPassDTO.getImgUrl()));
        } else {
            //记录异常,未知车场
            return false;
        }
    }

    /**
     * 回调添加车辆出场图片
     *
     * @param parkingPassDTO
     * @return
     */
    @Override
    public boolean outerImg(EventDeviceParkingPassDTO parkingPassDTO) {

        //调用业务
        ProjectParkingInfo parkingInfo = projectParkingInfoService.getByThirdCode(parkingPassDTO.getThirdCode());

        if (parkingInfo != null) {
            return projectParkEntranceHisService.addOuterImg(parkingInfo.getParkId(), parkingPassDTO.getOrderNo(), imgConvertUtil.convertToLocalUrl(parkingPassDTO.getImgUrl()));
        } else {
            //记录异常,未知车场
            return false;
        }
    }

    /**
     * 回调添加临时车缴费记录
     * 该方法为预留方法，暂未使用
     *
     * @param deviceParkingPayDTO
     * @return
     */
    @Override
    public boolean pay(EventDeviceParkingPayDTO deviceParkingPayDTO) {
        return false;
    }

    /**
     * 获取车道信息
     *
     * @param parkingId
     * @return
     */
    @Override
    public List<ProjectEntryExitLane> getLaneList(String parkingId) {
        List<ProjectEntryExitLane> laneList = ParkingFactoryProducer.getFactory(parkingId).getParkingService().getLaneList(parkingId);
        return laneList;
    }

    /**
     * 设置车场在线状态
     *
     * @param eventDeviceParkingPassDTO
     */
    @Override
    public void parkingStatus(EventDeviceParkingPassDTO eventDeviceParkingPassDTO) {
        projectParkingInfoService.update(Wrappers.lambdaUpdate(ProjectParkingInfo.class)
                .set(ProjectParkingInfo::getIsOnline,eventDeviceParkingPassDTO.getIsOnline())
                .eq(ProjectParkingInfo::getCompany, eventDeviceParkingPassDTO.getCompany()));
    }
}
