package com.aurine.cloudx.estate.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.aurine.cloudx.estate.constant.FeeConstant;
import com.aurine.cloudx.estate.entity.ProjectPromotionConf;
import com.aurine.cloudx.estate.entity.ProjectPromotionFeeRel;
import com.aurine.cloudx.estate.mapper.ProjectPromotionConfMapper;
import com.aurine.cloudx.estate.service.ProjectPromotionConfService;
import com.aurine.cloudx.estate.service.ProjectPromotionFeeRelService;
import com.aurine.cloudx.estate.vo.ProjectPromotionConfFormVo;
import com.aurine.cloudx.estate.vo.ProjectPromotionConfOnFeeIdVo;
import com.aurine.cloudx.estate.vo.ProjectPromotionConfPageVo;
import com.aurine.cloudx.estate.vo.ProjectPromotionConfVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.common.core.util.R;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 优惠活动设置(ProjectPromotionConf)表服务实现类
 *
 * @author makejava
 * @since 2020-07-20 16:43:48
 */
@Service
@AllArgsConstructor
public class ProjectPromotionConfServiceImpl extends ServiceImpl<ProjectPromotionConfMapper, ProjectPromotionConf> implements ProjectPromotionConfService {

    @Autowired
    ProjectPromotionFeeRelService projectPromotionFeeRelService;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public R savePromotionConf(ProjectPromotionConfVo projectPromotionConfVo) {
        projectPromotionConfVo.setPromotionId(null);
        if (!checkPromotionConf(projectPromotionConfVo)) {
            return R.failed("普通优惠活动已存在,请避免重复设置");
        }
        String promotionId = UUID.randomUUID().toString().replaceAll("-", "");
        ProjectPromotionConf projectPromotionConf = new ProjectPromotionConf();
        BeanUtils.copyProperties(projectPromotionConfVo, projectPromotionConf);
        projectPromotionConf.setPromotionId(promotionId);
        this.save(projectPromotionConf);
        List<String> feeIds = projectPromotionConfVo.getFeeIds();
        if (feeIds != null && feeIds.size() > 0) {
            List<ProjectPromotionFeeRel> projectPromotionFeeRels = new ArrayList<>();
            feeIds.forEach(e -> {
                ProjectPromotionFeeRel projectPromotionFeeRel = new ProjectPromotionFeeRel();
                projectPromotionFeeRel.setPromotionId(promotionId);
                projectPromotionFeeRel.setFeeId(e);
                projectPromotionFeeRels.add(projectPromotionFeeRel);
            });
            projectPromotionFeeRelService.saveBatch(projectPromotionFeeRels);
        }
        return R.ok();
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public R updatePromotionConf(ProjectPromotionConfVo projectPromotionConfVo) {
        if (!checkPromotionConf(projectPromotionConfVo)) {
            return R.failed("普通优惠活动已存在,请避免重复设置");
        }
        //先清除关系数据
        projectPromotionFeeRelService.remove(Wrappers.lambdaUpdate(ProjectPromotionFeeRel.class)
                .eq(ProjectPromotionFeeRel::getPromotionId, projectPromotionConfVo.getPromotionId()));
        //再添加关系数据
        List<String> feeIds = projectPromotionConfVo.getFeeIds();
        if (feeIds != null && feeIds.size() > 0) {
            List<ProjectPromotionFeeRel> projectPromotionFeeRels = new ArrayList<>();
            feeIds.forEach(e -> {
                ProjectPromotionFeeRel projectPromotionFeeRel = new ProjectPromotionFeeRel();
                projectPromotionFeeRel.setPromotionId(projectPromotionConfVo.getPromotionId());
                projectPromotionFeeRel.setFeeId(e);
                projectPromotionFeeRels.add(projectPromotionFeeRel);
            });
            projectPromotionFeeRelService.saveBatch(projectPromotionFeeRels);
        }
        updateById(projectPromotionConfVo);
        return R.ok();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean removePromotionConf(String id) {
        //先清除关系数据
        projectPromotionFeeRelService.remove(Wrappers.lambdaUpdate(ProjectPromotionFeeRel.class)
                .eq(ProjectPromotionFeeRel::getPromotionId, id));
        removeById(id);
        return true;
    }

    @Override
    public IPage<ProjectPromotionConfPageVo> pageByForm(Page<ProjectPromotionConfPageVo> page, ProjectPromotionConfFormVo projectPromotionConf) {

        return baseMapper.pageByForm(page, projectPromotionConf);
    }

    @Override
    public List<ProjectPromotionConfOnFeeIdVo> listConfByType(List<String> types) {
        return baseMapper.listConfByType(types);
    }

    @Override
    public List<ProjectPromotionConfOnFeeIdVo> listConfById(List<String> ids) {
        return baseMapper.listConfById(ids);
    }

    @Override
    public ProjectPromotionConfVo getPromotion(String id) {
        ProjectPromotionConfVo confVo = new ProjectPromotionConfVo();
        ProjectPromotionConf projectPromotionConf = getById(id);
        BeanUtils.copyProperties(projectPromotionConf, confVo);
        List<String> feeIds = null;
        List<ProjectPromotionFeeRel> feeRelList = projectPromotionFeeRelService.list(
                Wrappers.lambdaQuery(ProjectPromotionFeeRel.class).eq(ProjectPromotionFeeRel::getPromotionId, id));
        if (feeRelList != null && feeRelList.size() > 0) {
            feeIds = feeRelList.stream().map(ProjectPromotionFeeRel::getFeeId).collect(Collectors.toList());
        }
        confVo.setFeeIds(feeIds);
        return confVo;

    }

    private boolean checkPromotionConf(ProjectPromotionConfVo projectPromotionConf) {
        if (FeeConstant.NORMAL_DISCOUNTS.equals(projectPromotionConf.getPromotionType())) {
            Integer count = baseMapper.selectPromotionNormalCount(
                    projectPromotionConf.getEffTime(),
                    projectPromotionConf.getExpTime(),
                    projectPromotionConf.getPromotionId());
            if (count > 0) {
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }
}