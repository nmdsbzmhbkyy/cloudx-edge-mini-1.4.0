package com.aurine.cloudx.open.origin.vo;

import com.aurine.cloudx.open.origin.entity.ProjectMediaAd;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/**
 * Title: ProjectMediaAdFormVo
 * Description:
 *
 * @author xull@aurine.cn
 * @version 1.0.0
 * @date 2020/6/5 8:53
 */
@Data
@ApiModel("项目媒体广告设备配置表单")
public class ProjectMediaAdFormVo extends ProjectMediaAd {
    /**
     * 查询时间范围:开始时间
     * <p>
     * 字符串类型 用于接收前端传值
     */
    @ApiModelProperty("查询时间范围:开始时间")
    private String beginTimeString;
    /**
     * 播放频率中文显示
     */
    @ApiModelProperty("播放频率中文显示")
    private String finishTimeString;
    /**
     * 查询时间范围:开始时间
     */
    @ApiModelProperty("查询时间范围:开始时间")
    private LocalDate beginTime;
    /**
     * 查询时间范围:结束时间
     */
    @ApiModelProperty("查询时间范围:结束时间")
    private LocalDate finishTime;
    /**
     * 设备id列表
     */
    @ApiModelProperty("设备id列表")
    private List<String> deviceIds;

    /**
     * 资源id列表
     */
    @ApiModelProperty("资源id列表")
    private List<String> repoIds;


}
