package com.aurine.cloudx.estate.thirdparty.remote.edge.ali.entity.checker;

import com.alibaba.fastjson.JSONObject;

/**
 * @description: DTO类型校验器 基类
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2021-06-22
 * @Copyright:
 */
public abstract class AbstractChecker {

    /**
     * 校验
     * @param jsonObject
     * @return
     */
    public abstract boolean check(JSONObject jsonObject);

}
