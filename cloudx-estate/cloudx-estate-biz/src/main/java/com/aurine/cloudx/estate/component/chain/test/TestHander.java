package com.aurine.cloudx.estate.component.chain.test;

import com.aurine.cloudx.estate.component.chain.AbstractHandler;
import com.aurine.cloudx.estate.component.chain.annotation.ChainHandler;
import org.springframework.stereotype.Component;

/**
 * @description:
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2021/09/17
 * @Copyright:
 */
@ChainHandler(chainClass = TestChain.class)
//@Component
public class TestHander<JSONObject> extends AbstractHandler<JSONObject> {

    /**
     * handler条件过滤函数，不满足该函数则跳过校验
     *
     * @param handleEntity
     * @return
     */
    @Override
    public boolean filter(JSONObject handleEntity) {
        return false;
    }

    /**
     * 执行方法，执行后返回调用next继续下一组handle，调用done则结束处理
     *
     * @param handleEntity
     */
    @Override
    public boolean doHandle(JSONObject handleEntity) {
        return false;
    }

}
