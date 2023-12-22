package com.aurine.cloudx.open.origin.entity;

import com.aurine.cloudx.open.common.entity.base.OpenBasePo;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 黑名单(ProjectBlackList)管理表实体类
 *
 * @author guwh
 * @since 2022-06-09 14:43:48
 */
@Data
@TableName(value = "project_blacklist", schema = "aurine_parking")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "黑名单信息(ProjectBlackList)")
public class ProjectBlacklist extends OpenBasePo<ProjectBlacklist> {
    private static final long serialVersionUID = 1L;

    /**
     * 楼栋模板id
     */
    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "黑名单uuid")
    private String blackId;
    /**
     * 停车场ID
     */
    @ApiModelProperty(value = "停车场ID")
    private String parkId;
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
    @ApiModelProperty(value = "租户id", hidden = true)
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
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
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
