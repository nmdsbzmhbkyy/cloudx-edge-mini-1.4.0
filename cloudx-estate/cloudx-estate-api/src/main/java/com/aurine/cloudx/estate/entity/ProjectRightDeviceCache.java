package com.aurine.cloudx.estate.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 凭证信息缓存
 * @author:zy
 * @data:2023/4/26 9:22 上午
 */
@Data
@TableName("project_right_device_cache")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "凭证信息缓存")
public class ProjectRightDeviceCache extends Model<ProjectRightDeviceCache> {
    private static final long serialVersionUID = 1L;

    /**
     * 主键，自增
     */
    @ApiModelProperty(value = "主键，自增")
    private Integer seq;

    /**
     * uuid
     */
    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "uuid")
    private String uuid;

    /**
     * 2人脸 3卡片
     */
    @ApiModelProperty(value = "2人脸 3卡片")
    private String type;

    /**
     * 设备id
     */
    @ApiModelProperty(value = "设备id")
    private String deviceId;

    /**
     * 设备第三方编号
     */
    @ApiModelProperty(value = "设备第三方编号")
    private String deviceThirdCode;

    /**
     * 凭证编号
     */
    @ApiModelProperty(value = "凭证编号")
    private String passNo;

    /**
     * 凭证状态  PassRightCertDownloadStatusEnum
     */
    @ApiModelProperty(value = "凭证状态  PassRightCertDownloadStatusEnum")
    private String state;

    /**
     *
     */
    @ApiModelProperty(value = "", hidden = true)
    @TableField(value = "tenant_id")
    private Integer tenantId;

    /**
     * 操作人
     */
    @ApiModelProperty(value = "操作人")
    @TableField(fill = FieldFill.INSERT)
    private Integer operator;
}