package com.aurine.cloudx.wjy.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 房屋实体
 */
@Data
public class WjyRoom implements Serializable {
    /**
     * 房间ID
     */
    private String id;

    /**
     * 房间名称, 长度50
     */
    private String name;
    /**
     * 房间编码, 长度50
     */
    private String number;
    /**
     * decimal(28,10)，建筑面积
     */
    private Number buildingArea;

    /**
     * 公司ID
     */
    private String partAId;

    /**
     * 公司名称
     */
    private String partAName;

    /**
     * 项目ID
     */
    private String projectID;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     *  楼栋ID
     */
    private String buildingID;

    /**
     * 长度32，楼栋名称
     */
    private String buildingName;

    /**
     * 单元ID
     */
    private String buildUnitID;

    /**
     * 长度50，单元名称
     */
    private String buildUnitName;


    /**
     * 楼层ID
     */
    private String floorID;

    /**
     * 长度16，楼层
     */
    private String floor;

    /**
     * 入住时间 当返回为0时表示为业主
     */
    private String zhujojintime;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 资源分类 1房源 2场地 3广告位 4车位
     */
    private String catalog;

    /**
     * 更新时间
     */
    private String updateTime;

    /**
     * 费用计算状态描述字符串
     * 费用计算状态（0空置，1已售，2已收楼 3装修中 4 已入住）
     */
    private String feeStatusStr;

    /**
     * ???暂未知
     */
    private String changeStatus;


    // --------------------------按我家云接口4.3.1，以上是获取房屋时，房屋信息内容 --------------------------




    // --------------------------按我家云接口4.3.3，以下是保存房屋时，房屋信息内容 --------------------------


    /**
     * decimal(28,10)，套内面积,且小于建筑面积
     */
    private Number roomArea;


    /**
     * 长度32，产品类型名称
     */
    private String productTypeName;

    /**
     * int度11，业务属性（0 住宅,1 商业,2 工业,3 车位,4 仓库,
     *  5 保障性用房,6 公寓,7 办公楼,8 厂房,9 商铺,10 土地,
     *  11 宿舍,12 其他 13车卡）
     */
    private int property;
    /**
     * int，长度11，费用计算状态（0空置，1已售，2已售楼 3装修中 4 已入住
     */
    private int feeStatus;
    /**
     * int，长度11，状态（0未入住，1自住，2出租）
     */
    private String status;

    /**
     * 长度32，源ID
     */
    private String sourceID;
    /**
     * 长度32，来源系统
     */
    private String sourceSystem;
    /**
     * 长度50，房间属性
     */
    private String roomAttributeStr;
    /**
     * 长度50，户型名称
     */
    private String roomModelName;
    /**
     * 长度500，备注
     */
    private String description;
    /**
     * 长度128，产权状况
     */
    private String propertyRight;
    /**
     * 长度128，物业用途
     */
    private String purpose;
    /**
     * datetime，竣工日期1970-01-01
     */
    private String completionDate;
    /**
     * decimal(28,10),花园面积
     */
    private Number gardenArea;
    /**
     * decimal(28,10),阁楼面积
     */
    private Number loftArea;

}
