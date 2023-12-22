package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.vo.ProjectParkRegionVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aurine.cloudx.estate.entity.ProjectParkRegion;
import com.pig4cloud.pigx.common.core.util.R;

import java.util.List;

/**
 * 车位区域表
 *
 * @author 王良俊
 * @date 2020-07-07 11:00:29
 */
public interface ProjectParkRegionService extends IService<ProjectParkRegion> {

    /**
     * <p>
     * 获取车位区域列表
     * </p>
     *
     * @param page                分页对象
     * @param projectParkRegionVo 查询条件
     * @return 分页数据
     */
    IPage<ProjectParkRegionVo> fetchList(IPage<ProjectParkRegionVo> page, ProjectParkRegionVo projectParkRegionVo);


    /**
     * <p>
     * 检查是否已经有公共停车区域了
     * 如果已有公共区域则返回true
     * </p>
     *
     * @param parkId       停车场id
     * @param parkRegionId 停车区域id
     * @return 处理结果
     * @author: 王良俊
     */
    boolean checkHasPublic(String parkId, String parkRegionId);

    /**
     * <p>
     * 保存车位区域
     * </p>
     *
     * @param projectParkRegion 车位区域对象
     * @return 处理结果
     * @author: 王良俊
     */
    boolean saveParkRegion(ProjectParkRegion projectParkRegion);

    /**
     * <p>
     * 更新车位区域信息
     * </p>
     *
     * @param projectParkRegion 车位区域对象
     * @return R
     * @author: 王良俊
     */
    R updateParkRegion(ProjectParkRegion projectParkRegion);

    /**
    * <p>
    * 公共区域车位数小于1000则补充到1000
    * </p>
    *
    * @param parkId 停车场ID
    * @author: 王良俊
    */
    void initPublicParkingPlace(String parkId, Integer projectId);

    /**
     * <p>
     * 获取这个人有归属车位的车位区域列表
     * </p>
     *
     * @param parkId   所属车场id
     * @param personId 人员id
     * @param relType  归属关系
     * @return 车位区域对象列表
     * @author: 王良俊
     */
    List<ProjectParkRegion> listPersonAttrParkingRegionByRelType(String parkId, String personId, String relType, String placeId);

    List<ProjectParkRegion> listByParkId(String parkId);
}
