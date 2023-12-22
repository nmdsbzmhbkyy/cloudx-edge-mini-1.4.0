

package com.aurine.cloudx.open.origin.entity;

import com.aurine.cloudx.open.common.entity.base.OpenBasePo;
import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

/**
 * 访客
 *
 * @author 王伟
 * @date 2020-06-03 19:42:52
 */
@Data
@TableName("project_visitor")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "访客")
public class ProjectVisitor extends OpenBasePo<ProjectVisitor> {
    private static final long serialVersionUID = 1L;


    /**
     * 访客id
     */
    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "访客id")
    private String visitorId;
    /**
     * 姓名
     */
//    @NotBlank(message = "未输入访客姓名")
    @ApiModelProperty(value = "姓名")
    private String personName;
    /**
     * 姓名
     */
    @ApiModelProperty(value = "性别")
    private String gender;
    /**
     * 证件类型
     */
    @ApiModelProperty(value = "证件类型")
    private String credentialType;
    /**
     * 证件号码
     */
    @ApiModelProperty(value = "证件号码")
    private String credentialNo;
    /**
     * 手机号
     */
//    @NotBlank(message = "未输入访客手机号")
    @ApiModelProperty(value = "手机号")
    private String mobileNo;
    /**
     * 照片
     */
    @ApiModelProperty(value = "照片")
    private String picUrl;
    /**
     * 证件照正面
     */
    @ApiModelProperty(value = "证件照正面")
    private String credentialPicFront;
    /**
     * 证件照背面
     */
    @ApiModelProperty(value = "证件照背面")
    private String credentialPicBack;

    /**
     * 用户id
     */
    @ApiModelProperty("用户Id")
    private Integer userId;
}
