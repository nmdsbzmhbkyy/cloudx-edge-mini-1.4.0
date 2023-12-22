package com.aurine.cloudx.estate.thirdparty.module.wr20.entity.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * WR20住户对象
 *
 * @description:
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-12-23 15:04
 * @Copyright:
 */
@Data
public class WR20WorkObj {
    /**
     * 工作人员ID	,增加时填写-1
     */
    @NotEmpty
    private Long guardID;
    /**
     * 工作人员第三方ID
     */
    private String thirdID;

    /**
     * 姓名
     */
    @NotEmpty
    private String name;
    /**
     * 证件类型 111
     */
    @NotEmpty
    private String credentialType;
    /**
     * 证件号码
     */
    private String credentialID;
    /**
     * 手机号
     */
    private String telephone;
    /**
     * 性别
     */
    private String gender;
    /**
     * 卡时效总开关	1启用，0停用
     */
    private Integer isEnabledCardLimit;
    /**
     * 通行有效期	截止这个日期为有效期限yyyy-MM-dd HH:mm:ss
     */
    private String limitDateTime;

    /**
     * 卡张数
     */
    private Integer countCard;

    /**
     * 卡列表
     */
    private List<String> cards;

    /**
     * 人脸个数
     */
    private String countFaces;
    /**
     * 人脸列表
     */
    private List<WR20FaceObj> faces;

    /**
     * 权限列表
     */
    private List<WR20DeviceRightObj> rights;

    /**
     * 职务类别ID
     */
    private Integer vocationTypeID;

}
