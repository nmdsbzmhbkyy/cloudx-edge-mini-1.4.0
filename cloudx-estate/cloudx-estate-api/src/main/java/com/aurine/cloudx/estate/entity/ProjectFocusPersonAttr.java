package com.aurine.cloudx.estate.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 重点人员拓展信息表(ProjectFocusPersonAttr)表实体类
 *
 * @author 王良俊
 * @since 2020-08-18 09:06:38
 */
@Data
@TableName("project_focus_person_attr")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "重点人员拓展信息表(ProjectFocusPersonAttr)")
public class ProjectFocusPersonAttr extends Model<ProjectFocusPersonAttr> {

    private static final long serialVersionUID = -83027838959547091L;


    /**
     * 人员ID,关联人员表的personId
     */
    @ApiModelProperty(value = "人员ID,关联人员表的personId")
    private String personId;


    /**
     * 重点人员管理类别，多值逗号分隔
     */
    @ApiModelProperty(value = "重点人员管理类别，多值逗号分隔")
    private String focusCategory;


    /**
     * 管理地一省
     */
    @ApiModelProperty(value = "管理地一省")
    private String province1;


    /**
     * 管理地一地市
     */
    @ApiModelProperty(value = "管理地一地市")
    private String city1;


    /**
     * 管理地一县（区）
     */
    @ApiModelProperty(value = "管理地一县（区）")
    private String county1;


    /**
     * 管理地一街道（乡镇）
     */
    @ApiModelProperty(value = "管理地一街道（乡镇）")
    private String street1;


    /**
     * 管理地一详细地址
     */
    @ApiModelProperty(value = "管理地一详细地址")
    private String address1;


    /**
     * 管理地一治安重点人员管理类别
     */
    @ApiModelProperty(value = "管理地一治安重点人员管理类别")
    private String focusCategory1;


    /**
     * 管理地一管控状态
     */
    @ApiModelProperty(value = "管理地一管控状态")
    private String status1;


    /**
     * 管理地一管控地联系方式
     */
    @ApiModelProperty(value = "管理地一管控地联系方式")
    private String phone1;


    /**
     * 管理地一管控事由
     */
    @ApiModelProperty(value = "管理地一管控事由")
    private String reason1;


    /**
     * 管理地一管控民警姓名
     */
    @ApiModelProperty(value = "管理地一管控民警姓名")
    private String policeName1;


    /**
     * 管理地一管控民警联系电话
     */
    @ApiModelProperty(value = "管理地一管控民警联系电话")
    private String policePhone1;


    /**
     * 管理地一管控民警证件类型
     */
    @ApiModelProperty(value = "管理地一管控民警证件类型")
    private String policeIdType1;


    /**
     * 管理地一管控民警证件号码
     */
    @ApiModelProperty(value = "管理地一管控民警证件号码")
    private String policeIdNo1;


    /**
     * 管理地二省
     */
    @ApiModelProperty(value = "管理地二省")
    private String province2;


    /**
     * 管理地二地市
     */
    @ApiModelProperty(value = "管理地二地市")
    private String city2;


    /**
     * 管理地二县（区）
     */
    @ApiModelProperty(value = "管理地二县（区）")
    private String county2;


    /**
     * 管理地二街道（乡镇）
     */
    @ApiModelProperty(value = "管理地二街道（乡镇）")
    private String street2;


    /**
     * 管理地二详细地址
     */
    @ApiModelProperty(value = "管理地二详细地址")
    private String address2;


    /**
     * 管理地二治安重点人员管理类别
     */
    @ApiModelProperty(value = "管理地二治安重点人员管理类别")
    private String focusCategory2;


    /**
     * 管理地二管控状态
     */
    @ApiModelProperty(value = "管理地二管控状态")
    private String status2;


    /**
     * 管理地二管控地联系方式
     */
    @ApiModelProperty(value = "管理地二管控地联系方式")
    private String phone2;


    /**
     * 管理地二管控事由
     */
    @ApiModelProperty(value = "管理地二管控事由")
    private String reason2;


    /**
     * 管理地二管控民警姓名
     */
    @ApiModelProperty(value = "管理地二管控民警姓名")
    private String policeName2;


    /**
     * 管理地二管控民警联系电话
     */
    @ApiModelProperty(value = "管理地二管控民警联系电话")
    private String policePhone2;


    /**
     * 管理地二管控民警证件类型
     */
    @ApiModelProperty(value = "管理地二管控民警证件类型")
    private String policeIdType2;


    /**
     * 管理地二管控民警证件号码
     */
    @ApiModelProperty(value = "管理地二管控民警证件号码")
    private String policeIdNo2;

    /**
     * 操作人
     */
    @ApiModelProperty(value = "操作人")
    private Integer operator;


    /**
     * 操作时间
     */
    @ApiModelProperty(value = "操作时间")
    private Date createTime;


    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;


}