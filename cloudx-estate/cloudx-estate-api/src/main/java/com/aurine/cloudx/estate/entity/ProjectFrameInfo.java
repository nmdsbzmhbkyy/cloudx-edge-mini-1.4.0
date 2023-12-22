
package com.aurine.cloudx.estate.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 框架
 *
 * @author 王伟
 * @date 2020-05-07 14:00:08
 */
@Data
@TableName("project_frame_info")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "框架")
public class ProjectFrameInfo extends Model<ProjectFrameInfo> {
    private static final long serialVersionUID = 1L;


    /**
     * uid，实体ID
     */

    @TableId(type = IdType.ASSIGN_UUID)
//    @TableId(type = IdType.INPUT)
    @ApiModelProperty(value = "uid，实体ID")
    private String entityId;

    /**
     * 实体编码
     */
    @ApiModelProperty(value = "实体编码")
    private String entityCode;

    /**
     * 框架编码
     */
    @ApiModelProperty(value = "框架编码")
    private String frameNo;
    
    /**
     * 实体名称
     */
    @ApiModelProperty(value = "实体名称")
    private String entityName;
    /**
     * 上级id
     */
    @ApiModelProperty(value = "上级id")
    private String puid;
    /**
     * 是否楼栋
     */
    @ApiModelProperty(value = "是否楼栋")
    private String isBuilding;
    /**
     * 是否单元
     */
    @ApiModelProperty(value = "是否单元")
    private String isUnit;
    /**
     * 是否房屋
     */
    @ApiModelProperty(value = "是否房屋")
    private String isHouse;
    /**
     * 层级
     */
    @ApiModelProperty(value = "层级")
    private Integer level;
    /**
     * 操作人
     */
    @ApiModelProperty(value = "操作人", hidden = true)
    @TableField(fill = FieldFill.INSERT)
    private Integer operator;
    /**
     * 操作时间，东八区
     */
    @ApiModelProperty(value = "操作时间，东八区", hidden = true)
    private LocalDateTime createTime;
    /**
     * 更新时间，东八区
     */
    @ApiModelProperty(value = "更新时间，东八区", hidden = true)
    private LocalDateTime updateTime;
}
