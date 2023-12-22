package com.aurine.cloudx.wjy.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ： huangjj
 * @date ： 2021/4/15
 * @description： 房间
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "房间-我家云标准接口信息")
public class RoomStandardVo {
    /**
     * 长度50，名称,必填
     */
    @ApiModelProperty(value = "长度50，名称")
    private String roomName;
    /**
     * 长度50，编码，必填
     */
    @ApiModelProperty(value = "长度50，编码")
    private String roomNumber;
    /**
     * 长度16，楼层，必填
     */
    @ApiModelProperty(value = "长度16，楼层")
    private String floor;
    /**
     * 长度32，楼栋名称，必填
     */
    @ApiModelProperty(value = "长度32，楼栋名称")
    private String buildingName;
    /**
     * 长度50，单元名称，必填
     */
    @ApiModelProperty(value = "长度50，单元名称")
    private String buildUnitName;
    /**
     * int度11，业务属性（0 住宅,1 商业,2 工业,3 车位,4 仓库,5 保障性用房,6 公寓,7 办公楼,8 厂房,9 商铺,10 土地,11 宿舍,12 其他 13车卡），必填
     */
    @ApiModelProperty(value = "int度11，业务属性（0 住宅,1 商业,2 工业,3 车位,4 仓库,5 保障性用房,6 公寓,7 办公楼,8 厂房,9 商铺,10 土地,11 宿舍,12 其他 13车卡）")
    private Integer property;
    /**
     * decimal(28,10)，建筑面积，必填
     */
    @ApiModelProperty(value = "decimal(28,10)，建筑面积")
    private Integer buildingArea;
    /**
     * decimal(28,10)，套内面积,且小于建筑面积，必填
     */
    @ApiModelProperty(value = "decimal(28,10)，套内面积")
    private Integer roomArea;
    /**
     * decimal(28,10),花园面积
     */
    @ApiModelProperty(value = "decimal(28,10),花园面积")
    private Integer gardenArea;
    /**
     * decimal(28,10),阁楼面积
     */
    @ApiModelProperty(value = "decimal(28,10),阁楼面积")
    private Integer loftArea;
    /**
     * 计费面积(㎡)（格式：decimal(28,10)）
     */
    @ApiModelProperty(value = "计费面积(㎡)（格式：decimal(28,10)）")
    private Integer chargeArea;
    /**
     * 长度32，产品类型名称，必填
     */
    @ApiModelProperty(value = "长度32，产品类型名称")
    private String productName;
    /**
     * 长度50，户型名称
     */
    @ApiModelProperty(value = "长度50，户型名称")
    private String modelName;
    /**
     * 长度50，房间属性，必填
     */
    @ApiModelProperty(value = "长度50，房间属性")
    private String attributeName;
    /**
     * 长度32，源ID，必填
     */
    @ApiModelProperty(value = "长度32，源ID")
    private String sourceID;
    /**
     * 长度32，来源系统，必填
     */
    @ApiModelProperty(value = "长度32，来源系统")
    private String sourceSystem;
    /**
     * 长度500，备注
     */
    @ApiModelProperty(value = "长度500，备注")
    private String description;


    /**
     * int，长度11，费用计算状态（0空置，1已售，2已售楼 3装修中 4 已入住，必填
     */
    @ApiModelProperty(value = "int，长度11，费用计算状态（0空置，1已售，2已售楼 3装修中 4 已入住，必填")
    private Integer feeStatus;
    /**
     * int，长度11，状态（0未入住，1自住，2出租），必填
     */
    @ApiModelProperty(value = "int，长度11，状态（0未入住，1自住，2出租）")
    private Integer status;
    /**
     * 长度128，产权状况
     */
    @ApiModelProperty(value = "长度128，产权状况")
    private String propertyRight;
    /**
     * 长度128，物业用途
     */
    @ApiModelProperty(value = "长度128，物业用途")
    private String purpose;
    /**
     * datetime，竣工日期1970-01-01
     */
    @ApiModelProperty(value = "datetime，竣工日期1970-01-01")
    private String completionDate;
    /**
     * 项目id
     */
    @ApiModelProperty(value = "项目id")
    private Integer projectId;

    /**
     * 单元id
     */
    @ApiModelProperty(value = "单元id")
    private String unitId;

}