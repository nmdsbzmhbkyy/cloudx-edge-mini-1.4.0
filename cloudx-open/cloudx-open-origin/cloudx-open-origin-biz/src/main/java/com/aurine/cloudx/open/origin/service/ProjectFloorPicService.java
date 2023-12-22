package com.aurine.cloudx.open.origin.service;

import com.aurine.cloudx.open.origin.entity.ProjectFloorPic;
import com.aurine.cloudx.open.origin.vo.ProjectFloorPicSearchCondition;
import com.aurine.cloudx.open.origin.vo.ProjectFloorPicVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>平面图 服务接口</p>
 * @ClassName: IPlanService   
 * @author: wangwei<wangwei@aurine.cn>
 * @date: 2020年1月16日
 * @Copyright:
 */
public interface ProjectFloorPicService extends IService<ProjectFloorPic> {

	
	/**
     * <p>
     * 分页查询
     * </p>
     *
     * @param page
     * @param searchCondition
     *
     * @return
     */
    IPage<ProjectFloorPicVo> findPage(IPage<ProjectFloorPicVo> page, ProjectFloorPicSearchCondition searchCondition);


	/**
	 * 获取设备打点的平面图
	 *
	 * @param page
	 * @param deviceId
	 * @param regionId
	 * @param projectId
	 * @return
	 */
	IPage<ProjectFloorPicVo> getPicLocation(Page page, String deviceId, String regionId, Integer projectId);

    /**
     * <p>
     *  保存平面图
     * </p>
     *
     * @param vo 平面图vo对象
     * @return
     * @throws
    */
	boolean savePlan(ProjectFloorPicVo vo) ;

	/**
	 * <p>
	 *  更新平面图信息
	 * </p>
	 *
	 * @param vo 平面图vo对象
	 * @return
	 * @throws
	*/
	boolean update(ProjectFloorPicVo vo) ;

	/**
	 * 获取当前层级的平面图列表
	 * @return
	 */
	List<ProjectFloorPic> listPicByRegionId(String regionId);

	/**
	 * 判断当前节点下是否存在平面图
	 * @param regionId
	 * @return
	 */
	boolean haveFloorPicInRegion(String regionId);


}
