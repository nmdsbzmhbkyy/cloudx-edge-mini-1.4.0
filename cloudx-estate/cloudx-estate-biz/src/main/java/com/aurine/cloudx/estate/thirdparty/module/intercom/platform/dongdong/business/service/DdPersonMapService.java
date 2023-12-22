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

package com.aurine.cloudx.estate.thirdparty.module.intercom.platform.dongdong.business.service;

import com.aurine.cloudx.estate.entity.ProjectHousePersonRel;
import com.aurine.cloudx.estate.thirdparty.module.intercom.platform.dongdong.business.entity.DdPersonMap;
import com.aurine.cloudx.estate.vo.ProjectDeviceInfoProxyVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 社区人员映射
 *
 * @author 王伟
 * @date 2020-11-18 17:16:19
 */
public interface DdPersonMapService extends IService<DdPersonMap> {

    /**
     * 根据设备id，获取有哪些住户可以访问当前设备
     * 排除关闭了增值服务的房屋
     *
     * @param deviceInfo
     * @return
     */
    List<DdPersonMap> listOriginPersonMapByDeviceId(ProjectDeviceInfoProxyVo deviceInfo);

    /**
     * 通过房屋id，获取到该房屋下所有可以使用的设备-人物 关系
     *
     * @param houseId
     * @return
     */
    List<DdPersonMap> listOriginPersonMapByHouse(String houseId);

    /**
     * 通过人物关系，获取当前住户在当前的房屋下，可以使用的设备-人物 关系
     *
     * @param housePersonRel
     * @return
     */
    List<DdPersonMap> listOriginPersonMapByHousePersonRel(ProjectHousePersonRel housePersonRel);


    /**
     * 根据projectId删除
     *
     * @param projectId
     * @return
     */
    boolean delByProjectId(int projectId);

    /**
     * 根据房间号和人员id，获取数据列表
     *
     * @param houseId
     * @param personId
     * @return
     */
    List<DdPersonMap> listByHouseAndPerson(String houseId, String personId);


    /**
     * 根据房间号，获取数据列表
     *
     * @param houseId
     * @return
     */
    List<DdPersonMap> listByHouse(String houseId);
}
