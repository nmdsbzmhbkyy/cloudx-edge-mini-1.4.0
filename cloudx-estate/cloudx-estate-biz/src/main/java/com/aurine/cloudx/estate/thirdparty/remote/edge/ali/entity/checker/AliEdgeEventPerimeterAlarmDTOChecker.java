package com.aurine.cloudx.estate.thirdparty.remote.edge.ali.entity.checker;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;

/**
 * @description: DTO类型校验器
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2021-06-22
 * @Copyright:
 */
public class AliEdgeEventPerimeterAlarmDTOChecker extends AbstractChecker {
    /**
     * 校验是否为周界告警事件对象
     *
     * @param jsonObject
     * @return
     */
    @Override
    public boolean check(JSONObject jsonObject) {
        if (
                1 == 1
                        && StringUtils.isNotEmpty(jsonObject.getString("deviceType"))
                        && StringUtils.isNotEmpty(jsonObject.getString("identifier"))
                        && StringUtils.isNotEmpty(jsonObject.getString("iotId"))
                        && StringUtils.equals("channelAlarm", jsonObject.getString("identifier"))
        ) {
            return true;
        }


        return false;
    }
}
