

package com.aurine.cloudx.open.origin.vo;

import com.aurine.cloudx.open.origin.entity.ProjectHouseInfo;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 房屋VO
 *
 * @author 王伟
 * @date 2020-05-08 18:29:18
 */
@Data
public class ProjectHouseInfoVo extends ProjectHouseInfo {
    private static final long serialVersionUID = 1L;


    /**
     * 房屋编号
     */
    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "房屋编号")
    private String houseId;
    /**
     * 房屋编码，可传入第三方编码
     */
    @ApiModelProperty(value = "房屋编码，可传入第三方编码")
    private String houseCode;
    /**
     * 是否启用，用于批量添加
     */
    @ApiModelProperty(value = "是否启用，用于批量添加")
    private boolean enable;

    /**
     * 楼栋编号
     */
    @ApiModelProperty(value = "楼栋编号")
    private String buildingId;

    /**
     * 楼栋名称
     */
    @ApiModelProperty(value = "楼栋名称")
    private String buildingName;

    /**
     * 单元编号
     */
    @ApiModelProperty(value = "单元编号")
    private String unitId;

    /**
     * 单元名称
     */
    @ApiModelProperty(value = "单元名称")
    private String unitName;
    /**
     * 楼层
     */
    @ApiModelProperty(value = "楼层")
    private Integer floor;

    /**
     * 是否被使用
     */
    @ApiModelProperty(value = "是否被使用")
    private Integer isUse;

    /**
     * 是否被使用
     */
    @ApiModelProperty(value = "是否被使用")
    private String isUseText;

    /**
     * 单元编号
     */
    @ApiModelProperty(value = "单元编号")
    private String buildingUnit;

    /**
     * 朝向
     */
    @ApiModelProperty(value = "朝向")
    private String orientation;
    /**
     * 户型ID
     */
    @ApiModelProperty(value = "户型ID")
    private String houseDesginId;

    /**
     * 户型名称+面积
     */
    @ApiModelProperty(value = "户型名称与面积")
    private String houseDesginName;
    /**
     * 房屋名称
     */
    @ApiModelProperty(value = "房屋名称")
    private String houseName;
    /**
     * 房屋类别编码
     */
    @ApiModelProperty(value = "房屋类别编码")
    private String houseLabelCode;
    /**
     * 房屋类别
     */
    @ApiModelProperty(value = "房屋类别")
    private String houseLabel;
    /**
     * 房屋用途编码
     */
    @ApiModelProperty(value = "房屋用途编码")
    private String housePurposeCode;
    /**
     * 房屋用途
     */
    @ApiModelProperty(value = "房屋用途")
    private String housePurpose;
    /**
     * 房屋面积
     */
    @ApiModelProperty(value = "房屋面积")
    private BigDecimal houseArea;
    /**
     * 最大居住/办公人数
     */
    @ApiModelProperty(value = "最大居住/办公人数")
    private Integer personNumber;
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String note;
    /**
     * 经度
     */
    @ApiModelProperty(value = "经度")
    private BigDecimal lon;
    /**
     * 纬度
     */
    @ApiModelProperty(value = "纬度")
    private BigDecimal lat;
    /**
     * 高度
     */
    @ApiModelProperty(value = "高度")
    private BigDecimal alt;
    /**
     * 坐标
     */
    @ApiModelProperty(value = "坐标")
    private String gisArea;
    /**
     * 坐标系代码
     */
    @ApiModelProperty(value = "坐标系代码")
    private String gisType;
    /**
     * 1 自用 2 出租 3 闲置
     */
    @ApiModelProperty(value = "1 自用 2 出租 3 闲置")
    private String usageType;
    /**
     * 房屋产权性质
     */
    @ApiModelProperty(value = "房屋产权性质")
    private String propertyType;
    /**
     * 地址编码
     */
    @ApiModelProperty(value = "地址编码")
    private String locationCode;
    /**
     * 地址名称
     */
    @ApiModelProperty(value = "地址名称")
    private String locationName;

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

    public boolean getEnable() {
        return this.enable;
    }
    /**
     * 项目ID
     */
    @ApiModelProperty(value = "项目ID")
    private Integer projectId;

    /**
     * 租户号
     */
    private Integer tenantId;

}

