package com.aurine.cloudx.estate.dto;

import com.aurine.cloudx.estate.entity.ProjectEntranceEvent;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 通行事件记录DTO
 */
@Data
@ApiModel(value = "通行事件记录DTO")
public class ProjectEntranceEventDto  extends ProjectEntranceEvent {
    private static final long serialVersionUID = 1L;
    /**
     * 第三方介质id
     */
    @ApiModelProperty(value = "第三方介质id")
    private String thirdCertMediaId;
    /**
     * 二维码字符串
     */
    @ApiModelProperty(value = "二维码字符串")
    private String qrcode;
}
