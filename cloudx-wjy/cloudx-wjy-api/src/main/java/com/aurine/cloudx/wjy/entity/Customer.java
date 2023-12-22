package com.aurine.cloudx.wjy.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 客户映射实体
 * @author ：huangjj
 * @date ：2021/4/14
 * @description：保存4.0系统和我家云客户映射
 */
@TableName("wjy_customer")
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "客户映射实体")
public class Customer extends Model<Customer> {
    /**
     * 自增序列
     */
    @TableId
    @ApiModelProperty(value = "自增序列")
    private Integer seq;
    /**
     * 我家云客户id
     */
    @ApiModelProperty(value = "我家云客户id")
    private String wjyCusId;
    /**
     * 项目id
     */
    @ApiModelProperty(value = "项目id")
    private Integer projectId;
    /**
     * 客户名称
     */
    @ApiModelProperty(value = "客户名称")
    private String name;
    /**
     * 4.0客户id
     */
    @ApiModelProperty(value = "4.0客户id")
    private String cusId;
    /**
     * 客户手机号码
     */
    @ApiModelProperty(value = "客户手机号码")
    private String phone;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;
}