package com.aurine.cloudx.estate.vo;

import lombok.Data;

/**
 * <p>
 * 车辆预登记页面查询类
 * </p>
 * @author : 王良俊
 * @date : 2021-03-08 11:20:17
 */
@Data
public class CarPreRegisterSearchCondition {

    /**
     * 车牌号
     * */
    private String plateNumber;

    /**
     * 车主姓名
     * */
    private String personName;

    /**
     * 审核状态
     * */
    private String auditStatus;

}
