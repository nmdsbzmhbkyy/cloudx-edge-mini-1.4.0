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

import com.aurine.cloudx.estate.entity.ProjectInspectPointConf;
import com.aurine.cloudx.estate.vo.ProjectInspectPointConfSearchConditionVo;
import com.aurine.cloudx.estate.vo.ProjectInspectPointConfVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.context.request.RequestAttributes;

import java.util.List;

/**
 * 设备巡检点配置
 *
 * @author 王良俊
 * @date 2020-07-23 16:26:33
 */
public interface ProjectisWrService {


    void findSaveFace(String relaId, String personId, String picUrl, Integer projectId);

}
