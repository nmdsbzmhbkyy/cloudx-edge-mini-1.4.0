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


import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.open.origin.constant.enums.DecidedEnum;
import com.aurine.cloudx.open.origin.mapper.SysDecidedMapper;
import com.aurine.cloudx.open.origin.entity.SysDecided;
import com.aurine.cloudx.open.origin.service.SysDecidedService;
import com.aurine.cloudx.open.common.core.util.RedisUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.common.security.util.SecurityUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author pigx code generator
 * @date 2021-07-28 15:29:36
 */
@Service
@AllArgsConstructor
public class SysDecidedServiceImpl extends ServiceImpl<SysDecidedMapper, SysDecided> implements SysDecidedService {


    @Override
    public String subscription(SysDecided sysDecided) {

        List<SysDecided> sysDecidedList = this.list(new LambdaQueryWrapper<SysDecided>()
                .eq(SysDecided::getProjectid, ProjectContextHolder.getProjectId())
                .eq(SysDecided::getUserid, SecurityUtils.getUser().getId()));


        for (SysDecided decided : sysDecidedList) {
            if (decided.getAddr().equals(sysDecided.getAddr()) && decided.getType().equals(sysDecided.getType())) {
                return "该地址已订阅";
            }
        }
        boolean save = this.save(sysDecided);
        RedisUtils.del("subscribe:" + DecidedEnum.getEnum(sysDecided.getType()) + ":" + sysDecided.getProjectid());
        return save ? "成功订阅地址" : "地址订阅失败";
    }

    /**
     * 删除对应项目redis的数据
     *
     * @param projectId
     */
//    @Override
//    public void deleteByKey(Integer projectId) {
//        Class decidedKeyConstantClass = decidedKeyConstant.class;
//        Field[] fields = decidedKeyConstantClass.getDeclaredFields();
//        for (Field field : fields) {
//            try {

//                RedisUtil.hdel(field.get(field.getName()).toString(), projectId.toString());
////                System.out.println(field.getName() + ":" + field.get(field.getName()));
//            } catch (IllegalArgumentException | IllegalAccessException e) {
//                e.printStackTrace();
//            }
//        }
//    }

}
