package com.aurine.cloudx.open.common.data.service.impl;

import com.aurine.cloudx.open.common.data.mapper.CommonMapper;
import com.aurine.cloudx.open.common.data.service.CommonService;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * 自定义基础ServiceImpl继承ServiceImpl实现CommonService
 *
 * @author : Qiu
 * @date : 2022 04 21 14:41
 */
public class CommonServiceImpl<M extends CommonMapper<T>, T> extends ServiceImpl<M, T> implements CommonService<CommonMapper<T>, T> {

    @Override
    public int insertIgnore(T entity) {
        return baseMapper.insertIgnore(entity);
    }

    @Override
    public int insertIgnoreBatch(List<T> entityList) {
        return this.insertIgnoreBatch(entityList, BATCH_SIZE);
    }

    @Override
    public int insertIgnoreBatch(List<T> entityList, int batchSize) {
        if (CollectionUtils.isEmpty(entityList)) return 0;

        batchSize = batchSize < 1 ? BATCH_SIZE : batchSize;
        if (batchSize >= entityList.size()) {
            return baseMapper.insertIgnoreBatch(entityList);
        }

        int size = entityList.size();
        for (int fromIdx = 0, endIdx = batchSize; ; fromIdx += batchSize, endIdx += batchSize) {
            if (endIdx > size) endIdx = size;

            baseMapper.insertIgnoreBatch(entityList.subList(fromIdx, endIdx));
            if (endIdx == size) return size;
        }
    }

    @Override
    public int replace(T entity) {
        return baseMapper.replace(entity);
    }
}
