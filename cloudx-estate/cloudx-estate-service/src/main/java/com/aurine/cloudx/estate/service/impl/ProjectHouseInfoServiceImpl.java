
package com.aurine.cloudx.estate.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.dto.ProjectHouseDTO;
import com.aurine.cloudx.estate.entity.ProjectFrameInfo;
import com.aurine.cloudx.estate.entity.ProjectHouseInfo;
import com.aurine.cloudx.estate.entity.ProjectHousePersonRel;
import com.aurine.cloudx.estate.entity.ProjectHouseService;
import com.aurine.cloudx.estate.mapper.ProjectHouseInfoMapper;
import com.aurine.cloudx.estate.service.*;
import com.aurine.cloudx.estate.vo.ProjectDeviceSelectTreeVo;
import com.aurine.cloudx.estate.vo.ProjectHouseHisRecordVo;
import com.aurine.cloudx.estate.vo.ProjectHouseInfoVo;
import com.aurine.cloudx.estate.vo.ProjectHouseResidentVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 房屋
 *
 * @author 王伟
 * @date 2020-05-08 16:29:18
 */
@Service
public class ProjectHouseInfoServiceImpl extends ServiceImpl<ProjectHouseInfoMapper, ProjectHouseInfo> implements ProjectHouseInfoService {
    @Resource
    private ProjectFrameInfoService projectFrameInfoService;

    @Resource
    private ProjectConfigService projectConfigService;
    @Resource
    private ProjectHouseServiceService projectHouseServiceService;

    @Resource
    private ProjectHousePersonRelService housePersonRelService;

    @Override
    public IPage<ProjectHouseInfoVo> findPage(IPage<ProjectHouseInfoVo> page, ProjectHouseInfoVo houseInfoVo) {
        return baseMapper.select(page, houseInfoVo.getBuildingId(), houseInfoVo.getBuildingName(), houseInfoVo.getUnitId(), houseInfoVo.getUnitName(), houseInfoVo.getHouseName(), houseInfoVo.getIsUse());
    }


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


        //根据uid更新框架数据
        projectFrameInfoService.updateById(projectFrameInfo);
        //更新房屋信息相关数据
        ProjectHouseInfo house = baseMapper.selectById(projectHouseInfoVo.getHouseId());
        BeanUtils.copyProperties(projectHouseInfoVo, house);
        return updateById(house);
    }

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
        projectHouseServiceService.remove(Wrappers.lambdaUpdate(ProjectHouseService.class).eq(ProjectHouseService::getHouseId, id));
        //删除房屋远端相关增值服务
        //projectHouseServiceService.removeRemoteHouseService(id);
        //删除房屋详细信息
        return this.removeById(id);
    }

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

    @Override
    public List<ProjectHouseResidentVo> getHouseResidents(String houseId) {
        return baseMapper.getHouseResidents(houseId, ProjectContextHolder.getProjectId());
    }

    @Override
    public List<ProjectHouseResidentVo> getHouseResidentsWithoutStatus(String houseId) {
        return baseMapper.getHouseResidentsWithoutStatus(houseId);
    }

    @Override
    public List<ProjectHouseResidentVo> getHouseResidentsWithoutStatus(String houseId, String phone) {
        return baseMapper.getHousePerson(houseId, phone);
    }

    @Override
    public IPage<ProjectHouseHisRecordVo> getHouseRecord(IPage<ProjectHouseResidentVo> page, String houseId) {
        return baseMapper.getHouseRecord(page, houseId, ProjectContextHolder.getProjectId());
    }

    @Override
    public boolean checkHouseDesignExist(String houseDesignId) {
        int count = this.count(new QueryWrapper<ProjectHouseInfo>().lambda().eq(ProjectHouseInfo::getHouseDesginId, houseDesignId));
        return count > 0;
    }

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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeBatch(List<String> houseIdList) {
        if (CollUtil.isNotEmpty(houseIdList)) {
//            houseIdList.forEach(this::removeHouseAndFrameById);
            List<String> removeAbleHouseIdList = baseMapper.getRemoveAbleHouseIdList(houseIdList);
            if (CollUtil.isNotEmpty(removeAbleHouseIdList)) {
                projectFrameInfoService.remove(new QueryWrapper<ProjectFrameInfo>().lambda()
                        .in(ProjectFrameInfo::getEntityId, removeAbleHouseIdList));
                projectHouseServiceService.remove(new QueryWrapper<ProjectHouseService>().lambda()
                        .in(ProjectHouseService::getHouseId, removeAbleHouseIdList));
                this.remove(new QueryWrapper<ProjectHouseInfo>().lambda().in(ProjectHouseInfo::getHouseId, removeAbleHouseIdList));
            }
            return true;
        }
        return false;
    }

    @Override
    public int countHouse() {
        return this.count();
    }

    @Override
    public Integer countLive() {
        return baseMapper.countLive();
    }

    @Override
    public Integer countSublet() {
        return baseMapper.countSublet();
    }

    @Override
    public Integer countCommercial() {
        return baseMapper.countCommercial();
    }

    @Override
    public Integer countFree() {
        return baseMapper.countFree();
    }

    @Override
    public IPage<ProjectDeviceSelectTreeVo> findIndoorByName(IPage page, String name) {
        return baseMapper.findIndoorByName(page,name);
    }
}
