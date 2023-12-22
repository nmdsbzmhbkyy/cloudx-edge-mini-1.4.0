package com.aurine.cloudx.open.origin.vo;

import com.aurine.cloudx.open.origin.entity.SysPlatFeedback;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SysPlatFeedbackVo extends SysPlatFeedback {
     /**
     * 用户账号
     */
    @ApiModelProperty(value = "用户账号")
    private String username;
}
