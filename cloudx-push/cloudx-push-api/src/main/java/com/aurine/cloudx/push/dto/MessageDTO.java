package com.aurine.cloudx.push.dto;


import com.pig4cloud.pigx.admin.api.entity.SysUser;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

/**
 * <p></p>
 *
 * @ClassName: MessageDTO
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020/6/1 8:33
 * @Copyright:
 */
@Data
@ApiModel(value = "消息传输对象")
public class MessageDTO {
    private static final long serialVersionUID = 1L;



    @ApiModelProperty(value = "内容")
    @NotBlank(message = "信息内容不能为空")
    private String message;

}
