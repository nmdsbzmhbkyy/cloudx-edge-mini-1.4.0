package com.aurine.cloudx.estate.cert.vo;

import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.estate.cert.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

/**
 * @description: 下载请求对象
 * @author: wangwei
 * @date: 2021/12/14 14:12
 **/
@Data
public class CertAdownRequestVO extends BaseEntity {

    /**
     * 请求 使用msgId，msgId传入为空时，创建一个uuid
     */
    @Size(max = 32, message = "请求ID长度超出最大允许范围")
    private String requestId;

    /**
     * 项目ID
     */
    @NotEmpty(message = "项目ID不能为空")
    @Size(max = 12, message = "项目ID长度超出最大允许范围")
    private String projectId;

    /**
     * 应用ID 1：CloudX 4.0云平台
     */
    @NotEmpty(message = "应用ID不能为空")
    @Size(max = 12, message = "APPID长度超出最大允许范围")
    private String appId;

    /**
     * 平台ID 调用下发的平台，主要用于流量控制 2:中台2.0，3:中台3.0 - 华为平台
     */
    @NotEmpty(message = "平台ID不能为空")
    @Size(max = 12, message = "平台ID长度超出最大允许范围")
    private String platformId;

    /**
     * 设备ID 设备唯一标识
     */
    @NotEmpty(message = "设备ID不能为空")
    @Size(max = 32, message = "设备ID长度超出最大允许范围")
    private String deviceId;

    /**
     * '0:卡片，1:人脸',
     */
    private String certMediaType;

    /**
     * 设备名称
     */
    private String deviceName;

    /**
     * 请求主体
     */
    @NotEmpty(message = "请求主体不能为空")
    private JSONObject body;

    /**
     * 下发状态 0:待下载;2:下载中;3:已下载;4:下载失败;5:超时
     * 不填写默认为0
     * 更新状态时，填写3/4
     */
    private String state;

    /**
     * 0最优先，预留，1：优先下载，2：次优先，3：低优先任务
     */
    @ApiModelProperty(value = "0最优先，预留，1：优先下载，2：次优先，3：低优先任务")
    private Integer priotity;
}
