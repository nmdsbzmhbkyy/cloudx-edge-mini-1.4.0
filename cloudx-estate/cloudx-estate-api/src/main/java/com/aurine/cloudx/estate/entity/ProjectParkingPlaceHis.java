

package com.aurine.cloudx.estate.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 车位变动记录
 *
 * @author 王伟
 * @date 2020-05-11 13:53:53
 */
@Data
@TableName("project_parking_place_his")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "车位变动记录")
public class ProjectParkingPlaceHis extends Model<ProjectParkingPlaceHis> {
private static final long serialVersionUID = 1L;

    /**
     * 
     */
    @TableId
    @ApiModelProperty(value="")
    private Integer seq;

    /**
     * 车位ID
     */
    @ApiModelProperty(value="车位ID")
    private String placeId;
    /**
     * 车位编号，如A-2201。可用于第三方对接
     */
    @ApiModelProperty(value="车位编号，如A-2201。可用于第三方对接")
    private String placeCode;
    /**
     * 车位号
     */
    @ApiModelProperty(value="车位号")
    private String placeName;
    /**
     * 归属停车场ID
     */
    @ApiModelProperty(value="归属停车场ID")
    private String parkId;
    /**
     * 归属人
     */
    @ApiModelProperty(value="归属人")
    private String personId;
    /**
     * 归属人姓名
     */
    @ApiModelProperty(value="归属人姓名")
    private String personName;
    /**
     * 关系类型 0 闲置 1 产权 2 租赁
     */
    @ApiModelProperty(value="关系类型 0 闲置 1 产权 2 租赁")
    private String relType;

    /**
     * 启用时间
     */
    @ApiModelProperty(value="启用时间")
    private LocalDateTime checkInTime;
    /**
     * 生效时间
     */
    @ApiModelProperty(value="生效时间")
    private LocalDateTime effTime;
    /**
     * 失效时间
     */
    @ApiModelProperty(value="失效时间")
    private LocalDateTime expTime;
    /**
     * 迁入迁出类型 1 迁入 0 迁出
     */
    @ApiModelProperty(value="迁入迁出类型 1 迁入 0 迁出")
    private String action;

    /**
     * 创建人
     */
    @ApiModelProperty(value="创建人")
    @TableField(fill = FieldFill.INSERT)
    private Integer operator;

    }
