package com.aurine.cloudx.wjy.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 楼栋映射实体
 * @author ： huangjj
 * @date ： 2021/4/14
 * @description： 我家云楼栋映射
 */
@TableName("wjy_building")
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "楼栋映射")
public class Building extends Model<Building> {
    /**
     * 自增序列
     */
    @TableId
    @ApiModelProperty(value = "序列，自增")
    private Integer seq;
    /**
     * 4.0楼栋id
     */
    @ApiModelProperty(value = "楼栋id")
    private String buildingId;
    /**
     * 我家云楼栋id
     */
    @ApiModelProperty(value = "我家云楼栋id")
    private String wjyBuildingId;
    /**
     * 项目id
     */
    @ApiModelProperty(value = "项目id")
    private Integer projectId;
    /**
     * 楼栋名称
     */
    @ApiModelProperty(value = "楼栋名称")
    private String buildingName;
    /**
     * 生成时间
     */
    @ApiModelProperty(value = "生成时间")
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;
}