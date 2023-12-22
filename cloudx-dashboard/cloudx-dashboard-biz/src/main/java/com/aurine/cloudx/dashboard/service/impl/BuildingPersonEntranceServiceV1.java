package com.aurine.cloudx.dashboard.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.dashboard.constant.ServiceNameEnum;
import com.aurine.cloudx.dashboard.constant.VersionEnum;
import com.aurine.cloudx.dashboard.entity.ProjectBuildingPersonEntranceH07MView;
import com.aurine.cloudx.dashboard.exception.DashboardErrorEnum;
import com.aurine.cloudx.dashboard.exception.DashboardException;
import com.aurine.cloudx.dashboard.mapper.ProjectBuildingPersonEntranceH07Mapper;
import com.aurine.cloudx.dashboard.service.AbstractDashboardService;
import com.aurine.cloudx.dashboard.util.JSONUtil;
import com.aurine.cloudx.dashboard.vo.DashboardRequestVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @version 1
 * @description: 楼栋人员通行7小时
 * @author : Qiu <qiujb@miligc.com>
 * @date : 2021 08 16 15:12
 * @Copyright:
 */

@Service
public class BuildingPersonEntranceServiceV1 extends AbstractDashboardService {

    @Resource
    private ProjectBuildingPersonEntranceH07Mapper projectBuildingPersonEntranceH07Mapper;

    @Override
    public JSONObject getData(DashboardRequestVo request) {
        if (StringUtils.isEmpty(request.getProjectId()) || StringUtils.isEmpty(request.getBuildingId())) {
            throw new DashboardException(DashboardErrorEnum.ARGUMENT_INVALID);
        }
        boolean whole = request.getStorey() == null || request.getStorey() <= 0;
        List<ProjectBuildingPersonEntranceH07MView> peList = super.getListByProjectIdAndBuildingId(projectBuildingPersonEntranceH07Mapper, ProjectBuildingPersonEntranceH07MView.class, request.getProjectId(), request.getBuildingId(), request.getStorey());
        if (CollUtil.isEmpty(peList)) throw new DashboardException(DashboardErrorEnum.EMPTY_RESULT);

        VO vo = new VO();
        int axisNumber = 7;
        Long[] all = initArray(axisNumber);
        Long[] allIn = initArray(axisNumber);
        Long[] allOut = initArray(axisNumber);
        Long[][] entrance = initTwoDimensionalArray(peList.size(), axisNumber);
        Long[][] in = initTwoDimensionalArray(peList.size(), axisNumber);
        Long[][] out = initTwoDimensionalArray(peList.size(), axisNumber);

        for (int i = 0; i < peList.size(); i++) {
            ProjectBuildingPersonEntranceH07MView pe = peList.get(i);
            int index = i;
            if (whole && pe.getStorey() != null && pe.getStorey() > 0) {
                index = pe.getStorey() - 1;
            }
            entrance[index][0] = pe.getCntH01();
            entrance[index][1] = pe.getCntH02();
            entrance[index][2] = pe.getCntH03();
            entrance[index][3] = pe.getCntH04();
            entrance[index][4] = pe.getCntH05();
            entrance[index][5] = pe.getCntH06();
            entrance[index][6] = pe.getCntH07();
            in[index][0] = pe.getCntInH01();
            in[index][1] = pe.getCntInH02();
            in[index][2] = pe.getCntInH03();
            in[index][3] = pe.getCntInH04();
            in[index][4] = pe.getCntInH05();
            in[index][5] = pe.getCntInH06();
            in[index][6] = pe.getCntInH07();
            out[index][0] = pe.getCntOutH01();
            out[index][1] = pe.getCntOutH02();
            out[index][2] = pe.getCntOutH03();
            out[index][3] = pe.getCntOutH04();
            out[index][4] = pe.getCntOutH05();
            out[index][5] = pe.getCntOutH06();
            out[index][6] = pe.getCntOutH07();

            all[0] += pe.getCntH01();
            all[1] += pe.getCntH02();
            all[2] += pe.getCntH03();
            all[3] += pe.getCntH04();
            all[4] += pe.getCntH05();
            all[5] += pe.getCntH06();
            all[6] += pe.getCntH07();
            allIn[0] += pe.getCntInH01();
            allIn[1] += pe.getCntInH02();
            allIn[2] += pe.getCntInH03();
            allIn[3] += pe.getCntInH04();
            allIn[4] += pe.getCntInH05();
            allIn[5] += pe.getCntInH06();
            allIn[6] += pe.getCntInH07();
            allOut[0] += pe.getCntOutH01();
            allOut[1] += pe.getCntOutH02();
            allOut[2] += pe.getCntOutH03();
            allOut[3] += pe.getCntOutH04();
            allOut[4] += pe.getCntOutH05();
            allOut[5] += pe.getCntOutH06();
            allOut[6] += pe.getCntOutH07();
        }
        vo.setAll(all);
        vo.setAllIn(allIn);
        vo.setAllOut(allOut);
        vo.setEntrance(entrance);
        vo.setIn(in);
        vo.setOut(out);
        vo.setHAxis(initAxis(axisNumber, HOUR, false, -1));

        return JSONUtil.toJSONObject(vo);
    }

    @Data
    private class VO {
        /**
         * 整楼进出人数
         */
        @ApiModelProperty(value = "整楼进出人数")
        private Long[] all;

        /**
         * 整楼进人数
         */
        @ApiModelProperty(value = "整楼进人数")
        private Long[] allIn;

        /**
         * 整楼出人数
         */
        @ApiModelProperty(value = "整楼出人数")
        private Long[] allOut;

        /**
         * 进人数
         */
        @ApiModelProperty(value = "进人数")
        private Long[][] in;

        /**
         * 出人数
         */
        @ApiModelProperty(value = "出人数")
        private Long[][] out;

        /**
         * 出入人数
         */
        @ApiModelProperty(value = "出入人数")
        private Long[][] entrance;

        /**
         * 小时
         */
        @ApiModelProperty(value = "小时")
        private String[] hAxis;
    }

    @Override
    public String getServiceName() {
        return ServiceNameEnum.BUILDING_PERSON_ENTRANCE.serviceName;
    }

    @Override
    public String getVersion() {
        return VersionEnum.V1.number;
    }
}
