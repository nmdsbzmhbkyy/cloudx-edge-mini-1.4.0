package com.aurine.cloudx.open.origin.entity;

import com.aurine.cloudx.open.origin.deserializer.BooleanToStringDeserializer;
import com.aurine.cloudx.open.origin.deserializer.DataTypeDeserializer;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 以产品为维度进行平台设备参数配置项管理(SysProductParamCategory)表实体类
 *
 * @author makejava
 * @since 2020-12-15 10:24:57
 */
@Data
@TableName("sys_product_service")
@ApiModel(value = "以产品为维度进行平台设备参数配置项管理(SysProductService)")
public class SysProductService extends Model<SysProductService> {

    private static final long serialVersionUID = -13641393925886939L;

    /**
     * 序列，自增
     */
    @TableId
    @ApiModelProperty(value = "序列，自增")
    private Integer seq;


    /**
     * 产品id，设备参数配置项的唯一标识
     */
    @ApiModelProperty(value = "产品id，设备参数配置项的唯一标识")
    @JacksonInject("productId")
    private String productId;


    /**
     * 服务ID
     */
    @ApiModelProperty(value = "服务ID")
    @JacksonInject("serviceId")
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
    @JsonProperty("property_name")
    private String paramId;


    /**
     * 参数名称
     */
    @ApiModelProperty(value = "参数名称")
    @JsonProperty("description")
    private String paramName;


    /**
     * 是否必填 1 是 0 否
     */
    @ApiModelProperty(value = "是否必填 1 是 0 否")
    @JsonProperty("required")
    @JsonDeserialize(using = BooleanToStringDeserializer.class)
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
    @JsonProperty("data_type")
    @JsonDeserialize(using = DataTypeDeserializer.class)
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SysProductService that = (SysProductService) o;
        return Objects.equals(productId, that.productId)
                && Objects.equals(serviceId, that.serviceId)
                && Objects.equals(getParamId(), that.getParamId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), productId, serviceId, getParamId());
    }
}