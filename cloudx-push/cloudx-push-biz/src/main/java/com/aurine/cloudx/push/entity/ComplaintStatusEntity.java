package com.aurine.cloudx.push.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 投诉处理进展
 *
 * @ClassName: ComplaintStatusEntity
 * @author: 邹宇
 * @date: 2021-8-27 14:06:49
 * @Copyright:
 */
@Data
@ApiModel(value = "投诉处理进展")
public class ComplaintStatusEntity extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 是否完成
     * true 完成
     * false 变更
     */
    @ApiModelProperty(value = "是否完成", required = true)
    private boolean isItDone;

    /**
     * 投诉人房号
     */
    @ApiModelProperty(value = "投诉人房号", required = true)
    private String complainantsRoomNo;

    /**
     * 投诉主题
     */
    @ApiModelProperty(value = "投诉主题", required = true)
    private String complaintTheme;

    /**
     * 投诉时间
     */
    @ApiModelProperty(value = "投诉时间", required = true)
    private String complaintTime;

    /**
     * 当前进展
     */
    @ApiModelProperty(value = "当前进展", required = true)
    private String currentProgress;

}
