

package com.aurine.cloudx.estate.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 单元
 *
 * @author 王伟
 * @date 2020-06-10 11:10:40
 */
@Data
@TableName("project_unit_info")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "单元")
public class ProjectUnitInfo extends Model<ProjectUnitInfo> {
    private static final long serialVersionUID = 1L;


    /**
     * 单元ID
     */
    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "单元ID")
    private String unitId;

    /**
     * 序列
     */
    @ApiModelProperty(value = "序列")
    private Integer seq;
    /**
     * 单元编码
     */
    @ApiModelProperty(value = "单元编码")
    private String unitCode;

    /**
     * 框架编码
     */
    @ApiModelProperty(value = "框架编码")
    private String frameNo;

    /**
     * 标准地址串(应该就是前端的地址编码了)
     */
    @ApiModelProperty(value = "标准地址串")
    private String standardAddress;

    /**
     * 单元名称
     */
    @ApiModelProperty(value = "单元名称")
    private String unitName;
    /**
     * 地址名称
     */
    @ApiModelProperty(value = "地址名称")
    private String addressName;
    /**
     * 图片1
     */
    @ApiModelProperty(value = "图片1")
    private String picUrl1;
    /**
     * 图片2
     */
    @ApiModelProperty(value = "图片2")
    private String picUrl2;
    /**
     * 图片3
     */
    @ApiModelProperty(value = "图片3")
    private String picUrl3;
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;

    /**
     * 操作人
     */
    @ApiModelProperty(value = "操作人")
    private Integer operator;
    /**
     * 操作时间，东八区
     */
    @ApiModelProperty(value = "操作时间，东八区")
    private LocalDateTime createTime;
    /**
     * 更新时间，东八区
     */
    @ApiModelProperty(value = "更新时间，东八区")
    private LocalDateTime updateTime;
}
