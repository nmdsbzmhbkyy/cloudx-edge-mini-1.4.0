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

import com.aurine.cloudx.estate.entity.SysDeviceProductMap;
import com.aurine.cloudx.estate.vo.SysDeviceProductMapVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 设备产品
 *
 * @author 王伟
 * @date 2020-10-26 11:46:15
 */
public interface SysDeviceProductMapService extends IService<SysDeviceProductMap> {

    /**
     * 同步产品列表信息
     * @param sysDeviceProductMapList
     */
    void syncProductList(List<SysDeviceProductMap> sysDeviceProductMapList);

    /**
    * <p>
    * 获取当前项目已拥有设备的产品列表
    * </p>
    *
    * @author: 王良俊
    */
    List<SysDeviceProductMapVo> getProductList();

    /**
     * 根据productCode获取产品信息
     * @param productCode
     * @return
     */
    SysDeviceProductMap getByProductCode(String productCode);

}
