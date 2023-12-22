package com.aurine.cloudx.wjy.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class Organization implements Serializable {
    /**
     *
     */
    private String id;

    /**
     * 长度64，名称
     */
    private String name;
    /**
     * 长度64，编码
     */
    private String phone;

    /**
     * 长度64，编码
     */
    private String number;
    /**
     * 长度64，简码
     */
    private String simpleName;
    /**
     * 长度11，是否叶子节点
     */
    private int isLeaf;
    /**
     * 长度11，级别
     */
    private int level;
    /**
     * 长度512，长编码
     */
    private String longNumber;
    /**
     * 长度11，是否公司(0区域 1公司 2部门 3备用公司)
     */
    private String isCompany;
    /**
     * 长度44，源ID
     */
    private String sourceID;
    /**
     * 长度32，来源系统
     */
    private String sourceSystem;
    /**
     * 长度44，父级ID
     */
    private String parentID;


    /**
     * 长度256，地址
     */
    private String address;





    /**
     * 长度32，联系人
     */
    private String linkName;
    /**
     * 长度32，联系人电话
     */
    private String linkPhone;
    /**
     * 长度32，国家
     */
    private String country;
    /**
     * 长度32，地区
     */
    private String area;
    /**
     * 长度50，纳税企业名称
     */
    private String billingECName;
    /**
     * 长度200，地址、电话
     */
    private String addrAndphone;
    /**
     * 长度20，纳税人识别号
     */
    private String taxPayerIDNumber;
    /**
     * 长度200，开户行及账号
     */
    private String bankNameAndAccount;
    /**
     * 长度11，应税税率类型(0一般纳税人 1 小规模税率),默认0
     */
    private int taxableType;
    /**
     * 长度32，开户行账号
     */
    private String bankAccount;
    /**
     * 长度32，社会信用代码
     */
    private String socialNumber;
    /**
     * 长度32，邮编
     */
    private String zipCode;
    /**
     * 长度32，纳税人联系电话
     */
    private String taxpayerPhone;
    /**
     * 长度1，地址类型，0国内地址，1自定义地址,默认0
     */
    private int addressType;
    /**
     * 长度255，备注
     */
    private String description;
}
