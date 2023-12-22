

package com.aurine.cloudx.estate.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * <p>人脸 微信传输对象</p>
 *
 * @ClassName: ProjectFaceResourcesWeChatVo
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2021-01-28 9:40
 * @Copyright:
 */
@Data

@ApiModel(value = "项目人脸 app获取人脸")
public class AppFaceGetVo {
    /**
     * 下发任务uid
     */
    @ApiModelProperty(value = "当前下发任务uid")
    private String jobUid;

    /**
     * 下载状态
     */
    @ApiModelProperty(value = "状态 1 授权成功 2 部分授权成功，请联系管理员， 0 全部下载失败，面部不符合规范")
    private String dlStatus;

    /**
     * 是否有人脸
     */
    @ApiModelProperty(value = "状态 1 有人脸 0 无人脸")
    private String haveFace;

    /**
     * 通行方案状态
     */
    @ApiModelProperty(value="通行方案状态 0 禁用 1 启用 2 过期 3 无设备 4 未配置")
    private String isActive;

    /**
     * 项目下是否有设备
     */
    @ApiModelProperty(value="项目下是否有设备 0 无设备 1 有设备 ")
    private String isHaveDevice;

    /**
     * 人脸id，uuid
     */
    @ApiModelProperty(value = "人脸id，uuid")
    private String faceId;
    /**
     * 人员类型 1 住户 2 员工 3 访客
     */
    @NotEmpty
    @ApiModelProperty(value = "人员类型 1 住户 2 员工 3 访客", required = true)
    private String personType;

    /**
     * 图片来源 1 web端 2 小程序 3 app
     */
    @ApiModelProperty(value = "图片来源 1 web端 2 小程序 3 app", required = true)
    private String origin;
    /**
     * 人员id, 根据人员类型取对应表id
     */
    @ApiModelProperty(value = "人员id, 根据人员类型取对应表id")
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
    @ApiModelProperty(value = "状态 1 正常 2 冻结", hidden = true)
    private String status;

}
