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

import com.aurine.cloudx.estate.entity.ProjectInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aurine.cloudx.estate.thirdparty.module.intercom.platform.dongdong.business.entity.DdCommunityMap;

/**
 * 社区映射
 *
 * @author 王伟
 * @date 2020-11-18 17:13:14
 */
public interface DdCommunityMapService extends IService<DdCommunityMap> {

    /**
     * 检查项目是否已存在关联
     *
     * @param projectId
     * @return
     */
    boolean checkCommunityExist(int projectId);

    /**
     * 通过projectId获取配置对象
     * @param projectId
     * @return
     */
    DdCommunityMap getByProjectId(int projectId);


    /**
     * 添加社区
     *
     * @param projectInfo
     * @param villageId
     * @return
     */
    boolean addCommunity(ProjectInfo projectInfo, Integer villageId);

    /**
     * 删除社区，并清空和当前社区相关的所有数据
     * @param projectId
     * @return
     */
    boolean delCommunity(int projectId);
}
