package com.aurine.cloudx.estate.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.lang.reflect.Field;

/**
 * 数字政通小区信息表
 */
@Data
public class Szzt6193 implements Serializable {
    // 小区名称
    @JsonProperty("VILLAGENAME")
    private String VILLAGENAME;

    // 小区唯一标识
    @JsonProperty("VILLAGENO")
    private String VILLAGENO;

    // 小区详细地址
    @JsonProperty("ADDRESS")
    private String ADDRESS;

    // 楼宇总数
    @JsonProperty("BUILDINGSUM")
    private Integer BUILDINGSUM;

    // 物业单位
    @JsonProperty("PROPERTY")
    private String PROPERTY;

    // 是否封闭小区
    @JsonProperty("CLOSE")
    private String CLOSE;

    // 所属区域
    @JsonProperty("DISTRICTNAME")
    private String DISTRICTNAME;

    // 所属街道
    @JsonProperty("STREETNAME")
    private String STREETNAME;

    // 所属社区
    @JsonProperty("COMMUNITYNAME")
    private String COMMUNITYNAME;

    // 小区经度
    @JsonProperty("LONGITUDE")
    private Double LONGITUDE;

    // 小区纬度
    @JsonProperty("LATITUDE")
    private Double LATITUDE;

    // 上传人标识
    @JsonProperty("HUMAN_ID")
    private String HUMAN_ID;

    // 设置属性值
    public void setValue(Szzt6193 value) throws IllegalAccessException {
        Field[] fields = Szzt6193.class.getDeclaredFields();

        for (int i = 0; i < fields.length; i++) {
            Field f = fields[i];
            f.setAccessible(true);

            if (f.get(this) == null && f.get(value) != null) {
                f.set(this, f.get(value));
            }
        }
    }
}
