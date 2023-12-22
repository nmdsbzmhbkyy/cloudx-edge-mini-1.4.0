package com.aurine.cloudx.push.template;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Map;

/**
 * @ClassName: TemplateMessage
 * @author: 邹宇
 * @date: 2021-8-24 14:39:14
 * @Copyright:
 */
@Data
@ApiModel(value = "发送消息模板")
public class TemplateMessage {
    /**
     * 接收者openid
     */
    @ApiModelProperty("接收者openid")
    private String touser;
    /**
     * 模板ID
     */
    @ApiModelProperty("模板ID")
    private String template_id;

    /**
     * 模板数据
     */
    @ApiModelProperty("模板数据")
    private Map<String,TemplateData> data;
    /**
     * 模板内容字体颜色，不填默认为黑色
     */
    @ApiModelProperty("模板内容字体颜色，不填默认为黑色")
    private String color;

    /**
     * 模板跳转链接（海外帐号没有跳转能力）
     */
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private String url;
    /**
     * 跳小程序所需数据，不需跳小程序可不用传该数据
     */
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private MiniProgram miniprogram;

    /**
     * 所需跳转到的小程序appid（该小程序appid必须与发模板消息的公众号是绑定关联关系，暂不支持小游戏）
     */
    private String appid;
}
