package com.aurine.cloudx.open.origin.dto;

import com.aurine.cloudx.open.origin.entity.ProjectBlacklist;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;


/**
 * <p>
 *  设备参数服务DTO对象
 * </p>
 *
 * @author 王良俊
 * @since 2022/5/19 9:50
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectBlacklistDto extends Model<ProjectBlacklist> {
    private static final long serialVersionUID = 1L;

    /**
     * 黑名单uuid
     */
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

    /**
     * 开始时间,yyyy-MM-dd
     */
    @ApiModelProperty(value = "开始时间")
    private String startDate;
    /**
     * 结束时间,yyyy-MM-dd
     */
    @ApiModelProperty(value = "结束时间")
    private String endDate;

}
