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

package com.aurine.cloudx.edge.sync.biz.mapper;


import com.aurine.cloudx.edge.sync.common.entity.po.TaskInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 *
 * @author pigx code generator
 * @date 2021-12-22 16:46:12
 */
@Mapper
public interface TaskInfoMapper extends BaseMapper<TaskInfo> {

    /**
     * 查询未发送的队列列表
     *
     * @return
     */
    List<String> getDispatchQueueList();

    /**
     * 查询未发送的事件队列列表
     *
     * @return
     */
    List<String>getEventDispatchQueueList();

    /**
     * 查询队列列表
     *
     * @return
     */
    List<String> getSendQueueList();

    /**
     * 查询事件类型队列列表
     *
     * @return
     */
    List<String> getEventSendQueueList();

    /**
     * 根据公共过滤标识查询数据
     *
     * @params query
     * @return
     */
    List<TaskInfo> getTaskInfoByMarks(@Param("query") TaskInfo query);

    /**
     * 根据uuid获取项目id
     *
     * @param projectUuid
     * @return
     */
    Integer getProjectIdByProjectUuid(@Param("projectUuid") String projectUuid);

}
