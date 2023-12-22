package com.aurine.cloudx.estate.open.vistor.bean;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 分页数据
 */
@Data
public class Page {
    /**
     * 每页显示条数，默认 20
     */
    @ApiModelProperty("每页显示条数，默认 20")
    private long size = 20;

    /**
     * 当前页
     */
    @ApiModelProperty("当前页，默认 1")
    private long current = 1;
}
