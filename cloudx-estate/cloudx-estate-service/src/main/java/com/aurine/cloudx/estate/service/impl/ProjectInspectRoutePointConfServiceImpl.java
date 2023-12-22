package com.aurine.cloudx.estate.service.impl;

import cn.hutool.core.util.ArrayUtil;
import com.aurine.cloudx.estate.constant.InspectSortConstants;
import com.aurine.cloudx.estate.entity.ProjectInspectRoutePointConf;
import com.aurine.cloudx.estate.mapper.ProjectInspectRoutePointConfMapper;
import com.aurine.cloudx.estate.service.ProjectInspectRoutePointConfService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 设备巡检路线与巡更点关系表(ProjectInspectRoutePointConf)表服务实现类
 *
 * @author 王良俊
 * @since 2020-07-23 18:32:50
 */
@Service
public class ProjectInspectRoutePointConfServiceImpl extends ServiceImpl<ProjectInspectRoutePointConfMapper,
        ProjectInspectRoutePointConf> implements ProjectInspectRoutePointConfService {

    @Override
    public boolean saveOrUpdateRoutePointRel(String inspectRouteId, String[] pointIdArr, String isSort) {
        // 移除原有关系（如果有的话）
        this.remove(new QueryWrapper<ProjectInspectRoutePointConf>().lambda().eq(ProjectInspectRoutePointConf::getInspectRouteId, inspectRouteId));
        List<ProjectInspectRoutePointConf> routePointConfList = new ArrayList<>();

        if (ArrayUtil.isNotEmpty(pointIdArr)) {
            Arrays.stream(pointIdArr).forEach(pointId -> {
                ProjectInspectRoutePointConf projectInspectRoutePointConf = new ProjectInspectRoutePointConf();
                projectInspectRoutePointConf.setInspectRouteId(inspectRouteId);
                projectInspectRoutePointConf.setInspectPointId(pointId);
                if (InspectSortConstants.SORT.equals(isSort)){
                    projectInspectRoutePointConf.setSort(routePointConfList.size()+1);
                }else {
                    // 未设置排序时默认为0
                    projectInspectRoutePointConf.setSort(0);
                }
                routePointConfList.add(projectInspectRoutePointConf);
            });
            return this.saveBatch(routePointConfList);
        }
        return true;
    }

    @Override
    public boolean removeRoutePointRel(String inspectRouteId) {
        return this.remove(new QueryWrapper<ProjectInspectRoutePointConf>().lambda().eq(ProjectInspectRoutePointConf::getInspectRouteId, inspectRouteId));
    }

}