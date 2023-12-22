package com.aurine.cloudx.open.common.entity.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.aurine.cloudx.open.common.entity.base.OpenBaseVo;
import com.aurine.cloudx.open.common.validate.annotation.SizeCustom;
import com.aurine.cloudx.open.common.validate.group.InsertGroup;
import com.aurine.cloudx.open.common.validate.group.UpdateGroup;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 项目信息Vo
 *
 * @author : Qiu
 * @date : 2021 12 28 16:35
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "项目信息Vo")
public class ProjectInfoVo extends OpenBaseVo {

    /**
     * 序列
     */
    @JsonProperty("seq")
    @JSONField(name = "seq")
    @ApiModelProperty(value = "序列", hidden = true)
    @Null(message = "序列（seq）需要为空")
    private Integer seq;

    /**
     * 项目id,关联pigxx.sys_dept.dept_id
     */
    @JsonProperty("projectId")
    @JSONField(name = "projectId")
    @ApiModelProperty(value = "项目id,关联pigxx.sys_dept.dept_id")
    @Null(message = "项目id（projectId）新增时需要为空", groups = {InsertGroup.class})
    @NotNull(message = "项目id（projectId）不能为空", groups = {UpdateGroup.class})
    @Max(value = Integer.MAX_VALUE, message = "项目id（projectId）数值过大")
    private Integer projectId;

    /**
     * 项目UUID
     */
    @JsonProperty("projectUUID")
    @JSONField(name = "projectUUID")
    @ApiModelProperty(value = "项目UUID")
    @Size(max = 64, message = "项目UUID（projectUUID）长度不能超过64")
    private String projectUUID;

    /**
     * 第三方项目UUID，如果是入云要取这个字段的数据作为项目UUID
     */
    @JsonProperty("projectCode")
    @JSONField(name = "projectCode")
    @ApiModelProperty(value = "第三方项目UUID")
    @Size(max = 64, message = "第三方项目UUID（projectCode）长度不能超过64")
    private String projectCode;

    /**
     * 项目名称
     */
    @JsonProperty("projectName")
    @JSONField(name = "projectName")
    @ApiModelProperty(value = "项目名称")
    @NotBlank(message = "项目名称（projectName）不能为空", groups = {InsertGroup.class})
    @SizeCustom(message = "项目名称（projectName）长度不能小于1")
    @Size(max = 255, message = "项目名称（projectName）长度不能超过255")
    private String projectName;

    /**
     * 项目简称
     */
    @JsonProperty("shortName")
    @JSONField(name = "shortName")
    @ApiModelProperty(value = "项目简称")
    @Size(max = 32, message = "项目简称（shortName）长度不能超过32")
    private String shortName;

    /**
     * 项目类型 1 正式 2 测试 3 演示
     */
    @JsonProperty("projectType")
    @JSONField(name = "projectType")
    @ApiModelProperty(value = "项目类型 1 正式 2 测试 3 演示")
    @NotBlank(message = "项目类型（projectType）不能为空", groups = {InsertGroup.class})
    @SizeCustom(message = "项目类型（projectType）长度不能小于1")
    @Size(max = 5, message = "项目类型（projectType）长度不能超过5")
    private String projectType;

    /**
     * 联系人
     */
    @JsonProperty("contactPerson")
    @JSONField(name = "contactPerson")
    @ApiModelProperty(value = "联系人")
    @Size(max = 64, message = "联系人（contactPerson）长度不能超过64")
    private String contactPerson;

    /**
     * 联系电话
     */
    @JsonProperty("contactPhone")
    @JSONField(name = "contactPhone")
    @ApiModelProperty(value = "联系电话")
    @Size(max = 32, message = "联系电话（contactPhone）长度不能超过32")
    private String contactPhone;

    /**
     * 省编码
     */
    @JsonProperty("provinceCode")
    @JSONField(name = "provinceCode")
    @ApiModelProperty(value = "省编码")
    @NotBlank(message = "省编码（provinceCode）不能为空", groups = {InsertGroup.class})
    @SizeCustom(message = "省编码（provinceCode）长度不能小于1")
    @Size(max = 32, message = "省编码（provinceCode）长度不能超过32")
    private String provinceCode;

    /**
     * 市编码
     */
    @JsonProperty("cityCode")
    @JSONField(name = "cityCode")
    @ApiModelProperty(value = "市编码")
    @NotBlank(message = "市编码（cityCode）不能为空", groups = {InsertGroup.class})
    @SizeCustom(message = "市编码（cityCode）长度不能小于1")
    @Size(max = 32, message = "市编码（cityCode）长度不能超过32")
    private String cityCode;

    /**
     * 县(区)编码
     */
    @JsonProperty("countyCode")
    @JSONField(name = "countyCode")
    @ApiModelProperty(value = "县(区)编码")
    @NotBlank(message = "县(区)编码（countyCode）不能为空", groups = {InsertGroup.class})
    @SizeCustom(message = "县(区)编码（countyCode）长度不能小于1")
    @Size(max = 32, message = "县(区)编码（countyCode）长度不能超过32")
    private String countyCode;

    /**
     * 街道
     */
    @JsonProperty("streetCode")
    @JSONField(name = "streetCode")
    @ApiModelProperty(value = "街道")
    @Size(max = 32, message = "街道（streetCode）长度不能超过32")
    private String streetCode;

    /**
     * 详细地址
     */
    @JsonProperty("address")
    @JSONField(name = "address")
    @ApiModelProperty(value = "详细地址")
    @Size(max = 255, message = "详细地址（address）长度不能超过255")
    private String address;

    /**
     * 建筑面积
     */
    @JsonProperty("acreage")
    @JSONField(name = "acreage")
    @ApiModelProperty(value = "建筑面积")
    @Digits(integer = 10, fraction = 2, message = "建筑面积（acreage）格式不正确，整数最多10位，小数最多2位")
    @DecimalMin(value = "0.00", message = "建筑面积（acreage）格式不正确，不能小于0")
    private BigDecimal acreage;

    /**
     * 图片路径
     */
    @JsonProperty("picPath")
    @JSONField(name = "picPath")
    @ApiModelProperty(value = "图片路径")
    @Size(max = 255, message = "图片路径（picPath）长度不能超过255")
    private String picPath;

    /**
     * 经度，保留小数点后6位
     */
    @JsonProperty("lon")
    @JSONField(name = "lon")
    @ApiModelProperty(value = "经度，保留小数点后6位")
    @Digits(integer = 9, fraction = 6, message = "经度（lon）格式不正确，整数最多9位，小数最多6位")
    private Double lon;

    /**
     * 纬度，保留小数点后6位
     */
    @JsonProperty("lat")
    @JSONField(name = "lat")
    @ApiModelProperty(value = "纬度，保留小数点后6位")
    @Digits(integer = 9, fraction = 6, message = "纬度（lat）格式不正确，整数最多9位，小数最多6位")
    private Double lat;

    /**
     * 高度，保留小数点后1位
     */
    @JsonProperty("alt")
    @JSONField(name = "alt")
    @ApiModelProperty(value = "高度，保留小数点后1位")
    @Digits(integer = 6, fraction = 1, message = "高度（alt）格式不正确，整数最多6位，小数最多1位")
    private Double alt;

    /**
     * 坐标
     */
    @JsonProperty("gisArea")
    @JSONField(name = "gisArea")
    @ApiModelProperty(value = "坐标")
    @Size(max = 255, message = "坐标（gisArea）长度不能超过255")
    private String gisArea;

    /**
     * 坐标系代码
     */
    @JsonProperty("gisType")
    @JSONField(name = "gisType")
    @ApiModelProperty(value = "坐标系代码")
    @Size(max = 5, message = "坐标系代码（gisType）长度不能超过5")
    private String gisType;

    /**
     * 所属派出所
     */
    @JsonProperty("policeStation")
    @JSONField(name = "policeStation")
    @ApiModelProperty(value = "所属派出所")
    @Size(max = 32, message = "所属派出所（policeStation）长度不能超过32")
    private String policeStation;

    /**
     * 物业公司
     */
    @JsonProperty("propertyCompany")
    @JSONField(name = "propertyCompany")
    @ApiModelProperty(value = "物业公司")
    @Size(max = 32, message = "物业公司（propertyCompany）长度不能超过32")
    private String propertyCompany;

    /**
     * 物业负责人
     */
    @JsonProperty("propertyPrincipal")
    @JSONField(name = "propertyPrincipal")
    @ApiModelProperty(value = "物业负责人")
    @Size(max = 32, message = "物业负责人（propertyPrincipal）长度不能超过32")
    private String propertyPrincipal;

    /**
     * 物业组织机构代码
     */
    @JsonProperty("propertyOrgCode")
    @JSONField(name = "propertyOrgCode")
    @ApiModelProperty(value = "物业组织机构代码")
    @Size(max = 32, message = "物业组织机构代码（propertyOrgCode）长度不能超过32")
    private String propertyOrgCode;

    /**
     * 物业公司电话
     */
    @JsonProperty("propertyPhone")
    @JSONField(name = "propertyPhone")
    @ApiModelProperty(value = "物业公司电话")
    @Size(max = 20, message = "物业公司电话（propertyPhone）长度不能超过20")
    private String propertyPhone;

    /**
     * 物业公司地址
     */
    @JsonProperty("propertyAddress")
    @JSONField(name = "propertyAddress")
    @ApiModelProperty(value = "物业公司地址")
    @Size(max = 128, message = "物业公司地址（propertyAddress）长度不能超过128")
    private String propertyAddress;

    /**
     * 公安机关编码
     */
    @JsonProperty("policeCode")
    @JSONField(name = "policeCode")
    @ApiModelProperty(value = "公安机关编码")
    @Size(max = 32, message = "公安机关编码（policeCode）长度不能超过32")
    private String policeCode;

    /**
     * 审核状态
     */
    @JsonProperty("auditStatus")
    @JSONField(name = "auditStatus")
    @ApiModelProperty(value = "审核状态 1 待审核 2 已通过 9 未通过 字典wechat_audit_status")
    @Size(max = 1, message = "审核状态（auditStatus）长度不能超过1")
    private String auditStatus;

    /**
     * 第三方项目编号
     */
    @JsonProperty("thirdPartyNo")
    @JSONField(name = "thirdPartyNo")
    @ApiModelProperty(value = "第三方项目编号")
    @Size(max = 64, message = "第三方项目编号（thirdPartyNo）长度不能超过64")
    private String thirdPartyNo;

    /**
     * 接口版本号
     */
    @JsonProperty("appVersion")
    @JSONField(name = "appVersion")
    @ApiModelProperty(value = "接口版本号")
    @Size(max = 64, message = "接口版本号（appVersion）长度不能超过64")
    private String appVersion;

    /**
     * 备注
     */
    @JsonProperty("remark")
    @JSONField(name = "remark")
    @ApiModelProperty(value = "备注")
    @Size(max = 128, message = "备注（remark）长度不能超过128")
    private String remark;

    /**
     * 所属集团
     */
    @JsonProperty("companyId")
    @JSONField(name = "companyId")
    @ApiModelProperty(value = "所属集团")
    @Max(value = Integer.MAX_VALUE, message = "所属集团（companyId）数值过大")
    private Integer companyId;

    /**
     * 所属项目组
     */
    @JsonProperty("projectGroupId")
    @JSONField(name = "projectGroupId")
    @ApiModelProperty(value = "所属项目组")
    @Max(value = Integer.MAX_VALUE, message = "所属项目组（projectGroupId）数值过大")
    private Integer projectGroupId;

    /**
     * 过期时间
     */
    @JsonProperty("expTime")
    @JSONField(name = "expTime", format = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "过期时间")
    private LocalDateTime expTime;

    /**
     * 审核不通过原因
     */
    @JsonProperty("auditReason")
    @JSONField(name = "auditReason")
    @ApiModelProperty(value = "审核不通过原因")
    @Size(max = 128, message = "审核不通过原因（auditReason）长度不能超过128")
    private String auditReason;

    /**
     * 出入口数量
     */
    @JsonProperty("entraExitNum")
    @JSONField(name = "entraExitNum")
    @ApiModelProperty(value = "出入口数量")
    @Max(value = Integer.MAX_VALUE, message = "出入口数量（entraExitNum）数值过大")
    private Integer entraExitNum;

    /**
     * 是否启用
     */
    @JsonProperty("status")
    @JSONField(name = "status")
    @ApiModelProperty(value = "是否启用:0否1是")
    @NotBlank(message = "是否启用（status）不能为空", groups = {InsertGroup.class})
    @SizeCustom(message = "是否启用（status）长度不能小于1")
    @Size(max = 1, message = "是否启用（status）长度不能超过1")
    private String status;

    /**
     * 地址编码:公安标准编码
     */
    @JsonProperty("locationCode")
    @JSONField(name = "locationCode")
    @ApiModelProperty(value = "地址编码:公安标准编码")
    @Size(max = 32, message = "地址编码:公安标准编码（locationCode）长度不能超过32")
    private String locationCode;

    /**
     * 同步类型，1 使用边缘网关数据 2 使用云端数据
     */
    @JsonProperty("syncType")
    @JSONField(name = "syncType")
    @ApiModelProperty(value = "同步类型，1 使用边缘网关数据 2 使用云端数据")
    @Max(value = 1, message = "同步类型（syncType）长度不能超过1")
    private Integer syncType;

    /**
     * 云端项目uuid
     */
    @JsonProperty("cloudProjectUid")
    @JSONField(name = "cloudProjectUid")
    @ApiModelProperty(value = "云端项目uuid")
    @Size(max = 32, message = "云端项目uuid（cloudProjectUid）长度不能超过32")
    private String cloudProjectUid;

    /**
     * 边缘网关的设备编号
     */
    @JsonProperty("edgeDeviceId")
    @JSONField(name = "edgeDeviceId")
    @ApiModelProperty(value = "边缘网关的设备编号")
    @Size(max = 64, message = "边缘网关的设备编号（edgeDeviceId）长度不能超过64")
    private String edgeDeviceId;
}
