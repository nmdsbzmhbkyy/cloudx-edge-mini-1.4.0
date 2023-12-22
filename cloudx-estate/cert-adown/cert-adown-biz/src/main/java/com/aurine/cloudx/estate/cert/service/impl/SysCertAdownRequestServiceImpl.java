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
package com.aurine.cloudx.estate.cert.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.aurine.cloudx.estate.cert.config.CertAdownConfig;
import com.aurine.cloudx.estate.cert.entity.SysCertAdownRequest;
import com.aurine.cloudx.estate.cert.util.SqlCacheUtil;
import com.aurine.cloudx.estate.cert.mapper.SysCertAdownRequestMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aurine.cloudx.estate.cert.service.SysCertAdownRequestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 下载任务请求服务
 *
 * @author wangwei
 * @date 2021-12-15 13:45:44
 */
@Service
@Slf4j
public class SysCertAdownRequestServiceImpl extends ServiceImpl<SysCertAdownRequestMapper, SysCertAdownRequest> implements SysCertAdownRequestService {

	@Override
	public boolean updateById(SysCertAdownRequest entity) {
		//缓存操作
		long count = SqlCacheUtil.inToUpdateCache(entity);

		//检查缓存数量，缓存到一定的数量进行批量入库
		if (count >= CertAdownConfig.getSqlCacheSize()) {
			List<SysCertAdownRequest> list = SqlCacheUtil.updateList();

			if (CollUtil.isNotEmpty(list)) {
				log.debug("[清空SQL缓存] SQL超出阈值，执行批量更新操作");
				return super.updateBatchById(list);
			}
		}

		return true;
	}

	@Override
	public boolean save(SysCertAdownRequest entity) {
		//缓存操作
		long count = SqlCacheUtil.inToInsertCache(entity);

		//检查缓存数量，缓存到一定的数量进行批量入库
		if (count >= CertAdownConfig.getSqlCacheSize()) {
			List<SysCertAdownRequest> list = SqlCacheUtil.insertList();

			if (CollUtil.isNotEmpty(list)) {
				log.info("[清空SQL缓存] SQL超出阈值，执行入库操作");
				return super.saveBatch(list);
			}
		}

		return true;
	}

	@Override
	public boolean saveCertBatch(List<SysCertAdownRequest>  entityList) {
		//缓存操作
		long count = SqlCacheUtil.inToInsertCache(entityList);

		//检查缓存数量，缓存到一定的数量进行批量入库
		if (count >= CertAdownConfig.getSqlCacheSize()) {
			List<SysCertAdownRequest> list = SqlCacheUtil.insertList();

			if (CollUtil.isNotEmpty(list)) {
				log.info("[清空SQL缓存] SQL超出阈值，执行批量入库操作");
				return super.saveBatch(list);
			}
		}

		return true;
	}
}
