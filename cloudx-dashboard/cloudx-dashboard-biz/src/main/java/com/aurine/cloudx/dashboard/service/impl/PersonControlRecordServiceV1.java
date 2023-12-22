package com.aurine.cloudx.dashboard.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.dashboard.constant.ServiceNameEnum;
import com.aurine.cloudx.dashboard.constant.VersionEnum;
import com.aurine.cloudx.dashboard.entity.DwProjectPersonAbnormal;
import com.aurine.cloudx.dashboard.entity.DwProjectPersonControl;
import com.aurine.cloudx.dashboard.exception.DashboardErrorEnum;
import com.aurine.cloudx.dashboard.exception.DashboardException;
import com.aurine.cloudx.dashboard.mapper.DwProjectPersonAbnormalMapper;
import com.aurine.cloudx.dashboard.mapper.DwProjectPersonControlMapper;
import com.aurine.cloudx.dashboard.service.AbstractDashboardService;
import com.aurine.cloudx.dashboard.util.JSONUtil;
import com.aurine.cloudx.dashboard.vo.DashboardRequestVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.Data;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @version 1
 * @description:人员防控记录
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2021-04-12
 * @Copyright:
 */
@Service
public class PersonControlRecordServiceV1 extends AbstractDashboardService {
    @Resource
    private DwProjectPersonControlMapper dwProjectPersonControlMapper;

    /**
     * 获取数据
     *
     * @param request
     * @return
     */
    @Override
    public JSONObject getData(DashboardRequestVo request) {
        if (ArrayUtil.isEmpty(request.getProjectIdArray())) throw new DashboardException(DashboardErrorEnum.ARGUMENT_INVALID);

        List<DwProjectPersonControl> poList = dwProjectPersonControlMapper.selectList(
                new QueryWrapper<DwProjectPersonControl>().lambda()
                        .in(DwProjectPersonControl::getProjectId, request.getProjectIdArray())
                        .last("and rownum < 10 order by ENTRADATE desc ")
        );

        if (CollUtil.isEmpty(poList)) throw new DashboardException(DashboardErrorEnum.EMPTY_RESULT);

        long countAd = dwProjectPersonControlMapper.selectCount(new QueryWrapper<DwProjectPersonControl>().lambda()
                .in(DwProjectPersonControl::getProjectId, request.getProjectIdArray())
                .eq(DwProjectPersonControl::getControlType, "广告传单")
        );

        long countleaflet = dwProjectPersonControlMapper.selectCount(new QueryWrapper<DwProjectPersonControl>().lambda()
                .in(DwProjectPersonControl::getProjectId, request.getProjectIdArray())
                .eq(DwProjectPersonControl::getControlType, "疑似偷盗")
        );
        long total = dwProjectPersonControlMapper.selectCount(new QueryWrapper<DwProjectPersonControl>().lambda()
                .in(DwProjectPersonControl::getProjectId, request.getProjectIdArray())
        );


        VO vo = new VO();

        vo.setDataList(poList);
        vo.setAd(countAd);
        vo.setLeaflet(countleaflet);
        vo.setTotal(total);

        return JSONUtil.toJSONObject(vo);
    }

    @Data
    private class VO {

        private List dataList;
        private Long total = 0L;
        /**
         * 发传单
         */
        private Long ad = 0L;
        /**
         * 贴广告
         */
        private Long leaflet = 0L;


    }

    /**
     * 获取服务名称
     *
     * @return
     */
    @Override
    public String getServiceName() {
        return ServiceNameEnum.PERSON_CONTROL.serviceName;
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
