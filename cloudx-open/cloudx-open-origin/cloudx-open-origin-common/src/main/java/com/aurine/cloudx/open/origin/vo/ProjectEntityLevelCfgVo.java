package com.aurine.cloudx.open.origin.vo;

import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.open.origin.entity.ProjectEntityLevelCfg;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * @author cjw
 * @description:
 * @date 2021/4/19 10:17
 */
@Data
public class ProjectEntityLevelCfgVo extends ProjectEntityLevelCfg {

    /**
     * 房号长度
     */
    @ApiModelProperty("房号长度")
    private Integer houseRule;
    /**
     * 单元长度
     */
    @ApiModelProperty("单元长度")
    private Integer unitRule;
    /**
     * 楼栋长度
     */
    @ApiModelProperty("楼栋长度")
    private Integer buildingRule;
    /**
     * 楼层号长度占
     */
    @ApiModelProperty("楼层号长度占")
    private Integer cellRule;
    /**
     * 组团长度
     */
    @ApiModelProperty("组团长度")
    private Integer groupRule;
    /**
     * 房屋描述
     */
    @ApiModelProperty("房屋描述")
    private String houseDesc;
    /**
     * 单元描述
     */
    @ApiModelProperty("单元描述")
    private String unitDesc;
    /**
     * 楼栋描述
     */
    @ApiModelProperty("楼栋描述")
    private String buildingDesc;
    /**
     * 楼栋描述
     */
    @ApiModelProperty("楼栋描述")
    private String groupDesc;

    @ApiModelProperty("选择组团等级")
    private Integer maxLevel;

    private JSONObject groupRuleArray;

    private JSONObject groupDescArray;


}
