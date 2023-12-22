package com.aurine.cloudx.estate.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;


/**
 * 卡片归属人vo
 *
 * @author 邹宇
 * @date 2021-12-9 13:52:36
 */
@Data
public class ProjectCardVo {

    /**
     * 姓名
     */
    @ApiModelProperty(value = "姓名")
    private String name;


    /**
     * 手机号
     */
    @ApiModelProperty(value = "手机号")
    private String phone;

    /**
     *  住址
     */
    @ApiModelProperty(value = "住址")
    private List<String> address;

    /**
     * 卡号
     */
    @ApiModelProperty(value = "卡号")
    private String cardNo;

    /**
     * 人员类型 1 住户 2 员工 3 访客
     */
    @ApiModelProperty(value = "人员类型 1 住户 2 员工 3 访客")
    private String personType;

    /**
     * 可通行设备
     */
    @ApiModelProperty(value = "可通行设备")
    private String device;

    /**
     * 卡状态 1 正常  2挂失
     */
    @ApiModelProperty(value = "卡状态 1 正常  2挂失")
    private String cardStatus;

    /**
     * 房屋id
     */
    @ApiModelProperty(value = "房屋id")
    private String houseId;

}
