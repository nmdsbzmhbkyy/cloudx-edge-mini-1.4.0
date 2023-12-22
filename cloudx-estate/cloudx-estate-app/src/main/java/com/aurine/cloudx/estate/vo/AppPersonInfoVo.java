package com.aurine.cloudx.estate.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 人员信息Vo(继承了重点人员信息的数据)
 *
 * @author pigx code generator
 * @date 2020-05-12 13:37:22
 */
@Data
@ApiModel(value = "小区房屋状态")
public class AppPersonInfoVo {


    /**
     * 小区列表
     */
    @ApiModelProperty(value = "小区列表")
    private List<String> projectIdList;

    /**
     * 是否绑定房屋状态
     */
     @ApiModelProperty(value = "是否绑定房屋状态 0 1 2 9")
     private String auditStatus;


}
