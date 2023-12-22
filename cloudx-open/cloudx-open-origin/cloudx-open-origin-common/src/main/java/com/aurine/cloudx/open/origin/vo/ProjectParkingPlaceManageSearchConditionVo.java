

package com.aurine.cloudx.open.origin.vo;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>车位归属管理查询条件VO</p>
 *
 * @ClassName: ProjectParkingPlaceManageSearchConditionVo
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020/5/11 12:00
 * @Copyright:
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "车位")
public class ProjectParkingPlaceManageSearchConditionVo extends Model<ProjectParkingPlaceManageSearchConditionVo> {
    private static final long serialVersionUID = 1L;


    /**
     * 停车场Id
     */
    @ApiModelProperty(value = "停车场Id")
    private String parkId;

    /**
     * 车位号
     */
    @ApiModelProperty(value = "车位号")
    private String placeName;

    /**
     * 停车场区域名
     */
    @ApiModelProperty(value = "停车区域名")
    private String parkRegionName;

    /**
     * 姓名
     */
    @ApiModelProperty(value = "姓名")
    private String personName;

}
