package com.aurine.cloudx.push.entity;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName: BaseEntity
 * @author: 邹宇
 * @date: 2021-8-27 14:06:49
 * @Copyright:
 */
@Data
public class BaseEntity  {

    private static final long serialVersionUID = 1L;

    /**
     * 建议
     */
    @ApiModelProperty(value = "建议")
    private String remark;
}
