package com.aurine.cloudx.estate.thirdparty.module.edge.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>中台桥接配置信息对象</p>
 * @author : 王良俊
 * @date : 2021-12-22 09:27:30
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BridgingConfInfo {

    /**
     * 云端/级联(主)IP
     */
    private String address;

    /**
     * 入云码/级联码
     */
    private String cascadeCode;

    /**
     * 入云或是级联 级联 0 入云 1
     */
    private String cascadeSwitch;

    /**
     * 社区ID （UUID）
     */
    private String communityId;
}
