package com.aurine.cloudx.estate.component.chain.test;

import com.aurine.cloudx.estate.component.chain.annotation.Chain;
import com.aurine.cloudx.estate.component.chain.AbstractHandleChain;
import org.springframework.stereotype.Component;

/**
 * @description:
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2021/09/17
 * @Copyright:
 */
@Chain
//@Component
public class TestChain<JSONObject> extends AbstractHandleChain<JSONObject> {

    /**
     * 执行处理链
     *
     * @param handleEntity 处理对象
     */
    @Override
    public AbstractHandleChain doChain(JSONObject handleEntity) {
        return this
                .doHandle(new TestHander())
                .doHandle(new TestHander())
                .doHandle(new TestHander())
                .done();
    }
}
