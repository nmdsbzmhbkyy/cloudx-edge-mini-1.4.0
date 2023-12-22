package com.aurine.cloudx.estate.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@TableName("sys_devicetype_modeltype_config")
@ApiModel("第三方产品设备类型关联表")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysDeviceTypeModelTypeConfig extends Model<SysDeviceTypeModelTypeConfig> {

    /*
     * 自增序列
     **/
    @ApiModelProperty("自增序列")
    private Integer seq;

    /*
     * 配置ID
     **/
    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty("配置ID")
    private String configUUid;

    /*
     * 第三方产品类别标识
     **/
    @ApiModelProperty("第三方产品类别标识")
    private String productModelType;

    /*
     * 平台名
     **/
    @ApiModelProperty("平台名")
    private String platformName;

    /*
     * 设备类型ID
     **/
    @ApiModelProperty("设备类型ID")
    private String deviceTypeId;

    /*
     * 备注信息
     **/
    @ApiModelProperty("备注信息")
    private String remark;

    /*
     * 租户ID（暂无用）
     **/
    @ApiModelProperty("租户ID")
    @TableField("tenant_id")
    private Integer tenantId;

    /*
     * 操作人员ID（暂无用）
     **/
    @ApiModelProperty("操作人员ID")
    private Integer operator;

    /*
     * 创建时间
     **/
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    /*
     * 更新时间
     **/
    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;

}
