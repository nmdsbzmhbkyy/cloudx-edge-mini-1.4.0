package com.aurine.cloudx.open.origin.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.lang.reflect.Field;

/**
 * 房屋信息表
 */
@Data
public class Szzt6196 {

    // 房屋名称
    @JsonProperty("HOUSENAME")
    private String HOUSENAME;

    // 房号
    @JsonProperty("HOUSENUM")
    private Integer HOUSENUM;

    // 层号
    @JsonProperty("STOREY")
    private Integer STOREY;

    // 房屋编号
    @JsonProperty("HOUSENO")
    private String HOUSENO;

    // 房屋所属单元编号
    @JsonProperty("UNITNO")
    private String UNITNO;

    // 户主姓名
    @JsonProperty("HOUSEHOLDER")
    private String HOUSEHOLDER;

    // 户主身份证
    @JsonProperty("IDENTITYID")
    private String IDENTITYID;

    // 户主联系方式
    @JsonProperty("HOUSEID")
    private String HOUSEID;

    // 户主年龄
    @JsonProperty("AGE")
    private Integer AGE;

    // 户主性别
    @JsonProperty("GENDER")
    private String GENDER;

    // 户主籍贯
    @JsonProperty("NATIVEPLACE")
    private String NATIVEPLACE;

    // 户主民族
    @JsonProperty("NATIONCODE")
    private String NATIONCODE;

    // 政治面貌
    @JsonProperty("POLITICALCODE")
    private String POLITICALCODE;

    // 房屋用途
    @JsonProperty("HOUSETYPE")
    private String HOUSETYPE;

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
    public void setValue(Szzt6196 value) throws IllegalAccessException {
        Field[] fields = Szzt6196.class.getDeclaredFields();

        for (int i = 0; i < fields.length; i++) {
            Field f = fields[i];
            f.setAccessible(true);

            if (f.get(this) == null && f.get(value) != null) {
                f.set(this, f.get(value));
            }
        }
    }
}
