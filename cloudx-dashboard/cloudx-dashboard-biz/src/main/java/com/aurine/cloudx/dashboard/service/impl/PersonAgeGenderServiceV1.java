package com.aurine.cloudx.dashboard.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.dashboard.constant.ServiceNameEnum;
import com.aurine.cloudx.dashboard.constant.VersionEnum;
import com.aurine.cloudx.dashboard.entity.ProjectPersonAgeGenderMview;
import com.aurine.cloudx.dashboard.exception.DashboardErrorEnum;
import com.aurine.cloudx.dashboard.exception.DashboardException;
import com.aurine.cloudx.dashboard.mapper.ProjectPersonAgeGenderMapper;
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
 * @description:年龄性别占比 柱状图数据
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2021-03-25
 * @Copyright:
 */
@Service
public class PersonAgeGenderServiceV1 extends AbstractDashboardService {
    @Resource
    private ProjectPersonAgeGenderMapper projectPersonAgeGenderMapper;

    /**
     * 获取数据
     * {
     * "male":[1,2,3,4],
     * "female":[1,2,3,4]
     * }
     *
     * @param request
     * @return
     */
    @Override
    public JSONObject getData(DashboardRequestVo request) {

        List<ProjectPersonAgeGenderMview> poList = super.getListByProjectId(projectPersonAgeGenderMapper, ProjectPersonAgeGenderMview.class, request.getProjectIdArray());

        if (CollUtil.isEmpty(poList)) throw new DashboardException(DashboardErrorEnum.EMPTY_RESULT);


        VO vo = new VO();
        Long[] male = initArray(10);
        Long[] female = initArray(10);

        for (ProjectPersonAgeGenderMview po : poList) {
            male[0] += po.getMale0to9();
            male[1] += po.getMale10to19();
            male[2] += po.getMale20to29();
            male[3] += po.getMale30to39();
            male[4] += po.getMale40to49();
            male[5] += po.getMale50to59();
            male[6] += po.getMale60to69();
            male[7] += po.getMale70to79();
            male[8] += po.getMale80to89();
            male[9] += (po.getMale90to99() + po.getMale100());
//            male[10] += po.getMale100();

            female[0] += po.getFemale0to9();
            female[1] += po.getFemale10to19();
            female[2] += po.getFemale20to29();
            female[3] += po.getFemale30to39();
            female[4] += po.getFemale40to49();
            female[5] += po.getFemale50to59();
            female[6] += po.getFemale60to69();
            female[7] += po.getFemale70to79();
            female[8] += po.getFemale80to89();
            female[9] += (po.getFemale90to99() + po.getFemale100());
//            female[10] += po.getFemale100();

        }

        vo.setMale(male);
        vo.setFemale(female);
//        vo.setXAxis(new String[]{"0-9岁", "10-19岁", "20-29岁", "30-39岁", "40-49岁", "50-59岁", "60-69岁", "70-79岁", "80-89岁", "90-99岁", "100岁+"});
        vo.setXAxis(new String[]{"0-9岁", "10-19岁", "20-29岁", "30-39岁", "40-49岁", "50-59岁", "60-69岁", "70-79岁", "80-89岁", "90+岁"});

        return JSONUtil.toJSONObject(vo);
    }

    @Data
    private class VO {
        @ApiModelProperty(value = "男性人数")
        private Long[] male;
        @ApiModelProperty(value = "女性人数")
        private Long[] female;
        @ApiModelProperty(value = "X轴文字")
        private String[] xAxis;
    }

    /**
     * 获取服务名称
     *
     * @return
     */
    @Override
    public String getServiceName() {
        return ServiceNameEnum.PERSON_AGE_GENDER.serviceName;
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
