package com.aurine.cloudx.estate.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.lang.reflect.Field;
import java.time.LocalDateTime;

/**
 * 出入信息表
 */
@Data
public class Szzt6199 {

    // 开门人员/房间号
    @JsonProperty("OPENNAME")
    private String OPENNAME;

    // 出入信息编号
    @JsonProperty("RECORDNO")
    private String RECORDNO;

    // 开门身份标识
    @JsonProperty("IDENTITYID")
    private String IDENTITYID;

    // 房屋编号
    @JsonProperty("HOUSENO")
    private String HOUSENO;

    // 开门人员类型
    @JsonProperty("IDENTITYTYPE")
    private String IDENTITYTYPE;

    // 开门方式
    @JsonProperty("ENTERTYPE")
    private String ENTERTYPE;

    // 开门时间
    @JsonProperty("OPENTIME")
    private String OPENTIME;

    // 开门方向
    @JsonProperty("DIRECTION")
    private String DIRECTION;

    // 开门设备
    @JsonProperty("EQUIPMENT")
    private String EQUIPMENT;

    // 开门是否成功
    @JsonProperty("OPENSTATUS")
    private String OPENSTATUS;

    // 开门人员抓拍照片
    @JsonProperty("PICTURE")
    private String PICTURE;

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
    public void setValue(Szzt6199 value) throws IllegalAccessException {
        Field[] fields = Szzt6199.class.getDeclaredFields();

        for (int i = 0; i < fields.length; i++) {
            Field f = fields[i];
            f.setAccessible(true);

            if (f.get(this) == null && f.get(value) != null) {
                f.set(this, f.get(value));
            }
        }
    }
}
