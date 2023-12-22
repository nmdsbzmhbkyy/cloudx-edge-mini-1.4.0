package com.aurine.cloudx.estate.vo;

import com.aurine.cloudx.estate.entity.ProjectFaceResources;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * (ProjectVistitorRequestFormVo)
 *
 * @author xull
 * @version 1.0.0
 * @date 2020/9/9 17:21
 */
@Data
@ApiModel(value = "访客申请表单")
public class ProjectVisitorRequestFormVo {

    /**
     * 访客类型 1 亲情人员 2 快递人员 3 外卖人员 4 住户服务
     */
    @ApiModelProperty(value = "访客类型 1 亲情人员 2 快递人员 3 外卖人员 4 住户服务")
    private String visitorType;
    /**
     * 手机号
     */
    @ApiModelProperty(value = "手机号")
    private String mobileNo;


    /**
     * 被访人id
     */
    @ApiModelProperty(value = "被访人id")
    private String visitPersonId;
    /**
     * 被访人房屋id
     */
    @ApiModelProperty(value = "被访人房屋id")
    private String visitHouseId;
    /**
     * 姓名
     */
    @ApiModelProperty(value = "姓名")
    private String personName;
    /**
     * 时间范围可能包含小时看情况
     */
    @ApiModelProperty(value = "时间范围 (某年某月某日-某年某月某日)")
    private String[] timeRange;
    /**
     * 人脸图片
     */
    @ApiModelProperty(value = "人脸图片 base64")
    private String faceUrl;
    /**
     * 是否人脸
     */
    @ApiModelProperty(value = "是否人脸")
    private String isFace;
    /**
     * 车牌号
     */
    @ApiModelProperty(value = "车牌号")
    private String plateNumber;
}
