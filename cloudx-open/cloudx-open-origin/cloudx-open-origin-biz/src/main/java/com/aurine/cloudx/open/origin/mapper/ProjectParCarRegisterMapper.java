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

import com.aurine.cloudx.open.origin.dto.ProjectParCarRegisterDto;
import com.aurine.cloudx.open.origin.entity.ProjectParCarRegister;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 车辆登记
 *
 * @author 王伟
 * @date 2020-07-08 14:10:32
 */
@Mapper
public interface ProjectParCarRegisterMapper extends BaseMapper<ProjectParCarRegister> {
    Page<ProjectParCarRegisterDto> pageCarRegister(Page page, @Param("query") ProjectParCarRegisterDto query, @Param("projectId") Integer projectId);

//    List<ParCarRegisterExportExcel> pageCarRegisterExport(@Param("query") ProjectParCarRegisterDto query, @Param("projectId") Integer projectId);

    int getMultiCarNum();

    Page<ProjectParCarRegisterDto> getParkIssuePage(Page page, @Param("query") ProjectParCarRegisterDto query, @Param("projectId") Integer projectId);

    Page<ProjectParCarRegisterDto> getPlateNumberStatusPage(Page page, @Param("query") ProjectParCarRegisterDto query, @Param("projectId") Integer projectId);

    Integer getPlateNumberStatusCount();

    Page<ProjectParCarRegisterDto> getPlateNumberStatusOnePage(Page page, @Param("query") ProjectParCarRegisterDto toDto, @Param("projectId") Integer projectId);

    ProjectParCarRegister getCarByPlateNumber(@Param("plateNumber") String plateNumber);

    boolean updateByEntity(@Param("query") ProjectParCarRegister carByPlateNumber);
}
