package com.aurine.cloudx.estate.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 车辆登记表单
 *
 * @author pigx code generator
 * @date 2020-05-12 13:37:22
 */
@Data
@ApiModel(value = "车辆登记表单")
public class AppCarRegisterFormVo {


    /**
     * 车牌号
     */
    @ApiModelProperty(value = "车牌号", required = true)
    private String plateNumber;

    /**
     * 人员ID
     */
     @ApiModelProperty(value = "人员ID", required = true)
     private String personId;


}
