
package com.aurine.cloudx.estate.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.aurine.cloudx.common.core.constant.DataConstants;
import com.aurine.cloudx.common.core.util.TreeUtil;
import com.aurine.cloudx.estate.entity.*;
import com.aurine.cloudx.estate.mapper.ProjectDeviceRegionMapper;
import com.aurine.cloudx.estate.service.*;
import com.aurine.cloudx.estate.vo.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.common.core.util.R;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>  </p>
 *
 * @ClassName: ProjectDeviceRegionServiceImpl
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020/5/29 9:32
 * @Copyright:
 */
@Service
public class ProjectDeviceRegionServiceImpl extends ServiceImpl<ProjectDeviceRegionMapper, ProjectDeviceRegion> implements ProjectDeviceRegionService {

    @Autowired
    ProjectDeviceInfoService projectDeviceInfoService;
    @Autowired
    ProjectDeviceInfoProxyService projectDeviceInfoProxyService;
    @Autowired
    ProjectBuildingInfoService projectBuildingInfoService;
    @Autowired
    ProjectEntityLevelCfgService projectEntityLevelCfgService;
    @Autowired
    ProjectStaffRegionService projectStaffRegionService;

    @Resource
    ProjectDeviceRegionMapper projectDeviceRegionMapper;
    /**
     * 区域管理树根节点名称 xull@aurine.cn 2020年7月8日 15点44分
     */
    private static final String HOME_NAME = "小区";
    /**
     * 区域管理根节点父级节点id xull@aurine.cn 2020年7月8日 15点44分
     */
    private static final String HOME_ROOT = "0";
    // 设备类型
    private final static String CAMERA_TYPE = "6";
    // 区域ID
    private final static String CAMERA_REGION_ID = "1";

    @Override
    public List<ProjectDeviceRegionTreeVo> findTree(String name) {

        List<ProjectDeviceRegion> projectDeviceRegionsList = this.list(new QueryWrapper<ProjectDeviceRegion>().lambda().orderByDesc(ProjectDeviceRegion::getIsDefault));
        if (projectDeviceRegionsList == null) {
            //查询为空插入数据会异常,故这里添加一层判断进行初始化 xull@aurine.cn 2020年7月23日 08点55分
            projectDeviceRegionsList = new ArrayList<>();
        }
        //定义根节点
        ProjectDeviceRegion projectDeviceRegion = new ProjectDeviceRegion();
        projectDeviceRegion.setRegionLevel(0);
        projectDeviceRegion.setRegionId(DataConstants.ROOT);
        projectDeviceRegion.setIsDefault("0");
        projectDeviceRegion.setParRegionId(HOME_ROOT);
        projectDeviceRegion.setRegionName(StringUtils.isEmpty(name) ? HOME_NAME : name);
        projectDeviceRegionsList.add(projectDeviceRegion);

        return getTree(projectDeviceRegionsList);
    }

    /**
     * 验证节点是否还有子节点
     *
     * @param regionId
     * @return
     */
    @Override
    public boolean haveChild(String regionId) {
        int count = this.count(new QueryWrapper<ProjectDeviceRegion>().lambda().eq(ProjectDeviceRegion::getParRegionId, regionId));
        return count >= 1;
    }

    @Override
    public List<ProjectDeviceRegionDetailTreeVo> findTreeByDeviceType(String name, String type, String deviceRegionId) {

        List<ProjectDeviceRegionVo> projectDeviceRegionVos = baseMapper.findByDeviceType(type);

        //定义根节点
        ProjectDeviceRegionVo projectDeviceRegion = new ProjectDeviceRegionVo();
        projectDeviceRegion.setRegionLevel(0);
        projectDeviceRegion.setRegionId(DataConstants.ROOT);
        projectDeviceRegion.setParRegionId(HOME_ROOT);
        projectDeviceRegion.setIsDefault("0");
        projectDeviceRegion.setRegionName(StringUtils.isEmpty(name) ? HOME_NAME : name);
        // 获取未设置绑定区域时的设备数量
        Integer count = projectDeviceInfoService.count(Wrappers.lambdaQuery(ProjectDeviceInfo.class)
                .eq(ProjectDeviceInfo::getDeviceType, type).eq(ProjectDeviceInfo::getDeviceRegionId, deviceRegionId));
        projectDeviceRegion.setNum(count);
        projectDeviceRegionVos.add(projectDeviceRegion);
        return getTreeByDeviceType(projectDeviceRegionVos);
    }

    /**
     * 构建结构树
     *
     * @param projectDeviceRegions
     * @return
     */
    private List<ProjectDeviceRegionTreeVo> getTree(List<ProjectDeviceRegion> projectDeviceRegions) {
        List<ProjectDeviceRegionTreeVo> treeList = projectDeviceRegions.stream()
                .filter(subsystem -> !subsystem.getRegionId().equals(subsystem.getParRegionId()))
                .map(deviceRegion -> {
                    ProjectDeviceRegionTreeVo node = new ProjectDeviceRegionTreeVo();
                    node.setId(deviceRegion.getRegionId());
                    node.setParentId(deviceRegion.getParRegionId());
                    node.setName(deviceRegion.getRegionName());
                    node.setIsDefault(deviceRegion.getIsDefault());
                    node.setLevel(deviceRegion.getRegionLevel());
                    node.setCode(deviceRegion.getRegionCode());
                    return node;
                }).collect(Collectors.toList());
        return TreeUtil.build(treeList, HOME_ROOT);
    }

    /**
     * 构建结构树(含设备数量) xull@aurne.cn 2020年7月8日 15点45分
     *
     * @param projectDeviceRegions
     * @return
     */
    private List<ProjectDeviceRegionDetailTreeVo> getTreeByDeviceType(List<ProjectDeviceRegionVo> projectDeviceRegions) {
        List<ProjectDeviceRegionDetailTreeVo> treeList = projectDeviceRegions.stream()
                .filter(subsystem -> !subsystem.getRegionId().equals(subsystem.getParRegionId()))
                .map(deviceRegion -> {
                    ProjectDeviceRegionDetailTreeVo node = new ProjectDeviceRegionDetailTreeVo();
                    node.setId(deviceRegion.getRegionId());
                    node.setParentId(deviceRegion.getParRegionId());
                    node.setName(deviceRegion.getRegionName());
                    node.setLevel(deviceRegion.getRegionLevel());
                    node.setCode(deviceRegion.getRegionCode());
                    node.setIsDefault(deviceRegion.getIsDefault());
                    node.setNum(deviceRegion.getNum());
                    return node;
                }).collect(Collectors.toList());
        return TreeUtil.build(treeList, HOME_ROOT);
    }

    @Override
    public List<ProjectMonitorNodeVo> findMonitorTreeByDeviceType(String name) {
        List<ProjectDeviceRegionVo> projectDeviceRegionVos = baseMapper.findByDeviceType(CAMERA_TYPE);
        //定义根节点
        ProjectDeviceRegionVo projectDeviceRegion = new ProjectDeviceRegionVo();
        projectDeviceRegion.setRegionLevel(0);
        projectDeviceRegion.setRegionId(DataConstants.ROOT);
        projectDeviceRegion.setParRegionId(HOME_ROOT);
        projectDeviceRegion.setRegionName(StringUtils.isEmpty(name) ? HOME_NAME : name);
        // 获取未设置绑定区域时的设备数量
        Integer count = projectDeviceInfoService.count(Wrappers.lambdaQuery(ProjectDeviceInfo.class)
                .eq(ProjectDeviceInfo::getDeviceType, CAMERA_TYPE).eq(ProjectDeviceInfo::getDeviceRegionId, CAMERA_REGION_ID));
        projectDeviceRegion.setNum(count);
        projectDeviceRegionVos.add(projectDeviceRegion);

        List<ProjectDeviceInfo> list = projectDeviceInfoService.list(Wrappers.lambdaQuery(ProjectDeviceInfo.class)
                .eq(ProjectDeviceInfo::getDeviceType, CAMERA_TYPE));

        // 合并
        List<ProjectMonitorNodeVo> nodes = new ArrayList<ProjectMonitorNodeVo>();

        for (ProjectDeviceRegionVo region : projectDeviceRegionVos) {
            if (region.getNum() == 0) {
                continue;
            }

            ProjectMonitorNodeVo node = new ProjectMonitorNodeVo();

            node.setId(region.getRegionId());
            node.setParentId(region.getParRegionId());
            node.setName(region.getRegionName());
            node.setLevel(region.getRegionLevel());
            node.setCode(region.getRegionCode());
            node.setNum(region.getNum());
            node.setType(ProjectMonitorNodeVo.TYPE_REGION);

            nodes.add(node);
        }

        for (ProjectDeviceInfo info : list) {
            ProjectMonitorNodeVo node = new ProjectMonitorNodeVo();

            node.setId(info.getDeviceId());
            node.setParentId(info.getDeviceRegionId());
            node.setName(info.getDeviceName());
            node.setType(ProjectMonitorNodeVo.TYPE_MONITOR);

            nodes.add(node);
        }

        return TreeUtil.build(nodes, HOME_ROOT);
    }

    /**
     * 初始化默认节点
     * 项目创建后，调用该接口，初始化默认数据
     *
     * @param projectId
     * @return
     */
    @Override
    public boolean initDefaultData(Integer projectId, Integer tenantId) {
        //检查项目下是否已存在默认节点，如果存在则不在添加
        ProjectDeviceRegionVo result = projectDeviceRegionMapper.selectByDefault(projectId);
        if (result != null) {
            return false;
        }
        //查询默认的方案
        ProjectDeviceRegionVo vo = projectDeviceRegionMapper.selectByTemplate();

        /**
         * 修正vo->入库 无主键bug
         * @author: 王伟
         * @since 2020-09-08
         */
        vo.setRegionId(UUID.randomUUID().toString().replace("-", ""));
        vo.setParRegionId("1");


        //去除uid，并修改项目id为指定项目
        return projectDeviceRegionMapper.initInsert(vo, projectId, tenantId);
    }

    @Override
    public Page<ProjectBuildingRegionInfoVo> pageBuildingRegionInfo(Page<ProjectBuildingRegionInfoVo> page) {
        if (projectEntityLevelCfgService.checkIsEnabled()) {
            return projectDeviceRegionMapper.pageBuildingRegionInfoGroup(page);
        }
        return projectDeviceRegionMapper.pageBuildingRegionInfo(page);
    }

    @Override
    public Page<ProjectRegionManagerVo> pageRegionManager(Page<ProjectBuildingRegionInfoVo> page) {
        return baseMapper.pageRegionManager(page);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean moveBuildingsRegion(List<String> buildingIdList, String regionId) {
        if (CollUtil.isEmpty(buildingIdList) || StrUtil.isEmpty(regionId)) {
            throw new RuntimeException("未选择楼栋或区域");
        }
        projectBuildingInfoService.update(new UpdateWrapper<ProjectBuildingInfo>().lambda()
                .set(ProjectBuildingInfo::getRegionId, regionId).in(ProjectBuildingInfo::getBuildingId, buildingIdList));

        List<String> deviceIdList = projectDeviceInfoProxyService.listByBuildingIdList(buildingIdList);
        if (CollUtil.isNotEmpty(deviceIdList)) {
            return projectDeviceInfoService.update(new UpdateWrapper<ProjectDeviceInfo>().lambda()
                    .set(ProjectDeviceInfo::getDeviceRegionId, regionId).in(ProjectDeviceInfo::getDeviceId, deviceIdList));
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateRegionManager(List<String> staffIdList, String regionId) {
        // 对员工ID去重避免前端ID重复导致异常
        Set<String> staffIdSet = new HashSet<>(staffIdList);
        // 先删除原有的管辖关系
        projectStaffRegionService.remove(new QueryWrapper<ProjectStaffRegion>().lambda().eq(ProjectStaffRegion::getRegionId, regionId));

        List<ProjectStaffRegion> staffRegionList = projectStaffRegionService.list();
        // 这里获取到已经有管辖区域的员工-此时需求是员工只能管辖一个区域
        Set<String> existStaffIdList = staffRegionList.stream().map(ProjectStaffRegion::getStaffId).collect(Collectors.toSet());
        staffIdList.removeAll(existStaffIdList);
        if (CollUtil.isNotEmpty(staffIdList)) {
            List<ProjectStaffRegion> projectStaffRegionList = new ArrayList<>();
            staffIdSet.forEach(staffId -> {
                ProjectStaffRegion staffRegion = new ProjectStaffRegion();
                staffRegion.setStaffId(staffId);
                staffRegion.setRegionId(regionId);
                projectStaffRegionList.add(staffRegion);
            });
            return projectStaffRegionService.saveBatch(projectStaffRegionList);
        }
        return true;
    }

    @Override
    public List<String> listBuildingByRegionId(String regionId) {
        int publicRegion = this.count(new QueryWrapper<ProjectDeviceRegion>().lambda()
                .eq(ProjectDeviceRegion::getRegionId, regionId)
                .eq(ProjectDeviceRegion::getRegionName, "公共区域"));
        return baseMapper.listBuildingByRegionId(regionId, projectEntityLevelCfgService.checkIsEnabled(), publicRegion != 0);
    }

    @Override
    public String getChildRegionIds(String regionId) {
        return baseMapper.getChildRegionIdList(regionId);
    }

    @Override
    public ProjectDeviceRegion saveRegion(ProjectDeviceRegion projectDeviceRegion) {
        int count = this.count();
        // 区域总数不能超过99个
        if (count >= 99) {
            throw new RuntimeException("区域最多只能创建99个");
        }
        String uid = UUID.randomUUID().toString().replace("-", "");
        projectDeviceRegion.setRegionId(uid);
        projectDeviceRegion.setIsDefault("0");//通过页面接口生成的阶段，均为非默认节点
        String parRegionId = projectDeviceRegion.getParRegionId();
        ProjectDeviceRegion parRegion = this.getOne(new QueryWrapper<ProjectDeviceRegion>().lambda().eq(ProjectDeviceRegion::getRegionId, parRegionId));
        Integer regionLevel = 0;
        if (parRegion != null) {
            regionLevel = parRegion.getRegionLevel();
        }
        regionLevel++;
        int regionNum = 0;
        if ("1".equals(parRegionId)) {
            regionNum = this.count(new QueryWrapper<ProjectDeviceRegion>().lambda().eq(ProjectDeviceRegion::getRegionLevel, regionLevel)
                    .eq(ProjectDeviceRegion::getRegionName, projectDeviceRegion.getRegionName()));
        } else {
            regionNum = this.count(new QueryWrapper<ProjectDeviceRegion>().lambda().eq(ProjectDeviceRegion::getParRegionId, parRegion.getRegionId())
                    .eq(ProjectDeviceRegion::getRegionLevel, regionLevel).eq(ProjectDeviceRegion::getRegionName, projectDeviceRegion.getRegionName()));
        }
        if (regionNum == 0) {
            this.save(projectDeviceRegion);
            return projectDeviceRegion;
        } else {
            throw new RuntimeException("该区域下已有同名节点");
        }
    }

    @Override
    public List<String> listRegionDeviceByRegionId(String regionId) {
        int num = this.count(new QueryWrapper<ProjectDeviceRegion>().lambda()
                .eq(ProjectDeviceRegion::getRegionName, "公共区域")
                .eq(ProjectDeviceRegion::getRegionId, regionId));
        return baseMapper.listRegionDeviceByRegionId(num > 0, regionId);
    }

}
