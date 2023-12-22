

package com.aurine.cloudx.open.origin.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 系统增值服务配置
 *
 * @author guhl@aurine.cn
 * @date 2020-06-04 11:16:57
 */
@Data
@TableName("sys_service_cfg")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "系统增值服务配置")
public class SysServiceCfg extends Model<SysServiceCfg> {
private static final long serialVersionUID = 1L;

    /**
     * 服务id
     */
    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value="服务id")
    private String serviceId;
    /**
     * 服务编码
     */
    @ApiModelProperty(value="服务编码")
    private String serviceCode;
    /**
     * 服务名称
     */
    @ApiModelProperty(value="服务名称")
    private String serviceName;
    /**
     * 服务分类 1 开门服务 2  智能家居服务 3 常用工具服务
     */
    @ApiModelProperty(value="服务分类 1 开门服务 2  智能家居服务 3 常用工具服务")
    private String serviceCategory;
    /**
     * 服务类型 1 社区服务 2 私人服务
     */
    @ApiModelProperty(value="服务类型 1 社区服务 2 私人服务")
    private String serviceType;
    /**
     * 服务描述
     */
    @ApiModelProperty(value="服务描述")
    private String serviceDesc;
    /**
     * 是否增值服务
     */
    @ApiModelProperty(value="是否增值服务 0否 1是")
    private String isValueadded;
    /**
     * 是否启用
     */
    @ApiModelProperty(value="是否启用 0否 1是")
    private String isActive;
    /**
     * 服务厂商
     */
    @ApiModelProperty(value="服务厂商")
    private String manufacturer;
    /**
     * 创建人
     */
    @ApiModelProperty(value="创建人")
    @TableField(fill = FieldFill.INSERT)
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
