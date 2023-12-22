package com.aurine.cloudx.estate.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * (ProjectInfoSimplePageVo)
 *
 * @author xull
 * @version 1.0.0
 * @date 2020/9/10 16:50
 */
@Data
@ApiModel("项目简单分页查询结果视图")
public class ProjectInfoSimplePageVo {
    /**
     * 项目ID,关联pgixx.sys_dept.dept_id
     */
    @ApiModelProperty(value = "项目ID,关联pgixx.sys_dept.dept_id")
    private Integer projectId;
    /**
     * 项目名称
     */
    @ApiModelProperty(value = "项目名称")
    private String projectName;
    /**
     * 项目简称
     */
    @ApiModelProperty(value = "项目简称")
    private String shortName;

    /**
     * 所属集团
     */
    @ApiModelProperty(value = "所属集团")
    private String companyId;
    /**
     * 所属集团名称
     */
    @ApiModelProperty(value = "所属集团名称")
    private String companyName;
    /**
     * 所属项目组
     */
    @ApiModelProperty(value = "所属项目组")
    private String projectGroupId;
    /**
     * 所属项目组
     */
    @ApiModelProperty(value = "所属项目组名称")
    private String projectGroupName;
    /**
     * 省编码
     */
    @ApiModelProperty(value = "省编码")
    private String provinceCode;
    /**
     * 市编码
     */
    @ApiModelProperty(value = "市编码")
    private String cityCode;
    /**
     * 县(区)编码
     */
    @ApiModelProperty(value = "县(区)编码")
    private String countyCode;
    /**
     * 街道
     */
    @ApiModelProperty(value = "街道")
    private String streetCode;
    /**
     * 详细地址
     */
    @ApiModelProperty(value = "详细地址")
    private String address;
    /**
     * 图片路径
     */
    @ApiModelProperty(value = "图片路径")
    private String picPath;
    /**
     * 经度，保留小数点后6位
     */
    @ApiModelProperty(value = "经度，保留小数点后6位")
    private Double lon;
    /**
     * 纬度，保留小数点后6位
     */
    @ApiModelProperty(value = "纬度，保留小数点后6位")
    private Double lat;
    /**
     * 距离
     */
    @ApiModelProperty("距离")
    private Double distance;
    /**
     * 是否已经登记
     */
    @ApiModelProperty("是否已经登记：1、是0、否")
    private String enable;
}
