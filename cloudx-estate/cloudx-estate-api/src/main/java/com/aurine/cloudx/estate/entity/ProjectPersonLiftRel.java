

package com.aurine.cloudx.estate.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 人员设备权限
 *
 * @author pigx code generator
 * @date 2020-05-22 08:16:11
 */
@Data
@TableName("project_person_lift_rel")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "人员电梯设备权限")
public class ProjectPersonLiftRel extends Model<ProjectPersonLiftRel> {
private static final long serialVersionUID = 1L;

    /**
     * 关系id, 32位uuid
     */
    @ApiModelProperty(value = "关系id, 32位uuid")
    @TableId(type = IdType.ASSIGN_UUID)
    private String relId;

    /**
     * 项目id
     */
    @ApiModelProperty(value = "项目id")
    private Integer projectId;

    /**
     * 人员类型 1 住户 2 员工 3 访客
     */
    @ApiModelProperty(value="人员类型 1 住户 2 员工 3 访客")
    private String personType;

    /**
     * 人员id，根据人员类型取相应表id，注意访客应取当前的来访id
     */
    @ApiModelProperty(value="人员id，根据人员类型取相应表id，注意访客应取当前的来访id")
    private String personId;

    /**
     * 设备id
     */
    @ApiModelProperty(value="设备id")
    private String deviceId;

    /**
     * 是否有效
     */
    @ApiModelProperty(value="状态  是否启用 1 启用 0 禁用")
    private String isActive;

    /**
     * 楼栋id
     */
    @ApiModelProperty(value="楼栋id")
    private String buildingId;

    /**
     * 楼层集合
     */
    @ApiModelProperty(value="楼层集合，jsonarray")
    private String floors;

    /**
     * 状态 1 正常 2 失效
     */
    @ApiModelProperty(value="状态 1 正常 2 失效")
    private String status;
    /**
     * 生效时间
     */
    @ApiModelProperty(value="生效时间")
    private LocalDateTime effTime;
    /**
     * 失效时间
     */
    @ApiModelProperty(value="失效时间")
    private LocalDateTime expTime;

    /**
     * 操作人
     */
    @ApiModelProperty(value="操作人")
    @TableField(fill = FieldFill.INSERT)
    private Integer operator;
    /**
     * 操作时间
     */
    @ApiModelProperty(value="操作时间")
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    @ApiModelProperty(value="更新时间")
    private LocalDateTime updateTime;
    }
