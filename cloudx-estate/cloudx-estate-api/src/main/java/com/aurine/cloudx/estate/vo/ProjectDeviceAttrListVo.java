package com.aurine.cloudx.estate.vo;

import com.aurine.cloudx.estate.entity.ProjectDeviceAttr;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * (ProjectDeviceAttrListVo)
 *
 * @author xull
 * @since 2020/7/6 8:58
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("设备拓展属性列表对象")
public class ProjectDeviceAttrListVo extends ProjectDeviceAttr {

    /**
     * 属性名称
     */
    @ApiModelProperty(value = "属性名称")
    private String attrName;
    /**
     * 值描述
     */
    @ApiModelProperty(value = "值描述")
    private String remark;
}
