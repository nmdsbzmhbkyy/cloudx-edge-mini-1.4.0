package com.aurine.cloudx.estate.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 巡检任务明细检查项列表(ProjectInspectDetailCheckItem)表实体类
 *
 * @author 王良俊
 * @since 2020-07-28 17:06:11
 */
@Data
@TableName("project_inspect_detail_check_item")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "巡检任务明细检查项列表(ProjectInspectDetailCheckItem)")
public class ProjectInspectDetailCheckItem extends Model<ProjectInspectDetailCheckItem> {

    private static final long serialVersionUID = -14673268278621393L;


    /**
     * 检查项明细id
     */
    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "检查项明细id")
    private String itemDetailId;

    /**
     * 明细设备id
     */
    @ApiModelProperty(value = "明细设备id")
    private String deviceDetailId;

    /**
     * 检查项名称
     */
    @ApiModelProperty(value = "检查项名称")
    private String checkItemName;

    /**
     * 异常说明
     */
    @ApiModelProperty(value = "异常说明")
    private String content;

    /**
     * 完成状态
     */
    @ApiModelProperty(value = "完成状态")
    private String status;


    /**
     * 检查内容
     */
    @ApiModelProperty(value = "检查内容")
    private String remark;

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