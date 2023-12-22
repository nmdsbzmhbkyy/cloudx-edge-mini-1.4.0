package com.aurine.cloudx.push.template;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 模板消息data对象的单个内容
 *
 * @ClassName: TemplateData
 * @author: 邹宇
 * @date: 2021-8-24 14:39:14
 * @Copyright:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "模板消息data对象的单条内容")
public class TemplateData implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 内容
     */
    @ApiModelProperty("内容")
    private String value;
    /**
     * 颜色
     */
    @ApiModelProperty("内容文字颜色")
    private String color;
}
