

package com.aurine.cloudx.estate.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;


@Data
@ApiModel(value = "app上传头像")
public class AppFaceHeadPortraitVo {
    /**
     * 人员类型 1 住户 2 员工 3 访客
     */
    @NotEmpty
    @ApiModelProperty(value = "人员类型 1 住户 2 员工 3 访客", required = false)
    private String personType;

    /**
     * 图片来源 1 web端 2 小程序 3 app
     */
    @ApiModelProperty(value = "图片来源 1 web端 2 小程序 3 app", required = false)
    private String origin;
    /**
     * 人员id, 根据人员类型取对应表id
     */
    @ApiModelProperty(value = "人员id, 根据人员类型取对应表id", required = true)
    private String personId;
    /**
     * 图片base64
     */
    @ApiModelProperty(value = "图片base64", required = true)
    private String picBase64;
    /**
     * 路径
     */
    @NotEmpty
    @ApiModelProperty(value = "图片url", hidden = false)
    private String headPortraitUrl;



}
