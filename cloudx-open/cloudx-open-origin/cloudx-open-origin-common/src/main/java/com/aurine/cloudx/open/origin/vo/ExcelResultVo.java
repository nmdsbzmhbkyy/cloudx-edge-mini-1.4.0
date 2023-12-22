package com.aurine.cloudx.open.origin.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * (ExcelResultVo)
 *
 * @author xull
 * @version 1.0.0
 * @date 2020/8/19 8:37
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "excel导入结果响应")
public class ExcelResultVo {
    @ApiModelProperty("异常excel下载路径")
    private String path;
    @ApiModelProperty("异常描述")
    private Map<Integer,String> describe;
    @ApiModelProperty("保存的设备id信息")
    List<String> list;
    @ApiModelProperty("保存的人屋关系vo")
    List<ProjectHousePersonRelVo> projectHousePersonRelVos;
}
