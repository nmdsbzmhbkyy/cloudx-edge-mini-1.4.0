package com.aurine.cloudx.push.dto;


import com.aurine.cloudx.push.constant.EmailTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * sms传输对象
 * @ClassName: EmailDTO
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020/6/1 8:35
 * @Copyright:
 */
@Data
@ApiModel(value = "sms传输对象")
@EqualsAndHashCode(callSuper = true)
public class SmsDTO extends MessageDTO {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "手机号")
    private String mobile;

    @ApiModelProperty(value = "手机号列表")
    private List<String> mobileList;


}
