package com.aurine.cloudx.estate.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.aurine.cloudx.common.core.exception.OpenApiServiceException;
import com.aurine.cloudx.common.core.util.StringUtil;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.constant.ProjectConfigConstant;
import com.aurine.cloudx.estate.constant.enums.FrameTypeEnum;
import com.aurine.cloudx.estate.dto.OpenApiProjectHouseInfoDto;
import com.aurine.cloudx.estate.entity.*;
import com.aurine.cloudx.estate.mapper.ProjectHouseInfoMapper;
import com.aurine.cloudx.estate.service.*;
import com.aurine.cloudx.estate.thirdparty.module.intercom.factory.IntercomFactoryProducer;
import com.aurine.cloudx.estate.vo.ProjectServiceInfoVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 开放平台内部项目房屋信息ServiceImpl
 *
 * @author : Qiu
 * @date : 2022/7/13 15:31
 */

@Service
public class OpenApiProjectHouseInfoServiceImpl extends ServiceImpl<ProjectHouseInfoMapper, ProjectHouseInfo> implements OpenApiProjectHouseInfoService {

    @Resource
    private ProjectServiceService projectServiceService;

    @Resource
    private ProjectFrameInfoService projectFrameInfoService;

    @Resource
    private ProjectConfigService projectConfigService;

    @Resource
    private ProjectHouseServiceService projectHouseServiceService;

    @Resource
    private ProjectUnitInfoService projectUnitInfoService;

    @Resource
    private ProjectHousePersonRelService projectHousePersonRelService;

    @Resource
    private ProjectBillingInfoService projectBillingInfoService;

    @Resource
    private ProjectHouseInfoService projectHouseInfoService;

    /**
     * 计算已入住的房屋数
     *
     * @param buildingId
     * @param unitId
     * @return
     */
    /*逻辑沿用了ProjectHouseInfoServiceImpl - countHouseUsed*/
    @Override
    public Integer countHouseUsed(String buildingId, String unitId) {
        return this.baseMapper.countHouseRecordWithPerson(buildingId, unitId, 1);
    }


    /*逻辑沿用了ProjectHouseInfoServiceImpl - saveVo*/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<OpenApiProjectHouseInfoDto> save(OpenApiProjectHouseInfoDto dto) {
        // 验证房号是否已存在
        if (projectFrameInfoService.checkExists(dto.getHouseName(), dto.getBuildingUnit())) {
            throw new OpenApiServiceException(dto.getHouseName() + "在该单元下已存在，请更换为其他名称");
        }

        String uid = UUID.randomUUID().toString().replaceAll("-", "");

        ProjectUnitInfo unitInfo = projectUnitInfoService.getById(dto.getBuildingUnit());

        // 存储框架数据
        ProjectFrameInfo projectFrameInfo = new ProjectFrameInfo();
        projectFrameInfo.setEntityId(uid);
        projectFrameInfo.setIsBuilding("0");
        projectFrameInfo.setIsHouse("1");
        projectFrameInfo.setIsUnit("0");
        projectFrameInfo.setEntityName(dto.getHouseName());
        projectFrameInfo.setPuid(dto.getBuildingUnit());
        projectFrameInfo.setLevel(FrameTypeEnum.HOUSE.code);

        /**
         *  添加楼栋时，如果单元存在框架好，房屋根据房号生成框架好
         *  依照 bug（需求？） 2715
         * @since 2021-01-22
         */
        if (StringUtil.isNotEmpty(unitInfo.getUnitCode())) {
            projectFrameInfo.setEntityCode(unitInfo.getUnitCode() + dto.getHouseName());
            projectFrameInfo.setFrameNo(dto.getHouseName());
        }

        projectFrameInfoService.save(projectFrameInfo);

        // 存储房屋详细信息
        ProjectHouseInfo projectHouseInfo = new ProjectHouseInfo();
        BeanUtils.copyProperties(dto, projectHouseInfo);
        projectHouseInfo.setHouseId(uid);

        // 因为OpenApiProjectHouseInfoDto中有一个属性houseDesignId（户型ID）和ProjectHouseInfo中的户型ID（houseDesginId）不一致，所以这里手动设置
        if (StringUtil.isBlank(projectHouseInfo.getHouseDesginId())) {
            projectHouseInfo.setHouseDesginId(dto.getHouseDesignId());
        }

        /**
         *  添加楼栋时，如果单元存在框架好，房屋根据房号生成框架好
         *  依照 bug（需求？） 2715
         * @since 2021-01-22
         */
        if (StringUtil.isNotEmpty(unitInfo.getUnitCode())) {
            projectHouseInfo.setHouseCode(unitInfo.getUnitCode() + dto.getHouseName());
        }

        boolean result = super.save(projectHouseInfo);
        if (!result) {
            throw new OpenApiServiceException("新增房屋失败");
        }

        // 获取默认房屋增值服务状态 guhl.@aurine.cn
        /*String serviceInitalStatus = projectConfigService.getConfig().getServiceInitalStatus();
        if (serviceInitalStatus.equals(ProjectConfigConstant.SERVICE_INITAL_STATUS_OPEN)) {
            this.saveHouseService(uid);
        }*/

        OpenApiProjectHouseInfoDto resultDto = new OpenApiProjectHouseInfoDto();
        BeanUtils.copyProperties(projectHouseInfo, resultDto);

        // 因为OpenApiProjectHouseInfoDto中有一个属性houseDesignId（户型ID）和ProjectHouseInfo中的户型ID（houseDesginId）不一致，所以这里手动设置
        if (StringUtil.isNotBlank(projectHouseInfo.getHouseDesginId())) {
            resultDto.setHouseDesignId(projectHouseInfo.getHouseDesginId());
        }

        return R.ok(resultDto);
    }


    /**
     * 创建房屋时设置默认的增值服务
     *
     * @param houseId
     */
    /*逻辑沿用了ProjectHouseInfoServiceImpl - saveHouseService*/
    private void saveHouseService(String houseId) {
        /*// 获取项目增值服务列表 guhl.@aurine.cn
        List<ProjectServiceInfoVo> list = projectServiceService.getHouseServiceByProjectId(ProjectContextHolder.getProjectId());
        if (CollUtil.isNotEmpty(list)) {
            // 房屋增值服务列表 guhl.@aurine.cn
            List<ProjectHouseService> projectHouseServices = new ArrayList<>();
            list.forEach(e -> {
                ProjectHouseService projectHouseService = new ProjectHouseService();
                projectHouseService.setServiceId(e.getServiceId());
                projectHouseService.setHouseId(houseId);
                projectHouseServices.add(projectHouseService);
                // 为房屋中的住户配置增值服务
                IntercomFactoryProducer.getFactory(e.getServiceCode()).getIntercomService().addByHouse(houseId, ProjectContextHolder.getProjectId());
                projectHouseService.setExpTime(e.getExpTime());
            });

            // 注：因为有房屋ID和服务ID的唯一索引，重复新增会报错，所以这里添加try catch
            try {
                projectHouseServiceService.saveBatch(projectHouseServices);
            } catch (Exception ignore) {
            }
            LocalDateTime date = list.get(0).getExpTime();
            projectHouseInfoService.update(Wrappers.lambdaUpdate(ProjectHouseInfo.class)
                    .set(ProjectHouseInfo::getServiceExpTime, date)
                    .eq(ProjectHouseInfo::getHouseId, houseId));
        }*/
    }

    /*逻辑沿用了ProjectHouseInfoServiceImpl - updateById*/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<OpenApiProjectHouseInfoDto> update(OpenApiProjectHouseInfoDto dto) {
        // 获取原始房屋信息
        String houseId = dto.getHouseId();
        ProjectHouseInfo projectHouseinfo = this.getById(houseId);

        // 验证房号是否已存在
        boolean isSameName = projectHouseinfo.getHouseName().equals(dto.getHouseName());
        boolean isExits = projectFrameInfoService.checkExists(dto.getHouseName(), dto.getBuildingUnit());

        if (isExits && !isSameName) {
            throw new RuntimeException(dto.getHouseName() + "在该单元下已存在，请更换为其他名称");
        }

        // 从VO类获取框架数据
        ProjectFrameInfo projectFrameInfo = new ProjectFrameInfo();
        projectFrameInfo.setEntityId(houseId);
        projectFrameInfo.setEntityName(dto.getHouseName());
        projectFrameInfo.setPuid(dto.getBuildingUnit());

        /**
         *  获取单元框架号，并拼接房屋框架号
         * @Auther 王伟
         * @since 2021-03-12
         */
        ProjectUnitInfo unit = projectUnitInfoService.getById(projectHouseinfo.getBuildingUnit());
        if (StringUtil.isNotEmpty(unit.getUnitCode())) {
            String frameCode = unit.getUnitCode() + dto.getHouseName();

            projectFrameInfo.setEntityCode(frameCode);
            projectFrameInfo.setFrameNo(dto.getHouseName());

            dto.setHouseCode(frameCode);
        }

        // 根据uid更新框架数据
        projectFrameInfoService.updateById(projectFrameInfo);
        // 更新房屋信息相关数据
        ProjectHouseInfo projectHouseInfo = baseMapper.selectById(houseId);
        BeanUtils.copyProperties(dto, projectHouseInfo);

        // 因为OpenApiProjectHouseInfoDto中有一个属性houseDesignId（户型ID）和ProjectHouseInfo中的户型ID（houseDesginId）不一致，所以这里手动设置
        if (StringUtil.isBlank(projectHouseInfo.getHouseDesginId())) {
            projectHouseInfo.setHouseDesginId(dto.getHouseDesignId());
        }

        boolean result = updateById(projectHouseInfo);
        if (!result) {
            throw new OpenApiServiceException(String.format("修改房屋失败，houseId=%s", houseId));
        }

        OpenApiProjectHouseInfoDto resultDto = new OpenApiProjectHouseInfoDto();
        BeanUtils.copyProperties(projectHouseInfo, resultDto);

        // 因为OpenApiProjectHouseInfoDto中有一个属性houseDesignId（户型ID）和ProjectHouseInfo中的户型ID（houseDesginId）不一致，所以这里手动设置
        if (StringUtil.isNotBlank(projectHouseInfo.getHouseDesginId())) {
            resultDto.setHouseDesignId(projectHouseInfo.getHouseDesginId());
        }

        return R.ok(resultDto);
    }

    @Override
    public R<Boolean> delete(OpenApiProjectHouseInfoDto dto) {
        String houseId = dto.getHouseId();
        String unitId = dto.getUnitId();

        if (StringUtil.isNotEmpty(houseId)) {
            return this.deleteByHouseId(houseId);
        }

        if (StringUtil.isNotEmpty(unitId)) {
            return this.deleteByUnitId(unitId);
        }

        throw new OpenApiServiceException("删除房屋失败，缺少房屋ID（houseId）或单元ID（unitId）");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<Boolean> deleteByHouseId(String houseId) {
        if (StringUtil.isEmpty(houseId)) {
            throw new OpenApiServiceException("删除房屋失败，房屋ID（houseId）为空");
        }
        // 房屋如果正在使用就不能删除
        int count = projectHousePersonRelService.count(new LambdaQueryWrapper<ProjectHousePersonRel>()
                .eq(ProjectHousePersonRel::getHouseId, houseId)
        );
        if (count > 0) {
            throw new OpenApiServiceException(String.format("房屋正在使用，无法删除房屋，houseId=%s", houseId));
        }

        // 删除房屋框架信息
        projectFrameInfoService.removeById(houseId);
        /*// 批量删除房屋增值服务信息
        projectHouseServiceService.remove(new LambdaQueryWrapper<ProjectHouseService>()
                .eq(ProjectHouseService::getHouseId, houseId)
        );
        // 批量删除房屋相关账单
        projectBillingInfoService.remove(new LambdaQueryWrapper<ProjectBillingInfo>()
                .eq(ProjectBillingInfo::getHouseId, houseId)
        );*/

        boolean result = super.removeById(houseId);
        if (!result) {
            throw new OpenApiServiceException(String.format("删除房屋失败，houseId=%s", houseId));
        }

        // 删除房屋远端相关增值服务（原代码中这里就是注释掉的）
        //projectHouseServiceService.removeRemoteHouseService(id);

        return R.ok(true);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<Boolean> deleteByUnitId(String unitId) {
        if (StringUtil.isEmpty(unitId)) {
            throw new OpenApiServiceException("通过单元ID删除房屋失败，单元ID（unitId）为空");
        }

        // 房屋下如果有人，不能删除
        int usedCount = this.countHouseUsed(null, unitId);
        if (usedCount >= 1) {
            throw new OpenApiServiceException(String.format("该单元下有房屋正在使用，无法删除房屋，unitId=%s", unitId));
        }

        // 查询该单元下的所有房屋，没有房屋则直接返回ok
        List<ProjectFrameInfo> houseFrameList = projectFrameInfoService.list(new LambdaQueryWrapper<ProjectFrameInfo>()
                .eq(ProjectFrameInfo::getPuid, unitId)
        );
        if (CollectionUtils.isEmpty(houseFrameList)) return R.ok(false);

        // 获取除所有房屋的ID
        List<String> houseIdList = houseFrameList.stream().map(ProjectFrameInfo::getEntityId).collect(Collectors.toList());

        // 批量删除房屋框架信息
        projectFrameInfoService.removeByIds(houseIdList);
        /*// 批量删除房屋增值服务信息
        projectHouseServiceService.remove(new LambdaQueryWrapper<ProjectHouseService>()
                .in(ProjectHouseService::getHouseId, houseIdList)
        );
        // 批量删除房屋相关账单
        projectBillingInfoService.remove(new LambdaQueryWrapper<ProjectBillingInfo>()
                .in(ProjectBillingInfo::getHouseId, houseIdList)
        );*/

        // 批量删除房屋，如果删除失败就回滚
        boolean result = super.removeByIds(houseIdList);
        if (!result) {
            throw new OpenApiServiceException(String.format("通过单元ID删除房屋失败，unitId=%s", unitId));
        }

        /*houseIdList.forEach(e -> {
            // 删除房屋远端相关增值服务（原代码中这里就是注释掉的）
            //projectHouseServiceService.removeRemoteHouseService(e);
        });*/

        return R.ok(true);
    }
}
