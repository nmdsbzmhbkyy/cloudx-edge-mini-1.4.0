

package com.aurine.cloudx.estate.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 记录项目辖区内允许通行的指纹信息，供辖区内已开放通行权限的指纹识别设备下载
 *
 * @author 王良俊
 * @date 2020-05-22 11:20:22
 */
@Data
@TableName("project_fingerprints")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "记录项目辖区内允许通行的指纹信息，供辖区内已开放通行权限的指纹识别设备下载")
public class ProjectFingerprints extends Model<ProjectFingerprints> {
    private static final long serialVersionUID = 1L;


    /**
     * 指纹id，uuid
     */
    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "指纹id，uuid")
    private String fingerprintId;
    /**
     * 指纹编码，第三方传入
     */
    @ApiModelProperty(value = "指纹编码，第三方传入")
    private String fingerprintCode;
    /**
     * 指纹名称
     */
    @ApiModelProperty(value = "指纹名称")
    private String fingerprintName;

    /**
     * 人员类型 1 住户 2 员工 3 访客
     */
    @ApiModelProperty(value = "人员类型 1 住户 2 员工 3 访客")
    private String personType;
    /**
     * 人员id, 根据人员类型取对应表id
     */
    @ApiModelProperty(value = "人员id, 根据人员类型取对应表id")
    private String personId;
    /**
     * 指纹图片地址
     */
    @ApiModelProperty(value = "指纹图片地址")
    private String fpUrl;
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
    private String operator;
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
