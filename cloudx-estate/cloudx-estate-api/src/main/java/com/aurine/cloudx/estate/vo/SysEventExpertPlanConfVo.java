package com.aurine.cloudx.estate.vo;

import com.aurine.cloudx.estate.entity.SysEventExpertPlanConf;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author 谢泽毅
 */
@Data
@ApiModel("用于列表展示")
public class SysEventExpertPlanConfVo extends SysEventExpertPlanConf {
    @ApiModelProperty("预案关联设备数量")
    private Integer count;
}
