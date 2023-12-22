package com.aurine.cloudx.open.origin.vo;

import lombok.Data;

/**
 * 车辆登记记录表
 *
 */
@Data
public class ProjectCarPreRegisterVo {


    /**
     * 预登记ID
     * */
    private String preRegId;

    /**
     * 人员ID
     * */
    private String personId;

    /**
     * 车牌号
     * */
    private String plateNumber;

    /**
     * 车主姓名
     * */
    private String personName;

    /**
     * 手机号
     * */
    private String telephone;

    /**
     * 所属房屋信息（使用‘,’分隔）
     * */
    private String address;

    /**
     * 审核状态
     * */
    private String auditStatus;

    /**
     * 提交时间
     * */
    private String commitTime;

    /**
     * 审核人
     * */
    private String auditor;

    /**
     * 审核意见
     */
    private String auditRemark;

}
