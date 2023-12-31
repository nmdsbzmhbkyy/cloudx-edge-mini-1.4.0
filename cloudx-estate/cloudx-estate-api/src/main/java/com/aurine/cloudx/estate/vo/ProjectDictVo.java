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

import com.aurine.cloudx.estate.entity.ProjectDict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * @author cjw
 * @description: 字典VO 用于下拉框
 * @date 2021/7/7 9:06
 */
@Data
@ApiModel("字典VO 用于下拉框")
public class ProjectDictVo extends ProjectDict {
    @ApiModelProperty("下拉框名称")
    private String label;
    @ApiModelProperty("下拉框的值")
    private String value;
    @ApiModelProperty("字典类型")
    private String dictType;

}
