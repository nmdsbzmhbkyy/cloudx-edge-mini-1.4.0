
package com.aurine.cloudx.estate.service.impl;

import cn.hutool.core.util.StrUtil;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.entity.ProjectPersonDevice;
import com.aurine.cloudx.estate.mapper.ProjectPersonDeviceMapper;
import com.aurine.cloudx.estate.service.*;
import com.aurine.cloudx.estate.vo.ProjectProprietorDeviceRecordVo;
import com.aurine.cloudx.estate.vo.ProjectProprietorDeviceSearchCondition;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>住户设备权限代理实现类</p>
 *
 * @ClassName: ProjectPersonDeviceServiceImpl
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020/5/22 8:40
 * @Copyright:
 */
@Service
public class ProjectProprietorDeviceProxyServiceImpl extends ServiceImpl<ProjectPersonDeviceMapper, ProjectPersonDevice> implements ProjectProprietorDeviceProxyService {
    @Autowired
    private ProjectPersonDeviceService projectPersonDeviceService;

    @Autowired
    private ProjectPersonPlanRelService projectPersonPlanRelService;

    @Autowired
    private ProjectRightDeviceService projectRightDeviceService;

    @Autowired
    private ProjectEntityLevelCfgService projectEntityLevelCfgService;

    /**
     * 住户门禁权限查询
     *
     * @param page
     * @param searchCondition
     * @return
     */
    @Override
    public IPage<ProjectProprietorDeviceRecordVo> findPage(IPage<ProjectProprietorDeviceRecordVo> page, ProjectProprietorDeviceSearchCondition searchCondition) {

       /*
        //根据查询条件，分发查询
        boolean buildingNone = false;
        boolean parkingNone = false;

        if (StringUtils.isEmpty(searchCondition.getParkName()) && StringUtils.isEmpty(searchCondition.getParkRegionName()) && StringUtils.isEmpty(searchCondition.getParkId())) {
            parkingNone = true;
        }

        if (StringUtils.isEmpty(searchCondition.getBuildingName()) && StringUtils.isEmpty(searchCondition.getUnitName()) && StringUtils.isEmpty(searchCondition.getHouseName())) {
            buildingNone = true;
        }

        // 获取房屋地址组合
        String groupAddressHouse =
                (StrUtil.isNotBlank(searchCondition.getBuildingName()) ? searchCondition.getBuildingName() + "-" : "-") +
                (StrUtil.isNotBlank(searchCondition.getUnitName()) ? searchCondition.getUnitName() + "-": "-") +
                (StrUtil.isNotBlank(searchCondition.getHouseName()) ? searchCondition.getHouseName() : "");
        String groupAddressPark =
                (StrUtil.isNotBlank(searchCondition.getParkName()) ? searchCondition.getParkName() + "-" : "-") +
                (StrUtil.isNotBlank(searchCondition.getParkRegionName()) ? searchCondition.getParkRegionName() + "-" : "-") +
                (StrUtil.isNotBlank( searchCondition.getPlaceName()) ? searchCondition.getPlaceName() : "");
        //只查询楼栋+姓名+状态
        if (parkingNone && !buildingNone) {
            return this.baseMapper.findProprietorDeviceHousePage(page, searchCondition, groupAddressHouse, ProjectContextHolder.getProjectId());
            //只查询车位+姓名+状态
        } else if (!parkingNone && buildingNone) {
            return this.baseMapper.findProprietorDeviceParkingPage(page, searchCondition, groupAddressPark, ProjectContextHolder.getProjectId());
            //其他情况（同时查询或者都不查询楼栋和车位）
        } else {
            return this.baseMapper.findProprietorDevicePage(page, searchCondition, groupAddressPark, groupAddressHouse, ProjectContextHolder.getProjectId());
        }
        */
        searchCondition.setHouseSearch(false);
        searchCondition.setParkSearch(false);
        if (StrUtil.isNotBlank(searchCondition.getHouseName()) || StrUtil.isNotBlank(searchCondition.getUnitName()) || StrUtil.isNotBlank(searchCondition.getBuildingName())) {
            searchCondition.setHouseSearch(true);
        }
        if (StrUtil.isNotBlank(searchCondition.getParkName()) || StrUtil.isNotBlank(searchCondition.getParkRegionName()) || StrUtil.isNotBlank(searchCondition.getPlaceName())) {
            searchCondition.setParkSearch(true);
        }
        IPage<ProjectProprietorDeviceRecordVo> projectProprietorDeviceRecordVoIPage = this.baseMapper.fetchList(page,
                searchCondition, ProjectContextHolder.getProjectId(), projectEntityLevelCfgService.checkIsEnabled());
        return projectProprietorDeviceRecordVoIPage;


    }

}
