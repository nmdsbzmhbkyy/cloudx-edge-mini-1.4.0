package com.aurine.cloudx.open.origin.constant.enums;

import lombok.AllArgsConstructor;

/**
 * <p>默认标签名称</p>
 *
 * @ClassName: DefaultLabelEnum
 * @author: 黄阳光 <huangyg@aurine.cn>
 * @date: 2020/9/7 14:05
 * @Copyright:
 */
@AllArgsConstructor
public enum DefaultDeptEnum {
    /**
     * 办公室
     */
    OFFICE("办公室"),
    /**
     * 财务部
     */
    FINANCE("财务部"),
    /**
     * 综合管理部
     */
    COMPREHENSIVE("综合管理部"),
    /**
     * 保安部
     */
    SECURITY_STAFF("保安部"),
    /**
     * 维修部
     */
    REPAIR("维修部"),
    /**
     * 环境管理部
     */
    ENVIRONMENT("环境管理部");


    public String code;
}
