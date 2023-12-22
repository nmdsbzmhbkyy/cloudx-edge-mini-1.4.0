

package com.aurine.cloudx.estate.vo;

import com.aurine.cloudx.estate.entity.ProjectDeviceInfo;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 设备信息表
 *
 * @author huangjj
 * @date 2022-03-08 10:04:10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "电梯设备信息表")
public class ProjectDeviceLiftVo extends ProjectDeviceInfo {

    @ApiModelProperty(value = "楼层列表")
    private List<ProjectFloorVo> floors;

    @ApiModelProperty(value = "已选楼层")
    private String[] checked;

    @ApiModelProperty(value = "电梯编号")
    private List<String> liftNos;


}
