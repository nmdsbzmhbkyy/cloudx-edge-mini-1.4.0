package com.aurine.cloudx.estate.vo;

import com.aurine.cloudx.estate.entity.ProjectEmployerInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Title: ProjectEmployerInfoPageVo
 * Description: 实有单位信息查询Vo
 *
 * @author guhl@aurine.cn
 * @version 1.0.0
 * @date 2020/8/25 16:50
 */
@Data
@ApiModel(value = "实有单位分页查询视图")
public class ProjectEmployerInfoPageVo extends ProjectEmployerInfo {
    @ApiModelProperty("楼栋名称")
    private String buildingName;
    @ApiModelProperty("单元名称")
    private String unitName;
    @ApiModelProperty("房屋名称")
    private String houseName;
    @ApiModelProperty("房屋id")
    private String houseId;
}
