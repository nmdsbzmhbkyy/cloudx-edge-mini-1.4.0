package com.aurine.cloudx.wjy.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "你好对象")
public class Hello implements Serializable {
    @ApiModelProperty(value = "你好名称")
    private String name;
}
