

package com.aurine.cloudx.estate.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 项目密码库
 *
 * @author 王良俊
 * @date 2020-06-04 18:16:17
 */
@Data
@TableName("project_passwd")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "项目密码库")
public class ProjectPasswd extends Model<ProjectPasswd> {
    private static final long serialVersionUID = 1L;

    /**
     * 序列，自增
     */
    @ApiModelProperty(value = "序列，自增")
    private Integer seq;
    /**
     * uid
     */
    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "密码uid")
    private String passId;
    /**
     * 密码
     */
    @ApiModelProperty(value = "密码")
    private String passwd;
    /**
     * 密码第三方ID
     */
    @ApiModelProperty(value = "密码第三方ID")
    private String passCode;
    /**
     * 人员类型 1 住户 2 员工 3 访客
     */
    @ApiModelProperty(value = "人员类型 1 住户 2 员工 3 访客")
    private String personType;
    /**
     * 人员id，根据人员类型取对应表id。
     */
    @ApiModelProperty(value = "人员id，根据人员类型取对应表id。")
    private String personId;
    /**
     * 状态 1 正常 2 冻结
     */
    @ApiModelProperty(value = "状态 1 正常 2 冻结")
    private String status;
    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    @TableField(fill = FieldFill.INSERT)
    private Integer operator;
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
}
