package com.aurine.cloudx.open.origin.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * <p>
 *  设备产品和设备类型映射表对象
 * </p>
 * @author : 王良俊
 * @date : 2021-07-22 18:29:30
 */
@Data
@TableName("sys_device_type_model")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "设备产品和设备类型映射表")
@NoArgsConstructor
public class SysDeviceTypeModel extends Model<SysDeviceTypeModel> {

    private static final long serialVersionUID = 107713526202970557L;

    public SysDeviceTypeModel(String deviceTypeId, String productId, Integer tenantId, Integer operator) {
        this.deviceTypeId = deviceTypeId;
        this.productId = productId;
        this.tenantId = tenantId;
        this.operator = operator;
    }

    /**
     * 序列，自增
     */
    @ApiModelProperty(value = "序列，自增")
    private Integer seq;

    /**
     * 设备类型ID
     */
    @ApiModelProperty(value = "设备类型ID")
    private String deviceTypeId;

    /**
     * 产品ID
     */
    @ApiModelProperty(value = "产品ID")
    private String productId;

    /**
     * 租户ID
     */
    @ApiModelProperty(value="租户ID")
    @TableField("tenant_id")
    private Integer tenantId;

    /**
     * 操作者ID
     */
    @ApiModelProperty(value="操作者ID")
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
