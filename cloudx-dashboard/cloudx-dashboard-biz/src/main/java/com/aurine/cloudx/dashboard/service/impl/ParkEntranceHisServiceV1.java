package com.aurine.cloudx.dashboard.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.dashboard.config.MinioConfig;
import com.aurine.cloudx.dashboard.constant.ServiceNameEnum;
import com.aurine.cloudx.dashboard.constant.VersionEnum;
import com.aurine.cloudx.dashboard.entity.ProjectParkEntranceHis;
import com.aurine.cloudx.dashboard.exception.DashboardErrorEnum;
import com.aurine.cloudx.dashboard.exception.DashboardException;
import com.aurine.cloudx.dashboard.mapper.ProjectParkEntranceHisMapper;
import com.aurine.cloudx.dashboard.service.AbstractDashboardService;
import com.aurine.cloudx.dashboard.util.JSONUtil;
import com.aurine.cloudx.dashboard.vo.DashboardRequestVo;
import lombok.Data;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @version 1
 * @description:人行历史记录
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2021-04-12
 * @Copyright:
 */
@Service
public class ParkEntranceHisServiceV1 extends AbstractDashboardService {

    @Resource
    private MinioConfig minioConfig;

    @Resource
    private ProjectParkEntranceHisMapper projectParkEntranceHisMapper;

    /**
     * 获取数据
     *
     * @param request
     * @return
     */
    @Override
    public JSONObject getData(DashboardRequestVo request) {
        if (ArrayUtil.isEmpty(request.getProjectIdArray())) {
            throw new DashboardException(DashboardErrorEnum.ARGUMENT_INVALID);
        }

        List<ProjectParkEntranceHis> poList = projectParkEntranceHisMapper.selectParkEntranceHis(request.getProjectIdArray());
//                projectParkEntranceHisMapper.selectList(new QueryWrapper<ProjectParkEntranceHis>().lambda()
//                        .in(ProjectParkEntranceHis::getProjectId, request.getProjectIdArray())
//                        .isNotNull(ProjectParkEntranceHis::getEnterPicUrl)
//                        .last("and rownum < 10 order by createtime desc ")
//                );

        if (CollUtil.isEmpty(poList)) throw new DashboardException(DashboardErrorEnum.EMPTY_RESULT);

        poList.forEach(po -> {
            if (po.getEnterPicUrl().startsWith("/")) {
                po.setEnterPicUrl(minioConfig.getServerUrl() + po.getEnterPicUrl());
            }
        });

        VO vo = new VO();
        vo.setDataList(poList);

        return JSONUtil.toJSONObject(vo);
    }

    @Data
    private class VO {
        private List dataList = new ArrayList();
    }

    /**
     * 获取服务名称
     *
     * @return
     */
    @Override
    public String getServiceName() {
        return ServiceNameEnum.PARK_ENTRANCE.serviceName;
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
