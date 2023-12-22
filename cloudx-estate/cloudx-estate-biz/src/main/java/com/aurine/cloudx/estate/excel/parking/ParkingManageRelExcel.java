package com.aurine.cloudx.estate.excel.parking;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName: ParkingManageRelExcel
 * @author: 王良俊 <>
 * @date: 2020年08月24日 下午06:46:58
 * @Copyright:
 */
@Data
public class ParkingManageRelExcel {

    /**
     * 车场名称 *
     */
    @ApiModelProperty(value = "车场名称 *")
    private String parkName;

    /**
     * 车场区域名 *
     */
    @ApiModelProperty(value = "车场区域名 *")
    private String regionName;

    /**
     * 车位号 *
     */
    @ApiModelProperty(value = "车位号 *")
    private String placeCode;

    /**
     * 关联房屋—楼栋 中文 楼栋-单元-房号
     */
    @ExcelProperty()
    @ApiModelProperty(value = "关联房屋—楼栋 中文")
    private String houseAddressCh;

    /**
     * 姓名 *
     */
    @ApiModelProperty(value = "姓名 *")
    private String personName;

    /**
     * 手机号 *
     */
    @ApiModelProperty(value = "手机号 *")
    private String telephone;

    /**
     * 性别 中文 （公安必填）
     */
    @ApiModelProperty(value = "性别 中文")
    private String genderCh;

    /**
     * 证件类型 中文
     */
    @ApiModelProperty(value = "证件类型 中文")
    private String credentialTypeCh;

    /**
     * 证件号
     */
    @ApiModelProperty(value = "证件号")
    private String credentialNo;

    /**
     * 归属类型 * 中文
     */
    @ApiModelProperty(value = "归属类型 * 中文")
    private String relTypeCh;

    /**
     * 租赁时间 * 中文 effTime 起始时间 expTime 结束时间 只有归属是租赁类型的时候才是必填
     */
    @ApiModelProperty(value = "租赁时间")
    private String rentTime;

    /**
     * 启用时间
     */
    @ApiModelProperty(value = "启用时间")
    private String checkInTimeStr;

    /**
     * 委托代理人姓名
     */
    @ApiModelProperty(value = "委托代理人姓名")
    private String wtdlrxm;

    /**
     * 委托代理人电话
     */
    @ApiModelProperty(value = "委托代理人电话")
    private String dlrlxdh;

    /**
     * 委托代理人证件类型
     */
    @ApiModelProperty(value = "委托代理人证件类型 中文")
    private String dlrzjlxCh;

    /**
     * 委托代理人证件号码
     */
    @ApiModelProperty(value = "委托代理人证件号码")
    private String dlrzjhm;

}
