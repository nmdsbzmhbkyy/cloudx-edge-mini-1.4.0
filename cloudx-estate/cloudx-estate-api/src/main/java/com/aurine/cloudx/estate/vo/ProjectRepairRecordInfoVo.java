package com.aurine.cloudx.estate.vo;

import com.aurine.cloudx.estate.entity.ProjectRepairRecord;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Title: ProjectRepairRecordInfoVo
 * Description: 报修详情vo
 *
 * @author guhl@aurine.cn
 * @version 1.0.0
 * @date 2020/7/20 14:49
 */
@Data
@ApiModel(value = "报修服务详情视图")
public class ProjectRepairRecordInfoVo extends ProjectRepairRecord {
    @ApiModelProperty("当前房屋名称")
    String currentHouseName;
    @ApiModelProperty("代报人联系方式")
    String phone;
    @ApiModelProperty("代报人姓名")
    String trueName;
    @ApiModelProperty("处理人姓名")
    String handlerName;
}
