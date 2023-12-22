

package com.aurine.cloudx.estate.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>房屋DTO</p>
 * @ClassName: ProjectHouseDTO
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020/5/22 17:22
 * @Copyright:
 */
@Data
public class ProjectHouseDTO {
private static final long serialVersionUID = 1L;
    /**
     * 楼栋ID
     */
    @ApiModelProperty(value="楼栋ID")
    private String buildingId;
    /**
     * 单元ID
     */
    @ApiModelProperty(value="单元ID")
    private String unitId;
    /**
     * 房屋ID
     */
    @ApiModelProperty(value="房屋ID")
    private String houseId;
    /**
     * 楼栋ID
     */
    @ApiModelProperty(value="楼栋ID")
    private String buildingName;
    /**
     * 单元ID
     */
    @ApiModelProperty(value="单元ID")
    private String unitName;
    /**
     * 房屋ID
     */
    @ApiModelProperty(value="房屋ID")
    private String houseName;

    /**
     * 房屋编码
     */
    @ApiModelProperty(value="房屋编码")
    private String houseCode;

    /**
     * 审核状态
     */
    @ApiModelProperty(value="审核状态 1 待审核 2 已通过 9 未通过 字典wechat_audit_status")
    private String auditStatus;

    /**
     * 居住人数
     */
    @ApiModelProperty(value="居住人数")
    private int personNum;

    /**
     * 是否有人入住
     */
    @ApiModelProperty(value="是否有人入住")
    private boolean havePerson;

    }
