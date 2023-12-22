package com.aurine.cloudx.wjy.service.impl;

import com.aurine.cloudx.wjy.entity.Building;
import com.aurine.cloudx.wjy.entity.Project;
import com.aurine.cloudx.wjy.mapper.BuildingMapper;
import com.aurine.cloudx.wjy.pojo.RDataRowsPager;
import com.aurine.cloudx.wjy.service.BuildingService;
import com.aurine.cloudx.wjy.service.ProjectService;
import com.aurine.cloudx.wjy.service.WjyBuildingService;
import com.aurine.cloudx.wjy.vo.BuildingVo;
import com.aurine.cloudx.wjy.vo.WjyBuilding;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ： huangjj
 * @date ： 2021/4/25
 * @description： 楼栋接口实现类
 */
@Service
public class BuildingServiceImpl extends ServiceImpl<BuildingMapper, Building> implements BuildingService {
    @Resource
    BuildingMapper buildingMapper;
    @Resource
    WjyBuildingService wjyBuildingService;
    @Resource
    ProjectService projectService;
    @Override
    public Building getByProjectIdAndName(Integer projectId, String name) {
        return buildingMapper.selectOne(new QueryWrapper<Building>().lambda().eq(Building::getProjectId,projectId).eq(Building::getBuildingName,name));
    }

    @Override
    public List<Building> getList() {
        return this.list();
    }

    public R addBuilding(BuildingVo buildingVo){
        Project project = projectService.getByProjectId(buildingVo.getProjectId());
        if(project == null){
            return R.failed("未找到项目信息");
        }
        List<BuildingVo> buildingVos = new ArrayList<>();
        buildingVos.add(buildingVo);

        Boolean isSave = wjyBuildingService.buildingSave(project,buildingVos);
        if (!isSave){
            return R.failed("添加楼栋失败");
        }
        com.aurine.cloudx.wjy.pojo.R<RDataRowsPager<WjyBuilding>> r = wjyBuildingService.buildingGetByPage(1,1,project,buildingVo.getName());
        if(r != null && r.isSuccess()){
            try {
                WjyBuilding wjyBuilding = r.getData().getRows().get(0);
                Building building = getByProjectIdAndName(project.getProjectId(),buildingVo.getName());
                if(building == null){
                    building = new Building();
                }
                building.setProjectId(buildingVo.getProjectId());
                building.setBuildingId(buildingVo.getSourceID());
                building.setBuildingName(buildingVo.getName());
                building.setWjyBuildingId(wjyBuilding.getId());
                saveOrUpdate(building);
                return R.ok();
            } catch (Exception e) {
                e.printStackTrace();
                return R.failed("无法读取楼栋信息");
            }
        }
        return R.failed();
    }
}