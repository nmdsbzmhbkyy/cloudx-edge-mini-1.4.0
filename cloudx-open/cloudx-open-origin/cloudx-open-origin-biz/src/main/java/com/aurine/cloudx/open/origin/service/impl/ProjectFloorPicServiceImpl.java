package com.aurine.cloudx.open.origin.service.impl;

import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.open.origin.mapper.ProjectFloorPicMapper;
import com.aurine.cloudx.open.origin.entity.ProjectFloorPic;
import com.aurine.cloudx.open.origin.vo.ProjectFloorPicSearchCondition;
import com.aurine.cloudx.open.origin.vo.ProjectFloorPicVo;
import com.aurine.cloudx.open.origin.service.ProjectFloorPicService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>平面图实现类</p>
 *
 * @ClassName: PlanServiceImpl
 * @author: wangwei<wangwei @ aurine.cn>
 * @date: 2020年1月16日
 * @Copyright:
 */
@Service
public class ProjectFloorPicServiceImpl extends ServiceImpl<ProjectFloorPicMapper, ProjectFloorPic> implements ProjectFloorPicService {

    @Resource
    private ProjectFloorPicMapper projectFloorPicMapper;


    @Override
    public IPage<ProjectFloorPicVo> findPage(IPage<ProjectFloorPicVo> page, ProjectFloorPicSearchCondition searchCondition) {
        return projectFloorPicMapper.select(page, searchCondition);
    }

    /**
     * 获取设备打点的平面图
     *
     * @param page
     * @param deviceId
     * @param regionId
     * @param projectId
     * @return
     */
    @Override
    public IPage<ProjectFloorPicVo> getPicLocation(Page page, String deviceId, String regionId, Integer projectId) {
        IPage<ProjectFloorPicVo> projectFloorPicVoIPage = projectFloorPicMapper.selectLocation(page, deviceId, regionId, projectId);
        return projectFloorPicVoIPage;
    }

    /**
     * <p>
     * 保存平面图
     * </p>
     *
     * @param
     * @return
     * @throws
     * @author: 王良俊
     */
    @Override
    public boolean savePlan(ProjectFloorPicVo vo) {
        ProjectFloorPic projectFloorPic = toPo(vo);
        projectFloorPic.setProjectId(ProjectContextHolder.getProjectId());
        return this.save(projectFloorPic);
    }

    /**
     * <p>
     * 根据id更新平面图
     * </p>
     *
     * @param
     * @return
     * @throws
     * @author: 王良俊
     */
    @Override
    public boolean update(ProjectFloorPicVo vo) {
        ProjectFloorPic projectFloorPic = toPo(vo);
        projectFloorPic.setProjectId(ProjectContextHolder.getProjectId());
        return this.updateById(projectFloorPic);
    }

    /**
     * 获取当前层级的平面图列表
     *
     * @param regionId
     * @return
     */
    @Override
    public List<ProjectFloorPic> listPicByRegionId(String regionId) {
        return this.list(new QueryWrapper<ProjectFloorPic>().lambda().eq(ProjectFloorPic::getRegionId, regionId));
    }

    /**
     * 判断当前节点下是否存在平面图
     *
     * @param regionId
     * @return
     */
    @Override
    public boolean haveFloorPicInRegion(String regionId) {
        int count = count(new QueryWrapper<ProjectFloorPic>().lambda().eq(ProjectFloorPic::getRegionId, regionId));
        return count >= 1;
    }

    protected <F> ProjectFloorPic toPo(ProjectFloorPicVo projectFloorPicVo) {
        ProjectFloorPic projectFloorPic = BeanUtils.instantiateClass(ProjectFloorPic.class);
        BeanUtils.copyProperties(projectFloorPicVo, projectFloorPic);
        return projectFloorPic;
    }
}
