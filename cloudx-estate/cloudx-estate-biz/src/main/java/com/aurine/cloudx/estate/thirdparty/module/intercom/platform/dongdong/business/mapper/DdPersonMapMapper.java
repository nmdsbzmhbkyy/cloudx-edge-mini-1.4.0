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

package com.aurine.cloudx.estate.thirdparty.module.intercom.platform.dongdong.business.mapper;

import com.aurine.cloudx.estate.thirdparty.module.intercom.platform.dongdong.business.entity.DdPersonMap;
import com.baomidou.mybatisplus.annotation.SqlParser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 社区人员映射
 *
 * @author 王伟
 * @date 2020-11-18 17:16:19
 */
@Mapper
public interface DdPersonMapMapper extends BaseMapper<DdPersonMap> {

    /**
     * 根据框架查询当前框架下可以访问的住户列表
     * 参数均为空，则返回所有开启了云对讲服务的住户列表
     *
     * @param groupId
     * @param buildingId
     * @param unitId
     * @param useHouseName 是否使用房间号作为roomNo，梯口设备传true，其他为false
     * @return
     */
    @SqlParser(filter = true)
    List<DdPersonMap> listOriginByFrame(@Param("projectId") Integer projectId, @Param("groupId") String groupId, @Param("buildingId") String buildingId, @Param("unitId") String unitId, @Param("useHouseName") boolean useHouseName);

    /**
     * 根据房屋id和personid，获取可以使用的设备信息
     * 用于新增住户时，获取可插入的数据
     *
     * @param houseId
     * @param personId
     * @return
     */
    @SqlParser(filter = true)
    List<DdPersonMap> listOriginByHousePerson(@Param("houseId") String houseId, @Param("personId") String personId);
}
