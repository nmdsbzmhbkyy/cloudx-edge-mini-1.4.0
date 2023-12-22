package com.aurine.cloudx.estate.thirdparty.module.device.platform.yushi.entity.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubscriptionObj {

  /**
   * IP 地址类型
   * 0: IPv4 1: IPv6 2:域名 3: IPv4 和 IPv6 都需要
   * 当前仅支持 IPv4
   */
  private Long AddressType;

  /**
   * IPv4 地址
   */
  private String IPAddress;

  /**
   * 端口
   */
  private Long Port;

  /**
   * 订阅周期，单位为 s，范围为 [30, 3600]
   */
  private Long Duration;

}
