package com.aurine.cloudx.estate.vo.qrPasscode;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class DeleteProjectQrPasscodeRecordVo {
    /** 二维码校验字符串 */
    @ApiModelProperty(name = "二维码校验字符串",notes = "")
    @NotBlank(message = "二维码校验字符串不可为空")
    private String uniqueCode;
}
