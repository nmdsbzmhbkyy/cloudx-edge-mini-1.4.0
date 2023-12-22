package com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.entity.vo.param;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 网络参数对象 json名：netparam
 * </p>
 *
 * @ClassName: NetworkObj
 * @author: 王良俊 <>
 * @date: 2020年11月25日 下午04:03:06
 * @Copyright:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonRootName("netparam")
public class NetworkObj {

    /**
     * 本机
     */
    @NotNull
    String ipAddr;

    /**
     * 子网掩码
     */
    @NotNull
    String netmask;

    /**
     * 网关
     */
    @NotNull
    String gateway;

    /**
     * Dns服务器
     */
    @NotNull
    String dns;

    /**
     * 管理员机
     */
    String managerIp;

    /**
     * 中心服务器
     */
    String centerIp;

    /**
     * 流媒体服务器
     */
    String rtspServer;

    /**
     * 电梯控制器
     */
    String elevator;

    /**
     * 人脸识别服务器
     */
    String faceServer;


}
