package com.aurine.cloudx.push.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.Map;

/**
 * 公众号传输对象
 *
 * @ClassName: PublicAccountDTO
 * @author: 邹宇
 * @date: 2021-8-24 14:39:14
 * @Copyright:
 */
@Data
@ApiModel(value = "公众号传输对象")
@EqualsAndHashCode(callSuper = true)
public class WxDTO extends MessageDTO {

    private static final long serialVersionUID = 1L;

    /**
     * unionId
     */
    @ApiModelProperty(value = "unionId")
    private List<String> unionId;


    /**
     * 模板数据
     */
    @ApiModelProperty(value = "模板对象")
    private Map<String, Object> data;


    /**
     * 模板id
     */
    @ApiModelProperty(value = "模板id")
    private String templateId;


}
