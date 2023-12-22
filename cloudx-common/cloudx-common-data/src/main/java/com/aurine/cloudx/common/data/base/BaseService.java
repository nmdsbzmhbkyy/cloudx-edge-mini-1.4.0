package com.aurine.cloudx.common.data.base;

import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * @ClassName BaseService
 * @Description TODO
 * @USER cyw
 * @Date 2023年06月03日 0003
 **/
public interface BaseService<T,R extends PageParam> {
    /**
     * 分页查询
     * @param param
     * @return
     */
    IPage<T> queryPage(R param);

    void saveValid(T t);

    void updateValid(T t);

    void deleteValid(T t);
}
