package com.aurine.cloudx.open.origin.entity;

import com.aurine.cloudx.open.common.entity.base.OpenBasePo;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 疫情记录表
 *
 * @author 邹宇
 * @date 2021-6-7 11:08:18
 */

@Data
@TableName("project_epidemic_event")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "疫情记录表")
public class ProjectEpidemicEvent extends OpenBasePo<ProjectEpidemicEvent> {
    private static final long serialVersionUID = 1L;

    /**
     * 主键，自增
     */
    @TableId
    @ApiModelProperty(value = "主键，自增")
    private Integer seq;

    /**
     * 事件id
     */
    @ApiModelProperty(value = "事件id")
    private String eventId;
    /**
     * 姓名
     */
    @ApiModelProperty(value = "姓名")
    private String personName;
    /**
     * 手机号
     */
    @ApiModelProperty(value = "手机号")
    private String mobileNo;
    /**
     * 类型 1 住户 2 员工 3 访客
     */
    @ApiModelProperty(value = "类型 1 住户 2 员工 3 访客")
    private String personType;

    /**
     * 房屋地址
     */
    @ApiModelProperty(value = "房屋地址")
    private String houseName;
    /**
     * 时间
     */
    @ApiModelProperty(value = "时间")
    private Date eventTime;
    /**
     * 抓拍图片
     */
    @ApiModelProperty(value = "抓拍图片")
    private String snapshotPic;

    /**
     * 体温
     */
    @ApiModelProperty(value = "体温")
    private Double temperature;

    /**
     * 健康码状态 绿码、红码
     */
    @ApiModelProperty(value = "健康码状态 绿码、红码")
    private String codeStatus;
}
