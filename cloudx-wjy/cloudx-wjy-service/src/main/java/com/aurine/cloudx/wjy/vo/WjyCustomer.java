package com.aurine.cloudx.wjy.vo;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 客户实体，即指社区住户
 */
@Data
public class WjyCustomer implements Serializable {
    /**
     * 客户ID
     */
    private String id;

    /**
     * 客户ID，返回时用到
     */
    private String customerID;
    /**
     * 客户编码
     */
    private String number;
    /**
     * 客户名称
     */
    private String name;
    /**
     * 客户类型
     */
    private String type;
    /**
     * 电话
     */
    private String phone;
    /**
     * 电话
     */
    private String telephone;
    /**
     * 微信号
     */
    private String weixinNumber;
    /**
     * QQ号
     */
    private String qqNumber;
    /**
     * 地址
     */
    private String address;
    /**
     * 状态（0无效，1有效)
     */
    private String status;
    /**
     * 是否业主（0否，1是）
     */
    private String isOwner;
    /**
     * 用户ID
     */
    private String userID;
    /**
     * 项目ID
     */
    private String projectID;
    /**
     * 企业ID
     */
    private String ecID;
    /**
     * 证件类型
     */
    private String certificateType;
    /**
     * 证件名称
     */
    private String certificateName;
    /**
     * 客户头像
     */
    private String avatarUrl;

    /**
     * 生日（格式：yyyy-MM-dd）
     */
    private Date birthday;

    /**
     * 性别（0：男，1：女）
     */
    private String sex;

    /**
     * 区域
     */
    private String area;

}
