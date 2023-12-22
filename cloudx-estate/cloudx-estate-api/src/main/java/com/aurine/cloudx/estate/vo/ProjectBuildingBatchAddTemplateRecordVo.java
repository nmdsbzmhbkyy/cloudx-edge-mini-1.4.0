

package com.aurine.cloudx.estate.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 楼栋模板显示列表vo
 *
 * @author 王伟
 * @date 2020-06-04 15:36:20
 */
@Data
@ApiModel(value = "楼栋模板")
public class ProjectBuildingBatchAddTemplateRecordVo {
    private static final long serialVersionUID = 1L;

    /**
     * 楼栋模板id
     */
    @ApiModelProperty(value = "楼栋模板id")
    private String buildingTemplateId;
    /**
     * 模板名称
     */
    @ApiModelProperty(value = "模板名称")
    private String templateName;
    /**
     * 详细信息
     */
    @ApiModelProperty(value = "详细信息")
    private String info;
    /**
     * 每栋楼层数
     */
    @ApiModelProperty(value = "每栋楼层数")
    private Integer floorCount;
    /**
     *
     */
    @ApiModelProperty(value = "")
    private String startFloor;
    /**
     *
     */
    @ApiModelProperty(value = "")
    private String endFloor;
    /**
     *
     */
    @ApiModelProperty(value = "")
    private String exceptFloor;

    @ApiModelProperty(value = "命名策略")
    private String namePolicy;

    /**
     * 每楼层单元数
     */
    @ApiModelProperty(value = "每楼层单元数")
    private Integer unitCount;
    /**
     * 省编码
     */
    @ApiModelProperty(value = "省编码")
    private String provinceCode;
    /**
     * 市编码
     */
    @ApiModelProperty(value = "市编码")
    private String cityCode;
    /**
     * 县(区)编码
     */
    @ApiModelProperty(value = "县(区)编码")
    private String countyCode;
    /**
     * 街道编码
     */
    @ApiModelProperty(value = "街道编码")
    private String streetCode;
    /**
     * 详细地址
     */
    @ApiModelProperty(value = "详细地址")
    private String address;
    /**
     * 楼栋面积
     */
    @ApiModelProperty(value = "楼栋面积")
    private BigDecimal buildingArea;
    /**
     * 楼栋年代,YYYY
     */
    @ApiModelProperty(value = "楼栋年代,YYYY")
    private String buildingEra;
    /**
     * 建筑类别
     */
    @ApiModelProperty(value = "建筑类别")
    private String buildingType;

    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
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


    /**
     * 单元模板id，uuid
     */
    @ApiModelProperty(value = "单元模板id，uuid")
    private String unitTemplateId;
    /**
     * 模板中楼栋的单元号 ，如 1 2 3 或者A B C
     */
    @ApiModelProperty(value = "模板中楼栋的单元号 ，如 1 2 3 或者A B C")
    private String unitNo;
    /**
     * 所在单元号的房间数
     */
    @ApiModelProperty(value = "所在单元号的房间数")
    private Integer houseCount;

    @ApiModelProperty(value = "单元类型")
    private String unitNameType;

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

}
