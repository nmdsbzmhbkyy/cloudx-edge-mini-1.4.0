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

import com.aurine.cloudx.edge.sync.common.entity.po.ProjectRelation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author cjw
 * @date 2021-12-23 15:31:43
 */
@Mapper
public interface ProjectRelationMapper extends BaseMapper<ProjectRelation> {

    List<String> getUuidList();

    List<String> getCodeList();

    Integer deleteByProjectUUID(@Param("projectUuid")String projectUUID);

    Integer deleteByProjectCode(@Param("projectCode")String projectCode);
}
