

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

/**
 * 人员标签
 *
 * @author 王伟
 * @date 2020-05-13 10:42:38
 */
@Data
@TableName("project_person_label")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "人员标签")
public class ProjectPersonLabel extends Model<ProjectPersonLabel> {
private static final long serialVersionUID = 1L;

    /**
     * 
     */
    @TableId
    @ApiModelProperty(value="")
    private Integer seq;
    /**
     * 人员ID
     */
    @ApiModelProperty(value="人员ID")
    private String personId;

    /**
     * 证件类型
     */
    @ApiModelProperty(value="证件类型 111: 居民身份证 414  普通护照 554 外国人居留证 990 其他  字典credential_type")
    private String credentialType;
    /**
     * 证件号码
     */
    @ApiModelProperty(value="证件号码")
    private String credentialNo;

    /**
     * 人员特征标签id
     */
    @ApiModelProperty(value="人员特征标签id")
    private String labelId;
    /**
     * 可以为设备编号/设定者账号所属
     */
    @ApiModelProperty(value="可以为设备编号/设定者账号所属")
    private String infoFrom;
    /**
     * 操作人
     */
    @ApiModelProperty(value="操作人")
    @TableField(fill = FieldFill.INSERT)
    private Integer operator;
    /**
     * 信息等级
     */
    @ApiModelProperty(value="信息等级")
    private String infoLv;
    }
