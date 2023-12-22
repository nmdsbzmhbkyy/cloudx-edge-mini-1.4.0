package com.aurine.cloudx.estate.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.aurine.cloudx.common.core.constant.enums.DeviceTypeEnum;
import com.aurine.cloudx.common.core.util.StringUtil;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.constant.MessageFrameConstant;
import com.aurine.cloudx.estate.constant.enums.BuildingUnitTypeEnum;
import com.aurine.cloudx.estate.constant.enums.FrameTypeEnum;
import com.aurine.cloudx.estate.entity.*;
import com.aurine.cloudx.estate.mapper.ProjectBuildingInfoMapper;
import com.aurine.cloudx.estate.service.*;
import com.aurine.cloudx.estate.vo.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.common.core.util.R;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * (WebProjectBuildingInfoServiceImpl)
 *
 * @author xull
 * @version 1.0.0
 * @date 2020/12/8 17:28
 */
@Service
public class ProjectBuildingInfoServiceImpl extends ServiceImpl<ProjectBuildingInfoMapper, ProjectBuildingInfo> implements ProjectBuildingInfoService {

    @Resource
    private ProjectFrameInfoService projectFrameInfoService;
    @Resource
    private ProjectUnitInfoService projectUnitInfoService;
    @Resource
    private ProjectHouseInfoService projectHouseInfoService;

    @Resource
    private ProjectDeviceInfoService projectDeviceInfoService;

    @Resource
    private ProjectEntityLevelCfgService projectEntityLevelCfgService;

    @Resource
    private ProjectDeviceRegionService projectDeviceRegionService;
    @Resource
    private ProjectDeviceCollectService projectDeviceCollectService;


    /*无需调整*/

    /**
     * 批量添加房屋
     *
     * @param buildingBatchVo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveBatch(ProjectBuildingBatchVo buildingBatchVo) {

        List<String> buildingCodeList = list().stream().map(ProjectBuildingInfo::getBuildingCode).collect(Collectors.toList());

        //检查是否有名称重复，如果存在重复，重设楼栋名称 （已有名称 + (批量添加)）
        boolean buildingExist = false;
        for (ProjectBuildingBatchBuildingVo buildingVo : buildingBatchVo.getBuildingList()) {
            // 通过反复增加名称，避免重名
            while (projectFrameInfoService.checkExists(buildingVo.getBuildingName(), buildingVo.getGroup4(), FrameTypeEnum.BUILDING.code)) {
                buildingVo.setBuildingName(buildingVo.getBuildingName() + "(模板添加)");
            }
            if (buildingVo.getBuildingName().length() >= 30) {
                throw new RuntimeException("项目中存在过多模板添加的重名楼栋，请先处理后再进行添加");
            }
            if (CollUtil.isNotEmpty(buildingCodeList) && buildingCodeList.contains(buildingVo.getBuildingCode())) {
                throw new RuntimeException("项目中楼栋编号'" + buildingVo.getBuildingCode() + "'已存在，请修改后再进行添加");
            }
        }

        //init
        ProjectBuildingInfo buildingPo;
        ProjectUnitInfo unitPo;

        List<ProjectBuildingInfo> buildingPoList = new ArrayList<>();
        List<ProjectUnitInfo> unitPoList = new ArrayList<>();
        List<ProjectHouseInfo> housePoList = new ArrayList<>();
        ProjectHouseInfo housePo;

        String buildingId, unitId;

        // 获取楼栋编号位数限制
        int buildRule = projectEntityLevelCfgService.getCodeRuleByLevel("3");
        Set<String> buildingCodeSet = new HashSet<>();
        //添加楼栋
        for (ProjectBuildingBatchBuildingVo buildingVo : buildingBatchVo.getBuildingList()) {

            buildingId = UUID.randomUUID().toString().replaceAll("-", "");
            buildingPo = new ProjectBuildingInfo();
            BeanUtils.copyProperties(buildingVo, buildingPo);

            if (StringUtil.isEmpty(buildingVo.getBuildingCode())) {
                throw new RuntimeException("楼栋编号未填写");
            } else if (buildingVo.getBuildingCode().equals(String.format("%0" + buildRule + "d", 0))) {
                throw new RuntimeException("楼栋编号不符合要求不能全由0组成");
            } else if (!buildingVo.getBuildingCode().matches("^[0-9]{" + buildRule + "}$")) {
                throw new RuntimeException("楼栋编号不符合要求，必须为" + buildRule + "位数字组成");
            }
            // 这里楼栋编号长度如果没有达到要求则自动在前面补零
            if (StringUtil.isNotEmpty(buildingVo.getBuildingCode())) {
                buildingVo.setBuildingCode(String.format("%0" + buildRule + "d", Integer.parseInt(buildingVo.getBuildingCode())));
            }
            //楼栋编码
            String buildingCode = null;
            //查询组团4的信息
            List<ProjectFrameInfo> group4List = projectFrameInfoService.list(new QueryWrapper<ProjectFrameInfo>().eq("entityId", buildingVo.getGroup4()));
            if (CollectionUtil.isNotEmpty(group4List)) {
                ProjectFrameInfo group4 = group4List.get(0);
                buildingCode = group4.getEntityCode() + buildingVo.getBuildingCode();
                int repeatTimes = this.count(new QueryWrapper<ProjectBuildingInfo>().lambda().eq(ProjectBuildingInfo::getBuildingCode, buildingCode));
                if (repeatTimes != 0 || buildingCodeSet.contains(buildingCode)) {
                    throw new RuntimeException("楼栋编号：" + buildingVo.getBuildingCode().replaceAll("0", "") + " 已重复");
                }
                buildingCodeSet.add(buildingCode);
            } else {
                buildingCode = buildingVo.getBuildingCode();
            }


            //设置楼栋编码
            buildingPo.setBuildingCode(buildingCode);
            buildingPo.setFrameNo(buildingVo.getBuildingCode());
            buildingPo.setBuildingId(buildingId);

            saveBuilding(buildingPo, buildingCode, buildingVo.getBuildingCode());

            //添加单元
            for (ProjectBuildingBatchUnitVo unitVo : buildingVo.getUnitList()) {
                housePoList = new ArrayList<>();

                unitId = UUID.randomUUID().toString().replaceAll("-", "");
                unitPo = new ProjectUnitInfo();
                unitPo.setUnitId(unitId);
                unitPo.setUnitName(unitVo.getUnitName());


                this.projectUnitInfoService.saveUnit(unitPo, buildingId, buildingVo.getUnitNameType());

                for (ProjectHouseInfoVo houseVo : unitVo.getHouseList()) {
                    if (houseVo.getEnable()) {
                        housePo = new ProjectHouseInfo();
                        BeanUtils.copyProperties(houseVo, housePo);
                        if (getPoliceIsEnable()) {
                            housePo.setHouseLabelCode("10");
                        }
                        housePo.setBuildingUnit(unitId);
                        housePoList.add(housePo);
                    }
                }

                //添加房屋
                projectHouseInfoService.saveBatchHouse(housePoList, unitId);
            }
        }
        return true;
    }

    /*无需调整*/

    /**
     * 保存楼栋
     *
     * @param projectBuildingInfo
     * @param buildingCode
     * @param buidingFrameNo
     * @return
     */
    @Override
    public boolean saveBuilding(ProjectBuildingInfo projectBuildingInfo, String buildingCode, String buidingFrameNo) {

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

        projectFrameInfo.setEntityCode(buildingCode);
        projectFrameInfo.setFrameNo(buidingFrameNo);
        projectFrameInfoService.save(projectFrameInfo);

        //添加楼栋 区域默认设置为公共区域
        ProjectDeviceRegion region = projectDeviceRegionService.getOne(new QueryWrapper<ProjectDeviceRegion>()
                .eq("projectId", ProjectContextHolder.getProjectId())
                .eq("regionName", "公共区域"));
        if (region != null) {
            projectBuildingInfo.setRegionId(region.getRegionId());
        }

        return save(projectBuildingInfo);

    }

    /*无需调整*/

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

    /*无需调整*/

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
        int count = this.count(new QueryWrapper<ProjectBuildingInfo>().lambda().eq(ProjectBuildingInfo::getFrameNo, projectBuildingInfoVo.getBuildingCode()));
        if (count != 0) {
            throw new RuntimeException("楼栋编号已存在");
        }

        // 默认设置架空层为公共楼层
        if (StrUtil.isEmpty(projectBuildingInfoVo.getPublicFloors()) && "1".equals(projectBuildingInfoVo.getHasStiltFloor())) {
            projectBuildingInfoVo.setPublicFloors(projectBuildingInfoVo.getFloorGround().toString());
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

        String buildingCode = null;
        //获取楼栋上一级组团4的信息
        List<ProjectFrameInfo> group4List = projectFrameInfoService.list(new QueryWrapper<ProjectFrameInfo>().eq("entityId", projectBuildingInfoVo.getPuid()));
        if (CollectionUtil.isNotEmpty(group4List)) {
            ProjectFrameInfo group4 = group4List.get(0);
            buildingCode = group4.getEntityCode() + projectBuildingInfo.getBuildingCode();
        } else {
            buildingCode = projectBuildingInfo.getBuildingCode();
        }


        //设置楼栋编码，框架编码
        projectBuildingInfo.setBuildingCode(buildingCode);
        projectBuildingInfo.setFrameNo(projectBuildingInfoVo.getBuildingCode());

        //projectFrameInfo.setEntityCode(projectBuildingInfo.getBuildingCode());
        projectFrameInfo.setEntityCode(buildingCode);
        projectFrameInfo.setFrameNo(projectBuildingInfoVo.getBuildingCode());

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
        List<ProjectEntityLevelCfg> projectEntityLevelCfgList = projectEntityLevelCfgService.list(new QueryWrapper<ProjectEntityLevelCfg>()
                .eq("level", FrameTypeEnum.UNIT.code).eq("projectId", ProjectContextHolder.getProjectId()));
        int codeRule = projectEntityLevelCfgList.get(0).getCodeRule();

        for (int i = 1; i <= totalUnits; i++) {

            unitInfo = new ProjectUnitInfo();

            //数字单元
            if (projectBuildingInfoVo.getUnitNameType().equalsIgnoreCase(BuildingUnitTypeEnum.NUMBER.code)) {
                unitInfo.setUnitName(StringUtil.fillWithZero(codeRule, i) + "单元");//根据codeRule生成数字单元
                unitInfo.setUnitCode(StringUtil.fillWithZero(codeRule, i));
            } else {//字母单元
                unitInfo.setUnitName(StringUtil.numToLetter(i) + "单元");
                unitInfo.setUnitCode(StringUtil.numToLetter(i));
            }
            unitList.add(unitInfo);
        }

        projectUnitInfoService.saveBatchUnit(unitList, uid, projectBuildingInfoVo.getUnitNameType());

        //添加楼栋 区域默认设置为公共区域
        ProjectDeviceRegion region = projectDeviceRegionService.getOne(new QueryWrapper<ProjectDeviceRegion>()
                .eq("projectId", ProjectContextHolder.getProjectId())
                .eq("regionName", "公共区域"));
        ;
        if (region != null) {
            projectBuildingInfo.setRegionId(region.getRegionId());
        }

        //保存楼栋详情信息
        return this.save(projectBuildingInfo);
    }



    /*无需调整*/

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


    /*无需调整*/

    /**
     * 修改楼栋，如果楼栋已存在房屋，则修改失败
     *
     * @param projectBuildingInfoVo
     * @return
     */
    @Override
    public boolean updateById(ProjectBuildingInfoVo projectBuildingInfoVo) {

//        int count = countHouseInBuilding(projectBuildingInfoVo.getBuildingId());
//        if (count >= 1) {
//            throw new RuntimeException("已存在房屋，无法修改楼层数量");
//        }

        //樓棟名稱是否已存在
        boolean buildingExist = projectFrameInfoService.count(new QueryWrapper<ProjectFrameInfo>().lambda()
                .eq(ProjectFrameInfo::getEntityName, projectBuildingInfoVo.getBuildingName())
                .eq(StringUtils.isNotEmpty(projectBuildingInfoVo.getGroup4()), ProjectFrameInfo::getPuid, projectBuildingInfoVo.getGroup4())
                .ne(ProjectFrameInfo::getEntityId, projectBuildingInfoVo.getBuildingId())) >= 1;

        if (buildingExist) {
            throw new RuntimeException("当前楼栋名称已存在");
        }


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
        frameInfo = projectFrameInfoService.getById(deviceInfo.getUnitId());
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

    /**
     * 查询公安是否启用
     *
     * @return
     */
    @Override
    public boolean getPoliceIsEnable() {
        ProjectDeviceCollect projectDeviceCollect = projectDeviceCollectService.getOne(Wrappers.lambdaQuery(ProjectDeviceCollect.class)
                .eq(ProjectDeviceCollect::getProjectId, ProjectContextHolder.getProjectId())
                .eq(ProjectDeviceCollect::getDeviceType, "4"));
        if (projectDeviceCollect == null) {
            return false;
        }
        if ("0".equals(projectDeviceCollect.getAttrValue())) {
            return false;
        }
        return true;
    }

    ;


    /**
     * 根据楼栋id获取实际楼层和楼层编号映射表
     *
     * @param buildingId 楼栋id
     * @return 映射表
     */
    private HashMap<String, String> getFloorMap(String buildingId) {
        HashMap<String, String> floorMap = new HashMap<>();
        ProjectBuildingInfoVo building = getById(buildingId);
        // 查询不到楼栋
        if (building == null) {
            return null;
        }
        Integer floorGround = building.getFloorGround();
        Integer floorUnderground = building.getFloorUnderground();
        String hasStiltFloor = building.getHasStiltFloor();
        // 楼层编号字符串
        String floorNumberStr = building.getFloorNumber();
        // 楼层编号数组
        String[] floorNumbers = {};
        // 楼层编号下标
        int flag = 0;
        if (!StrUtil.isEmpty(floorNumberStr)) {
            floorNumbers = floorNumberStr.split(",");
        }
        // 实际楼层数与楼层编号数不匹配
        if (floorNumbers.length > 0 && floorNumbers.length != (floorGround + floorUnderground + Integer.parseInt(hasStiltFloor))) {
            return null;
        }
        // 地上部分
        if (floorGround > 0) {
            for (int i = floorGround; i >= 1; i--) {
                floorMap.put(String.valueOf(i), floorNumbers[flag++]);
            }
        }
        // 架空层
        if ("1".equals(hasStiltFloor)) {
            floorMap.put("G", floorNumbers[flag++]);
        }
        // 地下部分
        if (floorUnderground > 0) {

            for (int i = 1; i <= floorUnderground; i++) {
                floorMap.put(String.valueOf(-i), floorNumbers[flag++]);
            }
        }
        return floorMap;
    }
}
