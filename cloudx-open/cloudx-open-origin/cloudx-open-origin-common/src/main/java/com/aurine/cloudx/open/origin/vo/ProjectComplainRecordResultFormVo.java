package com.aurine.cloudx.open.origin.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * (ProjectComplainRecordResultFormVo)投诉处理结果表单视图
 *
 * @author xull
 * @version 1.0.0
 * @date 2020/9/2 8:38
 */
@ApiModel("投诉处理结果表单视图")
@Data
public class ProjectComplainRecordResultFormVo {
    /**
     * 投诉单号，uuid
     */
    @ApiModelProperty(value = "投诉单号，uuid", required = true)
    private String complaintId;
    /**
     * 回复内容
     */
    @ApiModelProperty(value = "回复内容")
    private String replyContent;
    /**
     * 完成图片
     */
    @ApiModelProperty(value = "完成图片")
    private String donePicPath;

    @ApiModelProperty(value="完成图片2")
    private String donePicPath2;

    @ApiModelProperty(value="完成图片3")
    private String donePicPath3;

    @ApiModelProperty(value="完成图片4")
    private String donePicPath4;

    @ApiModelProperty(value="完成图片5")
    private String donePicPath5;

    @ApiModelProperty(value="完成图片6")
    private String donePicPath6;


}
