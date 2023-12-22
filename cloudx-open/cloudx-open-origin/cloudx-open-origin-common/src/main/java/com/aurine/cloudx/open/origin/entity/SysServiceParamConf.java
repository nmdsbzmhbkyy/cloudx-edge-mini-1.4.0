package com.aurine.cloudx.open.origin.entity;

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
 * 平台设备参数定义表(SysDeviceParamConf)表实体类
 *
 * @author 王良俊
 * @since 2020-12-15 10:24:38
 */
@Data
@TableName("sys_service_param_conf")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "平台设备参数定义表(SysDeviceParamConf)")
public class SysServiceParamConf extends Model<SysServiceParamConf> {

    private static final long serialVersionUID = -98104699667780797L;

    /**
     * 序列，自增
     */
    @TableId
    @ApiModelProperty(value = "序列，自增")
    private Integer seq;


    /**
     * 参数类别id
     */
    @ApiModelProperty(value = "参数类别id")
    private String serviceId;


    /**
     * 参数类别名称
     */
    @ApiModelProperty(value = "参数类别名称")
    private String serviceName;


    /**
     * 参数id
     */
    @ApiModelProperty(value = "参数id")
    private String paramId;


    /**
     * 参数名称
     */
    @ApiModelProperty(value = "参数名称")
    private String paramName;


    /**
     * 是否必填 1 是 0 否
     */
    @ApiModelProperty(value = "是否必填 1 是 0 否")
    private String isMandatory;

    /**
     * 是否重启
     */
    @ApiModelProperty(value = "是否重启")
    private String isReboot;


    /**
     * 是否支持同步
     */
    @ApiModelProperty(value = "是否支持同步")
    private String isSync;


    /**
     * 是否可见（不可见则查询时忽略）
     */
    @ApiModelProperty(value = "是否可见 1可见 0不可见")
    private String isVisible ;


    /**
     * 是否支持修改
     */
    @ApiModelProperty(value = "是否支持修改")
    private String isModify;


    /**
     * 字段类型
     */
    @ApiModelProperty(value = "字段类型")
    private String columnType;


    /**
     * 默认值
     */
    @ApiModelProperty(value = "默认值")
    private String defaultValue;


    /**
     * 值域（正则表达式）
     */
    @ApiModelProperty(value = "值域")
    private String valueRange;


    /**
     * 错误提示（不符合正则表达式的错误提示）
     */
    @ApiModelProperty(value = "不符合正则表达式的错误提示")
    private String errorMsg;


    /**
     * 标签类型：输入框、下拉选择框、单选框.....
     */
    @ApiModelProperty(value = "标签类型")
    private String inputType;


    /**
     * 数据字典
     */
    @ApiModelProperty(value = "数据字典")
    private String dictMap;


    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;


    /**
     * 上级服务id
     */
    @ApiModelProperty(value = "上级服务id")
    private String parServId;



    /**
     * 上级的参数ID
     */
    @ApiModelProperty(value = "上级的参数ID")
    private String parParamId;


    /**
     * 服务层级，1为主服务 >1为子服务
     */
    @ApiModelProperty(value = "服务层级，1为主服务 >1为子服务")
    private Integer servLevel;


    /**
     * 租户id
     */
    @ApiModelProperty(value = "租户id")
    @TableField(value = "tenant_id")
    private Integer tenantId;


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