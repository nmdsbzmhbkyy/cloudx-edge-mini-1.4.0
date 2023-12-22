package com.aurine.cloudx.estate.thirdparty.module.intercom.platform.trtc.entity.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @program: cloudx
 * @description: 腾讯用户信息DTP
 * @author: 谢泽毅
 * @create: 2021-08-12 08:36
 **/
@Data
public class TencentUserDTO {
    /**
     * 应用id
     */
    private String appId;

    /**
     * 是否可用，1可用，0禁用
     */
    private Integer enabled;

    /**
     * 项目id
     */
    private String projectId;

    /**
     * 用户注册id
     */
    private String registerId;

    /**
     * 用户类型
     */
    private Integer userType;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * iot用户id
     */
    private String iotUserId;

    /**
     * 用户头像
     */
    private String userImage;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 用户昵称
     */
    private String userNick;

    private String rtcType;

}
