package com.aurine.cloudx.open.origin.vo;

import com.aurine.cloudx.open.origin.entity.SysServiceCfg;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * Title: SysServiceCfgClassifyVo
 * Description:新增业务时，用于接受关联设施类型
 *
 * @author guhl@aurine.cn
 * @version 1.0.0
 * @date 2020/6/4 13:42
 */
@Data
@ApiModel(value = "增值服务表单对象")
public class SysServiceCfgClassifyVo extends SysServiceCfg {
    @ApiModelProperty("关联设施类型")
    private List<String> deviceClassify;
}
