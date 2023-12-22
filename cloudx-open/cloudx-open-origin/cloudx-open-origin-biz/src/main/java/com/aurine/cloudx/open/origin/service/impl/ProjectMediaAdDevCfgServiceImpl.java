
package com.aurine.cloudx.open.origin.service.impl;

import com.aurine.cloudx.open.origin.mapper.ProjectMediaAdDevCfgMapper;
import com.aurine.cloudx.open.origin.constant.enums.DeviceMediaAdDlStatusEnum;
import com.aurine.cloudx.open.origin.entity.ProjectMediaAdDevCfg;
import com.aurine.cloudx.open.origin.vo.ProjectMediaAdDevCfgVo;
import com.aurine.cloudx.open.origin.service.ProjectMediaAdDevCfgService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
        return baseMapper.selectList(Wrappers.lambdaQuery(ProjectMediaAdDevCfg.class).in(ProjectMediaAdDevCfg::getDeviceId, deviceIds)
                .ne(ProjectMediaAdDevCfg::getDlStatus, DeviceMediaAdDlStatusEnum.CLEARED.systemCode)
                .ne(ProjectMediaAdDevCfg::getDlStatus, DeviceMediaAdDlStatusEnum.FAIL.systemCode));
    }

    @Override
    public List<ProjectMediaAdDevCfg> getMediaAdDevCfgByAdId(String adId) {
        return baseMapper.selectList(Wrappers.lambdaQuery(ProjectMediaAdDevCfg.class).eq(ProjectMediaAdDevCfg::getAdId, adId)
                .ne(ProjectMediaAdDevCfg::getDlStatus, DeviceMediaAdDlStatusEnum.FAIL.systemCode)
                .ne(ProjectMediaAdDevCfg::getDlStatus, DeviceMediaAdDlStatusEnum.CLEARED.systemCode)
        );
    }

    @Override
    public Page<ProjectMediaAdDevCfgVo> pageMediaAdDevCfg(Page page, ProjectMediaAdDevCfgVo projectMediaAdDevCfg) {
        return baseMapper.pageMediaAdDevCfg(page, projectMediaAdDevCfg);
    }

    @Override
    public void removeRel(String deviceId) {
        this.remove(new LambdaQueryWrapper<ProjectMediaAdDevCfg>().eq(ProjectMediaAdDevCfg::getDeviceId, deviceId));
    }
}
