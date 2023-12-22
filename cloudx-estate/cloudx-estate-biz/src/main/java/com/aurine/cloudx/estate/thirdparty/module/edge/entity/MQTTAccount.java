package com.aurine.cloudx.estate.thirdparty.module.edge.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>级联或入云账号创建类</p>
 * @author : 王良俊
 * @date : 2021-12-13 16:10:52
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MQTTAccount {

    /**
     * 开关：级联：0，入云：1
     */
    private String cascadeSwitch;

    /**
     * 机器码
     */
    private String machineSN;

    /**
     * 社区ID
     */
    private String communityId;

    /**
     * 入云码
     */
    private String cascadeCode;

}
