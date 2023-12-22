

package com.aurine.cloudx.estate.vo;

import com.aurine.cloudx.estate.entity.ProjectFaceResources;
import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * <p>人脸 微信传输对象</p>
 *
 * @ClassName: ProjectFaceResourcesWeChatVo
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-09-09 13:40
 * @Copyright:
 */
@Data

@ApiModel(value = "项目人脸库，用于项目辖区内的人脸识别设备下载")
public class ProjectFaceResourcesAppVo extends ProjectFaceResources {
    /**
     * 下发任务uid
     */
    @ApiModelProperty(value = "当前下发任务uid")
    private String jobUid;

    /**
     * 下载状态
     */
    @ApiModelProperty(value = "状态 1 授权成功 2 授权中，3 授权超时 4 授权失败 5 无状态 0 全部下载失败，面部不符合规范")
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
}
