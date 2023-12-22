package com.aurine.cloudx.open.common.data.service;

import com.aurine.cloudx.open.common.data.mapper.CommonMapper;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 自定义基础Service继承IService
 *
 * @author : Qiu
 * @date : 2022 04 21 14:39
 */
public interface CommonService<M extends CommonMapper<T>, T> extends IService<T> {

    Integer BATCH_SIZE = 100;

    /**
     * 插入数据。
     * 如果中已经存在相同的记录，则忽略当前新数据
     *
     * @param entity 实体类
     * @return 影响条数
     */
    int insertIgnore(T entity);

    /**
     * 批量插入数据。
     * 如果中已经存在相同的记录，则忽略当前新数据
     *
     * @param entityList 实体类列表
     * @return 影响条数
     */
    int insertIgnoreBatch(List<T> entityList);

    /**
     * 批量插入数据。
     * 如果中已经存在相同的记录，则忽略当前新数据
     *
     * @param entityList 实体类列表
     * @param batchSize  批量个数
     * @return 影响条数
     */
    int insertIgnoreBatch(List<T> entityList, int batchSize);

    /**
     * 替换数据
     * replace into表示插入替换数据，需要表中有PrimaryKey，或者unique索引，如果数据库已经存在数据，则用新数据替换，如果没有数据效果则和insert into一样；
     *
     * @param entity 实体类
     * @return 影响条数
     */
    int replace(T entity);
}
