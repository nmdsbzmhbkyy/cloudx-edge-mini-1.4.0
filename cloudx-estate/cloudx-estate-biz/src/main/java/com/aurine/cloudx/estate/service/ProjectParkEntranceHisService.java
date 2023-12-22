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

package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.entity.ProjectParkEntranceHis;
import com.aurine.cloudx.estate.vo.ProjectParkEntranceHisVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.time.LocalDateTime;

/**
 * 车场管理
 *
 * @author 黄阳光
 * @date 2020-07-07 15:10:26
 */
public interface ProjectParkEntranceHisService extends IService<ProjectParkEntranceHis> {

//    boolean save(ProjectParkEntranceHisVo vo);

    IPage<ProjectParkEntranceHisVo> page(Page page, ProjectParkEntranceHisVo vo);

    IPage<ProjectParkEntranceHisVo> enterVehiclePage(Page page, ProjectParkEntranceHisVo projectParkEntranceHisVo);

    ProjectParkEntranceHisVo getById(String uid);


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
    boolean addEnter(String parkingId, String parkOrderCode, String plateNumber, LocalDateTime enterTime, String enterGateName, String enterOperatorName);

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
    boolean addOuter(String parkingId, String parkOrderCode, LocalDateTime outTime, String outGateName, String outOperatorName);

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
    boolean addEnterImg(String parkingId, String parkOrderCode, String imgUrl);

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
    boolean addOuterImg(String parkingId, String parkOrderCode, String imgUrl);

    /**
     * 根据第三方订单ID，获取车场记录
     *
     * @param parkingId
     * @param parkOrderCode
     * @return
     * @author: 王伟
     * @since 2020-07-29
     */
    ProjectParkEntranceHis getByParkOrderCode(String parkingId, String parkOrderCode);

    /**
     * 获取当前项目下所有车场的临时停车数量
     * 既车辆类型为临时车、只有入场时间的记录
     * @return
     */
    int countTemperCarNumber();

    /**
     * 获取已入场车辆数量
     * @return
     */
    int countEnterCarNumber();
}
