

package com.aurine.cloudx.estate.vo;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 设备维护-门禁查看-列表
 *
 * @author 王良俊
 * @date 2020-05-21 09:52:28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "设备维护-门禁查看-用户列表")
public class ProjectRightDeviceOptsAccessSearchConditionVo extends Model<ProjectRightDeviceOptsAccessSearchConditionVo> {

    /**
     * 认证介质 1 指纹 2 人脸 3 卡
     */
    @ApiModelProperty(value = "通讯方式 1 指纹 2 人脸 3 卡")
    private String certMedia;

    /**
     * 下载状态 0 未下载 1 已下载 2 下载失败
     */
    @ApiModelProperty(value = "下载状态 0 未下载 1 已下载 2 下载失败")
    private String dlStatus;

    /**
     * 人员姓名
     */
    @ApiModelProperty(value = "人员姓名")
    private String personName;

    /**
     * 卡操作
     */
    @ApiModelProperty(value = "操作类型 1 下发 2 挂失 3 解挂 4 注销 5 换卡")
    private String cardStatus;

}
