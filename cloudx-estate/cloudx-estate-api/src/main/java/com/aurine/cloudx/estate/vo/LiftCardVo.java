package com.aurine.cloudx.estate.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Auther: hjj
 * @Date: 2022/4/1 09:26
 * @Description: 电梯卡写卡数据VO
 */
@Data
@ApiModel(value = "电梯卡写卡数据VO")
public class LiftCardVo {

    @ApiModelProperty("卡类型 =m1")
    private String type;

    @ApiModelProperty("应用id")
    private String appid;

    @ApiModelProperty("当前时间戳 10 位，精确到秒")
    private String timestamp;

    @ApiModelProperty("签名字符串")
    private String sign;

    @ApiModelProperty("扇区偏移量 取值范围：[1-21]")
    private Integer offset;

    @ApiModelProperty("卡密码 即项目秘钥 12个字符的16进制密文")
    private String key;

    @ApiModelProperty("写入字节数 取值范围：[1-1024]")
    private Integer bytes;

    @ApiModelProperty("数据")
    private String data;

}
