package com.aurine.cloudx.dashboard.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.dashboard.constant.ServiceNameEnum;
import com.aurine.cloudx.dashboard.constant.VersionEnum;
import com.aurine.cloudx.dashboard.entity.DwProjectPersonAbnormal;
import com.aurine.cloudx.dashboard.entity.ProjectInspectMview;
import com.aurine.cloudx.dashboard.exception.DashboardErrorEnum;
import com.aurine.cloudx.dashboard.exception.DashboardException;
import com.aurine.cloudx.dashboard.mapper.DwProjectPersonAbnormalMapper;
import com.aurine.cloudx.dashboard.mapper.ProjectInspectMapper;
import com.aurine.cloudx.dashboard.service.AbstractDashboardService;
import com.aurine.cloudx.dashboard.util.JSONUtil;
import com.aurine.cloudx.dashboard.vo.DashboardRequestVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @version 1
 * @description:异常行为记录
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2021-04-12
 * @Copyright:
 */
@Service
public class PersonAbnormalRecordServiceV1 extends AbstractDashboardService {
    @Resource
    private DwProjectPersonAbnormalMapper dwProjectPersonAbnormalMapper;

    /**
     * 获取数据
     *
     * @param request
     * @return
     */
    @Override
    public JSONObject getData(DashboardRequestVo request) {
        if (ArrayUtil.isEmpty(request.getProjectIdArray())) throw new DashboardException(DashboardErrorEnum.ARGUMENT_INVALID);

        List<DwProjectPersonAbnormal> poList = dwProjectPersonAbnormalMapper.selectList(
                new QueryWrapper<DwProjectPersonAbnormal>().lambda()
                        .in(DwProjectPersonAbnormal::getProjectId, request.getProjectIdArray())
                        .last("and rownum < 10 order by EVENTTIME desc ")
        );

        if (CollUtil.isEmpty(poList)) throw new DashboardException(DashboardErrorEnum.EMPTY_RESULT);

        VO vo = new VO();

        vo.setDataList(poList);


        return JSONUtil.toJSONObject(vo);
    }

    @Data
    private class VO {

        private List dataList;


    }

    /**
     * 获取服务名称
     *
     * @return
     */
    @Override
    public String getServiceName() {
        return ServiceNameEnum.PERSON_ABNORMAL.serviceName;
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
