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

package com.aurine.cloudx.estate.cert.service;

import com.aurine.cloudx.estate.cert.entity.SysCertAdownRequest;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 下载任务请求服务
 *
 * @author wangwei
 * @date 2021-12-15 13:45:44
 */
public interface SysCertAdownRequestService extends IService<SysCertAdownRequest> {
	/**
	 * 批量保存凭证信息
	 *
	 * @param entityList
	 * @return
	 */
	boolean saveCertBatch(List<SysCertAdownRequest> entityList);
}
