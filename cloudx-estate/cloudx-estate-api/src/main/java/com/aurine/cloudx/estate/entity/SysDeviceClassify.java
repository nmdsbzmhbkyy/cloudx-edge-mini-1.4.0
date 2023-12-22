

package com.aurine.cloudx.estate.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 增值业务设备类型配置
 *
 * @author guhl@aurine.cn
 * @date 2020-06-04 11:22:07
 */
@Data
@TableName("sys_device_classify")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "增值业务设备类型配置")
public class SysDeviceClassify extends Model<SysDeviceClassify> {
private static final long serialVersionUID = 1L;

    /**
     * 设施类型，uuid
     */
    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value="设施类型，uuid")
    private String deviceClassify;
    /**
     * 类型名称
     */
    @ApiModelProperty(value="类型名称")
    private String classifyName;
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
