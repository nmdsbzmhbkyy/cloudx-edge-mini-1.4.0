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

package com.aurine.cloudx.estate.mapper;

import com.aurine.cloudx.estate.vo.ProjectPersonAttrListVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.aurine.cloudx.estate.entity.ProjectPersonAttr;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 人员属性拓展表
 *
 * @author xull@aurine.cn
 * @date 2020-07-03 16:24:30
 */
@Mapper
public interface ProjectPersonAttrMapper extends BaseMapper<ProjectPersonAttr> {
    List<ProjectPersonAttrListVo> getPersonAtrrListVo(@Param("projectId")Integer projectId, @Param("type")String type,@Param("personId") String personId);
}
