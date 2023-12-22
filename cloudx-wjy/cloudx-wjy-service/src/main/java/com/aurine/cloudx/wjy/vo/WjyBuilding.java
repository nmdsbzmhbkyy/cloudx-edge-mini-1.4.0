package com.aurine.cloudx.wjy.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 楼栋实体
 */
@Data
public class WjyBuilding implements Serializable {
    /**
     * 楼栋ID
     */
    private String id;
    /**
     * 长度50，编码
     */
    private String number;
    /**
     * 长度50，名称
     */
    private String name;
    /**
     * 单元个数
     */
    private int unitCount;
    /**
     * 项目ID
     */
    private String prjectId;
    /**
     * 物业状态
     */
    private String propertyStatus;
    /**
     * 描述
     */
    private String description;
    /**
     * 企业ID
     */
    private String ecId;
    /**
     * 项目组团ID
     */
    private String projectGroupID;
    /**
     * 地上楼层数
     */
    private int overgroundCount;
    /**
     * 地下楼层数
     */
    private int undergroundCount;
    /**
     * 位置
     */
    private String position;
    /**
     * 文件ID
     */
    private String fileUrl;
    /**
     * 物业类型
     */
    private String propertyType;
    /**
     * '资源分类 1房源 2场地 3广告位 4车位'
     */
    private int catalog;
    /**
     * 面积
     */
    private int area;
    /**
     * 项目组团名称
     */
    private String projectGroupname;

    /**
     * 长度32，源ID
     */
    private String sourceID;

    /**
     * 长度32，来源系统
     */
    private String sourceSystem;

    /**
     * 楼栋下的单元信息
     */
    private List<WjyBuildingUnit> buildingUnitVoList;
}
