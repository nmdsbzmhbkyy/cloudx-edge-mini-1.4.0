package com.aurine.cloudx.estate.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * (ProjectComplainRecordRequestFormVo)
 *
 * @author xull
 * @version 1.0.0
 * @date 2020/9/2 15:04
 */
@Data
@ApiModel("投诉处理登记表单")
public class ProjectComplainRecordRequestFormVo {
    /**
     * 房号
     */
    @ApiModelProperty(value = "房号", required = true)
    private String houseId;
    /**
     * 投诉类型 参考字典类型 complaint_type
     */
    @ApiModelProperty(value = "投诉类型 0.环境 1.安全 2.秩序 3.工作人员 4.设备设施 5.供水 6.消防 7.供气8.供电 9.便民设施 10.其他投诉 参考字典类型 complaint_type", required = true)
    private String complaintType;
    /**
     * 投诉内容
     */
    @ApiModelProperty(value = "投诉内容", required = true)
    private String content;
    /**
     * 图片1
     */
    @ApiModelProperty(value = "图片1")
    private String picPath1;
    /**
     * 图片2
     */
    @ApiModelProperty(value = "图片2")
    private String picPath2;
    /**
     * 图片3
     */
    @ApiModelProperty(value = "图片3")
    private String picPath3;
    /**
     * 图片4
     */
    @ApiModelProperty(value = "图片4")
    private String picPath4;
    /**
     * 图片5
     */
    @ApiModelProperty(value = "图片5")
    private String picPath5;
    /**
     * 图片6
     */
    @ApiModelProperty(value = "图片6")
    private String picPath6;
}
