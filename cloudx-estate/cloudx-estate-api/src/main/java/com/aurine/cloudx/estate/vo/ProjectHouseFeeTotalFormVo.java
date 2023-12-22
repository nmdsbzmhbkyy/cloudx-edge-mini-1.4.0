package com.aurine.cloudx.estate.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 房屋费用汇总查询视图
 *
 * @author xull@aurine
 * @date 2020-07-29 13:27:59
 */
@Data
@ApiModel("房屋费用汇总查询视图")
public class ProjectHouseFeeTotalFormVo {

    /**
     * 楼栋名称
     */
    @ApiModelProperty(value = "楼栋名称")
    private String buildingName;

    /**
     * 单元名称
     */
    @ApiModelProperty(value = "单元名称")
    private String unitName;

    /**
     * 房屋名称
     */
    @ApiModelProperty("房屋名称")
    private String houseName;


    /**
     * 是否设置收费
     */
    @ApiModelProperty("是否设置收费")
    private String haveSetFee;

    /**
     * 是否缴清
     */
    @ApiModelProperty("是否缴清")
    private String havePayUp;

    /**
     * 费用id
     */
    @ApiModelProperty("费用id")
    private List<String> feeIds;
}
