package com.aurine.cloudx.estate.vo;

import com.aurine.cloudx.estate.entity.ProjectUnitInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 单元含房屋统计信息
 * @author xull
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "单元含房屋统计信息")
public class ProjectUnitInfoCountVo extends ProjectUnitInfo {
    @ApiModelProperty("房屋统计数量")
    private Integer houseCount;
}
