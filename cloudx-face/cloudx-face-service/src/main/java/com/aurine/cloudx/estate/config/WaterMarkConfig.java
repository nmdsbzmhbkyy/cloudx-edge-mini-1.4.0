package com.aurine.cloudx.estate.config;

import lombok.Data;

/**
 * @description: 水印配置信息
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2021-03-03
 * @Copyright:
 */
@Data
public class WaterMarkConfig {

    /**
     * 模板名称
     */
    private String name;
    /**
     * 字体颜色信息
     */
    private Integer[] rgb;

    /**
     * 透明度 0-1
     */
    private Float alpha;
    /**
     * 旋转角度
     */
    private Float degree;
    /**
     * 水印内容
     */
    private String mark;

    /**
     * 水印类型，等比放大，绝对大小
     * relative   absolute
     */
    private String type;

    /**
     * 字体
     */
    private String family;

    /**
     * 字体大小
     */
    private Integer[] size;

    private Boolean isDefault;
}
