package com.aurine.cloudx.estate.entity;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 培训资料库(ProjectTrainingFileDb)表实体类
 *
 * @author makejava
 * @since 2021-01-12 10:25:48
 */
@Data
@TableName("project_training_file_db")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "培训资料库(ProjectTrainingFileDb)")
public class ProjectTrainingFileDb extends Model<ProjectTrainingFileDb> {

    private static final long serialVersionUID = -72982498832893517L;


    /**
     * 资料id，目录则为目录id
     */
    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "资料id，目录则为目录id")
    private String fileId;


    /**
     * 名称
     */
    @ApiModelProperty(value = "名称")
    private String fileName;


    /**
     * 资料地址
     */
    @ApiModelProperty(value = "资料地址")
    private String filePath;


    /**
     * 是否目录 1 是 0 否
     */
    @ApiModelProperty(value = "是否目录 1 是 0 否")
    private String isDir;


    /**
     * 资料类型 1 文本 9 其他。 目录为空。
     */
    @ApiModelProperty(value = "资料类型 1 文本 9 其他。 目录为空。")
    private String fileType;


    /**
     * 上级id
     */
    @ApiModelProperty(value = "上级id")
    private String parId;

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


    /**
     * 最后操作人
     */
    @ApiModelProperty(value = "最后操作人")
    @TableField(fill = FieldFill.INSERT)
    private Integer operator;




}