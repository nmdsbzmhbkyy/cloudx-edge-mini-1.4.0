package com.aurine.cloudx.push.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 我的房屋-添加房屋
 *
 * @ClassName: HouseAddEntity
 * @author: 邹宇
 * @date: 2021-8-27 14:06:49
 * @Copyright:
 */
@Data
@ApiModel(value = "访客预约来访审核")
public class VisitAuditRemarkEntity extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 访客姓名
     */
    @ApiModelProperty(value = "访客姓名", required = true)
    private String visitorName;

    /**
     * 来访日期
     */
    @ApiModelProperty(value = "来访日期", required = true)
    private String visitDate;

    /**
     * 来访事由
     */
    @ApiModelProperty(value = "来访事由", required = true)
    private String visitReason;

}
