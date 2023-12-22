package com.aurine.cloudx.estate.component.chain;

import cn.hutool.core.collection.CollUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 处理链
 *
 * @description:
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2021/09/17 9:38
 * @Copyright:
 */
public abstract class AbstractHandleChain<T> {
    /**
     * 处理链处理的对象
     */
    protected T handleEntity;

    /**
     * 处理链中的Handler集合
     */
    protected List<AbstractHandler<T>> handlerList = new ArrayList<>();

    /**
     * 是否继续运行处理链
     */
    protected Boolean nextFlag = true;


    /**
     * 执行处理链
     *
     * @param handleEntity 处理对象
     */
    public AbstractHandleChain doChain(T handleEntity) {
        this.handleEntity = handleEntity;

        //循环执行处理器，直到处理完毕
        if (CollUtil.isNotEmpty(handlerList)) {
            for (AbstractHandler<T> handler : handlerList) {
                if (nextFlag) {
                    nextFlag = handler.handle(handleEntity);
                } else {
                    break;
                }
            }
        }

        if (nextFlag) {
            return this.done();//执行重写后的done()方法
        } else {
            this.nextFlag = true;//流程结束后初始化标记
            return this;
        }

    }


    /**
     * 执行处理
     *
     * @param handler 处理器
     * @return
     */
    public AbstractHandleChain doHandle(AbstractHandler handler) {
        if (nextFlag) {
            this.nextFlag = handler.handle(handleEntity);
        }
        return this;
    }

    /**
     * 完成处理链
     *
     * @return
     */
    public AbstractHandleChain done() {
        this.nextFlag = true;
        return this;
    }


    public void setHandlerList(List<AbstractHandler<T>> handlerList) {
        this.handlerList = handlerList;
    }

    /**
     * 为处理链添加执行器
     *
     * @param handler
     */
    public void addHandler(AbstractHandler<T> handler) {
        this.handlerList.add(handler);
    }


}
