package com.aurine.cloudx.estate.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName: CertVo
 * @author: 王良俊 <>
 * @date:  2020年11月09日 上午11:29:36
 * @Copyright:
*/
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "介质vo对象")
public class CertVo {
    /**
     * 介质ID
     */
    @ApiModelProperty("介质ID")
    private String certmediaId;
    /**
     * 介质类型
     */
    @ApiModelProperty("介质类型")
    private String certmedia;
    /**
     * 人员id
     */
    private String personId;

}