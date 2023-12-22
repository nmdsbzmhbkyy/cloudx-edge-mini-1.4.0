package com.aurine.cloudx.push.dto;


import com.aurine.cloudx.push.constant.PushSystemEnum;
import com.cloudx.common.push.constant.OSTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * Email传输对象
 *
 * @ClassName: EmailDTO
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020/6/1 8:35
 * @Copyright:
 */
@Data
@ApiModel(value = "Email传输对象")
@EqualsAndHashCode(callSuper = true)
public class AppDTO extends MessageDTO {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "客户端ID")
    private String clientId;

    @ApiModelProperty(value = "客户端ID列表")
    private List<String> clientIdList;

    @ApiModelProperty(value = "客户端操作系统")
    private OSTypeEnum osType;


    @ApiModelProperty(value = "客户端操作系统列表")
    private List<OSTypeEnum> osTypeList;

    @ApiModelProperty(value = "所用的推送系统")
    @NotNull(message = "所使用的推送系统不能为空")
    private PushSystemEnum pushSystemType;

    @ApiModelProperty(value = "要推送的APP")
    @NotBlank(message = "推送APP名称不能为空")
    private String appName;

    @ApiModelProperty(value = "扩展配置参数")
    private Map<String, Object> paramMap;

}
