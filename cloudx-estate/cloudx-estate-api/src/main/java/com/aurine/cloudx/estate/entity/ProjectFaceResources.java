

package com.aurine.cloudx.estate.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 项目人脸库，用于项目辖区内的人脸识别设备下载
 *
 * @author 王良俊
 * @date 2020-05-22 11:21:06
 */
@Data
@TableName("project_face_resources")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "项目人脸库，用于项目辖区内的人脸识别设备下载")
public class ProjectFaceResources extends Model<ProjectFaceResources> {
    private static final long serialVersionUID = 1L;

//    @ApiModelProperty(value = "主键，自增")
//    private Integer seq;
    /**
     * 人脸id，uuid
     */
    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "人脸id，uuid")
    private String faceId;
    /**
     * 人脸编号，第三方传入
     */
    @ApiModelProperty(value = "人脸编号，第三方传入")
    private String faceCode;
    /**
     * 人脸名称
     */
    @ApiModelProperty(value = "人脸名称")
    private String faceName;
    /**
     * 人员类型 1 住户 2 员工 3 访客
     */
    @ApiModelProperty(value = "人员类型 1 住户 2 员工 3 访客")
    private String personType;

    /**
     * 图片来源 1 web端 2 小程序 3 app 4 第三方传入
     */
    @ApiModelProperty(value = "图片来源 1 web端 2 小程序 3 app 4 第三方传入")
    private String origin;
    /**
     * 人员id, 根据人员类型取对应表id
     */
    @ApiModelProperty(value = "人员id, 根据人员类型取对应表id")
    private String personId;
    /**
     * 图片地址
     */
    @ApiModelProperty(value = "图片地址 base64")
    private String picUrl;
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
