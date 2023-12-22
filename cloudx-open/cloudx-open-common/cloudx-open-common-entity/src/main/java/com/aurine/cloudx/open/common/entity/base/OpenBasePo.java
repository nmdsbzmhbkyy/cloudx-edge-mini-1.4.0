package com.aurine.cloudx.open.common.entity.base;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * Open基础Po
 * 用于业务Po对象统一时使用，减少业务Po对象重复定义，业务Po对象需要继承该类
 * 注：Po对象是指对应数据库的实体对象
 * 注：业务Po对象是指除系统配置表外（一般是以sys开头的表）的其他Po对象
 *
 * @author : Qiu
 * @date : 2022 01 19 10:06
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "Open基础Po")
public class OpenBasePo<T extends OpenBasePo<?>> extends Model<T> {

    /**
     * 项目id
     */
    @ApiModelProperty(value = "项目id", hidden = true, position = 7)
    @TableField(value = "projectId")
    protected Integer projectId;

    /**
     * 租户id
     */
    @ApiModelProperty(value = "租户id", hidden = true, position = 7)
    @TableField(value = "tenant_id")
    protected Integer tenantId;

    /**
     * 操作人
     */
    @ApiModelProperty(value = "操作人", hidden = true, position = 8)
    @TableField(fill = FieldFill.INSERT)
    protected Integer operator;

    /**
     * 操作时间，东八区
     */
    @ApiModelProperty(value = "操作时间，东八区", hidden = true, position = 9)
    protected LocalDateTime createTime;

    /**
     * 更新时间，东八区
     */
    @ApiModelProperty(value = "更新时间，东八区", hidden = true, position = 9)
    protected LocalDateTime updateTime;
}
