package com.aurine.cloudx.open.origin.vo;

import com.aurine.cloudx.open.origin.entity.ProjectEmployerInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Title: ProjectEmployerInfoFromVo
 * Description: 实有单位信息表单Vo
 *
 * @author guhl@aurine.cn
 * @version 1.0.0
 * @date 2020/8/25 15:50
 */
@Data
@ApiModel(value = "实有单位表单对象")
public class ProjectEmployerInfoFromVo extends ProjectEmployerInfo {
    @ApiModelProperty("房屋id")
    private String houseId;
    @ApiModelProperty("旧的实有单位id")
    private String OldEmployerId;
    @ApiModelProperty("旧的房屋id")
    private String OldHouseId;
}
