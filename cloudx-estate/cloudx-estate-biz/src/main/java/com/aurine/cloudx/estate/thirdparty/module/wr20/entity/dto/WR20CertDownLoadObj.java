package com.aurine.cloudx.estate.thirdparty.module.wr20.entity.dto;

import lombok.Data;

/**
 * WR20 介质下载状态
 *
 * @description:
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-12-31
 * @Copyright:
 */
@Data
public class WR20CertDownLoadObj {

    /**
     * 住户id
     */
    private String teneID;

    /**
     * 住户第三方id
     */
    private String thirdID;

    /**
     * 介质描述 卡号或人脸id
     */
    private String desc;
    /**
     * 身份证号码
     */
    private String idNumber;
    /**
     * 手机号码
     */
    private String mobile;
    /**
     * 联系电话
     */
    private String telephone;

    /**
     * 设备编号
     */
    private String device;

    /**
     * 状态码 0：已删除，-3：失败 -4：卡已满 1：已下载 2：已停用
     */
    private Integer status;

    /**
     * 介质类型 0：人脸  1：卡片
     */
    private String type;

    /**
     * 卡号
     */
    private String identification;


}
