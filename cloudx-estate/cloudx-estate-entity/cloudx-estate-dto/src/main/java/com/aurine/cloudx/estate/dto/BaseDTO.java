package com.aurine.cloudx.estate.dto;

import lombok.Data;

/**
 * @description:
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-08-06
 * @Copyright:
 */
@Data
public class BaseDTO {
    /**
     * 厂家
     */
    private String company;

    /**
     * 动作，如车辆出场、入场、出场图片，入场图片等
     */
    private String action;

    /**
     * 车场在线状态
     */
    private char isOnline;
}
