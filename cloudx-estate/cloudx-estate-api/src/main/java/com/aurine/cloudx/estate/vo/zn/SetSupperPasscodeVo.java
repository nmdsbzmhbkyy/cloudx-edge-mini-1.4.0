package com.aurine.cloudx.estate.vo.zn;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class SetSupperPasscodeVo {
    @ApiModelProperty(value = "注册时间",notes = "密码最大不能超过999999,设置为0为取消密码")
    @NotNull(message = "密码不可为空")
    @Max(value = 999999,message = "密码最大不可超过999999")
    @Min(value = 0,message = "密码最小值为0（取消密码）")
    private Integer passcode;
}
