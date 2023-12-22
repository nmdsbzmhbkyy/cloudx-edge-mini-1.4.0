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

package com.aurine.cloudx.open.origin.service;


import com.aurine.cloudx.open.origin.dto.ProjectParCarRegisterDto;
import com.aurine.cloudx.open.origin.entity.ProjectCarInfo;
import com.aurine.cloudx.open.origin.vo.ProjectCarInfoVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 车辆信息
 *
 * @author 王伟
 * @date 2020-07-08 14:33:58
 */
public interface ProjectCarInfoService extends IService<ProjectCarInfo> {

    /**
     * <p>
     * 根据车牌号获取车辆信息
     * </p>
     *
     * @param carUid 车辆ID
     * @return 车辆对象
     */
    ProjectCarInfo getCar(String carUid);

    /**
     * <p>
     * 根据车牌号获取车辆信息
     * </p>
     *
     * @param plateNumber 车牌号
     * @return 车辆对象
     */
    ProjectCarInfo getCarByPlateNumber(String plateNumber);

    /**
     * 保存车辆信息
     *
     * @param carInfo
     * @return
     */
    boolean saveCar(ProjectCarInfo carInfo);

    /**
     * 根据车牌号，获取VO,显示车辆以及所属人员的信息
     *
     * @param plateNumber
     * @return
     */
    ProjectCarInfoVo getVoByPlateNumber(String plateNumber);

    /**
     * <p>
     * 根据对象中的车牌号初始化车辆（并把车辆的uid放入车辆登记对象中）
     * </p>
     *
     * @param
     * @return
     * @throws
     */
    List<ProjectParCarRegisterDto> initParCarRegisterCarUid(List<ProjectParCarRegisterDto> parCarRegisterVoList);

    /**
     * <p>
     * 根据传入的车辆登记vo对象列表对车辆进行添加或更新操作并返回带车辆uid的登记对象列表
     * </p>
     *
     * @param parCarRegisterVoList 车辆登记vo对象列表
     * @return 已经设置了车辆uid的车辆登记vo对象列表
     * @author: 王良俊
     */
    List<ProjectParCarRegisterDto> saveOrUpdateCarByRegisterVoList(List<ProjectParCarRegisterDto> parCarRegisterVoList);
}
