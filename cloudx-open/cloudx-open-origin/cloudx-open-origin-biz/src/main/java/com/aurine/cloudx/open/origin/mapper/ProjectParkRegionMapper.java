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

package com.aurine.cloudx.open.origin.mapper;


import com.aurine.cloudx.open.origin.dto.ProjectParkRegionDto;
import com.aurine.cloudx.open.origin.entity.ProjectParkRegion;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 车位区域表
 *
 * @author 王良俊
 * @date 2020-07-07 11:00:29
 */
@Mapper
public interface ProjectParkRegionMapper extends BaseMapper<ProjectParkRegion> {
    IPage<ProjectParkRegionDto> selectPage(IPage<ProjectParkRegionDto> page, @Param("query") ProjectParkRegionDto query);

    List<ProjectParkRegionDto> listByParkId(@Param("parkId") String parkId);
}
