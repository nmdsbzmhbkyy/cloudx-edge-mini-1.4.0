package com.aurine.cloudx.open.origin.vo;

import lombok.Data;

/**
 * <p>
 * 用于区域管辖页面数据展示
 * </p>
 *
 * @ClassName: ProjectRegionManagerVo
 * @author: 王良俊 <>
 * @date: 2020年12月10日 下午02:48:11
 * @Copyright:
 */
@Data
public class ProjectRegionManagerVo {

    /**
     * 区域编号
     */
    private Integer seq;
    /**
     * 区域ID
     */
    private String regionId;
    /**
     * 区域名
     */
    private String regionName;
    /**
     * 区域管辖人员名单
     */
    private String managerName;
    /**
     * 区域下拥有的设备数
     */
    private String deviceNum;
}
