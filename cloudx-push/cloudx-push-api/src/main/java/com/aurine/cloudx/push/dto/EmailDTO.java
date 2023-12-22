package com.aurine.cloudx.push.dto;


import com.aurine.cloudx.push.constant.EmailTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

/**
 * Email传输对象
 * @ClassName: EmailDTO
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020/6/1 8:35
 * @Copyright:
 */
@Data
@ApiModel(value = "Email传输对象")
@EqualsAndHashCode(callSuper = true)
public class EmailDTO extends MessageDTO {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "email地址")
    private String email;

    @ApiModelProperty(value = "email地址列表")
    private List<String> emailList;

    @ApiModelProperty(value = "标题")
    @NotBlank(message = "标题不能为空")
    private String title;

    @ApiModelProperty(value = "Email类型")
    @NotNull(message = "email类型不能为空")
    private EmailTypeEnum emailType;
}
