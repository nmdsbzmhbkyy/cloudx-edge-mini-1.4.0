package com.aurine.cloudx.open.origin.vo;

import com.aurine.cloudx.open.origin.entity.ProjectPersonAttr;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * (ProjectPersonAttrListVo)
 *
 * @author xull
 * @since 2020/7/6 9:01
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("人员拓展属性列表Vo")
public class ProjectPersonAttrListVo extends ProjectPersonAttr {
    /**
     * 属性编码
     */
    @ApiModelProperty(value = "属性编码")
    private String attrCode;

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
