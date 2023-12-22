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

package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.entity.ProjectDict;
import com.aurine.cloudx.estate.vo.ProjectDictVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.common.core.util.R;


import java.util.List;

/**
 * 项目字典表
 *
 * @author cjw
 * @date 2021-07-07 08:38:37
 */
public interface ProjectDictService extends IService<ProjectDict> {
    /**
     * 分页查询费用类型雷彪
     * @param page
     * @param projectDictVo
     * @return
     */
    IPage<ProjectDictVo> getFeeTypeList(Page page, ProjectDictVo projectDictVo);

    List<ProjectDictVo> getFeeTypeList( ProjectDictVo projectDictVo);


    /**
     * 获取费用类型下拉框
     */
    List<ProjectDictVo> feeTypeActiveList();

    /**
     * 新增费用类型
     * @param projectDictVo
     * @return
     */
    R insertFeeType (ProjectDictVo projectDictVo);

}
