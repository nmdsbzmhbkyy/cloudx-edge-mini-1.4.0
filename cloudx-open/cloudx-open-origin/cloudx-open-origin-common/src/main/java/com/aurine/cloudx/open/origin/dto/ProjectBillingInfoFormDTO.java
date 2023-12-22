package com.aurine.cloudx.open.origin.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * (ProjectBillingInfoFormVo) 账单查询vo
 *
 * @author xull
 * @since 2020/7/23 10:16
 */
@Data
@ApiModel(value = "账单查询传输对象")
public class ProjectBillingInfoFormDTO extends Page {
    private static final long serialVersionUID = 1L;
    /**
     * 开始时间
     */
    @ApiModelProperty("缴费时间")
    private String payDateString;

    /**
     * 缴费类型
     */
    @ApiModelProperty("缴费类型")
    private String feeType;
    /**
     * 房屋Id
     */
    @ApiModelProperty("房屋Id")
    private String houseId;

    /**
     * 账单id
     */
    @ApiModelProperty("账单id")
    private String billingNo;
    /**
     * 输入框搜索条件
     */
    @ApiModelProperty("输入框搜索条件(物业)")
    private String textString;

}
