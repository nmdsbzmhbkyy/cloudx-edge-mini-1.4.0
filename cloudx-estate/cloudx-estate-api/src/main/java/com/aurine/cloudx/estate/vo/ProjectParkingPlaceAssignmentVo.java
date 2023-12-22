
package com.aurine.cloudx.estate.vo;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @ClassName: ProjectParkingPlaceAssignmentVo
 * @author: 王良俊
 * @date:  2020年05月09日 上午09:24:02
 * @Copyright:
*/
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "车位")
public class ProjectParkingPlaceAssignmentVo extends Model<ProjectParkingPlaceAssignmentVo> {
    private static final long serialVersionUID = 1L;

    /**
     * 类别
     */
    @ApiModelProperty(value = "车位")
    private final String type = "车位";

    /**
     * 归属类型 0 闲置 1 产权 2 租赁
     */
    @ApiModelProperty(value = "归属类型 0 闲置 1 产权 2 租赁")
    private String relType;

    @ApiModelProperty(value = "用户名字")
    private String personName;

    @ApiModelProperty(value = "用户性别")
    private String gender;

    @ApiModelProperty(value = "用户证件号")
    private String credentialNo;

    @ApiModelProperty(value = "用户手机号")
    private String telephone;

}
