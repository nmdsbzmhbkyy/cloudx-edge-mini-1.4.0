
package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.entity.ProjectDeviceRegion;
import com.aurine.cloudx.estate.vo.*;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>区域管理</p>
 *
 * @ClassName: ProjectDeviceRegionService
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020/5/29 9:34
 * @Copyright:
 */
public interface ProjectDeviceRegionService extends IService<ProjectDeviceRegion> {

    /**
     * 获取当前项目下的区域树
     *
     * @param name 如果name不为空，则根节点名称为name，否则根节点名称为社区
     * @return
     */
    List<ProjectDeviceRegionTreeVo> findTree(String name);

    /**
     * 验证节点是否还有子节点
     *
     * @param regionId
     * @return
     */
    boolean haveChild(String regionId);

    /**
     * 根据设备类型获取设备区域树(含设备数量) xull@aurine.cn 2020年7月8日 15点46分
     *
     * @param name 如果name不为空，则根节点名称为name，否则根节点名称为社区
     * @param type 设备类型
     * @return
     */
    List<ProjectDeviceRegionDetailTreeVo> findTreeByDeviceType(String name, String type, String deviceRegionId);

    /**
     * 获取监控设备树形
     *
     * @param name
     * @return
     */
    List<ProjectMonitorNodeVo> findMonitorTreeByDeviceType(String name);

    /**
     * 初始化默认节点
     * 项目创建后，调用该接口，初始化默认数据
     *
     * @return
     */
    boolean initDefaultData(Integer projectId,Integer tenantId);

    /**
     * <p>
     *  获取楼栋区域信息的分页数据-区域管理
     * </p>
     *
     * @param page 分页信息
     * @author: 王良俊
    */
    Page<ProjectBuildingRegionInfoVo> pageBuildingRegionInfo(Page<ProjectBuildingRegionInfoVo> page);

    /**
     * <p>
     * 获取楼栋区域信息的分页数据-管辖设置
     * </p>
     *
     * @param page 分页信息
     * @author: 王良俊
     */
    Page<ProjectRegionManagerVo> pageRegionManager(Page<ProjectBuildingRegionInfoVo> page);

    /**
     * <p>
     *  移动楼栋到对应区域
     * </p>
     *
     * @param buildingIdList 楼栋ID集合
     * @param regionId 区域ID
     * @author: 王良俊
    */
    boolean moveBuildingsRegion(List<String> buildingIdList, String regionId);

    /**
     * <p>
     *  根据区域ID更新区域的管辖人
     * </p>
     *
     * @param regionId 区域ID
     * @param staffIdList 员工ID集合
     * @author: 王良俊
    */
    boolean updateRegionManager(List<String> staffIdList, String regionId);

    /**
     * <p>
     *  根据区域ID获取到这个区域下的所有楼栋
     * </p>
     *
     * @param regionId 区域ID
     * @author: 王良俊
    */
    List<String> listBuildingByRegionId(String regionId);

    /**
     * <p>
     *  根据区域ID获取到这个区域下的所有楼栋
     * </p>
     *
     * @param regionId 区域ID
     * @return 返回的是使用','分隔的区域ID
     * @author: 王良俊
    */
    String getChildRegionIds(String regionId);


    /**
     * <p>
     *  新增设备区域
     * </p>
     *
     * @param projectDeviceRegion 设备区域对象
     * @author: 王良俊
    */
    ProjectDeviceRegion saveRegion(ProjectDeviceRegion projectDeviceRegion);

    /**
     * <p>
     *  获取属于这个区域的区口终端列表
     * </p>
     *
     * @param regionId 区域ID
     * @author: 王良俊
    */
    List<String> listRegionDeviceByRegionId(String regionId);
}
