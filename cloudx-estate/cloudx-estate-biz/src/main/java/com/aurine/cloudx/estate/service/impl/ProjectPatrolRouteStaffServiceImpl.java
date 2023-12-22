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
import com.aurine.cloudx.estate.entity.ProjectPatrolRouteStaff;
import com.aurine.cloudx.estate.mapper.ProjectPatrolRouteStaffMapper;
import com.aurine.cloudx.estate.service.ProjectPatrolRouteStaffService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 巡更路线参与人列表
 *
 * @author wangwei
 * @date 2020-10-27 15:26:07
 */
@Service
public class ProjectPatrolRouteStaffServiceImpl extends ServiceImpl<ProjectPatrolRouteStaffMapper, ProjectPatrolRouteStaff> implements ProjectPatrolRouteStaffService {

    /**
     * 保存巡更线路可用员工
     * 每次保存先清空旧数据
     *
     * @param staffsIdList
     * @param patrolRouteId
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean savePatrolStaffs(List<String> staffsIdList, String patrolRouteId) {
        List<ProjectPatrolRouteStaff> patrolRouteStaffList = new ArrayList<>();
        ProjectPatrolRouteStaff patrolRouteStaff;

        if (CollUtil.isNotEmpty(staffsIdList)) {
            //生成关系对象列表
            for (String staffId : staffsIdList) {
                patrolRouteStaff = new ProjectPatrolRouteStaff();
                patrolRouteStaff.setStaffId(staffId);
                patrolRouteStaff.setPatrolRouteId(patrolRouteId);
                patrolRouteStaff.setStaffOffset("0");//只要有变化，offset清空处理
                patrolRouteStaffList.add(patrolRouteStaff);
            }

            //清空旧关系，存储新关系
            this.remove(new QueryWrapper<ProjectPatrolRouteStaff>().lambda().eq(ProjectPatrolRouteStaff::getPatrolRouteId, patrolRouteId));
            return this.saveBatch(patrolRouteStaffList);
        } else {
            return true;
        }

    }

    /**
     * 根据巡更路线，获取巡更人员id列表
     *
     * @param patrolRouteId
     * @return
     */
    @Override
    public List<String> getPatrolStaffsIdList(String patrolRouteId) {
        List<ProjectPatrolRouteStaff> patrolRouteStaffList = this.list(new QueryWrapper<ProjectPatrolRouteStaff>().lambda().eq(ProjectPatrolRouteStaff::getPatrolRouteId, patrolRouteId));
        List<String> staffIdList = new ArrayList<>();

        if (CollUtil.isNotEmpty(patrolRouteStaffList)) {
            staffIdList = patrolRouteStaffList.stream().map(e -> e.getStaffId()).collect(Collectors.toList());
        }
        return staffIdList;
    }

    /**
     * 获取下一组可用参与人员id
     *
     * @param patrolRouteId
     * @param count
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<String> getNextStaffsIdList(String patrolRouteId, int count) {

        //可以使用的参与人序列
        List<ProjectPatrolRouteStaff> patrolRouteStaffList = this.list(new QueryWrapper<ProjectPatrolRouteStaff>().lambda().eq(ProjectPatrolRouteStaff::getPatrolRouteId, patrolRouteId));
        List<String> staffsIdlist = new ArrayList<>();
        boolean reachOffset = false;//是否到达偏移量位置

        int offsetIndex = 0;//偏移量所在的index
        int queueListSize = 0;//队列列表的总量
        ProjectPatrolRouteStaff offsetRouteStaff = null;//当前偏移量所在对象
        ProjectPatrolRouteStaff lastRouteStaff = null;//最后取出的对象

        //数据不存在，或无需获取，直接返回空列表
        if (count <= 0){
            return staffsIdlist;
        }
        if (CollUtil.isEmpty(patrolRouteStaffList)){
            return staffsIdlist;
        }

        queueListSize = patrolRouteStaffList.size();



        //偏移量index计算，获取到最后一次使用员工的下一位索引
        List<ProjectPatrolRouteStaff> offsetList = patrolRouteStaffList.stream().filter(m -> "1".equals(m.getStaffOffset())).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(offsetList) || offsetList.size() == 0) { //未曾获取过数据，下一个index为首个对象
            offsetIndex = 0;
        } else {
            offsetRouteStaff = offsetList.get(0);
            offsetIndex = patrolRouteStaffList.indexOf(offsetRouteStaff);
            if (offsetIndex == queueListSize - 1) {//偏移量位于队末，则下一个index应该为队首
                offsetIndex = 0;
            } else {
                offsetIndex++;
            }
        }

        /**
         * 从上一次偏移量开始，最多获取一组到目标个数的顺序列表
         */
        for (int i = 0; i < 2; i++) {//二次循环以覆盖整个列表长度
            for (int j = 0; j < patrolRouteStaffList.size(); j++) {
                if (count <= 0){
                    break;
                }

                if (!reachOffset && j >= offsetIndex){
                    reachOffset = true;//到达偏移量位置，开始读出数据
                }

                if (!reachOffset){
                    continue;
                }

                if (i >= 1 && j >= offsetIndex){
                    break;//第二次循环，index应该小于偏移量位置，一次最多只能获取一组完整数据
                }

                lastRouteStaff = patrolRouteStaffList.get(j);
                staffsIdlist.add(lastRouteStaff.getStaffId());
                count--;
            }
            if (count <= 0){
                break;
            }
        }


        //将最后获取的数据作为新的偏移量记录
        if (offsetRouteStaff != null) {
            offsetRouteStaff.setStaffOffset("0");
            this.updateById(offsetRouteStaff);
        }
        if (lastRouteStaff != null) {
            lastRouteStaff.setStaffOffset("1");
            this.updateById(lastRouteStaff);
        }

        return staffsIdlist;
    }
}
