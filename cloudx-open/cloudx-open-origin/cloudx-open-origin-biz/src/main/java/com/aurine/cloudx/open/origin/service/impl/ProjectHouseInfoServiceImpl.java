package com.aurine.cloudx.open.origin.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.open.common.entity.vo.HouseInfoVo;
import com.aurine.cloudx.open.origin.constant.enums.FrameTypeEnum;
import com.aurine.cloudx.open.origin.dto.ProjectHouseDTO;
import com.aurine.cloudx.open.origin.entity.*;
import com.aurine.cloudx.open.origin.mapper.ProjectHouseInfoMapper;
import com.aurine.cloudx.open.origin.service.*;
import com.aurine.cloudx.open.origin.vo.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 房屋
 *
 * @author 王伟
 * @date 2020-05-08 16:29:18
 */
@Service
public class ProjectHouseInfoServiceImpl extends ServiceImpl<ProjectHouseInfoMapper, ProjectHouseInfo> implements ProjectHouseInfoService {

//    @Resource
//    private ProjectServiceService projectServiceService;
//    @Resource
//    private ProjectConfigService projectConfigService;

    @Resource
    private ProjectFrameInfoService projectFrameInfoService;

    @Resource
    private ProjectHouseServiceService projectHouseServiceService;

    @Resource
    private ProjectUnitInfoService projectUnitInfoService;

    @Resource
    private ProjectHousePersonRelService housePersonRelService;

    /*需要调整-已注释增值服务相关代码*/

    /**
     * 保存房屋
     *
     * @param projectHouseInfoVo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveVo(ProjectHouseInfoVo projectHouseInfoVo) {

        //验证房号是否已存在
        if (projectFrameInfoService.checkExists(projectHouseInfoVo.getHouseName(), projectHouseInfoVo.getBuildingUnit())) {
            throw new RuntimeException(projectHouseInfoVo.getHouseName() + "在该单元下已存在，请更换为其他名称");
        }

        String uid = UUID.randomUUID().toString().replaceAll("-", "");


        ProjectUnitInfo unitInfo = projectUnitInfoService.getById(projectHouseInfoVo.getBuildingUnit());

        //存储框架数据
        ProjectFrameInfo projectFrameInfo = new ProjectFrameInfo();
        projectFrameInfo.setEntityId(uid);
        projectFrameInfo.setIsBuilding("0");
        projectFrameInfo.setIsHouse("1");
        projectFrameInfo.setIsUnit("0");
        projectFrameInfo.setEntityName(projectHouseInfoVo.getHouseName());
        projectFrameInfo.setPuid(projectHouseInfoVo.getBuildingUnit());
        projectFrameInfo.setLevel(FrameTypeEnum.HOUSE.code);
        /**
         *  添加楼栋时，如果单元存在框架好，房屋根据房号生成框架好
         *  依照 bug（需求？） 2715
         * @since 2021-01-22
         */
        if (StringUtils.isNotEmpty(unitInfo.getUnitCode())) {
            projectFrameInfo.setEntityCode(unitInfo.getUnitCode() + projectHouseInfoVo.getHouseName());
            projectFrameInfo.setFrameNo(projectHouseInfoVo.getHouseName());
        }

        projectFrameInfoService.save(projectFrameInfo);


        //存储房屋详细信息
        ProjectHouseInfo projectHouseInfo = new ProjectHouseInfo();
        BeanUtils.copyProperties(projectHouseInfoVo, projectHouseInfo);
        projectHouseInfo.setHouseId(uid);

        /**
         *  添加楼栋时，如果单元存在框架好，房屋根据房号生成框架好
         *  依照 bug（需求？） 2715
         * @since 2021-01-22
         */
        if (StringUtils.isNotEmpty(unitInfo.getUnitCode())) {
            projectHouseInfo.setHouseCode(unitInfo.getUnitCode() + projectHouseInfoVo.getHouseName());
        }

        //获取默认房屋增值服务状态 guhl.@aurine.cn
        /*String serviceInitalStatus = projectConfigService.getConfig().getServiceInitalStatus();
        if (serviceInitalStatus.equals(ProjectConfigConstant.SERVICE_INITAL_STATUS_OPEN)) {
            saveHouseService(uid);
        }*/
        return this.save(projectHouseInfo);
    }

    /*需要调整-已注释增值服务相关代码*/

    /**
     * 批量保存房屋
     *
     * @param houseList
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveBatchHouse(List<ProjectHouseInfo> houseList, String unitId) {
        List<ProjectFrameInfo> frameList = new ArrayList<>();
        ProjectFrameInfo houseFrame;

        if (CollectionUtil.isEmpty(houseList)) {
            return true;
        }

        ProjectUnitInfo unitInfo = projectUnitInfoService.getById(unitId);

        //生成框架信息
        String uid = UUID.randomUUID().toString().replaceAll("-", "");
        for (ProjectHouseInfo housePO : houseList) {
            houseFrame = new ProjectFrameInfo();
            uid = UUID.randomUUID().toString().replaceAll("-", "");

            housePO.setHouseId(uid);
            houseFrame.setEntityId(uid);
            houseFrame.setIsBuilding("0");
            houseFrame.setIsHouse("1");
            houseFrame.setIsUnit("0");
            houseFrame.setEntityName(housePO.getHouseName());
            houseFrame.setPuid(unitId);
            houseFrame.setLevel(FrameTypeEnum.HOUSE.code);

            if (StringUtils.isNotEmpty(unitInfo.getUnitCode())) {
                housePO.setHouseCode(unitInfo.getUnitCode() + housePO.getHouseName());
                houseFrame.setEntityCode(unitInfo.getUnitCode() + housePO.getHouseName());
                houseFrame.setFrameNo(housePO.getHouseName());
            }


            frameList.add(houseFrame);
        }

        //获取默认房屋增值服务状态 guhl.@aurine.cn
       /* String serviceInitalStatus = projectConfigService.getConfig().getServiceInitalStatus();
        if (serviceInitalStatus.equals(ProjectConfigConstant.SERVICE_INITAL_STATUS_OPEN)) {
            houseList.forEach(house -> {
                saveHouseService(house.getHouseId());
            });
        }*/

        //保存
        this.saveBatch(houseList);
        return this.projectFrameInfoService.saveBatch(frameList);

    }

    /*需要调整-因为增值服务，注释整个方法*/

    /**
     * 创建房屋时设置默认的增值服务
     *
     * @param houseId
     */
    private void saveHouseService(String houseId) {
       /* //获取项目增值服务列表 guhl.@aurine.cn
        List<ProjectServiceInfoVo> list = projectServiceService.getHouseServiceByProjectId(ProjectContextHolder.getProjectId());
        //房屋增值服务列表 guhl.@aurine.cn
        List<ProjectHouseService> projectHouseServices = new ArrayList<>();
        list.forEach(e -> {
            ProjectHouseService projectHouseService = new ProjectHouseService();
            projectHouseService.setServiceId(e.getServiceId());
            projectHouseService.setHouseId(houseId);
            projectHouseServices.add(projectHouseService);
            //为房屋中的住户配置增值服务
            //IntercomFactoryProducer.getFactory(e.getServiceCode()).getIntercomService().addByHouse(houseId, ProjectContextHolder.getProjectId());
        });
        projectHouseServiceService.saveBatch(projectHouseServices);*/
    }

    /*无需调整*/
    @Override
    public IPage<ProjectHouseInfoVo> findPage(IPage<ProjectHouseInfoVo> page, ProjectHouseInfoVo houseInfoVo) {
        return baseMapper.select(page, houseInfoVo.getBuildingId(), houseInfoVo.getBuildingName(), houseInfoVo.getUnitId(), houseInfoVo.getUnitName(), houseInfoVo.getHouseName(), houseInfoVo.getIsUse());
    }


    /*无需调整*/

    /**
     * 更新房屋
     *
     * @param projectHouseInfoVo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateById(ProjectHouseInfoVo projectHouseInfoVo) {

        //获取原始房屋信息
        ProjectHouseInfo projectHouseinfo = this.getById(projectHouseInfoVo.getHouseId());

        //验证房号是否已存在
        boolean isSameName = projectHouseinfo.getHouseName().equals(projectHouseInfoVo.getHouseName());
        boolean isExits = projectFrameInfoService.checkExists(projectHouseInfoVo.getHouseName(), projectHouseInfoVo.getBuildingUnit());

        if (isExits && !isSameName) {
            throw new RuntimeException(projectHouseInfoVo.getHouseName() + "在该单元下已存在，请更换为其他名称");
        }


        //从VO类获取框架数据
        ProjectFrameInfo projectFrameInfo = new ProjectFrameInfo();
        projectFrameInfo.setEntityId(projectHouseInfoVo.getHouseId());
        projectFrameInfo.setEntityName(projectHouseInfoVo.getHouseName());
        projectFrameInfo.setPuid(projectHouseInfoVo.getBuildingUnit());

        /**
         *  获取单元框架号，并拼接房屋框架号
         * @Auther 王伟
         * @since 2021-03-12
         */

        ProjectUnitInfo unit = projectUnitInfoService.getById(projectHouseinfo.getBuildingUnit());
        if (StringUtils.isNotEmpty(unit.getUnitCode())) {
            String frameCode = unit.getUnitCode() + projectHouseInfoVo.getHouseName();

            projectFrameInfo.setEntityCode(frameCode);
            projectFrameInfo.setFrameNo(projectHouseInfoVo.getHouseName());

            projectHouseInfoVo.setHouseCode(frameCode);
        }

        //根据uid更新框架数据
        projectFrameInfoService.updateById(projectFrameInfo);
        //更新房屋信息相关数据
        ProjectHouseInfo house = baseMapper.selectById(projectHouseInfoVo.getHouseId());
        BeanUtils.copyProperties(projectHouseInfoVo, house);
        return updateById(house);
    }

    /*无需调整*/

    /**
     * 根据框架号和项目号，保存或修改楼栋信息
     *
     * @param projectHouseInfoVo
     */
    @Override
    public boolean saveOrUpdateByThirdCode(ProjectHouseInfoVo projectHouseInfoVo) {
        ProjectHouseInfoVo projectHouseInfo = this.baseMapper.getByCode(projectHouseInfoVo.getHouseCode(), projectHouseInfoVo.getProjectId());
        if (projectHouseInfo == null) {
            return this.baseMapper.saveHouse(projectHouseInfoVo) >= 1;
        } else {
            projectHouseInfoVo.setBuildingUnit(null);
            projectHouseInfoVo.setHouseId(projectHouseInfo.getHouseId());
            projectHouseInfoVo.setHouseDesginId(null);
            projectHouseInfoVo.setHouseDesginName(null);
            return this.updateById(projectHouseInfoVo);
        }
    }

    /*需要调整-注释和增值服务相关代码*/

    /**
     * 删除
     *
     * @param id
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeHouseAndFrameById(String id) {
        int count = housePersonRelService.count(new QueryWrapper<ProjectHousePersonRel>()
                .lambda().eq(ProjectHousePersonRel::getHouseId, id));
        if (count != 0) {
            return true;
        }
        //删除房屋框架信息
        projectFrameInfoService.removeById(id); //frameId与详细信息ID相同
        //删除房屋相关增值服务
//        projectHouseServiceService.remove(Wrappers.lambdaUpdate(ProjectHouseService.class).eq(ProjectHouseService::getHouseId, id));
        //删除房屋远端相关增值服务
        //projectHouseServiceService.removeRemoteHouseService(id);
        //删除房屋详细信息
        return this.removeById(id);
    }

    /*无需调整*/

    /**
     * 获取
     *
     * @param id
     * @return
     */
    @Override
    public ProjectHouseInfoVo getVoById(String id) {

        return baseMapper.getHouseInfoVo(id);
    }

    /*无需调整*/

    /**
     * <p>
     * 获取房屋所有住户
     * </p>
     *
     * @param
     * @return
     * @throws
     * @author:王良俊
     */
    @Override
    public IPage<ProjectHouseResidentVo> getHouseResidents(IPage<ProjectHouseResidentVo> page, String houseId) {
        return baseMapper.getHouseResidents(page, houseId, ProjectContextHolder.getProjectId());
    }

    /*无需调整*/
    @Override
    public List<ProjectHouseResidentVo> getHouseResidents(String houseId) {
        return baseMapper.getHouseResidents(houseId, ProjectContextHolder.getProjectId());
    }

    /*无需调整*/
    @Override
    public List<ProjectHouseResidentVo> getHouseResidentsWithoutStatus(String houseId) {
        return baseMapper.getHouseResidentsWithoutStatus(houseId);
    }

    /*无需调整*/
    @Override
    public IPage<ProjectHouseHisRecordVo> getHouseRecord(IPage<ProjectHouseResidentVo> page, String houseId) {
        return baseMapper.getHouseRecord(page, houseId, ProjectContextHolder.getProjectId());
    }

    /*无需调整*/
    @Override
    public boolean checkHouseDesignExist(String houseDesignId) {
        int count = this.count(new QueryWrapper<ProjectHouseInfo>().lambda().eq(ProjectHouseInfo::getHouseDesginId, houseDesignId));
        return count > 0;
    }

    /*无需调整*/

    /**
     * 获取房屋列表，带用户入住信息
     *
     * @param buildingId
     * @param unitId
     * @return
     */
    @Override
    public List<ProjectHouseDTO> listHouseWithPersonNum(String buildingId, String unitId) {
        return this.baseMapper.getHouseRecordWithPerson(buildingId, unitId, -1);
    }

    /*无需调整*/

    /**
     * 计算已入住的房屋数
     *
     * @param buildingId
     * @param unitId
     * @return
     */
    @Override
    public Integer countHouseUsed(String buildingId, String unitId) {
        return this.baseMapper.countHouseRecordWithPerson(buildingId, unitId, 1);
    }

    /*无需调整*/

    /**
     * 计算未入住的房屋数
     *
     * @param buildingId
     * @param unitId
     * @return
     */
    @Override
    public Integer countHouseUnuse(String buildingId, String unitId) {
        return this.baseMapper.countHouseRecordWithPerson(buildingId, unitId, 0);
    }

    /*无需调整*/

    /**
     * 删除房屋
     *
     * @param unitId
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteHouseByUnitId(String unitId) {
        //房屋下如果有人，不能删除
        int usedCount = this.countHouseUsed(null, unitId);
        if (usedCount >= 1) {
            return false;
        }

        List<ProjectFrameInfo> houseFrameList = projectFrameInfoService.list(new QueryWrapper<ProjectFrameInfo>().lambda().eq(ProjectFrameInfo::getPuid, unitId));
        List<String> idList = houseFrameList.stream().map(ProjectFrameInfo::getEntityId).collect(Collectors.toList());

        if (CollectionUtil.isEmpty(houseFrameList)) {
            return true;
        }

        projectFrameInfoService.removeByIds(idList);

        idList.forEach(e -> {
            projectHouseServiceService.remove(Wrappers.lambdaUpdate(ProjectHouseService.class).eq(ProjectHouseService::getHouseId, e));
            //projectHouseServiceService.removeRemoteHouseService(e);
        });
        return removeByIds(idList);
    }

    /*需要调整-已注释增值服务相关代码*/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeBatch(List<String> houseIdList) {

        if (CollUtil.isNotEmpty(houseIdList)) {
//            houseIdList.forEach(this::removeHouseAndFrameById);
            List<String> removeAbleHouseIdList = baseMapper.getRemoveAbleHouseIdList(houseIdList);


            for (int i = 0; i < removeAbleHouseIdList.size(); i++) {
                Integer num = baseMapper.getCount(removeAbleHouseIdList.get(i));
                if (num != null && num > 0) {
                    removeAbleHouseIdList.remove(i);
                }
            }
            if (removeAbleHouseIdList.isEmpty()) {
                return false;
            }

            if (CollUtil.isNotEmpty(removeAbleHouseIdList)) {
                projectFrameInfoService.remove(new QueryWrapper<ProjectFrameInfo>().lambda()
                        .in(ProjectFrameInfo::getEntityId, removeAbleHouseIdList));
               /* projectHouseServiceService.remove(new QueryWrapper<ProjectHouseService>().lambda()
                        .in(ProjectHouseService::getHouseId, removeAbleHouseIdList));*/
                this.remove(new QueryWrapper<ProjectHouseInfo>().lambda().in(ProjectHouseInfo::getHouseId, removeAbleHouseIdList));
            }
            return true;
        }
        return false;
    }

    /*无需调整*/
    @Override
    public boolean putBatch(HouseDesginVo houseDesginVo) {
        List<String> houseIdList = houseDesginVo.getHouseIds();
        if (CollUtil.isNotEmpty(houseIdList)) {
            houseIdList.forEach(houseId -> {
                baseMapper.putBatch(houseId, houseDesginVo.getHouseDesginId());
            });
            return true;
        }
        return false;
    }

    /*无需调整*/
    @Override
    public int countHouse() {
        return this.count();
    }

    /*无需调整*/
    @Override
    public Integer countLive() {
        return baseMapper.countLive();
    }

    /*无需调整*/
    @Override
    public Integer countSublet() {
        return baseMapper.countSublet();
    }

    /*无需调整*/
    @Override
    public Integer countCommercial() {
        return baseMapper.countCommercial();
    }

    /*无需调整*/
    @Override
    public Integer countFree() {
        return baseMapper.countFree();
    }

    /*无需调整*/
    @Override
    public IPage<ProjectDeviceSelectTreeVo> findIndoorByName(IPage page, String name) {
        return baseMapper.findIndoorByName(page, name);
    }

    /*无需调整*/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveBatchVo(ProjectBuildingBatchVo vo) {
        vo.getBuildingList().forEach(buildingVo -> {

            buildingVo.getUnitList().forEach(unitVo -> {
                List<ProjectHouseInfo> projectHouseInfos = list(
                        Wrappers.lambdaQuery(ProjectHouseInfo.class).eq(ProjectHouseInfo::getBuildingUnit, unitVo.getUnitId()));
                List<String> houseNames = new ArrayList<>();
                if (projectHouseInfos != null && projectHouseInfos.size() > 0) {
                    houseNames = projectHouseInfos.stream().map(ProjectHouseInfo::getHouseName).collect(Collectors.toList());
                }
                List<ProjectHouseInfo> housePoList = new ArrayList<>();
                for (ProjectHouseInfoVo houseVo : unitVo.getHouseList()) {
                    //过滤掉重名已存在的房屋
                    if (houseNames.contains(houseVo.getHouseName())) {
                        continue;
                    }
                    if (houseVo.getEnable()) {
                        ProjectHouseInfo housePo = new ProjectHouseInfo();
                        BeanUtils.copyProperties(houseVo, housePo);
                        housePo.setBuildingUnit(unitVo.getUnitId());
                        housePoList.add(housePo);
                    }
                }
                this.saveBatchHouse(housePoList, unitVo.getUnitId());
            });
        });
        return true;
    }

    /*无需调整*/
    @Override
    public List<ProjectHouseInfoVo> getByUnitId(String unitId, Integer projectId) {
        return baseMapper.getByUnitId(unitId, projectId);
    }

    @Override
    public Page<HouseInfoVo> page(Page page, HouseInfoVo vo) {
        ProjectHouseInfo po = new ProjectHouseInfo();
        BeanUtils.copyProperties(vo, po);

        return baseMapper.page(page, po);
    }
}
