package com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.vo.param.elevator.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * <p>电梯网络参数</p>
 * <p>参数类型：base</p>
 * <p>参数子类型：network</p>
 *
 * @author 王良俊
 * @date "2022/2/28"
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LiftNetwork {

    /*
     * mac地址 长度为6
     **/
    private List<String> mac;

    /*
     * IP地址 长度为4
     **/
    private List<String> ipAddr;

    /*
     * 子网掩码 长度为4
     **/
    private List<String> netMask;

    /*
     * 默认网关 长度为4
     **/
    private List<String> gateway;

    /*
     * 中心服务器地址 长度为4
     **/
    private List<String> centerIp;

    /*
     * 首选DNS服务器 长度为4
     **/
    private List<String> dns1;

    /*
     * 备用DNS服务器 长度为4
     **/
    private List<String> dns2;

    /*
     * 分机IP 长度为 7 * 4 （0,3 是 IP1； 4-7 是IP2...）
     **/
    private List<String> exitips;
//    /*
//     * 以太网模式
//     **/
//    private String EthMode;

}
