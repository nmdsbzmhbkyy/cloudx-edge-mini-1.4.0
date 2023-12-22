package com.aurine.cloudx.estate.thirdparty.remote.edge.ali.entity.checker;

import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.common.core.util.StringUtil;
import org.apache.commons.lang3.StringUtils;

/**
 * @description: DTO类型校验器
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2021-06-22
 * @Copyright:
 */
public class AliEdgeEventParkingDTOChecker extends AbstractChecker {
    /**
     * 校验是否为停车场对象
     *
     * @param jsonObject
     * @return
     */
    @Override
    public boolean check(JSONObject jsonObject) {
        /**
         *
         */
        if (
                1 == 1
                        && StringUtils.isNotEmpty(jsonObject.getString("modelId"))
                        && StringUtils.isNotEmpty(jsonObject.getString("data")
                )
        ) {
            return true;
        }


        return false;
    }
}
