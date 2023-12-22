package com.aurine.cloudx.open.origin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 抓拍时间记录表
 */
@Data
@TableName("project_snap_record")
@ApiModel(value = "抓拍时间记录表")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectSnapRecord extends Model<ProjectSnapRecord> {
    private static final long serialVersionUID = 1L;

    /**
     * 序列
     */
    @ApiModelProperty(value = "序列，自增")
    private Integer seq;

    /**
     * 记录id，uuid
     */
    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "记录id，uuid")
    private String uuid;

    /**
     * 设备id
     */
    @ApiModelProperty(value = "设备id")
    private String deviceId;

    /**
     * 抓拍机编码
     */
    @ApiModelProperty(value = "抓拍机编码")
    private String snapNo;

    /**
     * 小区编码
     */
    @ApiModelProperty(value = "小区编码")
    private String areaNo;

    /**
     * 设备名称
     */
    @ApiModelProperty(value = "设备名称")
    private String deviceName;

    /**
     * 抓拍机编码
     */
    @ApiModelProperty(value = "设备区域名称")
    private String deviceRegionName;

    /**
     * 事件类型
     */
    @ApiModelProperty(value = "事件类型")
    private String eventType;

    /**
     * 事件子类型
     */
    @ApiModelProperty(value = "事件子类型")
    private String eventSubType;

    /**
     * 描述
     */
    @ApiModelProperty(value = "描述")
    private String description;

    /**
     * 来源
     */
    @ApiModelProperty(value = "来源")
    private String source;

    /**
     * 抓拍时间
     */
    @ApiModelProperty(value = "抓拍时间")
    private LocalDateTime snapTime;

    /**
     * 抓拍小图
     */
    @ApiModelProperty(value = "抓拍小图")
    private String snapSmalImage;

    /**
     * 抓拍大图
     */
    @ApiModelProperty(value = "抓拍大图")
    private String snapBigImage;

    /**
     * 设备ip
     */
    @ApiModelProperty(value = "设备ip")
    private String reference;

    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private String operator;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    /**
     * 项目id
     */
    @ApiModelProperty(value="项目id")
    private Integer projectId;

    /**
     * 租户ID
     */
    @ApiModelProperty(value="租户ID")
    @TableField("tenant_id")
    private Integer tenantId;
}
