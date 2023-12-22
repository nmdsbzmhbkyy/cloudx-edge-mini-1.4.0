

package com.aurine.cloudx.open.origin.entity;

import com.aurine.cloudx.open.common.entity.base.OpenBasePo;
import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 记录项目卡资源信息，供辖区内已开放通行权限的卡识别设备下载
 *
 * @author 王良俊
 * @date 2020-05-22 11:21:18
 */
@Data
@TableName("project_card")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "记录项目卡资源信息，供辖区内已开放通行权限的卡识别设备下载")
public class ProjectCard extends OpenBasePo<ProjectCard> {
    private static final long serialVersionUID = 1L;

    /**
     * 序列，自增
     */
    @ApiModelProperty(value = "序列，自增")
    private Integer seq;
    /**
     * 卡片uid
     */
    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "卡片uid")
    private String cardId;

    /**
     * 卡号
     */
    @ApiModelProperty(value = "卡号")
    private String cardNo;
    /**
     * 卡号
     */
    @ApiModelProperty(value = "卡号第三方编码")
    private String cardCode;

    /**
     * 卡片状态  1 正常 2 挂失
     */
    @ApiModelProperty(value = "卡片状态  1 正常 2 挂失")
    private String cardStatus;

    /**
     * 人员类型 1 住户 2 员工 3 访客
     */
    @ApiModelProperty(value = "人员类型 1 住户 2 员工 3 访客")
    private String personType;
    /**
     * 人员id，根据人员类型取对应表id。卡未使用时为空
     */
    @ApiModelProperty(value = "人员id，根据人员类型取对应表id。卡未使用时为空")
    private String personId;
    /**
     * 状态 0 未使用 1 使用中 2 冻结
     */
    @ApiModelProperty(value = "状态 0 未使用 1 使用中 2 冻结")
    private String status;
}
