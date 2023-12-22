

package com.aurine.cloudx.estate.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * <p>通行方案列表结果VO</p>
 * @ClassName: ProjectPassPlanSearchCondition
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020/5/20 15:17
 * @Copyright:
 */
@Data
@ApiModel(value = "通行方案")
public class ProjectPassPlanRecodeVo {
private static final long serialVersionUID = 1L;


    /**
     * 方案id,uuid, 一个方案内可配置多个设备
     */
    @ApiModelProperty(value="方案id,uuid, 一个方案内可配置多个设备")
    private String planId;
    /**
     * 方案名称
     */
    @ApiModelProperty(value="方案名称")
    private String planName;
    /**
     * 方案对象 1 住户 2 员工 3 访客
     */
    @ApiModelProperty(value="方案对象 1 住户 2 员工 3 访客")
    private String planObject;

    /**
     * 是否默认 1 是 0 否
     */
    @ApiModelProperty(value = "是否默认 1 是 0 否")
    private String isDefault;

    /**
     * 楼栋id
     */
    @ApiModelProperty(value = "楼栋id")
    private String buildingId;
    /**
     * 单元id
     */
    @ApiModelProperty(value = "单元id")
    private String unitId;



    /**
     * 操作人
     */
    @ApiModelProperty(value="操作人")
    private Integer operator;
    /**
     * 创建时间
     */
    @ApiModelProperty(value="创建时间")
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    @ApiModelProperty(value="更新时间")
    private LocalDateTime updateTime;
    }
