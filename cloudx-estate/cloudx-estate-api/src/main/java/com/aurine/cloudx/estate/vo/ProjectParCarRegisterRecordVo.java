/*
 *    Copyright (c) 2018-2025, lengleng All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * Neither the name of the pig4cloud.com developer nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * Author: lengleng (wangiegie@gmail.com)
 */

package com.aurine.cloudx.estate.vo;

import com.aurine.cloudx.estate.entity.ProjectParCarRegister;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 车辆登记查询列表
 *
 * @author 王伟
 * @date 2020-07-08 14:10:32
 */
@Data
@ApiModel(value = "车辆登记VO")
public class ProjectParCarRegisterRecordVo extends ProjectParCarRegister {

    /**
     * 车主名
     */
    @ApiModelProperty(value = "车主名")
    private String personName;


    /**
     * 车主名
     */
    @ApiModelProperty(value = "车主名")
    private String parkName;
    /**
     * 关系类型 0 闲置(公共) 1 产权 2 租赁
     */
    @ApiModelProperty(value = "关系类型 0 闲置(公共) 1 产权 2 租赁")
    private String relType;

    /**
     * 规则类型 1 免费车 2 月租车 3 临时车
     */
    @ApiModelProperty(value = "规则类型 1 免费车 2 月租车 3 临时车")
    private String ruleType;

    /**
     * 收费规则ID
     */
    @ApiModelProperty(value = "收费规则ID")
    private String ruleId;

}
