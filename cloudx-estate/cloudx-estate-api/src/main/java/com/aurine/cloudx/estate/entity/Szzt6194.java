package com.aurine.cloudx.estate.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.lang.reflect.Field;

/**
 * 楼栋信息表
 */
@Data
public class Szzt6194 {
    // 楼栋名称
    @JsonProperty("BUILDINGNAME")
    private String BUILDINGNAME;

    // 楼栋编号
    @JsonProperty("BUILDINGNO")
    private String BUILDINGNO;

    // 楼栋所属小区编号
    @JsonProperty("VILLAGENO")
    private String VILLAGENO;

    // 单元数
    @JsonProperty("BUILDINGSUM")
    private Integer BUILDINGSUM;

    // 楼宇层数
    @JsonProperty("STOREYSUM")
    private Integer STOREYSUM;

    // 每层户数
    @JsonProperty("HOUSESUM")
    private Integer HOUSESUM;

    // 楼栋长（负责人）
    @JsonProperty("BUILDINGMAN")
    private String BUILDINGMAN;

    // 楼栋长联系方式
    @JsonProperty("BUILDINGTEL")
    private String BUILDINGTEL;

    // 楼栋经度
    @JsonProperty("LONGITUDE")
    private Double LONGITUDE;

    // 楼栋纬度
    @JsonProperty("LATITUDE")
    private Double LATITUDE;

    // 所属区域
    @JsonProperty("DISTRICTNAME")
    private String DISTRICTNAME;

    // 所属街道
    @JsonProperty("STREETNAME")
    private String STREETNAME;

    // 所属社区
    @JsonProperty("COMMUNITYNAME")
    private String COMMUNITYNAME;

    // 上传人标识
    @JsonProperty("HUMAN_ID")
    private String HUMAN_ID;

    // 设置属性值
    public void setValue(Szzt6194 value) throws IllegalAccessException {
        Field[] fields = Szzt6194.class.getDeclaredFields();

        for (int i = 0; i < fields.length; i++) {
            Field f = fields[i];
            f.setAccessible(true);

            if (f.get(this) == null && f.get(value) != null) {
                f.set(this, f.get(value));
            }
        }
    }
}
