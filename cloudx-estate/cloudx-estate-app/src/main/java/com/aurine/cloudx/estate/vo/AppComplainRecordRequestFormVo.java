package com.aurine.cloudx.estate.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Base64;
import java.util.List;

@Data
@ApiModel("投诉登记表单")
public class AppComplainRecordRequestFormVo {
    /**
     * 房号
     */
    @ApiModelProperty(value = "房间ID", required = true)
    private String houseId;
    /**
     * 投诉类型 参考字典类型 complaint_type
     */
    @ApiModelProperty(value = "投诉类型 投诉类型（0 环境 1 安全 2 秩序 3 工作人员 4 设备设施 5  供水 6 消防 7 供气 8 供电 9 便民设施 10 其他投诉）", required = true)
    private String complaintType;
    /**
     * 投诉内容
     */
    @ApiModelProperty(value = "投诉内容", required = true)
    private String content;
    /**
     * 图片
     */
    @ApiModelProperty(value = "图片列表 Base64")
    private List<String> picBase64List;
}
