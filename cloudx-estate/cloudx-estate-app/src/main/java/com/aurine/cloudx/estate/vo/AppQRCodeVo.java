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
@ApiModel("二维码")
@Data
public class AppQRCodeVo {
    @ApiModelProperty("二维码字符串")
    private String QRCodeStr;
    @ApiModelProperty("开始时间")
    private LocalDateTime beginTime;
    @ApiModelProperty("结束时间")
    private LocalDateTime endTime;
}
