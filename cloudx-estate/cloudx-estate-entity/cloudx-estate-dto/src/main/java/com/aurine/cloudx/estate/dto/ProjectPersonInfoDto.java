package com.aurine.cloudx.estate.dto;


import lombok.Data;

/**
 * 人员DTO
 */
@Data
public class ProjectPersonInfoDto {


    /**
     * 人员ID
     */
    private Integer seq;
    /**
     * 人员ID
     */
    private String personId;

    /**
     * 人员编码，可传入第三方编码
     */
    private String personCode;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 姓名
     */
    private String personName;

    /**
     * 证件类型
     */
    private String credentialType;

    /**
     * 证件号码
     */
    private String credentialNo;

    private String telephone;


}
