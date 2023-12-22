

package com.aurine.cloudx.estate.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 增值服务关联设施
 *
 * @author guhl@aurine.cn
 * @date 2020-06-04 11:17:42
 */
@Data
@TableName("sys_service_device_classify")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "增值服务关联设施")
public class SysServiceDeviceClassify extends Model<SysServiceDeviceClassify> {
private static final long serialVersionUID = 1L;

    /**
     * 服务id
     */
    @ApiModelProperty(value="服务id")
    private String serviceId;
    /**
     * 关联设施类型
     */
    @ApiModelProperty(value="关联设施类型")
    private String deviceClassify;
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
