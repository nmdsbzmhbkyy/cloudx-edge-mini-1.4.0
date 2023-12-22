package com.aurine.cloudx.open.origin.entity;

import com.aurine.cloudx.open.common.entity.base.OpenBasePo;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 员工通知下发对象设置
 *
 * @author guhl@aurine.cn
 * @date 2020-07-06 11:17:34
 */
@Data
@TableName("project_staff_notice_object")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "员工通知下发对象设置")
public class ProjectStaffNoticeObject extends OpenBasePo<ProjectStaffNoticeObject> {
    private static final long serialVersionUID = 1L;

    /**
     * 序列，自增
     */
    @TableId
    @ApiModelProperty(value = "序列，自增")
    private Integer seq;

    /**
     * 信息id，关联project_notice.notice_id
     */
    @ApiModelProperty(value = "信息id，关联project_notice.notice_id")
    private String noticeId;
    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
    private String userId;
    /**
     * 用户类型
     */
    @ApiModelProperty(value = "用户类型")
    private String userType;

    /**
     * 0 未读 1 已读
     */
    @ApiModelProperty(value = "0 未读 1 已读")
    private String status;
}
