package com.aurine.cloudx.estate.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("项目物业联系方式")
public class AppPropertyContactVo {
    /**
     * 名称
     */
    @ApiModelProperty(value = "联系人名称")
    private String name;


    /**
     * 电话
     */
    @ApiModelProperty(value = "电话")
    private String contactPhone;

}
