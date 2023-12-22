package com.aurine.cloudx.estate.vo;

import com.aurine.cloudx.estate.entity.ProjectMediaAdDevCfg;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Title: ProjectMediaDevCfgVo
 * Description:
 *
 * @author xull@aurine.cn
 * @version 1.0.0
 * @date 2020/6/5 9:58
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("项目媒体广告设备配置属性拓展Vo")
public class ProjectMediaAdDevCfgVo extends ProjectMediaAdDevCfg {
    @ApiModelProperty("设备名称")
    private String deviceName;
    @ApiModelProperty("楼栋Id")
    private String buildingId;
    @ApiModelProperty("楼栋名称")
    private String buildingName;
    @ApiModelProperty("单元id")
    private String unitId;
    @ApiModelProperty("单元名称")
    private String unitName;
}
