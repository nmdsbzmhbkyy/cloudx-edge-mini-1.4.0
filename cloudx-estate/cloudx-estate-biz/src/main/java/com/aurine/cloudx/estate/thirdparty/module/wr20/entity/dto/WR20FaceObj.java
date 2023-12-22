package com.aurine.cloudx.estate.thirdparty.module.wr20.entity.dto;

import lombok.Data;

/**
 * WR20 住户 面部识别信息 对象
 *
 * @description:
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-12-23 15:04
 * @Copyright:
 */
@Data
public class WR20FaceObj {
    /**
     * index
     */
    private String index;
    /**
     * 面部id信息
     */
    private String ID;
    /**
     * 面部信息表述
     */
    private String desc;

    /**
     * 面部图片地址
     */
    private String url;
    /**
     * 注册时间
     */
    private String registerTime;




}
