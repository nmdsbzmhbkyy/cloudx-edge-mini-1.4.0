package com.aurine.cloudx.open.origin.dto;

import com.aurine.cloudx.open.origin.constant.enums.CertmediaTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>通行凭证DTO,用于集成多种通行权限，中台对接</p>
 *
 * @ClassName: ProjectPassCertDTO
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020/6/22 13:50
 * @Copyright:
 */
@Data
@ApiModel(value = "通行凭证DTO")
public class ProjectPassCertDTO {

    @ApiModelProperty(value = "通行凭证ID")
    private String uid;

    @ApiModelProperty(value = "通行凭证类型")
    private CertmediaTypeEnum certmediaType;


    @ApiModelProperty(value = "凭证值 : 卡号/密码/URL/BASE64")
    private String value;

}
