

package com.aurine.cloudx.estate.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 停车区域
 *
 * @author 王伟
 * @date 2020-05-07 09:13:25
 */
@Data
@TableName("project_parking_info")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "停车场")
public class ProjectParkingInfo extends Model<ProjectParkingInfo> {
    private static final long serialVersionUID = 1L;

    /**
     * 停车场ID
     */
    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "停车场ID")
    private String parkId;
    /**
     * 停车场编码，可用于第三方编码
     */
    @ApiModelProperty(value = "停车场编码，可用于第三方编码")
    private String parkCode;
    /**
     * 停车场名称
     */
    @NotEmpty
    @ApiModelProperty(value = "停车场名称")
    private String parkName;
    /**
     * 停车位总数
     */
    @NotEmpty
    @ApiModelProperty(value = "停车位总数")
    private Integer parkNum;
    /**
     * 备注说明
     */
    @NotEmpty
    @ApiModelProperty(value = "备注说明")
    private String note;
    /**
     * 备注说明
     */
    @NotEmpty
    @ApiModelProperty(value = "使用状态")
    private String usageStatus;
    /**
     * 停车场类型
     */
    @NotEmpty
    @ApiModelProperty(value = "停车场类型")
    private String parkType;
    /**
     * 管理单位
     */
    @ApiModelProperty(value = "管理单位")
    private String orgUnit;
    /**
     * 单位名称
     */
    @ApiModelProperty(value = "单位名称")
    private String unitName;
    /**
     * 单位名称
     */
    @ApiModelProperty(value = "联系电话")
    private String contactPhone;
    /**
     * 经度
     */
    @NotEmpty
    @ApiModelProperty(value = "经度")
    private Double lon;
    /**
     * 纬度
     */
    @NotEmpty
    @ApiModelProperty(value = "纬度")
    private Double lat;
    /**
     * 高度
     */
    @NotEmpty
    @ApiModelProperty(value = "高度")
    private Double alt;
    /**
     * 坐标
     */
    @NotEmpty
    @ApiModelProperty(value = "坐标")
    private String gisArea;
    /**
     * 坐标系代码
     */
    @ApiModelProperty(value = "坐标系代码")
    private String gisType;

    /**
     * 操作人
     */
    @NotEmpty
    @ApiModelProperty(value = "操作人")
    @TableField(fill = FieldFill.INSERT)
    private Integer operator;
    /**
     * 操作时间
     */
    @ApiModelProperty(value = "操作时间")
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    /**
     * 对接状态 1 已连接 0 未连接
     */
    @ApiModelProperty(value = "对接状态 1 已连接 0 未连接")
    private char status;

    /**
     * 是否在线 1 在线 0 离线
     */
    @ApiModelProperty(value = "是否在线 1 在线 0 离线")
    private char isOnline;

    /**
     * 楼层
     */
    @ApiModelProperty(value = "楼层")
    private String floor;

    /**
     * 临时车收费规则id，关联project_park_billing_rule
     */
    @ApiModelProperty(value = "临时车收费规则id，关联project_park_billing_rule")
    private String ruleId;

    /**
     * 对接第三方厂商
     */
    @ApiModelProperty(value = "对接第三方厂商")
    private String company;
    /**
     * 项目id
     */
    @ApiModelProperty("项目id")
    private Integer projectId;

    /**
     *
     */
    @ApiModelProperty(value = "", hidden = true)
    @TableField(value = "tenant_id")
    private Integer tenantId;
}
