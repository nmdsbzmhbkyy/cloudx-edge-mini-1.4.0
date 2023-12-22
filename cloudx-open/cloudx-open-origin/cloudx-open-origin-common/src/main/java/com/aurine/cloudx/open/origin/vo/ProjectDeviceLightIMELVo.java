package com.aurine.cloudx.open.origin.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * (ProjectDeviceLightIMELVo)
 * 智能路灯
 *
 * @author zy
 * @since 2021年8月4日09:12:29
 */
@Data
@ApiModel("智能路灯控制器")
public class ProjectDeviceLightIMELVo {
    /**
     * 控制器IMEL
     */
    private String sn;

    /**
     * 上报数据频率
     */
    private String frequency;

    /**
     * 控制器类型
     */
    private String query;


    /**
     * 选择厂商   1.欧管  2.其他
     */
    private String vendor;


    /**
     * 1.单控    2.双控
     */
    private String type;


    /**
     * 物联网平台  1.电信 2.中移  3.HKT IOM  4.鹰潭市政
     */
    private String platform;

}
