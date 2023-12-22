package com.aurine.cloudx.open.origin.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @ClassName: ProjectHouseHistoryVo
 * @author: 王良俊
 * @date:  2020年05月09日 下午05:06:58
 * @Copyright:
*/
@Data
public class ProjectHouseHisRecordVo {

    /**
     * 序列
     */
    @TableId
    @ApiModelProperty(value = "序列")
    private Integer seq;
    /**
     * 房屋ID
     */
    @ApiModelProperty(value = "房屋ID")
    private String houseId;
    /**
     * 楼栋名称
     */
    @ApiModelProperty(value = "楼栋名称")
    private String buildingName;
    /**
     * 单元名称
     */
    @ApiModelProperty(value = "单元名称")
    private String unitName;
    /**
     * 房屋名称
     */
    @ApiModelProperty(value = "房屋名称")
    private String houseName;

    /**
     * 人员ID
     */
    @ApiModelProperty(value = "人员ID")
    private String personId;
    /**
     * 人屋关系 1 自住 2 租赁 3 民宿 4 其他,见通用字典house_people_rel
     */
    @ApiModelProperty(value = "人屋关系 1 自住 2 租赁 3 民宿 4 其他,见通用字典house_people_rel")
    private String housePeopleRel;
    /**
     * 住户类型 1 业主（产权人） 2 家属 3 租客
     */
    @ApiModelProperty(value = "住户类型 1 业主（产权人） 2 家属 3 租客 见通用字典household_type")
    private String householdType;
    /**
     * 家庭关系 见通用字典项member_type
     */
    @ApiModelProperty(value = "家庭关系 1: 配偶 2: 子 3: 女 4: 孙子、孙女或外孙子、外孙女 5: 父母 6: 祖父母或外祖父母 7: 兄、弟、姐、妹 8: 其他 见通用字典项member_type")
    private String memberType;
    /**
     * 0 迁出 1 迁入
     */
    @ApiModelProperty(value = "0 迁出 1 迁入")
    private String action;
    /**
     * 入住时间
     */
    @ApiModelProperty(value = "入住时间")
    private LocalDateTime checkInTime;
    /**
     * 生效时间
     */
    @ApiModelProperty(value = "生效时间")
    private LocalDateTime effTime;
    /**
     * 失效时间
     */
    @ApiModelProperty(value = "失效时间")
    private LocalDateTime expTime;
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;
    /**
     * 操作人
     */
    @ApiModelProperty(value = "操作人")
    private Integer operator;
    /**
     * 记录创建时间
     */
    @ApiModelProperty(value = "记录创建时间")
    private String createTime;
    /**
     * 住户姓名
     */
    @ApiModelProperty(value = "住户姓名")
    private String personName;

    /**
     * 手机号
     */
    @ApiModelProperty(value = "手机号")
    private String telephone;


}