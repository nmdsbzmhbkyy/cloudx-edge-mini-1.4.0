package com.aurine.cloudx.estate.thirdparty.module.device.platform.aurine.entity.dto;

import lombok.Data;

/**
 * 冠林中台 文本信息传输对象
 *
 * @description:
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2021-01-13
 * @Copyright:
 */
@Data
public class AurineMessageDTO {

    /**
     * x信息序列 seq
     */
    private Long msgid;
    /**
     * 信息类型 0：纯文本 1：富文本
     */
    private Integer msgtype;
    /**
     * 信息标题 20字符
     */
    private String title;
    /**
     * 信息内容
     */
    private String content;
    /**
     * 13位时间戳
     */
    private String time;
    /**
     * 1-65535
     */
    private Integer validity_day;
}
