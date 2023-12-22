package com.aurine.cloudx.open.common.entity.entity;

import com.aurine.cloudx.open.common.entity.base.OpenBasePo;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 开放平台应用信息
 *
 * @author : Qiu
 * @date : 2022 01 20 10:47
 */

@Data
@TableName("open_app_info")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "开放平台应用管理表")
public class OpenAppInfo extends OpenBasePo<OpenAppInfo> {

    /**
     * 序列号，自增
     */
    @ApiModelProperty(value = "序列号，自增")
    private Integer seq;

    /**
     * 应用UUID，uuid，标识内部唯一
     */
    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "应用UUID，uuid，标识内部唯一")
    private String appUUID;

    /**
     * 应用id，对外提供的应用id
     */
    @ApiModelProperty(value = "应用id，对外提供的应用id")
    private String appId;

    /**
     * 应用名称
     */
    @ApiModelProperty(value = "应用名称")
    private String appName;

    /**
     * 应用描述
     */
    @ApiModelProperty(value = "应用描述")
    private String appDesc;

    /**
     * 应用类型（预留字段）
     */
    @ApiModelProperty(value = "应用类型（预留字段）")
    private String appType;

    /**
     * 隐藏projectId在swagger和MybatisPlus上的映射
     * 目的是为了覆盖父类（OpenBasePo）对象的projectId属性
     * 因为本Po对象对应的数据库表（open_app_info）没有projectId这个字段
     */
    @ApiModelProperty(value = "项目id", hidden = true)
    @TableField(exist = false)
    private Integer projectId;
}
