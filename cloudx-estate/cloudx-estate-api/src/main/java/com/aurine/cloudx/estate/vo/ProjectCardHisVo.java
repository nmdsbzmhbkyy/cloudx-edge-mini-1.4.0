package com.aurine.cloudx.estate.vo;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 卡操作记录Vo
 *
 * @author zy
 * @date 2022-10-18 08:40:49
 */
@Data
public class ProjectCardHisVo extends Model<ProjectCardHisVo> {
    private static final long serialVersionUID = 1L;

    /**
     * 操作类型 1 下发 2 挂失 3 解挂 4 注销
     */
    @ApiModelProperty(value = "操作类型 1 下发 2 挂失 3 解挂 4 注销")
    private String operationType;

    /**
     * 开始时间
     */
    @ApiModelProperty(value = "开始时间")
    private LocalDateTime operationTimeBegin;

    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间")
    private LocalDateTime operationTimeEnd;

    /**
     * 操作人
     */
    @ApiModelProperty(value = "操作人")
    private String operationName;

    /**
     * 操作时间
     */
    @ApiModelProperty(value = "操作时间")
    private LocalDateTime operationTime;

    /**
     * 卡号
     */
    @ApiModelProperty(value = "卡号")
    private String cardNo;

    /**
     * 旧卡号
     */
    @ApiModelProperty(value = "旧卡号")
    private String oldCardNo;

    /**
     * 姓名
     */
    @ApiModelProperty(value = "姓名")
    private String personName;


    /**
     * 人员类型 1 住户 2 员工 3 访客
     */
    @ApiModelProperty(value = "人员类型 1 住户 2 员工 3 访客")
    private String personType;

    /*
     * 手机号
     */
    @ApiModelProperty(value = "手机号")
    private String phone;

    /*
     * 状态 0成功 1进行中 2失败
     */
    @ApiModelProperty(value = "状态 0成功 1进行中 2失败")
    private String state;


    /*
     * 人员id
     */
    @ApiModelProperty(value = "人员id")
    private String personId;
}
