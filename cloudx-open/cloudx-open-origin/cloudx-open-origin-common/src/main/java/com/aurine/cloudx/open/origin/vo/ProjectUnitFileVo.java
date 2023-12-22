package com.aurine.cloudx.open.origin.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName: UnitFileListVo
 * @author: 王良俊 <>
 * @date:  2020年08月14日 上午09:29:46
 * @Copyright:
*/
@Data
@ApiModel(value = "单元")
public class ProjectUnitFileVo {

    /**
     * 用于前端回显
     * */
    @ApiModelProperty(value = "图片地址")
    String url;

}
