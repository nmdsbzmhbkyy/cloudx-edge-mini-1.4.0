package com.aurine.cloudx.wjy.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 员工/工作人员实体
 */
@Data
public class WorkerVo implements Serializable {
    /**
     * id
     */
    private String id;
    /**
     *长度50，名称
     */
    private String name;
    /**
     *长度50，编码
     */
    private String number;
    /**
     *编码200，联系电话
     */
    private String phone;
    /**
     *长度32，源ID
     */
    private String sourceID;
    /**
     *长度32，来源系统
     */
    private String sourceSystem;
    /**
     *所属组织名称
     */
    private String orgUnitNames;


    /**
     *长度200，EMail
     */
    private String email;
    /**
     *长度200，备注
     */
    private String description;
    /**
     * int，长度11，是否加入了金蝶云的组织（0否，1是）
     */
    private int hasJoinOrg;
    /**
     *长度64，工作岗位Title
     */
    private String jobTitle;
    /**
     * int,,长度11，外包人员（0不是，1是） 默认0
     */
    private String bizType;
    /**
     *长度32，证件类型
     */
    private String certificateType;
    /**
     *长度1024，证件号码
     */
    private String certificateNumber;
    /**
     *长度32，岗位名称
     */
    private String quarterName;

    /**
     * 项目id
     */
    @ApiModelProperty(value = "项目id")
    private Integer projectId;
}
