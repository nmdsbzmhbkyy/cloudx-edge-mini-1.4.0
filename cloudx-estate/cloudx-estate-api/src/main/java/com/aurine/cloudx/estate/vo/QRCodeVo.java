package com.aurine.cloudx.estate.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * (QRCodeVo)微信二维码图片
 *
 * @author xull
 * @version 1.0.0
 * @date 2020/9/10 11:49
 */
@ApiModel("微信二维码图片")
@Data
public class QRCodeVo {
    @ApiModelProperty("二维码图片：base64编码")
    private String imgBase64;
    @ApiModelProperty("开始时间")
    private LocalDateTime beginTime;
    @ApiModelProperty("结束时间")
    private LocalDateTime endTime;
}
