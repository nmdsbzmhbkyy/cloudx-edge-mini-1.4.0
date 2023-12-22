package com.aurine.cloudx.estate.service.impl;

import com.aurine.cloudx.estate.entity.ProjectFeeConf;
import com.aurine.cloudx.estate.mapper.ProjectFeeConfMapper;
import com.aurine.cloudx.estate.service.ProjectFeeConfService;
import com.aurine.cloudx.estate.vo.ProjectFeeConfVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 费用设置(ProjectFeeConf)表服务实现类
 *
 * @author makejava
 * @since 2020-07-20 16:43:48
 */
@Service
public class ProjectFeeConfServiceImpl extends ServiceImpl<ProjectFeeConfMapper, ProjectFeeConf> implements ProjectFeeConfService {

    @Override
    public IPage<ProjectFeeConfVo> pageFee(Page<ProjectFeeConfVo> page, ProjectFeeConf projectFeeConf) {
        return baseMapper.pageFee(page,projectFeeConf);
    }

    @Override
    public List<ProjectFeeConfVo> listFee(String type,String status) {
        return baseMapper.liseFee(type,status);
    }


}