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


import com.aurine.cloudx.edge.sync.common.entity.po.UuidRelation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 *
 *
 * @author pigx code generator
 * @date 2022-01-05 16:09:50
 */
@Mapper
public interface UuidRelationMapper extends BaseMapper<UuidRelation> {

    int updateByMarks(@Param("uuidRelation") UuidRelation uuidRelation);

    int deleteByMarks(@Param("uuidRelation") UuidRelation uuidRelation);
}
