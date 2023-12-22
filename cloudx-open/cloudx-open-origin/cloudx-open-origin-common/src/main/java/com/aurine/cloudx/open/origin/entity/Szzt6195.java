package com.aurine.cloudx.open.origin.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.lang.reflect.Field;

/**
 * 单元信息表
 */
@Data
public class Szzt6195 {

    // 单元名称
    @JsonProperty("UNITNAME")
    private String UNITNAME;

    // 单元编号
    @JsonProperty("UNITNO")
    private String UNITNO;

    // 单元号
    @JsonProperty("UNITNUM")
    private Integer UNITNUM;

    // 单元所属楼栋编号
    @JsonProperty("BUILDINGNO")
    private String BUILDINGNO;

    // 单元每层户数
    @JsonProperty("UNITSUM")
    private Integer UNITSUM;

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
    public void setValue(Szzt6195 value) throws IllegalAccessException {
        Field[] fields = Szzt6195.class.getDeclaredFields();

        for (int i = 0; i < fields.length; i++) {
            Field f = fields[i];
            f.setAccessible(true);

            if (f.get(this) == null && f.get(value) != null) {
                f.set(this, f.get(value));
            }
        }
    }
}
