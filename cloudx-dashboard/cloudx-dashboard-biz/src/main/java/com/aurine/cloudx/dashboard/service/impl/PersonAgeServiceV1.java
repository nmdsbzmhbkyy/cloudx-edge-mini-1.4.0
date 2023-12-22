package com.aurine.cloudx.dashboard.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.dashboard.constant.ServiceNameEnum;
import com.aurine.cloudx.dashboard.constant.VersionEnum;
import com.aurine.cloudx.dashboard.entity.ProjectPersonAgeGenderMview;
import com.aurine.cloudx.dashboard.entity.ProjectPersonRegionMview;
import com.aurine.cloudx.dashboard.exception.DashboardErrorEnum;
import com.aurine.cloudx.dashboard.exception.DashboardException;
import com.aurine.cloudx.dashboard.mapper.ProjectPersonAgeGenderMapper;
import com.aurine.cloudx.dashboard.mapper.ProjectPersonRegionMapper;
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
 * @description: 人员年龄分布
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2021-03-30
 * @Copyright:
 */
@Service
public class PersonAgeServiceV1 extends AbstractDashboardService {
    @Resource
    private ProjectPersonAgeGenderMapper projectPersonAgeGenderMapper;


    /**
     * 获取数据
     *
     * @param request
     * @return
     */
    @Override
    public JSONObject getData(DashboardRequestVo request) {

        List<ProjectPersonAgeGenderMview> poList = super.getListByProjectId(projectPersonAgeGenderMapper, ProjectPersonAgeGenderMview.class, request.getProjectIdArray());

        if (CollUtil.isEmpty(poList)) throw new DashboardException(DashboardErrorEnum.EMPTY_RESULT);


        VO vo = new VO();


        for (ProjectPersonAgeGenderMview po : poList) {
            vo.setTotal(po.getTotal() + vo.getTotal());
            vo.setUnder10(po.getFemale0to9() + po.getMale0to9() + vo.getUnder10()
                    + po.getMale10to19() + po.getFemale10to19() + po.getUnknown10to19());
            vo.setOver70(
                    po.getFemale70to79() + po.getFemale80to89() + po.getFemale90to99() + po.getFemale100()
                            + po.getMale70to79() + po.getMale80to89() + po.getMale90to99() + po.getMale100()
                            + vo.getOver70()
            );

        }
        return JSONUtil.toJSONObject(vo);
    }

    @Data
    private class VO {
        @ApiModelProperty(value = "10岁以下人数")
        private Long under10 = 0L;
        @ApiModelProperty(value = "70以上老人")
        private Long over70 = 0L;
        @ApiModelProperty(value = "所有人数")
        private Long total = 0L;
    }

    /**
     * 获取服务名称
     *
     * @return
     */
    @Override
    public String getServiceName() {
        return ServiceNameEnum.PERSON_AGE.serviceName;
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
