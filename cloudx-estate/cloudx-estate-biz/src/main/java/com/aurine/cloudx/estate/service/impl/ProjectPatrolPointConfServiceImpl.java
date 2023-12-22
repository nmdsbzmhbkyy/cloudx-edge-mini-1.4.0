package com.aurine.cloudx.estate.service.impl;

import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.mapper.ProjectPatrolPointConfMapper;
import com.aurine.cloudx.estate.entity.ProjectPatrolPointConf;
import com.aurine.cloudx.estate.service.ProjectPatrolPointConfService;
import com.aurine.cloudx.estate.vo.ProjectPatrolPointConfVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.common.data.tenant.TenantContextHolder;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 项目巡更点配置(ProjectPatrolPointConf)表服务实现类
 *
 * @author 黄阳光 huangyg@aurine.cn
 * @since 2020-07-28 08:52:45
 */
@Service
public class ProjectPatrolPointConfServiceImpl extends ServiceImpl<ProjectPatrolPointConfMapper, ProjectPatrolPointConf> implements ProjectPatrolPointConfService {
    @Resource
    private ProjectPatrolPointConfMapper projectPatrolPointConfMapper;

    @Override
    public IPage<ProjectPatrolPointConfVo> page(Page page, ProjectPatrolPointConfVo vo) {
        return projectPatrolPointConfMapper.selectAll(page, vo);
    }

    @Override
    public boolean save(ProjectPatrolPointConfVo vo) {
        vo.setStatus("1");
        ProjectPatrolPointConf result = this.getOne(new QueryWrapper<ProjectPatrolPointConf>().lambda()
                .eq(ProjectPatrolPointConf::getProjectId, vo.getProjectId())
                .eq(ProjectPatrolPointConf::getPointName, vo.getPointName()));
        if (result != null) {
            throw new RuntimeException("该巡更点已存在");
        }
        ProjectPatrolPointConf po = new ProjectPatrolPointConf();
        BeanUtils.copyProperties(vo, po);
        return this.save(po);
    }

    @Override
    public boolean updateStatusById(String pointId) {
        ProjectPatrolPointConf po = this.getById(pointId);
        if ("1".equals(po.getStatus())) {
            po.setStatus("0");
        } else {
            po.setStatus("1");
        }
        return this.updateById(po);
    }


}