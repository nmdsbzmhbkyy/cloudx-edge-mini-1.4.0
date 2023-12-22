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

import com.aurine.cloudx.estate.entity.ProjectPatrolRouteStaff;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 巡更路线参与人列表
 *
 * @author wangwei
 * @date 2020-10-27 15:26:07
 */
public interface ProjectPatrolRouteStaffService extends IService<ProjectPatrolRouteStaff> {

    /**
     * 保存巡更线路可用员工
     * 每次保存先清空旧数据
     *
     * @param staffsIdList
     * @param patrolRouteId
     * @return
     */
    public boolean savePatrolStaffs(List<String> staffsIdList, String patrolRouteId);

    /**
     * 根据巡更路线，获取可参与巡更员工id列表
     *
     * @param patrolRouteId
     * @return
     */
    public List<String> getPatrolStaffsIdList(String patrolRouteId);


    /**
     * 获取下一组可用参与人员id
     * @param patrolRouteId
     * @param count
     * @return
     */
    public List<String> getNextStaffsIdList(String patrolRouteId, int count);

}
