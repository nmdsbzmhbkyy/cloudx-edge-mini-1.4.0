package com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.vo.respond;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @description:对象管理数据
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2021-07-13
 * @Copyright:
 */
@Data
public class ObjManagerData {

    @ApiModelProperty("对象管理命令 GET SYNC SET UPDATE ADD DELETE")
    private String action;

    @ApiModelProperty("事件源类型")
    private String eventSrc;

    @ApiModelProperty("事件系统类型")
    private String eventType;

    @ApiModelProperty("事件服务名称")
    private String eventName;

    @ApiModelProperty("事件类型编码")
    private String eventCode;

    @ApiModelProperty("事件上报时间 ")
    private String eventTime;

    @ApiModelProperty("事件信息内容")
    private JSONObject data;

    @ApiModelProperty("对象名称 非必填 ")
    private String objName;

    @ApiModelProperty("对象信息 - WR20等网关服务使用")
    private JSONObject objInfo;

    @ApiModelProperty("结果集")
    private JSONObject result;

    @ApiModelProperty("服务ID")
    private String serviceId;

    @ApiModelProperty("注册失败参数")
    private JSONObject param;

    @ApiModelProperty("社区ID（项目UUID）")
    private String communityId;
}
