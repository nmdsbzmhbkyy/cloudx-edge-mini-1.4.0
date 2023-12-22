package com.aurine.cloudx.estate.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@TableName("project_blacklist_attr")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "黑名单属性",description = "")
public class ProjectBlacklistAttr extends Model<ProjectBlacklistAttr> {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;
    /** 人脸库ID */
    @ApiModelProperty(name = "人脸库ID",notes = "")
    private String faceId ;
    /** 第三方人脸ID */
    @ApiModelProperty(name = "第三方人脸ID",notes = "")
    private String thirdFaceId ;
    /** 联系人员手机号 */
    @ApiModelProperty(name = "联系人员手机号",notes = "")
    private String mobile ;
    /** 联系人员名称 */
    @ApiModelProperty(name = "联系人员名称",notes = "")
    private String name ;
    /** 联系人员身份证 */
    @ApiModelProperty(name = "联系人员身份证",notes = "")
    private String credentialNo ;
    @TableLogic
    @ApiModelProperty(value = "是否删除")
    private Integer isDeleted = 0;
    /**
     * 操作人
     */
    @ApiModelProperty(value = "操作人")
    @TableField(fill = FieldFill.INSERT)
    private Integer operator;

    /**
     * 项目id
     */
    @ApiModelProperty(value = "项目id")
    private Integer projectId;
    /**
     * 租户id
     */
    @ApiModelProperty(value = "租户id")
    private Integer tenant_id;
    /**
     * 操作时间
     */
    @ApiModelProperty(value = "操作时间")
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;
}
