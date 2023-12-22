package com.aurine.cloudx.open.origin.entity;

import com.aurine.cloudx.open.common.entity.base.OpenBasePo;
import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 巡检点签到明细(ProjectInspectCheckinDetail)表实体类
 *
 * @author 王良俊
 * @since 2020-08-04 10:08:51
 */
@Data
@TableName("project_inspect_checkin_detail")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "巡检点签到明细(ProjectInspectCheckinDetail)")
public class ProjectInspectCheckinDetail extends OpenBasePo<ProjectInspectCheckinDetail> {

    private static final long serialVersionUID = -88074146642366092L;


    /**
     * 签到明细id，uuid
     */
    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "签到明细id，uuid")
    private String checkInDetailId;

    /**
     * 明细id
     */
    @ApiModelProperty(value = "明细id")
    private String detailId;

    /**
     * 签到时间
     */
    @ApiModelProperty(value = "签到时间")
    private LocalDateTime checkInTime;

    /**
     * 签到方式 参考字典 inspect_check_in_type
     */
    @ApiModelProperty(value = "签到方式 1.二维码 2.现场拍照 参考字典 inspect_check_in_type")
    private char checkInType;

    /**
     * 照片
     */
    @ApiModelProperty(value = "照片")
    private String picUrl;


}