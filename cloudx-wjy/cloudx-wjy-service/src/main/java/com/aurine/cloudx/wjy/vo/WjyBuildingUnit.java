package com.aurine.cloudx.wjy.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 楼栋下的单元实体
 */
@Data
public class WjyBuildingUnit implements Serializable {
    /**
     * id
     */
    private String id;
    /**
     * 长度50，名称
     */
    private String name;
    /**
     *长度50，编码
     */
    private String number;
    /**
     * int，长度11，序号
     */
    private int Seq;
}
