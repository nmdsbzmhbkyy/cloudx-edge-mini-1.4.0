

package com.aurine.cloudx.estate.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 房屋人员变更日志表
 *
 * @author 王伟
 * @date 2020-05-13 17:02:29
 */
@Data
@TableName("project_house_person_change_his")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "房屋人员变更日志表")
public class ProjectHousePersonChangeHis extends Model<ProjectHousePersonChangeHis> {
private static final long serialVersionUID = 1L;

    /**
     * 序列
     */
    @TableId
    @ApiModelProperty(value="序列")
    private Integer seq;
    /**
     * 房屋ID
     */
    @ApiModelProperty(value="房屋ID")
    private String houseId;

    /**
     * 人员ID
     */
    @ApiModelProperty(value="人员ID")
    private String personId;

    /**
     * 人员名称
     */
    @ApiModelProperty(value="人员名称")
    private String personName;

    /**
     * 人屋关系 1 自住 2 租赁 3 民宿 4 其他,见通用字典house_people_rel
     */
    @ApiModelProperty(value="人屋关系 1 自住 2 租赁 3 民宿 4 其他,见通用字典house_people_rel")
    private String housePeopleRel;
    /**
     * 住户类型 1 业主（产权人） 2 家属 3 租客
     */
    @ApiModelProperty(value="住户类型 1 业主（产权人） 2 家属 3 租客 见通用字典household_type")
    private String householdType;
    /**
     * 家庭关系 见通用字典项member_type
     */
    @ApiModelProperty(value="家庭关系 1: 配偶 2: 子 3: 女 4: 孙子、孙女或外孙子、外孙女 5: 父母 6: 祖父母或外祖父母 7: 兄、弟、姐、妹 8: 其他 见通用字典项member_type")
    private String memberType;
    /**
     * 0 迁出 1 迁入
     */
    @ApiModelProperty(value="0 迁出 1 迁入 2 审核迁出 3 审核迁入")
    private String action;
    /**
     * 入住时间
     */
    @ApiModelProperty(value="入住时间")
    private LocalDateTime checkInTime;
    /**
     * 生效时间
     */
    @ApiModelProperty(value="生效时间")
    private LocalDateTime effTime;
    /**
     * 失效时间
     */
    @ApiModelProperty(value="失效时间")
    private LocalDateTime expTime;
    /**
     * 备注
     */
    @ApiModelProperty(value="备注")
    private String remark;

    /**
     * 操作人
     */
    @ApiModelProperty(value="操作人")
    @TableField(fill = FieldFill.INSERT)
    private Integer operator;


    }
