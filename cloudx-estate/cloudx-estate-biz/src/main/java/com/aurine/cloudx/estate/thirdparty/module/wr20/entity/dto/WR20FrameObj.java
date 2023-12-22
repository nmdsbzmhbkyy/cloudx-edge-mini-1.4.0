package com.aurine.cloudx.estate.thirdparty.module.wr20.entity.dto;

import lombok.Data;

/**
 * @description:
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-12-21
 * @Copyright:
 */
@Data
public class WR20FrameObj {
    /**
     * 框架号
     */
    private String frameNo;
    private String parentNo;
    private String createTime;
    private String frameDesc;
    /**
     * 框架类型
     * 0 为社区框架类型
     * 1 为住户框架类型
     * 99 为区口顶级框架类型
     */
    private Integer frameType;
    private String updateTime;
    /**
     * 框架层级
     * 1：
     */
    private Integer levelNo;
}
