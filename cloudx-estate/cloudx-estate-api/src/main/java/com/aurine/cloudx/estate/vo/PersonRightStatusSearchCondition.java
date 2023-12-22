package com.aurine.cloudx.estate.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PersonRightStatusSearchCondition {

    /**
     * 所要查询的人员姓名
     * */
    private String personName;

    /**
     * 所要查询的人员类型
     * */
    private String personType;

    /**
     * 是否异常 （1是，0否）
     * */
    private Integer isException;


    /**
     * 楼栋名称
     */
    @ApiModelProperty(value="楼栋名称")
    private String buildingName;

    /**
     * 单元名称
     */
    @ApiModelProperty(value="单元名称")
    private String unitName;

    /**
     * 房屋名称
     */
    @ApiModelProperty(value="房屋名称")
    private String houseName;

}
