package com.aurine.cloudx.wjy.service;

import com.aurine.cloudx.wjy.entity.Project;
import com.aurine.cloudx.wjy.vo.BuildingVo;
import com.aurine.cloudx.wjy.pojo.RDataRowsPager;
import com.aurine.cloudx.wjy.pojo.R;
import com.aurine.cloudx.wjy.vo.WjyBuilding;

import java.util.List;

/**
 * @author ： huangjj
 * @date ： 2021/4/20
 * @description： 我家云楼栋管理接口
 */
public interface WjyBuildingService {
    /**
     * 功能描述: 获取楼栋
     *
     * @author huangjj
     * @date 2021/4/25
     * @param rowCount 每页总数
     * @param currentPage 当前页
     * @param project 项目配置参数
     * @param queryName 楼栋名称
     * @return
    */
    R<RDataRowsPager<WjyBuilding>> buildingGetByPage(int rowCount, int currentPage, Project project, String queryName);
    /**
     * 功能描述: 保存楼栋
     *
     * @author huangjj
     * @date 2021/4/25
     * @param  project 项目配置参数
     * @param  builds 楼栋数据
     * @return
    */
    boolean buildingSave(Project project, List<BuildingVo> builds);

}
