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

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.constant.enums.DeviceParamEnum;
import com.aurine.cloudx.estate.entity.SysDeviceProductMap;
import com.aurine.cloudx.estate.entity.SysProductService;
import com.aurine.cloudx.estate.mapper.SysDeviceProductMapMapper;
import com.aurine.cloudx.estate.service.SysDeviceProductMapService;
import com.aurine.cloudx.estate.service.SysProductServiceService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 设备产品
 *
 * @author 王伟
 * @date 2020-10-26 11:46:15
 */
@Service
@Slf4j
public class SysDeviceProductMapServiceImpl extends ServiceImpl<SysDeviceProductMapMapper, SysDeviceProductMap> implements SysDeviceProductMapService {

    @Autowired
    private SysProductServiceService sysProductServiceService;

    static ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    /**
     * 同步产品列表信息
     *
     * @param sysDeviceProductMapList
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void syncProductList(List<SysDeviceProductMap> sysDeviceProductMapList) {

        if (CollUtil.isNotEmpty(sysDeviceProductMapList)) {
            List<String> productCodeList = sysDeviceProductMapList.stream().map(SysDeviceProductMap::getProductCode).collect(Collectors.toList()); //要同步的code list
            List<String> originProductCodeList;//系统里原有的code list(并集+同步list补集)

            List<SysDeviceProductMap> originProductList;
            List<SysDeviceProductMap> addProductList = new ArrayList<>();
            List<SysDeviceProductMap> updateProductList = new ArrayList<>();


            //根据要同步的数据，取得现有数据对象
            originProductList = this.list(new QueryWrapper<SysDeviceProductMap>().lambda().in(SysDeviceProductMap::getProductCode, productCodeList));
            originProductCodeList = originProductList.stream().map(SysDeviceProductMap::getProductCode).collect(Collectors.toList());

            //添加差异数据
            for (String productCode : CollUtil.subtract(productCodeList, originProductCodeList)) {//获取productCode差集
                for (SysDeviceProductMap productMap : sysDeviceProductMapList) {
                    if (productMap.getProductCode().equals(productCode)) {
                        addProductList.add(productMap);
                        break;
                    }
                }
            }

            // 这里准备添加产品和设备参数的关系
            List<SysProductService> productParamCategoriesList = new ArrayList<>();
//            List<SysDeviceProductMap> newProductMapList = sysDeviceProductMapList.stream().filter(sysDeviceProductMap -> !originProductCodeList.contains(sysDeviceProductMap.getProductCode())).collect(Collectors.toList());
//            List<String> productIdList = newProductMapList.stream().map(SysDeviceProductMap::getProductId).collect(Collectors.toList());
            List<String> productIdList = sysDeviceProductMapList.stream().map(SysDeviceProductMap::getProductId).collect(Collectors.toList());
            sysDeviceProductMapList.forEach(sysDeviceProductMap -> {
                try {
                    ArrayNode capabilities = (ArrayNode) objectMapper.readTree(sysDeviceProductMap.getCapabilities());
                    capabilities.forEach(capability -> {
                        // 这里判断是否有设备参数设置这个功能
                        if (capability.path("service_type").asText().equals("DeviceParam")) {
                            List<JsonNode> params = capability.findValues("property_name");
                            if (CollUtil.isNotEmpty(params)) {
                                params.forEach(param -> {
                                    SysProductService productParamCategory = new SysProductService();
                                    productParamCategory.setProductId(sysDeviceProductMap.getProductId());
                                    productParamCategory.setServiceId(param.asText());
                                    // 如果是枚举里面没有的参数则跳过
                                    if (StrUtil.isNotEmpty(param.asText())) {
                                        productParamCategoriesList.add(productParamCategory);
//                                        if (objName.equals(DeviceParamEnum.DEVICE_NO_OBJ.objName)) {
//                                            // 这里由于设备编号规则有serviceId但是又是设备编号的一个属性只能这么添加了
//                                            SysProductService paramCategory = new SysProductService();
//                                            paramCategory.setProductId(sysDeviceProductMap.getProductId());
//                                            paramCategory.setServiceId(DeviceParamEnum.DEV_RULE_OBJ.objName);
//                                            productParamCategoriesList.add(paramCategory);
//                                        }
                                    }
                                });
                                log.info("获取到产品code：{}，设备参数集合：{}", sysDeviceProductMap.getProductCode(), params);
                            }
                        } else {
                            SysProductService productService = new SysProductService();
                            productService.setProductId(sysDeviceProductMap.getProductId());
                            productService.setServiceId(capability.path("service_id").asText());
                            productParamCategoriesList.add(productService);
                        }
                    });
                } catch (JsonProcessingException e) {
                    log.info("当前产品无服务，产品code：{}，项目ID：{}", sysDeviceProductMap.getProductCode(), ProjectContextHolder.getProjectId());
                    e.printStackTrace();
                }
            });
            if (CollUtil.isNotEmpty(productParamCategoriesList)) {
                // 同步产品的时候更新参数
                if (CollUtil.isNotEmpty(productIdList)) {
                    sysProductServiceService.remove(new QueryWrapper<SysProductService>().lambda().in(SysProductService::getProductId, productIdList));
                }

                sysProductServiceService.saveBatch(productParamCategoriesList);
            }




            this.saveBatch(addProductList);


            //更新重复数据
            SysDeviceProductMap originProductMap;
            for (String productCode : CollUtil.intersection(productCodeList, originProductCodeList)) {//獲取productCode并集
                for (SysDeviceProductMap productMap : sysDeviceProductMapList) {
                    if (productMap.getProductCode().equals(productCode)) {
                        //产品编号相同，将原有的主键赋值到新的数据对象，进行覆盖更新
                        originProductMap = originProductList.stream().filter(e -> e.getProductCode().equals(productCode)).findFirst().get();
                        productMap.setSeq(originProductMap.getSeq());
                        updateProductList.add(productMap);
                        break;
                    }
                }
            }

            this.updateBatchById(updateProductList);


        }
    }
}
