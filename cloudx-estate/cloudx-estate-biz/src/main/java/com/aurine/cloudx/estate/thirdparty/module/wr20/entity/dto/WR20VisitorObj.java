package com.aurine.cloudx.estate.thirdparty.module.wr20.entity.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * WR20访客对象
 *
 * @description:
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2021-01-25 9:28
 * @Copyright:
 */
@Data
public class WR20VisitorObj {

    /**
     * 访客姓名
     */
    @NotEmpty
    private String viName;
    /**
     * 访客性别
     */
    @NotEmpty
    private Integer viSex;
    /**
     * 证件类型
     */
    @NotEmpty
    private String viCredentialType;
    /**
     * 证件号码
     */
    @NotEmpty
    private String viCredentialID;
    /**
     * 访客数量
     */
    @NotEmpty
    private Integer viPersonNumber;

    /**
     * 被访人ID
     */
    private Long viDestID;
    /**
     * 被访人框架编号
     * VIDestID VIDestFrameNo两者必填一项，以ID为人员主要凭据
     */
    private String viDestFrameNo;

    /**
     * 门禁卡号
     */
    private String viCardNo;
    /**
     * 来访车牌号
     */
    private String viCarID;
    /**
     * 来访时间
     */
    private String viVisitorTime;
    /**
     * 离开时间
     */
    private String viLeaveTime;
    /**
     * 来访事由
     */
    private String viReason;
    /**
     * 身份证证件照
     */
    private String viCredCardImage;
    /**
     * 抓拍照
     */
    private String viImage;


}
