package com.aurine.cloudx.estate.vo;

import com.aurine.cloudx.common.data.annotation.Desensitization;
import com.aurine.cloudx.common.data.enums.DesensitizationTypeEnum;
import com.aurine.cloudx.estate.entity.ProjectBlacklistAttr;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 黑名单属性Vo
 *
 * @author 顾文豪
 * @date 2023/11/13 10:02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "黑名单属性Vo")
public class ProjectBlacklistAttrVo extends ProjectBlacklistAttr {
    private static final long serialVersionUID = 1L;

    /** 联系人员身份证 */
    @ApiModelProperty(name = "联系人员身份证",notes = "")
    @Desensitization(type = DesensitizationTypeEnum.CUSTOMER,startInclude = 10,endExclude = 14)
    private String credentialNo ;

    /** 图片地址 */
    @ApiModelProperty(name = "图片地址",notes = "")
    private String picUrl ;

}
