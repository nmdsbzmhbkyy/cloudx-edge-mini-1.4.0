package com.aurine.cloudx.wjy.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ：huangjj
 * @date ：Created in 2021/4/14 10:43
 * @description：客户信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "客户信息-我家云标准接口数据")
public class CustomerStandardVo {
    /**
     * 客户名称（长度小于100）,必填
     */
    @ApiModelProperty(value = "客户名称（长度小于100）")
    private String name;
    /**
     * 客户手机号码（长度小于50）（P和S类型客户手机号必填，其他类型非必填）
     */
    @ApiModelProperty(value = "客户手机号码（长度小于50）")
    private String phone;
    /**
     * 客户类型（P个人，E企业，G政府机构，S个体户，O其他企业，T临时），必填
     */
    @ApiModelProperty(value = "客户类型（P个人，E企业，G政府机构，S个体户，O其他企业，T临时）")
    private String type;
    /**
     *证件类型 (0101:大陆身份证,0102:军官证,0103:港澳台,0104:护照,0111:台胞证,0112:香港身份证,0105:营业执照,0106:税务登记号,0107:组织结构代码,0108:社会信用号码,0199:其它)
     */
    @ApiModelProperty(value = "证件类型 (0101:大陆身份证,0102:军官证,0103:港澳台,0104:护照,0111:台胞证,0112:香港身份证,0105:营业执照,0106:税务登记号,0107:组织结构代码,0108:社会信用号码,0199:其它)")
    private String certType;
    /**
     * 证件号码（身份证需校验格式）（长度小于300）
     */
    @ApiModelProperty(value = "证件号码（身份证需校验格式）（长度小于300）")
    private String certNo;
    /**
     * 联系电话（长度小于50）
     */
    @ApiModelProperty(value = "联系电话（长度小于50）")
    private String telephone;
    /**
     * 性别（0：男，1：女）（缺省默认男）
     */
    @ApiModelProperty(value = "性别（0：男，1：女）（缺省默认男）")
    private String sex;
    /**
     * 民族（需校验是否存在）
     */
    @ApiModelProperty(value = "民族")
    private String nation;
    /**
     * 生日（格式：yyyy-MM-dd）
     */
    @ApiModelProperty(value = "生日（格式：yyyy-MM-dd）")
    private String birthday;
    /**
     * 电子邮箱（需校验格式）
     */
    @ApiModelProperty(value = "电子邮箱")
    private String email;
    /**
     * 客户描述（长度小于256）
     */
    @ApiModelProperty(value = "客户描述（长度小于256）")
    private String description;
    /**
     * 客户性质（-1：无，0：内部客户，1：外部客户，2：关联客户）
     */
    @ApiModelProperty(value = "客户性质（-1：无，0：内部客户，1：外部客户，2：关联客户）")
    private String category;
    /**
     * 客户财务编码（长度小于50）
     */
    @ApiModelProperty(value = "客户财务编码（长度小于50）")
    private String authCode;
    /**
     * 省（例：北京省）
     */
    @ApiModelProperty(value = "省（例：北京省）")
    private String province;
    /**
     * 市（例：北京市）
     */
    @ApiModelProperty(value = "市（例：北京市）")
    private String city;
    /**
     * 区（例：东城区）
     */
    @ApiModelProperty(value = "区（例：东城区）")
    private String area;
    /**
     * 具体街道（例: 三里屯705）（长度小于180）
     */
    @ApiModelProperty(value = "具体街道（例: 三里屯705）（长度小于180）")
    private String residenceAddress;
    /**
     * 品牌（长度小于64）
     */
    @ApiModelProperty(value = "品牌（长度小于64）")
    private String brand;
    /**
     * 纳税人类型（0：一般纳税人，1：小规模纳税人）
     */
    @ApiModelProperty(value = "纳税人类型（0：一般纳税人，1：小规模纳税人）")
    private String taxpayerType;
    /**
     * 纳税人识别码（长度小于32）
     */
    @ApiModelProperty(value = "纳税人识别码（长度小于32）")
    private String specInvCountryTaxCode;
    /**
     * 税务单位地址（小于100）
     */
    @ApiModelProperty(value = "税务单位地址（小于100）")
    private String businessAddress;
    /**
     * 税务电话号码（小于100）
     */
    @ApiModelProperty(value = "税务电话号码（小于100）")
    private String businessPhone;
    /**
     * 开户行（小于100）
     */
    @ApiModelProperty(value = "开户行（小于100）")
    private String bankName;
    /**
     * 开户行账号（小于100）
     */
    @ApiModelProperty(value = "开户行账号（小于100）")
    private String bankCount;
    /**
     * 开票类型（ 1 普通发票 ，2 专用发票，3 电子发票）
     */
    @ApiModelProperty(value = "开票类型（ 1 普通发票 ，2 专用发票，3 电子发票）")
    private String billingType;
    /**
     * 更新后客户姓名
     */
    @ApiModelProperty(value = "更新后客户姓名")
    private String newName;
    /**
     * 更新后客户手机号
     */
    @ApiModelProperty(value = "更新后客户手机号")
    private String newPhone;
    /**
     * 长度32，源ID，必填
     */
    @ApiModelProperty(value = "长度32，源ID")
    private String sourceID;
    /**
     * 长度32，来源系统，必填
     */
    @ApiModelProperty(value = "长度32，来源系统")
    private String sourceSystem;
    /**
     * 项目id
     */
    @ApiModelProperty(value = "项目id")
    private Integer projectId;
}