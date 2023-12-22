
package com.aurine.cloudx.estate.service.impl;

import com.aurine.cloudx.estate.entity.ProjectMediaAdDevCfg;
import com.aurine.cloudx.estate.mapper.ProjectMediaAdDevCfgMapper;
import com.aurine.cloudx.estate.service.ProjectMediaAdDevCfgService;
import com.aurine.cloudx.estate.vo.ProjectMediaAdDevCfgVo;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 项目媒体广告设备配置表
 *
 * @author xull@aurine.cn
 * @date 2020-06-04 11:36:26
 */
@Service
public class ProjectMediaAdDevCfgServiceImpl extends ServiceImpl<ProjectMediaAdDevCfgMapper, ProjectMediaAdDevCfg> implements ProjectMediaAdDevCfgService {

    @Override
    public List<ProjectMediaAdDevCfg> getMediaAdDevCfgByDeviceIds(List<String> deviceIds) {
        return baseMapper.selectList(Wrappers.lambdaQuery(ProjectMediaAdDevCfg.class).in(ProjectMediaAdDevCfg::getDeviceId, deviceIds));
    }

    @Override
    public List<ProjectMediaAdDevCfg> getMediaAdDevCfgByAdId(String adId) {
        return baseMapper.selectList(Wrappers.lambdaQuery(ProjectMediaAdDevCfg.class).eq(ProjectMediaAdDevCfg::getAdId, adId));
    }

    @Override
    public Page<ProjectMediaAdDevCfgVo> pageMediaAdDevCfg(Page page, ProjectMediaAdDevCfgVo projectMediaAdDevCfg) {
        return baseMapper.pageMediaAdDevCfg(page, projectMediaAdDevCfg);
    }
}
