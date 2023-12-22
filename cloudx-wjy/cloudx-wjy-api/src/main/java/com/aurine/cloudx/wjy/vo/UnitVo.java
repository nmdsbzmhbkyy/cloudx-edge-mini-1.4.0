package com.aurine.cloudx.wjy.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ： huangjj
 * @date ： 2021/4/15
 * @description： 单元
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "单元")
public class UnitVo {
    /**
     * 长度50，名称,必填
     */
    @ApiModelProperty(value = "长度50，名称")
    private String name;
    /**
     * 长度50，编码,必填
     */
    @ApiModelProperty(value = "长度50，编码")
    private String number;
    /**
     * int，长度11，序号,必填
     */
    @ApiModelProperty(value = "长度11，序号")
    private Integer Seq;
    /**
     * 长度32，源ID，必填
     */
    @ApiModelProperty(value = "长度32，源ID")
    private String sourceID;
    /**
     * 长度32，来源系统，必填
     */
    @ApiModelProperty(value = "长度32，来源系统")
    private String sourceSystem;
}