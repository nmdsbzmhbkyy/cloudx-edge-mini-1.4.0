package com.aurine.cloudx.estate.service.impl;

import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.entity.ProjectFloorPic;
import com.aurine.cloudx.estate.mapper.ProjectFloorPicMapper;
import com.aurine.cloudx.estate.service.ProjectFloorPicService;
import com.aurine.cloudx.estate.vo.ProjectFloorPicSearchCondition;
import com.aurine.cloudx.estate.vo.ProjectFloorPicVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * <p>平面图实现类</p>
 * @ClassName: PlanServiceImpl   
 * @author: wangwei<wangwei@aurine.cn>
 * @date: 2020年1月16日
 * @Copyright:
 */
@Service
public class ProjectFloorPicServiceImpl extends ServiceImpl<ProjectFloorPicMapper, ProjectFloorPic> implements ProjectFloorPicService {

	@Autowired
	ProjectFloorPicMapper projectFloorPicMapper;


	@Override
	public IPage<ProjectFloorPicVo> findPage(IPage<ProjectFloorPicVo> page, ProjectFloorPicSearchCondition searchCondition) {
		return	projectFloorPicMapper.select(page, searchCondition);
		}

	/**
	 * <p>
	 *	保存平面图
	 * </p>
	 *
	 * @author: 王良俊
	 * @param
	 * @return
	 * @throws
	 */
	@Override
	public boolean savePlan(ProjectFloorPicVo vo) {
		ProjectFloorPic projectFloorPic = toPo(vo);
		projectFloorPic.setProjectId(ProjectContextHolder.getProjectId());
		return this.save(projectFloorPic);
	}

	/**
	 * <p>
	 *  根据id更新平面图
	 * </p>
	 *
	 * @author: 王良俊
	 * @param
	 * @return
	 * @throws
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
		return this.list(new QueryWrapper<ProjectFloorPic>().lambda().eq(ProjectFloorPic::getRegionId,regionId));
	}

	/**
	 * 判断当前节点下是否存在平面图
	 *
	 * @param regionId
	 * @return
	 */
	@Override
	public boolean haveFloorPicInRegion(String regionId) {
		int count =  count(new QueryWrapper<ProjectFloorPic>().lambda().eq(ProjectFloorPic::getRegionId,regionId));
		return count >= 1;
	}

	protected <F> ProjectFloorPic toPo(ProjectFloorPicVo projectFloorPicVo) {
		ProjectFloorPic projectFloorPic = BeanUtils.instantiateClass(ProjectFloorPic.class);
		BeanUtils.copyProperties(projectFloorPicVo, projectFloorPic);
		return projectFloorPic;
	}
}
