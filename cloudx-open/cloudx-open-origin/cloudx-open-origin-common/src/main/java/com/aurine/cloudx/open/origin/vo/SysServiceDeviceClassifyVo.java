package com.aurine.cloudx.open.origin.vo;

import com.aurine.cloudx.open.origin.entity.SysServiceDeviceClassify;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Title: SysServiceDeviceClassifyVo
 * Description:用于接受类型名称
 *
 * @author guhl@aurine.cn
 * @version 1.0.0
 * @date 2020/6/4 13:42
 */
@Data
public class SysServiceDeviceClassifyVo extends SysServiceDeviceClassify {
    @ApiModelProperty("类型名称")
    private String classifyName;
}
