package com.aurine.cloudx.estate.entity;

import java.time.LocalDateTime;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 项目信息发布对象配置表(ProjectNoticeObject)表实体类
 *
 * @author xull
 * @since 2021-02-07 17:15:31
 */
@Data
@TableName("project_notice_object")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "项目信息发布对象配置表(ProjectNoticeObject)")
public class ProjectNoticeObject extends Model<ProjectNoticeObject> {

    private static final long serialVersionUID = -69790891355341863L;



    /**
     * 唯一记录id
     */
    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "唯一记录id")
    private String uid;


    /**
     * 信息id
     */
    @ApiModelProperty(value = "信息id")
    private String noticeId;


    /**
     * 对象id，通常为houseId
     */
    @ApiModelProperty(value = "对象id，通常为houseId")
    private String objectId;


    /**
     * 项目id
     */
    @ApiModelProperty(value = "项目id")
    private Integer projectId;


    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
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
