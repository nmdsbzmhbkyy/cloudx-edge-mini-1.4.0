

package com.aurine.cloudx.estate.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * <p>项目框架层级配置</p>
 * @ClassName: BuildingFrameCfg
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020/5/7 11:14
 * @Copyright:
 */
@Data
@TableName("project_entity_level_cfg")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "配置项目区域层级")
public class ProjectEntityLevelCfg extends Model<ProjectEntityLevelCfg> {
private static final long serialVersionUID = 1L;

    /**
     * 序列
     */
    @TableId
    @ApiModelProperty(value="序列")
    private Integer seq;

    /**
     * 项目编码
     */
    @ApiModelProperty(value="项目编码")
    private Integer projectId;
    /**
     * 层级编号
     */
    @ApiModelProperty(value="层级编号")
    private Integer level;
    /**
     * 用于和第三方对接，编号位数
     */
    @ApiModelProperty(value="用于和第三方对接，编号位数")
    private Integer codeRule;
    /**
     * 层级描述
     */
    @ApiModelProperty(value="层级描述")
    private String levelDesc;
    /**
     * 是否启用
     */
    @ApiModelProperty(value="是否启用 0：启动，1关闭")
    private String isDisable;
    /**
     * 租户ID
     */
    @ApiModelProperty(value="租户ID")
    @TableField("tenant_id")
    private Integer tenantId;
    /**
     * 操作人
     */
    @ApiModelProperty(value="操作人")
    @TableField(fill = FieldFill.INSERT)
    private Integer operator;
    /**
     * 操作时间，东八区
     */
    @ApiModelProperty(value="操作时间，东八区")
    private LocalDateTime createTime;
    /**
     * 更新时间，东八区
     */
    @ApiModelProperty(value="更新时间，东八区")
    private LocalDateTime updateTime;
    }
