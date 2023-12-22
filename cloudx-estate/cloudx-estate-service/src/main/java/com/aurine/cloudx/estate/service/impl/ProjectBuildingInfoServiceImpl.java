
package com.aurine.cloudx.estate.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.aurine.cloudx.common.core.constant.enums.DeviceTypeEnum;
import com.aurine.cloudx.common.core.util.StringUtil;
import com.aurine.cloudx.estate.constant.MessageFrameConstant;
import com.aurine.cloudx.estate.constant.enums.BuildingUnitTypeEnum;
import com.aurine.cloudx.estate.constant.enums.FrameTypeEnum;
import com.aurine.cloudx.estate.entity.*;
import com.aurine.cloudx.estate.mapper.ProjectBuildingInfoMapper;
import com.aurine.cloudx.estate.service.*;
import com.aurine.cloudx.estate.vo.ProjectBuildingInfoVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.common.core.util.R;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 楼栋
 *
 * @author 王伟
 * @date 2020-05-07 16:52:22
 */
@Service
public class ProjectBuildingInfoServiceImpl extends ServiceImpl<ProjectBuildingInfoMapper, ProjectBuildingInfo> implements ProjectBuildingInfoService {

    @Autowired
    private ProjectFrameInfoService projectFrameInfoService;
    @Autowired
    private ProjectUnitInfoService projectUnitInfoService;
    @Autowired
    private ProjectHouseInfoService projectHouseInfoService;
    @Autowired
    private ProjectBuildingInfoService projectBuildingInfoService;
    @Autowired
    private ProjectDeviceInfoService projectDeviceInfoService;

    @Autowired
    private ProjectEntityLevelCfgService projectEntityLevelCfgService;


    /**
     * 保存楼栋
     *
     * @param projectBuildingInfo
     * @return
     */
    @Override
    public boolean saveBuilding(ProjectBuildingInfo projectBuildingInfo) {

        //保存楼栋信息

        //保存楼栋框架结构信息
        ProjectFrameInfo projectFrameInfo = new ProjectFrameInfo();
        projectFrameInfo.setEntityId(projectBuildingInfo.getBuildingId());

        projectFrameInfo.setIsBuilding("1");
        projectFrameInfo.setIsHouse("0");
        projectFrameInfo.setIsUnit("0");
        projectFrameInfo.setEntityName(projectBuildingInfo.getBuildingName());

        projectFrameInfo.setPuid(projectBuildingInfo.getGroup4());
        projectFrameInfo.setLevel(FrameTypeEnum.BUILDING.code);

        projectFrameInfoService.save(projectFrameInfo);
        return save(projectBuildingInfo);

    }

    /**
     * 根据框架号和项目号，保存或修改楼栋信息
     *
     * @param projectBuildingInfoVo
     */
    @Override
    public boolean saveOrUpdateByThirdCode(ProjectBuildingInfoVo projectBuildingInfoVo) {
        ProjectBuildingInfoVo projectBuildingInfo = this.baseMapper.getByCode(projectBuildingInfoVo.getBuildingCode(), projectBuildingInfoVo.getProjectId());
        if (projectBuildingInfo == null) {
            return this.baseMapper.saveBuilding(projectBuildingInfoVo) >= 1;
        } else {
            projectBuildingInfoVo.setSeq(projectBuildingInfo.getSeq());
            projectBuildingInfoVo.setBuildingId(projectBuildingInfo.getBuildingId());
            projectBuildingInfoVo.setPuid(null);
            projectBuildingInfoVo.setRegionId(null);
            return this.updateById(projectBuildingInfoVo);
        }
    }

    /**
     * 保存楼栋
     *
     * @param projectBuildingInfoVo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveBuildingAndUnit(ProjectBuildingInfoVo projectBuildingInfoVo) {

        //樓棟名稱是否已存在
        boolean buildingExist = this.projectFrameInfoService.checkExists(projectBuildingInfoVo.getBuildingName(), projectBuildingInfoVo.getGroup4());
        if (buildingExist) {
            throw new RuntimeException("当前楼栋名称已存在");
        }

        String uid = UUID.randomUUID().toString().replaceAll("-", "");

        ProjectBuildingInfo projectBuildingInfo = new ProjectBuildingInfo();
        BeanUtils.copyProperties(projectBuildingInfoVo, projectBuildingInfo);
        projectBuildingInfo.setBuildingId(uid);


        //保存楼栋框架结构信息
        ProjectFrameInfo projectFrameInfo = new ProjectFrameInfo();
        projectFrameInfo.setEntityId(uid);

        projectFrameInfo.setIsBuilding("1");
        projectFrameInfo.setIsHouse("0");
        projectFrameInfo.setIsUnit("0");
        projectFrameInfo.setEntityName(projectBuildingInfo.getBuildingName());

        projectFrameInfo.setPuid(projectBuildingInfoVo.getPuid());
        projectFrameInfo.setLevel(FrameTypeEnum.BUILDING.code);

        projectFrameInfoService.save(projectFrameInfo);


        //根据楼栋配置，自动生成单元信息
        int totalUnits = projectBuildingInfoVo.getUnitTotal();
//        List<ProjectFrameInfo> unitList = new ArrayList<>();
        List<ProjectUnitInfo> unitList = new ArrayList<>();
        ProjectUnitInfo unitInfo;

        //获取单元编码规则
        List<ProjectEntityLevelCfg> projectEntityLevelCfgList = projectEntityLevelCfgService.list(new QueryWrapper<ProjectEntityLevelCfg>().eq("level", FrameTypeEnum.UNIT.code));
        int codeRule = projectEntityLevelCfgList.get(0).getCodeRule();

        for (int i = 1; i <= totalUnits; i++) {

            unitInfo = new ProjectUnitInfo();

            //数字单元
            if (projectBuildingInfoVo.getUnitNameType().equalsIgnoreCase(BuildingUnitTypeEnum.NUMBER.code)) {
                unitInfo.setUnitName(StringUtil.fillWithZero(codeRule, i) + "单元");//根据codeRule生成数字单元
            } else {//字母单元
                unitInfo.setUnitName(StringUtil.numToLetter(i) + "单元");
            }
            unitList.add(unitInfo);
        }

        projectUnitInfoService.saveBatchUnit(unitList, uid);


        //保存楼栋详情信息
        return this.save(projectBuildingInfo);
    }


    /**
     * 批量保存楼栋
     *
     * @param buildingList
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveBatchBuilding(List<ProjectBuildingInfo> buildingList) {
        List<ProjectFrameInfo> frameList = new ArrayList<>();
        ProjectFrameInfo buildingFrame;

        //生成框架信息
        String uid = UUID.randomUUID().toString().replaceAll("-", "");
        for (ProjectBuildingInfo buildingPO : buildingList) {
            buildingFrame = new ProjectFrameInfo();
            uid = UUID.randomUUID().toString().replaceAll("-", "");

            buildingPO.setBuildingId(uid);
            buildingFrame.setEntityId(uid);
            buildingFrame.setIsBuilding("1");
            buildingFrame.setIsHouse("0");
            buildingFrame.setIsUnit("0");
            buildingFrame.setEntityName(buildingPO.getBuildingName());
            buildingFrame.setPuid(buildingPO.getGroup4());
            buildingFrame.setLevel(FrameTypeEnum.BUILDING.code);
            frameList.add(buildingFrame);
        }

        //保存
        this.saveBatch(buildingList);
        return this.projectFrameInfoService.saveBatch(frameList);
    }


    /**
     * 修改楼栋，如果楼栋已存在房屋，则修改失败
     *
     * @param projectBuildingInfoVo
     * @return
     */
    @Override
    public boolean updateById(ProjectBuildingInfoVo projectBuildingInfoVo) {

        //TODO： 楼栋的修改与单元、房屋的约束，等待3阶段需求确认 王伟 -> 2020-06-20
//        int count = countHouseInBuilding(projectBuildingInfoVo.getBuildingId());
//        if (count >= 1) {
//            throw new RuntimeException("当前楼栋下存在" + count + "个房间，无法修改信息");
//        }

        ProjectBuildingInfo projectBuildingInfo = new ProjectBuildingInfo();
        BeanUtils.copyProperties(projectBuildingInfoVo, projectBuildingInfo);

        //获取框架信息
        ProjectFrameInfo projectFrameInfo = projectFrameInfoService.getById(projectBuildingInfo.getBuildingId());
        projectFrameInfo.setPuid(projectBuildingInfoVo.getPuid());
        projectFrameInfo.setEntityName(projectBuildingInfoVo.getBuildingName());

        //更新框架
        projectFrameInfoService.updateById(projectFrameInfo);

        //更新楼栋信息
        return this.updateById(projectBuildingInfo);
    }

    /**
     * 获取楼栋信息
     *
     * @param id
     * @return
     */
    @Override
    public ProjectBuildingInfoVo getById(String id) {

        ProjectBuildingInfoVo projectBuildingInfoVo = new ProjectBuildingInfoVo();

        ProjectBuildingInfo projectBuildingInfo = super.getById(id);
        ProjectFrameInfo projectFrameInfo = projectFrameInfoService.getById(id);

        BeanUtils.copyProperties(projectBuildingInfo, projectBuildingInfoVo);
        //获取父类信息
        projectBuildingInfoVo.setPuid(projectFrameInfo.getPuid());

        return projectBuildingInfoVo;
    }

    /**
     * 获取一个楼栋的房屋数量
     *
     * @param buildingId
     * @return
     */
    @Override
    public int countHouseInBuilding(String buildingId) {
        int houseCount = 0;
        // 获取楼栋的单元
        List<ProjectFrameInfo> unitList = projectFrameInfoService.listByPuid(buildingId);
        if (CollectionUtil.isNotEmpty(unitList)) {
            for (ProjectFrameInfo unitEntity : unitList) {
                houseCount = houseCount + projectFrameInfoService.countByPuid(unitEntity.getEntityId());
            }

            return houseCount;
        } else {//无单元
            return 0;
        }

    }

    /**
     * 删除楼栋
     *
     * @param buildingId
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R deleteBuilding(String buildingId) {
        int usedCount = this.projectHouseInfoService.countHouseUsed(buildingId, null);
        if (usedCount >= 1) {
            return R.failed(MessageFrameConstant.BUILDING_DEL_HAVE_PERSON);
        }
        int deviceNum = projectDeviceInfoService.count(new QueryWrapper<ProjectDeviceInfo>().lambda().eq(ProjectDeviceInfo::getBuildingId, buildingId));
        if (deviceNum != 0) {
            return R.failed("楼栋下存在设备，无法删除");
        }
        //删除单元，如果存在房屋，禁止删除
        boolean result = this.projectUnitInfoService.deleteByBuildingId(buildingId);
        if (result) {
            //删除楼栋框架
            projectFrameInfoService.removeById(buildingId);
            //删除楼栋
            return R.ok(removeById(buildingId));
        } else {
            return R.failed("楼栋下存在房屋，无法删除");
        }
    }

    /**
     * 获取带组团信息的楼栋列表
     *
     * @return
     */
    @Override
    public List<ProjectBuildingInfo> listBuildingWithGroup(String name) {
        return this.baseMapper.listWithGroup(name);
    }

    @Override
    public int countBuilding() {
        return this.count();
    }

    /**
     * 通过设备的第三方ID,添加整栋楼、单元的框架号
     *
     * @param deviceInfo
     * @param thirdCode
     * @return
     */
    @Override
    public boolean addThirdCode(ProjectDeviceInfo deviceInfo, String thirdCode) {
        //长度需要大于4位 楼栋/单元/其他编号
        if (StringUtils.isEmpty(thirdCode) || thirdCode.length() <= 3) {
            return false;
        }

        //只有梯口机才能够覆写框架号
        if (!deviceInfo.getDeviceType().equalsIgnoreCase(DeviceTypeEnum.LADDER_WAY_DEVICE.getCode())) {
            return false;
        }

        String unitCode = thirdCode.substring(0, 4);
        String buildingCode = thirdCode.substring(0, 2);

        //房间号 = 单元框架号+房间号

        //通过设备获取楼栋、单元、房屋
        ProjectBuildingInfo building = this.getById(deviceInfo.getBuildingId());
        ProjectFrameInfo frameInfo = projectFrameInfoService.getById(deviceInfo.getBuildingId());

        if (building != null && StringUtils.isEmpty(building.getBuildingCode())) {
            building.setBuildingCode(buildingCode);
            frameInfo.setEntityCode(buildingCode);
            this.updateById(building);
            projectFrameInfoService.updateById(frameInfo);
        }

        ProjectUnitInfo unit = projectUnitInfoService.getById(deviceInfo.getUnitId());
        frameInfo = projectFrameInfoService.getById(deviceInfo.getBuildingId());
        if (unit != null && StringUtils.isEmpty(unit.getUnitCode())) {
            unit.setUnitCode(unitCode);
            frameInfo.setEntityCode(unitCode);
            projectUnitInfoService.updateById(unit);
            projectFrameInfoService.updateById(frameInfo);
        }

        //获取房屋列表，批量修改
        List<ProjectHouseInfo> houseList = projectHouseInfoService.list(new QueryWrapper<ProjectHouseInfo>().lambda().eq(ProjectHouseInfo::getBuildingUnit, unit.getUnitId()));
        List<ProjectFrameInfo> frameInfoList = projectFrameInfoService.list(new QueryWrapper<ProjectFrameInfo>().lambda().eq(ProjectFrameInfo::getPuid, unit.getUnitId()));

        houseList.forEach(e -> e.setHouseCode(unitCode + e.getHouseName()));
        frameInfoList.forEach(e -> e.setEntityCode(unitCode + e.getEntityName()));

        projectFrameInfoService.updateBatchById(frameInfoList);
        return projectHouseInfoService.updateBatchById(houseList);
    }
}
