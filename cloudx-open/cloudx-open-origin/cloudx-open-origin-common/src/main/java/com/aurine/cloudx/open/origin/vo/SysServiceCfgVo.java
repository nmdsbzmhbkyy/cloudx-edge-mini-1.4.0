package com.aurine.cloudx.open.origin.vo;

import com.aurine.cloudx.open.origin.entity.SysServiceCfg;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * Title: SysServiceCfgClassifyVo
 * Description:增值服务名称与厂商列表vo
 *
 * @author guhl@aurine.cn
 * @version 1.0.0
 * @date 2020/11/16 13:42
 */
@Data
@ApiModel(value = "增值服务名称与厂商列表vo")
public class SysServiceCfgVo {
    @ApiModelProperty("增值服务服务名称")
    private String valueaddName;

    @ApiModelProperty("厂商列表（列表）")
    private List<SysServiceCfg> manufacturerList;

}
