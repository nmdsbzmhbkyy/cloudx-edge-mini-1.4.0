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

package com.aurine.cloudx.open.origin.service;


import com.aurine.cloudx.open.origin.entity.SysDecided;
import com.baomidou.mybatisplus.extension.service.IService;


/**
 * 
 *
 * @author pigx code generator
 * @date 2021-07-28 15:29:36
 */
public interface SysDecidedService extends IService<SysDecided> {

    /**
     * 订阅
     * @param sysDecided
     * @return
     */
    String subscription(SysDecided sysDecided);

//    void deleteByKey(Integer projectId);
}
