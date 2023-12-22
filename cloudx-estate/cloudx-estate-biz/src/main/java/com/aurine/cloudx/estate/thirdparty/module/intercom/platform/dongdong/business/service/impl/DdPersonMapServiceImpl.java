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
package com.aurine.cloudx.estate.thirdparty.module.intercom.platform.dongdong.business.service.impl;

import com.aurine.cloudx.common.core.constant.enums.DeviceTypeEnum;
import com.aurine.cloudx.estate.entity.ProjectHousePersonRel;
import com.aurine.cloudx.estate.thirdparty.module.intercom.platform.dongdong.business.entity.DdPersonMap;
import com.aurine.cloudx.estate.thirdparty.module.intercom.platform.dongdong.business.mapper.DdPersonMapMapper;
import com.aurine.cloudx.estate.thirdparty.module.intercom.platform.dongdong.business.service.DdPersonMapService;
import com.aurine.cloudx.estate.vo.ProjectDeviceInfoProxyVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 社区人员映射
 *
 * @author 王伟
 * @date 2020-11-18 17:16:19
 */
@Service
@Slf4j
public class DdPersonMapServiceImpl extends ServiceImpl<DdPersonMapMapper, DdPersonMap> implements DdPersonMapService {


    /**
     * 根据设备id，获取有哪些住户可以访问当前设备
     * 排除关闭了增值服务的房屋
     *
     * @param deviceInfo
     * @return
     */
    @Override
    public List<DdPersonMap> listOriginPersonMapByDeviceId(ProjectDeviceInfoProxyVo deviceInfo) {


        List<DdPersonMap> resultlist = new ArrayList<>();

        DeviceTypeEnum deviceTypeEnum = DeviceTypeEnum.getByCode(deviceInfo.getDeviceType());

        if (deviceTypeEnum == DeviceTypeEnum.GATE_DEVICE) {

            if (StringUtils.isEmpty(deviceInfo.getDeviceEntityId())) {
                //公共区口机，项目下所有住户都可以被呼叫
                resultlist = this.baseMapper.listOriginByFrame(deviceInfo.getProjectId(), "", "", "", false);

            } else {
                //组团区口机，组团下楼栋住户都可以被呼叫
                String groupId = deviceInfo.getDeviceEntityId();
                resultlist = this.baseMapper.listOriginByFrame(deviceInfo.getProjectId(), groupId, "", "", false);

            }

        } else if (deviceTypeEnum == DeviceTypeEnum.LADDER_WAY_DEVICE) {

            //梯口机，梯口机单元内的住户都可以被呼叫
            //梯口机呼叫时，使用房间号作为拨号号码
            resultlist = this.baseMapper.listOriginByFrame(deviceInfo.getProjectId(), "", "", deviceInfo.getUnitId(), true);


        } else {

        }


        return resultlist;
    }

    /**
     * 通过房屋id，获取到该房屋下所有可以使用的设备-人物 关系
     *
     * @param houseId
     * @return
     */
    @Override
    public List<DdPersonMap> listOriginPersonMapByHouse(String houseId) {
        return this.baseMapper.listOriginByHousePerson(houseId, null);
    }

    /**
     * 通过人物关系，获取当前住户在当前的房屋下，可以使用的设备-人物 关系
     *
     * @param housePersonRel
     * @return
     */
    @Override
    public List<DdPersonMap> listOriginPersonMapByHousePersonRel(ProjectHousePersonRel housePersonRel) {
        return this.baseMapper.listOriginByHousePerson(housePersonRel.getHouseId(), housePersonRel.getPersonId());
    }


    /**
     * 根据projectId删除
     *
     * @param projectId
     * @return
     */
    @Override
    public boolean delByProjectId(int projectId) {
        return this.remove(new QueryWrapper<DdPersonMap>().lambda().eq(DdPersonMap::getProjectId, projectId));
    }

    /**
     * 根据房间号和人员id，获取数据列表
     *
     * @param houseId
     * @param personId
     * @return
     */
    @Override
    public List<DdPersonMap> listByHouseAndPerson(String houseId, String personId) {
        return this.list(new QueryWrapper<DdPersonMap>().lambda().eq(DdPersonMap::getHouseId, houseId).eq(DdPersonMap::getPersonId, personId).orderByAsc(DdPersonMap::getDeviceId));
    }

    /**
     * 根据房间号，获取数据列表
     *
     * @param houseId
     * @return
     */
    @Override
    public List<DdPersonMap> listByHouse(String houseId) {
        return this.list(new QueryWrapper<DdPersonMap>().lambda().eq(DdPersonMap::getHouseId, houseId).orderByAsc(DdPersonMap::getDeviceId));
    }
}
