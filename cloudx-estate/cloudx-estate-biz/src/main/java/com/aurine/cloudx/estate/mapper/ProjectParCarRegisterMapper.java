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

import com.aurine.cloudx.estate.excel.parking.ParCarRegisterExportExcel;
import com.aurine.cloudx.estate.vo.ProjectParCarRegisterRecordVo;
import com.aurine.cloudx.estate.vo.ProjectParCarRegisterSeachConditionVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.aurine.cloudx.estate.entity.ProjectParCarRegister;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 车辆登记
 *
 * @author 王伟
 * @date 2020-07-08 14:10:32
 */
@Mapper
public interface ProjectParCarRegisterMapper extends BaseMapper<ProjectParCarRegister> {
    Page<ProjectParCarRegisterRecordVo> pageCarRegister(Page page, @Param("query") ProjectParCarRegisterSeachConditionVo query,  @Param("projectId") Integer projectId);
    List<ParCarRegisterExportExcel> pageCarRegisterExport(@Param("query") ProjectParCarRegisterSeachConditionVo query, @Param("projectId") Integer projectId);
    int getMultiCarNum();
}
