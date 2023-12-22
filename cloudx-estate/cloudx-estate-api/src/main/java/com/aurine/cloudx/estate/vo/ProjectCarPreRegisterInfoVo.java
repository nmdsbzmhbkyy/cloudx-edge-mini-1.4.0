package com.aurine.cloudx.estate.vo;

import lombok.Data;

/**
 * <p>
 * 预登记-审核通过-查看页面vo对象
 * </p>
 * @author : 王良俊
 * @date : 2021-03-10 14:30:25
 */
@Data
public class ProjectCarPreRegisterInfoVo {

    /**
     * 车牌号
     * */
    private String plateNumber;

    /**
     * 车主姓名
     * */
    private String personName;

    /**
     * 车主手机号
     * */
    private String telephone;

    /**
     * 车场名
     * */
    private String parkName;

    /**
     * 车位类型
     * */
    private String relType;


    /**
     * 收费方式
     * */
    private String ruleId;

    /**
     * 车位区域名
     * */
    private String parkRegionName;

    /**
     * 车位名
     * */
    private String placeName;

    /**
     * 有效期-开始
     * */
    private String startTime;

    /**
     * 有效期-结束
     * */
    private String endTime;

    /**
     * 对应收费规则的月租金额
     * */
    private String MonthlyRent;

    /**
     * 收费规则名
     * */
    private String ruleName;

    /**
     * 收费类型
     * */
    private String ruleType;


}
