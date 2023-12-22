

package com.aurine.cloudx.dashboard.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * <p> 防控人员统计</p>
 *
 * @ClassName: DwProjectPersonAbnormal
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2021-04-12 14:42
 * @Copyright:
 */
@Data
@TableName("DW_PROJECT_PERSON_CONTROL")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "防控人员统计表")
public class DwProjectPersonControl extends Model<DwProjectPersonControl> {

    /**
     * seq
     */
    @ApiModelProperty(value = "seq")
    private Long seq;
    /**
     * PROJECTID
     */
    @ApiModelProperty(value = "PROJECTID")
    private Long projectId;

    /**
     * 事件时间
     */
    @ApiModelProperty(value = "事件时间")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime entraDate;

    /**
     * 防控类型
     */
    @ApiModelProperty(value = "防控类型")
    private String controlType;

    /**
     * 抓拍地址
     */
    @ApiModelProperty(value = "抓拍地址")
    private String picUrl;


}
