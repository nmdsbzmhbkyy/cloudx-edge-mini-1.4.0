package com.aurine.cloudx.dashboard.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.dashboard.constant.ServiceNameEnum;
import com.aurine.cloudx.dashboard.constant.VersionEnum;
import com.aurine.cloudx.dashboard.entity.ProjectParkplaceH07Mview;
import com.aurine.cloudx.dashboard.entity.ProjectRepairRecordDay7Mview;
import com.aurine.cloudx.dashboard.exception.DashboardErrorEnum;
import com.aurine.cloudx.dashboard.exception.DashboardException;
import com.aurine.cloudx.dashboard.mapper.ProjectParkplaceH07Mapper;
import com.aurine.cloudx.dashboard.mapper.ProjectRepairRecordDay7Mapper;
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
 * @description: 每日故障报修数
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2021-03-30
 * @Copyright:
 */
@Service
public class RepairRecordServiceV1 extends AbstractDashboardService {
    @Resource
    private ProjectRepairRecordDay7Mapper projectRepairRecordDay7Mapper;


    /**
     * 获取数据
     *
     * @param request
     * @return
     */
    @Override
    public JSONObject getData(DashboardRequestVo request) {
        List<ProjectRepairRecordDay7Mview> poList = super.getListByProjectId(projectRepairRecordDay7Mapper, ProjectRepairRecordDay7Mview.class, request.getProjectIdArray());

        if (CollUtil.isEmpty(poList)) throw new DashboardException(DashboardErrorEnum.EMPTY_RESULT);

        VO vo = new VO();

        Long[] day = initArray(7);
        for (ProjectRepairRecordDay7Mview po : poList) {
            day[0] += po.getDay01();
            day[1] += po.getDay02();
            day[2] += po.getDay03();
            day[3] += po.getDay04();
            day[4] += po.getDay05();
            day[5] += po.getDay06();
            day[6] += po.getDay07();
        }
        vo.setDay(day);

        vo.setDayAxis(initAxis(7, DAY, false, -1));

        return JSONUtil.toJSONObject(vo);
    }

    @Data
    private class VO {
        @ApiModelProperty(value = "每日报修数量")
        private Long[] day;
        @ApiModelProperty(value = "日")
        private String[] dayAxis;

    }

    /**
     * 获取服务名称
     *
     * @return
     */
    @Override
    public String getServiceName() {
        return ServiceNameEnum.REPAIR_RECORD.serviceName;
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
