package com.aurine.cloudx.dashboard.vo;

import cn.hutool.core.util.ArrayUtil;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

/**
 * Dashboard 查询服务VO
 *
 * @ClassName: RequestVo
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2021-03-24 13:51
 * @Copyright:
 */
@Data
@ApiModel("Dashboard 请求对象")
public class DashboardRequestVo {
    /**
     * 项目ID
     */
    @ApiModelProperty("项目ID")
    private String projectId;
    /**
     * 项目ID
     */
    @ApiModelProperty("项目ID")
    private String[] projectIds;
    /**
     * 服务版本，1，2，3等
     */
    @ApiModelProperty("服务版本，1，2，3等")
    @NotEmpty
    private String version;

    /**
     * 服务名称
     */
    @ApiModelProperty("服务名称")
    @NotEmpty
    private String serviceName;

    /**
     * 开始时间
     */
    @ApiModelProperty("开始时间")
    private LocalDateTime beginTime;

    /**
     * 设备Code对于门禁设备则为SN
     */
    @ApiModelProperty("设备Code对于门禁设备则为SN")
    private String deviceSn;

    /**
     * 产品类型编码
     */
    @ApiModelProperty("产品类型编码")
    private String deviceTypeCode;

    /**
     * 楼栋ID
     */
    @ApiModelProperty("楼栋ID")
    private String buildingId;

    /**
     * 楼层
     */
    @ApiModelProperty("楼层")
    private Integer storey;

    /**
     * 结束时间
     */
    @ApiModelProperty("结束时间")
    private LocalDateTime endTime;

    /**
     * 额外参数
     */
    private JSONObject params;


    public JSONObject getParamsData() {
        if (this.params == null) return new JSONObject();
        return this.params;
    }

    ;

    public String[] getProjectIdArray() {
        if (StringUtils.isNotEmpty(projectId)) {
            return new String[]{projectId};
        } else if (ArrayUtil.isNotEmpty(projectIds)) {
            return projectIds;
        } else {
            return null;
        }
    }

}
