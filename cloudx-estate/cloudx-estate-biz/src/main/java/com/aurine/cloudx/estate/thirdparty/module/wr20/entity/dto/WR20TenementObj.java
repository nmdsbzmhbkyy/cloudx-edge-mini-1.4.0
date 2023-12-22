package com.aurine.cloudx.estate.thirdparty.module.wr20.entity.dto;

import com.alibaba.fastjson.annotation.JSONField;
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
public class WR20TenementObj {
    /**
     * 住户ID,增加时填写-1
     */
    @NotEmpty
    @JSONField(name = "ID")
    private Long ID;
    /**
     * 框架号
     */
    @NotEmpty
    private String frameNo;

    /**
     * 姓名
     */
    @NotEmpty
    private String name;
    /**
     * 4.0的PersonId
     */
    private String thirdId;
    /**
     * 人脸注册图片	url
     */
    private String faceImage;
    /**
     * 人脸注册源类型	1：第三方（默认）2：云平台
     */
    private String faceSrcType = "2";
    /**
     * 证件类型 111
     */
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
     * 联系电话
     */
    private String telephone2;
    /**
     * 性别
     */
    private String gender;
    /**
     * 籍贯
     */
    private String nativePlace;
    /**
     * 国籍
     */
    private String nationality;
    /**
     * 民族
     */
    private String nation;
    /**
     * 文化程度
     */
    private String degreeOfEdu;
    /**
     * 婚姻情况
     */
    private String maritalStatus;
    /**
     * 现住地址
     */
    private String addressCurrent;
    /**
     * 户籍地址
     */
    private String addressPermanent;
    /**
     * 紧急联系人
     */
    private String emergencyContact;
    /**
     * 紧急联系人电话
     */
    private String emergencyContactTel;
    /**
     * 人员状况
     */
    private String personnelStatus;
    /**
     * 人员特征
     */
    private String personelFeature;
    /**
     * 工作单位
     */
    private String workUnit;
    /**
     * 工作单位地址
     */
    private String workUnitAddress;
    /**
     * 工作单位负责人
     */
    private String workUnitMaster;
    /**
     * 工作单位电话
     */
    private String workUnitTel;
    /**
     * 与业主关系
     */
    private String relationshipDesc;
    /**
     * 入住时间
     */
    private String registerTime;
    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 信息更新时间
     */
    private String updateTime;
    /**
     * 人员类别	0：户籍人员；1：来沪人员；2：境外人员；3：外来服务
     */
    private String personKinds;
    /**
     * 人屋关系	 0：自住；1：租凭；2：民宿；3：其他
     */
    private String personRoomRelation;
    /**
     * 卡时效总开关	1启用，0停用
     */
    private String isEnabledCardLimit;
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
     * 成员类型 1 业主 2租户 3亲情 4 住户服务 5访客 6 快递 7外卖
     */
    private String memberType;
    /**
     * 人脸列表
     */
    private List<WR20FaceObj> faces;
    /**
     * 权限列表
     */
    private List<WR20DeviceRightObj> rights;

}
