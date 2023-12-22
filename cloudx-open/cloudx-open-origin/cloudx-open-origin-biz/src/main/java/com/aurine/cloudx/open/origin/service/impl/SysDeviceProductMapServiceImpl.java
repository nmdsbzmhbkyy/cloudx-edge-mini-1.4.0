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
package com.aurine.cloudx.open.origin.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.open.origin.mapper.SysDeviceProductMapMapper;
import com.aurine.cloudx.open.origin.entity.SysDeviceProductMap;
import com.aurine.cloudx.open.origin.entity.SysProductService;
import com.aurine.cloudx.open.origin.vo.SysDeviceProductMapVo;
import com.aurine.cloudx.open.origin.service.SysDeviceProductMapService;
import com.aurine.cloudx.open.origin.service.SysProductServiceService;
import com.aurine.cloudx.open.common.core.util.ObjectMapperUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.InjectableValues;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.pig4cloud.pigx.common.data.tenant.TenantContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
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

    static ObjectMapper objectMapper = ObjectMapperUtil.instance();


    /**
     * 同步产品列表信息
     *
     * @param sysDeviceProductMapList
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void syncProductList(List<SysDeviceProductMap> sysDeviceProductMapList) {
        TenantContextHolder.setTenantId(1);
        if (CollUtil.isNotEmpty(sysDeviceProductMapList)) {
            List<String> productCodeList = sysDeviceProductMapList.stream().map(SysDeviceProductMap::getProductCode).collect(Collectors.toList()); //要同步的code list
            List<String> originProductCodeList;//系统里原有的code list(并集+同步list补集)

            List<SysDeviceProductMap> originProductList;
            List<SysDeviceProductMap> addProductList = new ArrayList<>();
            List<SysDeviceProductMap> updateProductList = new ArrayList<>();


            //根据要同步的数据，取得现有数据对象
            originProductList = this.list(new QueryWrapper<SysDeviceProductMap>().lambda().in(SysDeviceProductMap::getProductCode, productCodeList));
            originProductCodeList = originProductList.stream().map(SysDeviceProductMap::getProductCode).collect(Collectors.toList());
            Collection<String> subtract = CollUtil.subtract(productCodeList, originProductCodeList);
            //添加差异数据
            for (String productCode : subtract) {//获取productCode差集
                for (SysDeviceProductMap productMap : sysDeviceProductMapList) {
                    if (productMap.getProductCode().equals(productCode)) {
                        addProductList.add(productMap);
                        break;
                    }
                }
            }

            List<SysProductService> existProductService = sysProductServiceService.list();
            Set<String> productServiceSet = existProductService.stream().map(sysProductService -> {
                return sysProductService.getProductId() + sysProductService.getServiceId();
            }).collect(Collectors.toSet());

            // 这里准备添加产品和设备参数的关系
            List<SysProductService> productParamCategoriesList = new ArrayList<>();
//            List<SysDeviceProductMap> newProductMapList = sysDeviceProductMapList.stream().filter(sysDeviceProductMap -> !originProductCodeList.contains(sysDeviceProductMap.getProductCode())).collect(Collectors.toList());
//            List<String> productIdList = newProductMapList.stream().map(SysDeviceProductMap::getProductId).collect(Collectors.toList());
            List<String> productIdList = sysDeviceProductMapList.stream().map(SysDeviceProductMap::getProductId).collect(Collectors.toList());



            //更新重复数据
            SysDeviceProductMap originProductMap;
            for (String productCode : CollUtil.intersection(productCodeList, originProductCodeList)) {//獲取productCode并集
                for (SysDeviceProductMap productMap : sysDeviceProductMapList) {
                    if (productMap.getProductCode().equals(productCode)) {
                        //产品编号相同，将原有的主键赋值到新的数据对象，进行覆盖更新
                        originProductMap = originProductList.stream().filter(e -> e.getProductCode().equals(productCode)).findFirst().get();
                        productMap.setSeq(originProductMap.getSeq());
                        productMap.setProductId(originProductMap.getProductId());
                        updateProductList.add(productMap);
                        break;
                    }
                }
            }

            this.updateBatchById(updateProductList);

            this.saveBatch(addProductList);

            addProductList.forEach(sysDeviceProductMap -> {
                try {
                    ArrayNode capabilities = objectMapper.readValue(sysDeviceProductMap.getCapabilities(), ArrayNode.class);
                    for (int i = 0; i < capabilities.size(); i++) {
                        JsonNode capability = capabilities.get(i);

                        String serviceId = capability.path("service_id").asText();
                        if (checkHasExist(productServiceSet, sysDeviceProductMap.getProductId() + serviceId)) {
                            continue;
                        }
                        // 这里判断是否有设备参数设置这个功能
                        if ("DeviceParam".equals(serviceId) || "DeviceParams".equals(serviceId)) {
                            List<JsonNode> params = capability.findValues("property_name");
                            if (CollUtil.isNotEmpty(params)) {
                                params.forEach(param -> {
                                    // 如果和2107冲突使用这里的代码
                                    String currentServiceId = param.asText();
                                    // 如果是枚举里面没有的参数则跳过
                                    if (StrUtil.isNotEmpty(currentServiceId) && !checkHasExist(productServiceSet, sysDeviceProductMap.getProductId() + currentServiceId)) {
                                        SysProductService productParamCategory = new SysProductService();
                                        productParamCategory.setProductId(sysDeviceProductMap.getProductId());
                                        productParamCategory.setServiceId(currentServiceId);
                                        productParamCategoriesList.add(productParamCategory);
                                    }
                                });
                                log.info("获取到产品code：{}，设备参数集合：{}", sysDeviceProductMap.getProductCode(), params);
                            }
                        } else if ("DeviceSetting".equals(capability.path("service_id").asText())
                                && !checkHasExist(productServiceSet, sysDeviceProductMap.getProductId() + "DeviceSetting")) {
                            JsonNode properties = capability.path("properties");

                            InjectableValues.Std std = new InjectableValues.Std();
                            std.addValue("serviceId", "DeviceSetting");
                            std.addValue("productId", sysDeviceProductMap.getProductId());
                            objectMapper.setInjectableValues(std);
                            try {
                                productParamCategoriesList.addAll(objectMapper.readValue(properties.toString(), new TypeReference<List<SysProductService>>() {
                                }));
                            } catch (JsonProcessingException e) {
                                e.printStackTrace();
                            }

                        } else {
                            SysProductService productService = new SysProductService();
                            productService.setProductId(sysDeviceProductMap.getProductId());
                            productService.setServiceId(capability.path("service_id").asText());
                            productParamCategoriesList.add(productService);
                        }
                    };
                } catch (JsonProcessingException e) {
                    log.info("当前产品无服务，产品code：{}，项目ID：{}", sysDeviceProductMap.getProductCode(), ProjectContextHolder.getProjectId());
                    e.printStackTrace();
                }
            });
            if (CollUtil.isNotEmpty(productParamCategoriesList)) {
                // 先删除当前同步产品的原serviceId映射关系（如果是IOT设备需要加入paramId进行辅助判断，不删除已有的）
                if (CollUtil.isNotEmpty(productIdList)) {
                    List<SysProductService> productServiceList = sysProductServiceService.list(new LambdaQueryWrapper<SysProductService>()
                            .in(SysProductService::getProductId, productIdList)
                            .isNotNull(SysProductService::getParamId));
                    productParamCategoriesList.removeAll(productServiceList);
                }
                sysProductServiceService.saveBatch(productParamCategoriesList);
            }

        }
    }

    private boolean checkHasExist(Set<String> set, String value) {
        boolean contains = set.contains(value);
        if (!contains) {
            set.add(value);
        }
        return contains;
    }

    @Override
    public List<SysDeviceProductMapVo> getProductList() {
        return this.baseMapper.getProductList();
    }

    /**
     * 根据productCode获取产品信息
     *
     * @param productCode
     * @return
     */
    @Override
    public SysDeviceProductMap getByProductCode(String productCode) {
        List<SysDeviceProductMap> productMapList = this.baseMapper.selectList(new QueryWrapper<SysDeviceProductMap>().lambda().eq(SysDeviceProductMap::getProductCode, productCode));
        if (CollUtil.isNotEmpty(productMapList)) {
            return productMapList.get(0);
        }
        return null;
    }
}
