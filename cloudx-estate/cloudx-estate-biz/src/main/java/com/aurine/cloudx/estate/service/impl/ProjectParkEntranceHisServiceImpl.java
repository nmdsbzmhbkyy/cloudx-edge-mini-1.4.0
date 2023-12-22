/*
 *    Copyright (c) 2018-2025, lengleng All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * Neither the name of the pig4cloud.com developer nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * Author: lengleng (wangiegie@gmail.com)
 */
package com.aurine.cloudx.estate.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.constant.enums.ParkingRuleTypeEnum;
import com.aurine.cloudx.estate.entity.ProjectParCarRegister;
import com.aurine.cloudx.estate.entity.ProjectParkBillingRule;
import com.aurine.cloudx.estate.entity.ProjectParkEntranceHis;
import com.aurine.cloudx.estate.entity.ProjectParkingInfo;
import com.aurine.cloudx.estate.mapper.ProjectParkEntranceHisMapper;
import com.aurine.cloudx.estate.service.ProjectParCarRegisterService;
import com.aurine.cloudx.estate.service.ProjectParkBillingRuleService;
import com.aurine.cloudx.estate.service.ProjectParkEntranceHisService;
import com.aurine.cloudx.estate.service.ProjectParkingInfoService;
import com.aurine.cloudx.estate.subscribe.factory.EventFactory;
import com.aurine.cloudx.estate.subscribe.service.EventSubscribeService;
import com.aurine.cloudx.estate.vo.ProjectParkEntranceHisVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.common.data.tenant.TenantContextHolder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

import static com.aurine.cloudx.estate.constant.decidedTypeConstant.CAR_TYPE;
import static com.aurine.cloudx.estate.thirdparty.main.entity.constant.chargeType.*;

/**
 * 车场管理
 *
 * @author 黄阳光
 * @date 2020-07-07 15:10:26
 */
@Service
public class ProjectParkEntranceHisServiceImpl extends ServiceImpl<ProjectParkEntranceHisMapper, ProjectParkEntranceHis> implements ProjectParkEntranceHisService {

    @Resource
    ProjectParkEntranceHisMapper projectParkEntranceHisMapper;

    @Resource
    ProjectParkingInfoService projectParkingInfoService;

    @Resource
    ProjectParCarRegisterService projectParCarRegisterService;

    @Resource
    ProjectParkBillingRuleService projectParkBillingRuleService;

    @Resource
    EventFactory eventFactory;

//    @Resource
//    ProjectParkBillingInfoService projectParkBillingInfoService;

//    @Override
//    public boolean save(ProjectParkEntranceHisVo vo) {
//        ProjectParkEntranceHis po = new ProjectParkEntranceHis();
//        BeanUtils.copyProperties(vo, po);
//        return this.save(po);
//    }

    @Override
    public IPage<ProjectParkEntranceHisVo> page(Page page, ProjectParkEntranceHisVo vo) {
        vo.setProjectId(ProjectContextHolder.getProjectId());
        vo.setTenantId(TenantContextHolder.getTenantId());
        return projectParkEntranceHisMapper.selectPage(page, vo);
    }

    @Override
    public IPage<ProjectParkEntranceHisVo> enterVehiclePage(Page page, ProjectParkEntranceHisVo vo) {
        vo.setProjectId(ProjectContextHolder.getProjectId());
        vo.setTenantId(TenantContextHolder.getTenantId());
        return projectParkEntranceHisMapper.enterVehiclePage(page, vo);
    }

    @Override
    public ProjectParkEntranceHisVo getById(String uid) {
        return projectParkEntranceHisMapper.selectById(uid);
    }

    /**
     * 添加入场纪录
     *
     * @param parkingId
     * @param parkOrderCode
     * @param enterTime
     * @param enterGateName
     * @param enterOperatorName
     * @return
     * @author: 王伟
     * @since 2020-07-29
     */
    @Override
    public boolean addEnter(String parkingId, String parkOrderCode, String plateNumber, LocalDateTime enterTime, String enterGateName, String enterOperatorName) {


        ProjectParkEntranceHis po = new ProjectParkEntranceHis();
        po.setParkId(parkingId);
        po.setParkOrderCode(parkOrderCode);
        po.setEnterGateName(enterGateName);
        po.setEnterTime(enterTime);
        po.setEnterOperatorName(enterOperatorName);
        //车牌号转换，区域，字母+空格
        po.setPlateNumber(formatePlateNumber(plateNumber));

        ProjectParkingInfo parkingInfo = projectParkingInfoService.getById(parkingId);
        po.setProjectId(parkingInfo.getProjectId());
        po.setTenantId(parkingInfo.getTenantId());
        po.setParkName(parkingInfo.getParkName());
        ProjectParCarRegister one = projectParCarRegisterService.getOne(new LambdaQueryWrapper<ProjectParCarRegister>()
                .eq(ProjectParCarRegister::getPlateNumber, plateNumber));
        if (one == null) {
            //临时车
            po.setRuleTypeName(TEMPORARY_CAR);
        } else {
            ProjectParkBillingRule billingRule = projectParkBillingRuleService.getById(one.getRuleId());
            if (ParkingRuleTypeEnum.FREE.code.equals(billingRule.getRuleType())) {
                //免费车
                po.setRuleTypeName(FREE_CAR);
            } else if (ParkingRuleTypeEnum.MONTH.code.equals(billingRule.getRuleType())) {
                //月租车
                po.setRuleTypeName(RENT_CAR);
            }
        }


        //检查入场纪录是否已存在
        ProjectContextHolder.setProjectId(parkingInfo.getProjectId());
        long number = this.count(new QueryWrapper<ProjectParkEntranceHis>().lambda().eq(ProjectParkEntranceHis::getParkOrderCode, parkOrderCode));
        if (number >= 1) {
            return false;
        }
        //发送车行入场记录订阅信息
        EventSubscribeService eventSubscribeService = eventFactory.GetProduct(CAR_TYPE);
        String poString = JSONObject.toJSONString(po);
        eventSubscribeService.send(JSONObject.parseObject(poString), parkingInfo.getProjectId(), CAR_TYPE);
        return this.save(po);
    }

    /**
     * 格式化车牌号
     *
     * @param plateNumber
     * @return
     */
    private String formatePlateNumber(String plateNumber) {
        //京A11111 -> 京A 11111; 京AE11111 -> 京A E11111
//        if ((plateNumber.length() == 7 || plateNumber.length() == 8) && plateNumber.indexOf(" ") == -1 ) {//添加空格
//            char[] charArray = plateNumber.toCharArray();
//
//            plateNumber = "";
//            for (int i = 0; i < charArray.length; i++) {
//                plateNumber += charArray[i];
//                if (i == 1) {
//                    plateNumber += " ";
//                }
//            }
//            return plateNumber;
//
//        } else {
//            return plateNumber;
//        }

        /**
         * 车牌号存储改为无空格模式
         * @author: 王伟
         * @since 2020-10-21 17:03
         */
        return plateNumber;
    }


    /**
     * 添加出场记录
     *
     * @param parkingId
     * @param parkOrderCode
     * @param outTime
     * @param outGateName
     * @param outOperatorName
     * @return
     * @author: 王伟
     * @since 2020-07-29
     */
    @Override
    public boolean addOuter(String parkingId, String parkOrderCode, LocalDateTime outTime, String outGateName, String outOperatorName) {
        //根据parkOrderCode 获取要操作的车场记录
        ProjectParkEntranceHis hisPo = this.getByParkOrderCode(parkingId, parkOrderCode);
        if (hisPo != null) {
            //检查出场记录是否已存在
            if (StringUtils.isNotEmpty(hisPo.getOutGateName())) {
                return false;
            }

            hisPo.setOutTime(outTime);
            hisPo.setOutGateName(outGateName);
            hisPo.setOutOperatorName(outOperatorName);

            //发送车行出场记录订阅消息
            EventSubscribeService eventSubscribeService = eventFactory.GetProduct(CAR_TYPE);
            String poString = JSONObject.toJSONString(hisPo);
            eventSubscribeService.send(JSONObject.parseObject(poString), hisPo.getProjectId(), CAR_TYPE);

            return this.updateById(hisPo);
        } else {
            return false;
        }
    }

    /**
     * 添加入场照片
     *
     * @param parkingId
     * @param parkOrderCode
     * @param imgUrl
     * @return
     * @author: 王伟
     * @since 2020-07-29
     */
    @Override
    public boolean addEnterImg(String parkingId, String parkOrderCode, String imgUrl) {
        //根据parkOrderCode 获取要操作的车场记录
        ProjectParkEntranceHis hisPo = this.getByParkOrderCode(parkingId, parkOrderCode);
        if (hisPo != null) {
            hisPo.setEnterPicUrl(imgUrl);
            return this.updateById(hisPo);
        } else {
            return false;
        }
    }

    /**
     * 添加出场照片
     *
     * @param parkingId
     * @param parkOrderCode
     * @param imgUrl
     * @return
     * @author: 王伟
     * @since 2020-07-29
     */
    @Override
    public boolean addOuterImg(String parkingId, String parkOrderCode, String imgUrl) {
        //根据parkOrderCode 获取要操作的车场记录
        ProjectParkEntranceHis hisPo = this.getByParkOrderCode(parkingId, parkOrderCode);
        if (hisPo != null) {
            hisPo.setOutPicUrl(imgUrl);
            return this.updateById(hisPo);
        } else {
            return false;
        }
    }

    /**
     * 根据第三方订单ID，获取车场记录
     *
     * @param parkingId
     * @param parkOrderCode
     * @return
     * @author: 王伟
     * @since 2020-07-29
     */
    @Override
    public ProjectParkEntranceHis getByParkOrderCode(String parkingId, String parkOrderCode) {
        List<ProjectParkEntranceHis> hisList = this.list(new QueryWrapper<ProjectParkEntranceHis>().lambda()
                .eq(ProjectParkEntranceHis::getParkId, parkingId)
                .eq(ProjectParkEntranceHis::getParkOrderCode, parkOrderCode));

        if (CollectionUtil.isNotEmpty(hisList)) {
            return hisList.get(0);
        } else {
            return null;
        }
    }

    /**
     * 获取当前项目下所有车场的临时停车数量
     * 既车辆类型为临时车、只有入场时间的记录
     *
     * @return
     */
    @Override
    public int countTemperCarNumber() {
        return this.baseMapper.countTempCar(ProjectContextHolder.getProjectId());
    }

    /**
     * 獲取已入场车辆数量
     *
     * @return
     */
    @Override
    public int countEnterCarNumber() {
        return this.count(new QueryWrapper<ProjectParkEntranceHis>().lambda()
                .eq(ProjectParkEntranceHis::getProjectId, ProjectContextHolder.getProjectId())
                .isNull(ProjectParkEntranceHis::getOutTime));
    }
}
