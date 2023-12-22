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


import com.aurine.cloudx.estate.entity.ProjectPerimeterAlarmRecord;
import com.aurine.cloudx.estate.vo.ProjectPerimeterAlarmRecordVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 周界报警布防/撤防记录表
 *
 * @author pigx code generator
 * @date 2022-07-04 16:10:18
 */
@Mapper
public interface ProjectPerimeterAlarmRecordMapper extends BaseMapper<ProjectPerimeterAlarmRecord> {

    Page<ProjectPerimeterAlarmRecordVo> findPage(Page page, @Param("query") ProjectPerimeterAlarmRecordVo projectPerimeterAlarmRecordVo);

}
