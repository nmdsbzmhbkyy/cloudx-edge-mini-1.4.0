

package com.aurine.cloudx.estate.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 单元模板
 *
 * @author 王伟
 * @date 2020-06-04 15:36:50
 */
@Data
@TableName("project_house_batch_add_template")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "单元模板")
public class ProjectHouseBatchAddTemplate extends Model<ProjectHouseBatchAddTemplate> {
    private static final long serialVersionUID = 1L;


    /**
     * 单元模板id,关联上级单元模板id
     */
    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "单元模板id,关联上级单元模板id")
    private String unitTemplateId;
    /**
     * 房号
     */
    @ApiModelProperty(value = "房号")
    private String houseNo;
    /**
     * 户型
     */
    @ApiModelProperty(value = "户型")
    private String houseDesignId;


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
