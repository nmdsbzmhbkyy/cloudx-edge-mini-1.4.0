package com.aurine.cloudx.wjy.service;

import com.aurine.cloudx.wjy.entity.Building;
import com.aurine.cloudx.wjy.vo.BuildingVo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author ： huangjj
 * @date ： 2021/4/25
 * @description： 楼栋接口
 */
public interface BuildingService extends IService<Building> {
    Building getByProjectIdAndName(Integer projectId, String name);
    List<Building> getList();
    R addBuilding(BuildingVo buildingVo);
}
