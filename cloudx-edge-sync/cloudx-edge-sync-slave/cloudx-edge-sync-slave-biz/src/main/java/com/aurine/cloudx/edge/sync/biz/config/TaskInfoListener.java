/*
 *
 *      Copyright (c) 2018-2025, lengleng All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 *  this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  Neither the name of the pig4cloud.com developer nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 *  Author: lengleng (wangiegie@gmail.com)
 *
 */

package com.aurine.cloudx.edge.sync.biz.config;

import com.aurine.cloudx.edge.sync.biz.service.DispatchService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author cjw
 *
 */
@Slf4j
@AllArgsConstructor
@Component
public class TaskInfoListener {

	@Resource
	private DispatchService dispatchService;
	@Async
	@Order
	@EventListener(TaskInfoEvent.class)
	public void saveSysLog(TaskInfoEvent event) {
		//重新调度
		dispatchService.dispatchQueue(event.getTaskInfo().getServiceType(), event.getTaskInfo().getProjectUUID());
	}

}
