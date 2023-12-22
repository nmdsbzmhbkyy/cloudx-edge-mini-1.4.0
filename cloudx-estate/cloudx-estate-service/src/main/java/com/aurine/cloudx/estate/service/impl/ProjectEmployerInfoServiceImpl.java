package com.aurine.cloudx.estate.service.impl;

import cn.hutool.core.util.StrUtil;
import com.aurine.cloudx.estate.entity.ProjectEmployerInfo;
import com.aurine.cloudx.estate.entity.ProjectHouseInfo;
import com.aurine.cloudx.estate.mapper.ProjectEmployerInfoMapper;
import com.aurine.cloudx.estate.service.ProjectEmployerInfoService;
import com.aurine.cloudx.estate.service.ProjectHouseInfoService;
import com.aurine.cloudx.estate.vo.ProjectEmployerInfoFromVo;
import com.aurine.cloudx.estate.vo.ProjectEmployerInfoPageVo;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * 项目实有单位信息(ProjectEmployerInfo)表服务实现类
 *
 * @author guhl@aurine.cn
 * @since 2020-08-25 14:58:44
 */
@Service
public class ProjectEmployerInfoServiceImpl extends ServiceImpl<ProjectEmployerInfoMapper, ProjectEmployerInfo> implements ProjectEmployerInfoService {

    @Autowired
    private ProjectHouseInfoService projectHouseInfoService;

    @Override
    public Page<ProjectEmployerInfoPageVo> pageEmployer(Page page, ProjectEmployerInfoPageVo projectEmployerInfoPageVo) {
        return baseMapper.pageEmployer(page, projectEmployerInfoPageVo);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Boolean saveEmployerInfo(ProjectEmployerInfoFromVo projectEmployerInfoFromVo) {
        ProjectHouseInfo projectHouseInfo = projectHouseInfoService.getOne(Wrappers.lambdaQuery(ProjectHouseInfo.class)
                .eq(ProjectHouseInfo::getHouseId, projectEmployerInfoFromVo.getHouseId()));
        if (StrUtil.isNotEmpty(projectHouseInfo.getEmployerId())) {
            throw new RuntimeException("该房屋已存在实有单位");
        }
        if (StrUtil.isEmpty(projectEmployerInfoFromVo.getEmployerId())){
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            projectEmployerInfoFromVo.setEmployerId(uuid);
            ProjectEmployerInfo projectEmployerInfo = new ProjectEmployerInfo();
            BeanUtils.copyProperties(projectEmployerInfoFromVo, projectEmployerInfo);
            super.save(projectEmployerInfo);
        }

        //将该单位挂在房屋底下
        projectHouseInfo.setEmployerId(projectEmployerInfoFromVo.getEmployerId());
        projectHouseInfoService.updateById(projectHouseInfo);

        return true;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Boolean removeByHouseId(String houseId) {
        return projectHouseInfoService.update(Wrappers.lambdaUpdate(ProjectHouseInfo.class)
                .eq(ProjectHouseInfo::getHouseId, houseId)
                .set(ProjectHouseInfo::getEmployerId, null));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Boolean updateEmployerInfo(ProjectEmployerInfoFromVo projectEmployerInfoFromVo) {
        ProjectHouseInfo projectHouseInfo = projectHouseInfoService.getById(projectEmployerInfoFromVo.getHouseId());
        if (StrUtil.isNotEmpty(projectHouseInfo.getEmployerId()) && !projectEmployerInfoFromVo.getOldEmployerId().equals(projectHouseInfo.getEmployerId())) {
            throw new RuntimeException("该房屋已存在实有单位");
        }
        //修改时：若变更了房屋 要先将原先的房屋下的employerId清空
        if (!projectEmployerInfoFromVo.getHouseId().equals(projectEmployerInfoFromVo.getOldHouseId())) {
            projectHouseInfoService.update(Wrappers.lambdaUpdate(ProjectHouseInfo.class)
                    .eq(ProjectHouseInfo::getHouseId, projectEmployerInfoFromVo.getOldHouseId())
                    .set(ProjectHouseInfo::getEmployerId, null));
        }
        projectHouseInfo.setEmployerId(projectEmployerInfoFromVo.getEmployerId());
        projectHouseInfoService.updateById(projectHouseInfo);
        ProjectEmployerInfo projectEmployerInfo = new ProjectEmployerInfo();
        BeanUtils.copyProperties(projectEmployerInfoFromVo, projectEmployerInfo);
        return super.updateById(projectEmployerInfo);
    }

    @Override
    public ProjectEmployerInfoFromVo getByHouseId(String houseId) {
        ProjectHouseInfo projectHouseInfo = projectHouseInfoService.getById(houseId);
        ProjectEmployerInfoFromVo projectEmployerInfoFromVo = new ProjectEmployerInfoFromVo();
        projectEmployerInfoFromVo.setHouseId(projectHouseInfo.getHouseId());
        ProjectEmployerInfo projectEmployerInfo = super.getById(projectHouseInfo.getEmployerId());
        BeanUtils.copyProperties(projectEmployerInfo, projectEmployerInfoFromVo);
        return projectEmployerInfoFromVo;
    }

    @Override
    public ProjectEmployerInfo getBySocialCreditCode(String socialCreditCode, String employerId) {
        ProjectEmployerInfo projectEmployerInfo = getOne(Wrappers.lambdaQuery(ProjectEmployerInfo.class)
                .eq(ProjectEmployerInfo::getSocialCreditCode, socialCreditCode)
                .ne(ProjectEmployerInfo::getEmployerId, employerId));
        return projectEmployerInfo;
    }
}