package com.aurine.cloudx.dashboard.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.dashboard.constant.ServiceNameEnum;
import com.aurine.cloudx.dashboard.constant.VersionEnum;
import com.aurine.cloudx.dashboard.entity.ProjectParkplaceH07Mview;
import com.aurine.cloudx.dashboard.entity.ProjectPersonEntranceDay07Mview;
import com.aurine.cloudx.dashboard.entity.ProjectPersonEntranceH07Mview;
import com.aurine.cloudx.dashboard.exception.DashboardErrorEnum;
import com.aurine.cloudx.dashboard.exception.DashboardException;
import com.aurine.cloudx.dashboard.mapper.ProjectParkplaceH07Mapper;
import com.aurine.cloudx.dashboard.mapper.ProjectPersonEntranceDay07Mapper;
import com.aurine.cloudx.dashboard.mapper.ProjectPersonEntranceH07Mapper;
import com.aurine.cloudx.dashboard.service.AbstractDashboardService;
import com.aurine.cloudx.dashboard.util.JSONUtil;
import com.aurine.cloudx.dashboard.vo.DashboardRequestVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @version 1
 * @description: 每日车位使用率
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2021-03-30
 * @Copyright:
 */
@Service
public class ParkPlaceServiceV1 extends AbstractDashboardService {
    @Resource
    private ProjectParkplaceH07Mapper projectParkplaceH07Mapper;


    /**
     * 获取数据
     *
     * @param request
     * @return
     */
    @Override
    public JSONObject getData(DashboardRequestVo request) {
        List<ProjectParkplaceH07Mview> poList = super.getListByProjectId(projectParkplaceH07Mapper, ProjectParkplaceH07Mview.class, request.getProjectIdArray());

        if (CollUtil.isEmpty(poList)) throw new DashboardException(DashboardErrorEnum.EMPTY_RESULT);

        VO vo = new VO();

        Long[] hour = initArray(7);
        Long total = 0L;
        for (ProjectParkplaceH07Mview po : poList) {
            hour[0] += po.getH01();
            hour[1] += po.getH02();
            hour[2] += po.getH03();
            hour[3] += po.getH04();
            hour[4] += po.getH05();
            hour[5] += po.getH06();
            hour[6] += po.getH07();
            total += po.getTotal();
        }

        Double[] hourDouble = initDoubleArray(7);
        hourDouble[0] = (0.0 + hour[0]) / total;
        hourDouble[1] = (0.0 + hour[1]) / total;
        hourDouble[2] = (0.0 + hour[2]) / total;
        hourDouble[3] = (0.0 + hour[3]) / total;
        hourDouble[4] = (0.0 + hour[4]) / total;
        hourDouble[5] = (0.0 + hour[5]) / total;
        hourDouble[6] = (0.0 + hour[6]) / total;

        vo.setHour(hour);
        vo.setHourDouble(hourDouble);

        vo.setHourAxis(initAxis(7, HOUR, false, -1));

        return JSONUtil.toJSONObject(vo);
    }

    @Data
    private class VO {
        @ApiModelProperty(value = "每小时车位趋势")
        private Long[] hour;
        private Double[] hourDouble;
        @ApiModelProperty(value = "小时")
        private String[] hourAxis;

    }

    /**
     * 获取服务名称
     *
     * @return
     */
    @Override
    public String getServiceName() {
        return ServiceNameEnum.PARK_PLACE.serviceName;
    }

    /**
     * 获取版本号
     *
     * @return
     */
    @Override
    public String getVersion() {
        return VersionEnum.V1.number;
    }
}
