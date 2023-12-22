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
 * 单元映射实体
 * @author ： huangjj
 * @date ： 2021/4/14
 * @description： 我家云单元映射
 */
@TableName("wjy_unit")
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "单元映射实体")
public class Unit extends Model<Unit> {
    /**
     * 自增序列
     */
    @TableId
    @ApiModelProperty(value = "自增序列")
    private Integer seq;
    /**
     * 4.0单元id
     */
    @ApiModelProperty(value = "4.0单元id")
    private String unitId;
    /**
     * 我家云单元id
     */
    @ApiModelProperty(value = "我家云单元id")
    private String wjyUnitId;
    /**
     * 项目id
     */
    @ApiModelProperty(value = "项目id")
    private Integer projectId;
    /**
     * 单元名称
     */
    @ApiModelProperty(value = "单元名称")
    private String unitName;
    /**
     * 楼栋id
     */
    @ApiModelProperty(value = "楼栋id")
    private String buildingId;
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