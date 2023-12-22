package com.aurine.cloudx.open.origin.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * @author zouyu
 * @date 2021-9-28 09:57:58
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectDeviceCheckParamVo {

    /**
     * ipv4
     * */
    private boolean ipv4;

    /**
     * deviceCode
     * */
    private boolean deviceCode;

    /**
     * mac
     * */
    private boolean mac;



}
