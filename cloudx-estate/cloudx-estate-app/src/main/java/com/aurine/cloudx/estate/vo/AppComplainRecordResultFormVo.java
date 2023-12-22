package com.aurine.cloudx.estate.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * (ProjectComplainRecordResultFormVo)投诉处理结果表单视图
 *
 * @author xull
 * @version 1.0.0
 * @date 2020/9/2 8:38
 */
@ApiModel("投诉处理结果表单")
@Data
public class AppComplainRecordResultFormVo {
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
    @ApiModelProperty(value = "图片列表 Base64")
    private List<String> picBase64List;


}
