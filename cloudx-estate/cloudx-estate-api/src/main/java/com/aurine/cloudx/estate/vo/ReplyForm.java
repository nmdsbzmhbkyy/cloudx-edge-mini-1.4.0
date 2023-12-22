package com.aurine.cloudx.estate.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "回复表单")
@Data
public class ReplyForm {

    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private String feedbackId;

    /**
     * 回复内容
     */
    @ApiModelProperty(value = "回复内容")
    private String replyContent;

}