package com.aurine.cloudx.push.entity;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 分机来电
 *
 * @ClassName: ExtensionCallEntity
 * @author: 邹宇
 * @date: 2021-8-27 14:06:49
 * @Copyright:
 */
@Data
@ApiModel(value = "分机来电")
public class ExtensionCallEntity extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 来电号码
     */
    @ApiModelProperty(value = "来电号码", required = true)
    private String callerNumber;
    /**
     * 来电时间
     */
    @ApiModelProperty(value = "来电时间", required = true)
    private String callerTime;
    /**
     * 分机号码
     */
    @ApiModelProperty(value = "分机号码", required = true)
    private String extensionNumber;

}
