package com.aurine.cloudx.common.core.util.canal.handle;

import com.alibaba.otter.canal.protocol.CanalEntry.Entry;

/**
 * Canal事件处理链
 *
 * @description:
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2021-03-23
 * @Copyright:
 */
public abstract class AbstractCanalHandleChain {
    /**
     * 处理链处理的Canal事件对象
     */
    protected Entry canalEntry;

    /**
     * 是否继续运行处理链
     */
    protected Boolean nextFlag = true;


    /**
     * 执行处理链
     *
     * @param canalEntry 触发事件
     */
    public abstract AbstractCanalHandleChain doChain(Entry canalEntry);

    /**
     * 执行处理
     *
     * @param handler 处理器
     * @return
     */
    public abstract AbstractCanalHandleChain doHandle(AbstractCanalHandler handler);

    /**
     * 完成处理链
     *
     * @return
     */
    public AbstractCanalHandleChain done() {
        this.nextFlag = true;
        return this;
    }


}
