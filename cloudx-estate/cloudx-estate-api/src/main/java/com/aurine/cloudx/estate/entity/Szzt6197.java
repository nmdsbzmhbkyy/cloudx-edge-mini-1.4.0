package com.aurine.cloudx.estate.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.lang.reflect.Field;
import java.time.LocalDateTime;

/**
 * 居民信息表
 */
@Data
public class Szzt6197 {

    // 住户姓名
    @JsonProperty("NAME")
    private String NAME;

    // 住户身份证
    @JsonProperty("IDENTITYID")
    private String IDENTITYID;

    // 住户所属房屋编号
    @JsonProperty("HOUSENO")
    private String HOUSENO;

    // 住户与业主关系
    @JsonProperty("RELNATION")
    private String RELNATION;

    // 住户联系方式
    @JsonProperty("TEL")
    private String TEL;

    // 住户年龄
    @JsonProperty("AGE")
    private Integer AGE;

    // 住户性别
    @JsonProperty("GENDER")
    private String GENDER;

    // 住户籍贯
    @JsonProperty("NATIVEPLACE")
    private String NATIVEPLACE;

    // 户主民族
    @JsonProperty("NATIONCODE")
    private String NATIONCODE;

    // 政治面貌
    @JsonProperty("POLITICALCODE")
    private String POLITICALCODE;

    // 入住时间
    @JsonProperty("CREATETIME")
    private String CREATETIME;

    // 搬离时间
    @JsonProperty("LEAVETIME")
    private String LEAVETIME;

    // 住户头像照片
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
    public void setValue(Szzt6197 value) throws IllegalAccessException {
        Field[] fields = Szzt6197.class.getDeclaredFields();

        for (int i = 0; i < fields.length; i++) {
            Field f = fields[i];
            f.setAccessible(true);

            if (f.get(this) == null && f.get(value) != null) {
                f.set(this, f.get(value));
            }
        }
    }
}
