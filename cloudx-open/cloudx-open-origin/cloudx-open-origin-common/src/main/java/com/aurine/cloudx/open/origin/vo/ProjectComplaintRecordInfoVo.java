package com.aurine.cloudx.open.origin.vo;

import com.aurine.cloudx.open.origin.entity.ProjectComplaintRecord;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Title: ProjectComplaintRecordPageVo
 * Description: 投诉信息详情Vo
 *
 * @author guhl@aurine.cn
 * @version 1.0.0
 * @date 2020/7/20 14:49
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "报事投诉详情视图")
public class ProjectComplaintRecordInfoVo extends ProjectComplaintRecord {
    @ApiModelProperty("当前房屋名称")
    String currentHouseName;
    @ApiModelProperty("代报人联系方式")
    String phone;
    @ApiModelProperty("代报人姓名")
    String trueName;
    @ApiModelProperty("处理人姓名")
    String handlerName;
}
