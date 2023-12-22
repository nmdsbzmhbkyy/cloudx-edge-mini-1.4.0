package com.aurine.cloudx.open.origin.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;


/**
 * (ProjectBlackList)黑名单vo
 *
 * @author guwh
 * @since 2022-06-09 14:43:48
 */
@Data
@ApiModel(value = "黑名单vo")
public class ProjectBlacklistVo  {

    /**
     * 楼栋模板id
     */
    @ApiModelProperty(value = "黑名单uuid")
    private String blackId;
    /**
     * 停车场ID
     */
    @ApiModelProperty(value = "停车场ID")
    private String parkId;
    /**
     * 停车场名称
     */
    @ApiModelProperty(value = "停车场名称")
    private String parkName;
    /**
     * 车牌号
     */
    @ApiModelProperty(value = "车牌号")
    private String plateNumber;
    /**
     * 项目id
     */
    @ApiModelProperty(value = "项目id")
    private Integer projectId;
    /**
     * 租户id
     */
    @ApiModelProperty(value = "租户id",hidden = true)
    @TableField("tenant_id")
    private Integer tenantId;
    /**
     * 限行起始日期
     */
    @ApiModelProperty(value = "限行起始日期")
    private LocalDate limitStartDate;
    /**
     * 限行终止日期
     */
    @ApiModelProperty(value = "限行截止日期")
    private LocalDate limitEndDate;
    /**
     * 原因
     */
    @ApiModelProperty(value = "原因")
    private String reason;
    /**
     *创建人
     */
    @ApiModelProperty(value="创建人")
    private Integer operator;
    /**
     * 操作时间
     */
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;
}
