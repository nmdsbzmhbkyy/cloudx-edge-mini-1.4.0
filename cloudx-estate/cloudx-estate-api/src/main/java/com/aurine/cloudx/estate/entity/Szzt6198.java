package com.aurine.cloudx.estate.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 车辆信息表
 */
@Data
public class Szzt6198 {

    // 车辆型号
    @JsonProperty("CARMODEL")
    private String CARMODEL;

    // 车牌号码
    @JsonProperty("CARNO")
    private String CARNO;

    // 车主姓名
    @JsonProperty("CARNAME")
    private String CARNAME;

    // 车主联系方式
    @JsonProperty("CARTEL")
    private String CARTEL;

    // 车辆所属身份证编号
    @JsonProperty("IDENTITYID")
    private String IDENTITYID;

    // 车辆颜色
    @JsonProperty("CARCOLOR")
    private String CARCOLOR;

    // 车辆类型
    @JsonProperty("CARTYPE")
    private String CARTYPE;

    // 车辆登记时间
    @JsonProperty("CREATETIME")
    private String CREATETIME;

    // 车辆照片
    @JsonProperty("CARPICTURE")
    private String CARPICTURE;

    // 车辆型号
    @JsonProperty("HUMAN_ID")
    private String HUMAN_ID;

    // 所属区域
    @JsonProperty("DISTRICTNAME")
    private String DISTRICTNAME;

    // 所属街道
    @JsonProperty("STREETNAME")
    private String STREETNAME;

    // 所属社区
    @JsonProperty("COMMUNITYNAME")
    private String COMMUNITYNAME;

    // 设置属性值
    public void setValue(Szzt6198 value) throws IllegalAccessException {
        Field[] fields = Szzt6198.class.getDeclaredFields();

        for (int i = 0; i < fields.length; i++) {
            Field f = fields[i];
            f.setAccessible(true);

            if (f.get(this) == null && f.get(value) != null) {
                f.set(this, f.get(value));
            }
        }
    }
}
