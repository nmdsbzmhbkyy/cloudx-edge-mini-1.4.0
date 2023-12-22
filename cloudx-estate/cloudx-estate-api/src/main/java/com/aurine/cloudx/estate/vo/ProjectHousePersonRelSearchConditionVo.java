

package com.aurine.cloudx.estate.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * <p>住户查询条件VO</p>
 *
 * @ClassName: ProjectHousePersonRelSearchConditionVo
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020/5/11 10:07
 * @Copyright:
 */
@Data
@ApiModel(value = "住户查询条件")
public class ProjectHousePersonRelSearchConditionVo {
    private static final long serialVersionUID = 1L;

    /**
     * 单元名称
     */
    @ApiModelProperty(value = "单元名称")
    private String unitName;
    /**
     * 楼栋名称
     */
    @ApiModelProperty(value = "楼栋名称")
    private String buildingName;
    /**
     * 楼栋id
     */
    @ApiModelProperty(value = "楼栋Id")
    private String buildingId;

    @ApiModelProperty("房屋id")
    private String houseId;

    @ApiModelProperty("单元id")
    private String unitId;
    /**
     * 房屋name
     */
    @ApiModelProperty(value = "房屋名称")
    private String houseName;
    /**
     * 姓名
     */
    @ApiModelProperty(value = "姓名")
    private String personName;

    /**
     * 手机号
     */
    @ApiModelProperty(value = "手机号")
    private String phone;

    /**
     * 审核状态
     */
    @ApiModelProperty(value = "审核状态 1 待审核 2 已通过 9 未通过 字典wechat_audit_status")
    private String[] auditStatus;

    /**
     * 住户类型
     */
    @ApiModelProperty(value = "住户类型")
    private String  housePeopleRel;



    /**
     * 人员ID
     */
    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "人员ID", required = true)
    private String personId;
}
