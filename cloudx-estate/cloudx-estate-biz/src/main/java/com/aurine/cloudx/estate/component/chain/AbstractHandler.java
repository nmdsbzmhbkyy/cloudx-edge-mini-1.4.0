package com.aurine.cloudx.estate.component.chain;

import lombok.extern.slf4j.Slf4j;

/**
 * Canal事件处理器
 *
 * @description:
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2021-03-23
 * @Copyright:
 */
@Slf4j
public abstract class AbstractHandler<T> {


    /**
     * handler校验函数，不满足该函数则跳过处理
     * @param handleEntity
     * @return
     */
    public abstract boolean filter(T handleEntity);


    /**
     * 执行方法，执行后返回调用next继续下一组handle，调用done则结束处理
     *
     * @param handleEntity
     */
    public abstract boolean doHandle(T handleEntity);


    /**
     * 处理事件方法
     *
     * @param handleEntity 处理对象
     */
    public boolean handle(T handleEntity) {

        if (filter(handleEntity)) {
            return doHandle(handleEntity);
        } else {
            return next();
        }


    }

    /**
     * 执行完成
     *
     * @return
     */
    public boolean done() {
        return false;
    }

    /**
     * 不予执行,跳转到下一个执行方法
     *
     * @return
     */
    public boolean next() {
        return true;
    }


}
