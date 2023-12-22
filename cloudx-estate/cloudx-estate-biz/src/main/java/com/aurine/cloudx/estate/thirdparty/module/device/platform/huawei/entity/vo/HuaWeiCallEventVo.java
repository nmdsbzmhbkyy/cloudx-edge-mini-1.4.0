package com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.entity.vo;

import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.entity.vo.respond.ObjManagerData;
import lombok.Data;

/**
 * @author:zouyu
 * @data:2022/4/24 10:24 上午
 */
@Data
public class HuaWeiCallEventVo {
    /**
     * 发起人的id
     */
    private String sourceId;


    private String serviceId;

    /**
     * 具体对象数据
     */
    private ObjManagerData data;
}
