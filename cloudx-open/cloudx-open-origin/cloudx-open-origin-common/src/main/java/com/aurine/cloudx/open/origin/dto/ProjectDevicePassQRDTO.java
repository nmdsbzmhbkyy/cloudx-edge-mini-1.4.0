package com.aurine.cloudx.open.origin.dto;

import com.aurine.cloudx.open.origin.constant.enums.PersonTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

/**
 * 通行设备二维码请求DTO
 */
@Data
@ApiModel(value = "通行设备二维码请求DTO")
public class ProjectDevicePassQRDTO implements Serializable {
    @ApiModelProperty(value = "项目ID")
    @NotEmpty
    private Integer projectId;

    @ApiModelProperty(value = "人员ID")
    @NotEmpty
    private String personId;

    @ApiModelProperty(value = "人员名称，非必填")
    private String personName;

    @NotEmpty
    @ApiModelProperty(value = "人员类型  住户1，员工2，访客3")
    private PersonTypeEnum personType;

    @ApiModelProperty(value = "房间框架号列表")
    private List<String> roomCodeList;

    @ApiModelProperty(value = "有效次数")
    private int times;

    @ApiModelProperty(value = "开始时间，13位 时间戳")
    private long beginTime;

    @ApiModelProperty(value = "结束时间，13位时间戳。与有效时间必填一项。")
    private long endTime;

    @ApiModelProperty(value = "有效时间，单位分钟。当填写有效时间时，endTime失效")
    private int effectiveTime;
}
