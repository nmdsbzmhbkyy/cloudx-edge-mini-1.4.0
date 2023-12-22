package com.aurine.cloudx.estate.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * (ProjectAttrVo) 属性拓展
 *
 * @author xull
 * @since 2020/7/6 10:16
 */
@Data
@ApiModel("属性拓展")
public class ProjectAttrVo {
    /**
     * 属性id
     */
    @ApiModelProperty("属性id")
    private String attrId;
    /**
     * 类型
     */
    @ApiModelProperty("类型")
    private String style;
    /**
     * 所属
     */
    @ApiModelProperty(value = "所属")
    private String type;
    /**
     * 属性名称
     */
    @ApiModelProperty(value = "属性名称")
    private String attrName;
    /**
     * 属性编码
     */
    @ApiModelProperty(value = "属性编码")
    private String attrCode;
    /**
     * 值描述
     */
    @ApiModelProperty(value = "值描述")
    private String remark;

    /**
     * 项目id
     */
    @ApiModelProperty(value = "项目id")
    private Integer projectId;
}
