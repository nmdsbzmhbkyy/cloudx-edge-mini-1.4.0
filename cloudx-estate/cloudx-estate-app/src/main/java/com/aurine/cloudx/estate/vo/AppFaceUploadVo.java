

package com.aurine.cloudx.estate.vo;

import com.aurine.cloudx.estate.entity.ProjectFaceResources;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

/**
 * <p>人脸 微信传输对象</p>
 *
 * @ClassName: ProjectFaceResourcesWeChatVo
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2021-01-28 9:40
 * @Copyright:
 */
@Data
@ApiModel(value = "项目人脸 app上传人脸")
public class AppFaceUploadVo {
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
     * 状态 1 正常 2 冻结
     */
    @NotEmpty
    @ApiModelProperty(value = "状态 1 正常 2 冻结", hidden = false)
    private String status;

}
