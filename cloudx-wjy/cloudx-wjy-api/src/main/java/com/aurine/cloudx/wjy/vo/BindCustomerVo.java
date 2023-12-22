package com.aurine.cloudx.wjy.vo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 客户房间绑定实体
 */
@Data
public class BindCustomerVo implements Serializable {
    //楼栋名称
    private String buildName;
    //单元名称
    private String unitName;
    //房间名称
    private String roomName;
    //客户名称
    private String customerName;
    //客户手机号码
    private String customerPhone;
    //客户类型（P个人，E企业，G政府机构，S个体户，O其他企业，T临时）
    private String customerType;

    //以下非必填
    //入住时间（格式：yyyy-MM-dd）（缺省取值当前日期
    private String joinInDate;
    //房客关系类型
    private String type;
    //入住描述（长度小于256个字符）
    private String description;
    //是否收费对象（0：否，1：是）（缺省默认为0）
    private String isCharge;
    //租期开始日期（格式：yyyy-MM_dd）
    private String rentStartDate;
    //租期结束日期（格式：yyyy-MM_dd）
    private String rentEndDate;
    /**
     * 项目id
     */
    @ApiModelProperty(value = "项目id")
    private Integer projectId;
}
