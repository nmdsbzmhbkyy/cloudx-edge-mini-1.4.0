package com.aurine.cloudx.estate.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;
import java.util.Map;

/**
 * 前端返回数据
 */
@Data
@ApiModel("App优惠账单查询vo")
public class ProjectBillPromotionAppVo{



    /**
     * 月份 对应 月份的账单
     */
    @ApiModelProperty("预存优惠列表")
    private Map<String, List<ProjectBillAppVo>> dataMapList;

}
