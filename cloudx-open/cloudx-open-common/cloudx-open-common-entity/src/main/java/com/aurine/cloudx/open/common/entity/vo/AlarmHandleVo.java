package com.aurine.cloudx.open.common.entity.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.aurine.cloudx.open.common.entity.base.OpenBaseVo;
import com.aurine.cloudx.open.common.validate.annotation.SizeCustom;
import com.aurine.cloudx.open.common.validate.group.InsertGroup;
import com.aurine.cloudx.open.common.validate.group.UpdateGroup;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

/**
 * 报警处理Vo
 *
 * @author : Qiu
 * @date : 2021 12 20 16:35
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "报警处理Vo")
public class AlarmHandleVo extends OpenBaseVo {

    /**
     * 序列
     */
    @JsonProperty("seq")
    @JSONField(name = "seq")
    @ApiModelProperty(value = "序列", position = -1)
    @Null(message = "序列（seq）新增时需要为空", groups = {InsertGroup.class})
    @NotNull(message = "序列（seq）不能为空", groups = {UpdateGroup.class})
    @Max(value = Integer.MAX_VALUE, message = "序列（seq）数值过大")
    private Integer seq;

    /**
     * 项目id
     */
    @JsonProperty("projectId")
    @JSONField(name = "projectId")
    @ApiModelProperty(value = "项目id", required = true, position = -2)
    @NotNull(message = "项目id（projectId）不能为空", groups = {InsertGroup.class})
    @Max(value = Integer.MAX_VALUE, message = "项目id（projectId）数值过大")
    private Integer projectId;

    /**
     * 事件id
     */
    @JsonProperty("eventId")
    @JSONField(name = "eventId")
    @ApiModelProperty(value = "事件id")
    @NotBlank(message = "事件id（eventId）不能为空", groups = {InsertGroup.class})
    @SizeCustom(message = "事件id（eventId）长度不能小于1")
    @Size(max = 32, message = "事件id（eventId）长度不能超过32")
    private String eventId;

    /**
     * 时限
     */
    @JsonProperty("timeLeave")
    @JSONField(name = "timeLeave")
    @ApiModelProperty(value = "时限")
    @Size(max = 16, message = "时限（timeLeave）长度不能超过16")
    private String timeLeave;

    /**
     * 开始处理时间
     */
    @JsonProperty("handleBeginTime")
    @JSONField(name = "handleBeginTime", format = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "开始处理时间")
    private LocalDateTime handleBeginTime;

    /**
     * 结束处理时间
     */
    @JsonProperty("handleEndTime")
    @JSONField(name = "handleEndTime", format = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "结束处理时间")
    private LocalDateTime handleEndTime;

    /**
     * 处理时长
     */
    @JsonProperty("dealDuration")
    @JSONField(name = "dealDuration")
    @ApiModelProperty(value = "处理时长")
    @Size(max = 20, message = "处理时长（dealDuration）长度不能超过20")
    private String dealDuration;

    /**
     * 处理结果
     */
    @JsonProperty("result")
    @JSONField(name = "result")
    @ApiModelProperty(value = "处理结果")
    @Size(max = 128, message = "处理结果（result）长度不能超过128")
    private String result;

    /**
     * 图片
     */
    @JsonProperty("picUrl")
    @JSONField(name = "picUrl")
    @ApiModelProperty(value = "图片")
    @Size(max = 255, message = "图片（picUrl）长度不能超过255")
    private String picUrl;

    /**
     * 图片Base64（自定义，非数据库字段）
     */
    @JsonProperty("picBase64")
    @JSONField(name = "picBase64")
    @ApiModelProperty(value = "图片Base64（自定义，非数据库字段）")
    private String picBase64;
}
