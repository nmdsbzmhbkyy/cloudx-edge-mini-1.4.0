package com.aurine.cloudx.open.origin.vo;

import com.aurine.cloudx.open.origin.entity.ProjectFeeConf;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 费用配置查询视图
 *
 * @author xull@aurine
 * @date 2020-07-29 13:27:59
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "费用配置查询视图")
public class ProjectFeeConfVo extends ProjectFeeConf {
    /**
     * 计费标准
     */
    @ApiModelProperty("计费标准")
    private String unitString;
}
