package com.aurine.cloudx.dashboard.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.dashboard.constant.ServiceNameEnum;
import com.aurine.cloudx.dashboard.constant.VersionEnum;
import com.aurine.cloudx.dashboard.entity.ProjectComplaintRecordDay7Mview;
import com.aurine.cloudx.dashboard.exception.DashboardErrorEnum;
import com.aurine.cloudx.dashboard.exception.DashboardException;
import com.aurine.cloudx.dashboard.mapper.ProjectComplaintRecordDay07Mapper;
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
 * @description: 投诉建议
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2021-03-30
 * @Copyright:
 */
@Service
public class ComplaintRecordServiceV1 extends AbstractDashboardService {
    @Resource
    private ProjectComplaintRecordDay07Mapper projectComplaintRecordDay07Mapper;

    /**
     * 获取数据
     *
     * @param request
     * @return
     */
    @Override
    public JSONObject getData(DashboardRequestVo request) {
        List<ProjectComplaintRecordDay7Mview> poList = super.getListByProjectId(projectComplaintRecordDay07Mapper, ProjectComplaintRecordDay7Mview.class, request.getProjectIdArray());

        if (CollUtil.isEmpty(poList)) throw new DashboardException(DashboardErrorEnum.EMPTY_RESULT);

        VO vo = new VO();
        Long[] complaint = initArray(7);
        for (ProjectComplaintRecordDay7Mview po : poList) {
            complaint[0] += po.getDay01();
            complaint[1] += po.getDay02();
            complaint[2] += po.getDay03();
            complaint[3] += po.getDay04();
            complaint[4] += po.getDay05();
            complaint[5] += po.getDay06();
            complaint[6] += po.getDay07();
        }
        vo.setComplaint(complaint);

        vo.setDayAxis(initAxis(7, DAY, false, -1));

        return JSONUtil.toJSONObject(vo);
    }

    @Data
    private class VO {
        @ApiModelProperty(value = "投诉量")
        private Long[] complaint;

        @ApiModelProperty(value = "天")
        private String[] dayAxis;
    }

    /**
     * 获取服务名称
     *
     * @return
     */
    @Override
    public String getServiceName() {
        return ServiceNameEnum.COMPLAINT_RECORD.serviceName;
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
