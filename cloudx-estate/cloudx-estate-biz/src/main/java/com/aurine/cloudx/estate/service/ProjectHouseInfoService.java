package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.dto.ProjectHouseDTO;
import com.aurine.cloudx.estate.entity.ProjectHouseInfo;
import com.aurine.cloudx.estate.vo.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 房屋
 *
 * @author 王伟
 * @date 2020-05-08 16:29:18
 */
public interface ProjectHouseInfoService extends IService<ProjectHouseInfo> {
    /**
     * 保存房屋
     *
     * @param projectHouseInfoVo
     * @return
     */
    boolean saveVo(ProjectHouseInfoVo projectHouseInfoVo);

    /**
     * 批量保存房屋
     *
     * @param houseList
     * @return
     */
    boolean saveBatchHouse(List<ProjectHouseInfo> houseList, String unitId);

    /**
     * 查询房屋
     *
     * @param page
     * @param houseInfoVo
     * @return
     */
    IPage<ProjectHouseInfoVo> findPage(IPage<ProjectHouseInfoVo> page, ProjectHouseInfoVo houseInfoVo);


    /**
     * 更新房屋
     *
     * @param projectHouseInfoVo
     * @return
     */
    boolean updateById(ProjectHouseInfoVo projectHouseInfoVo);

    /**
     * 根据框架号和项目号，保存或修改楼栋信息
     */
    boolean saveOrUpdateByThirdCode(ProjectHouseInfoVo projectHouseInfoVo);

    /**
     * 删除
     *
     * @param id
     * @return
     */
    boolean removeHouseAndFrameById(String id);

    /**
     * 获取
     *
     * @param id
     * @return
     */
    ProjectHouseInfoVo getVoById(String id);

    /**
     * @param houseId
     * @return
     * @author: 王良俊
     */
    IPage<ProjectHouseResidentVo> getHouseResidents(IPage<ProjectHouseResidentVo> page, String houseId);

    /**
     * @param houseId
     * @return
     * @author: 王良俊
     */
    List<ProjectHouseResidentVo> getHouseResidents(String houseId);

    List<ProjectHouseResidentVo> getHouseResidentsWithoutStatus(String houseId);

    /**
     * @param houseId
     * @return
     * @author: 王良俊
     */
    IPage<ProjectHouseHisRecordVo> getHouseRecord(IPage<ProjectHouseResidentVo> page, String houseId);


    /**
     * 检查房屋设计图是否已经被使用
     *
     * @param houseDesignId
     * @return
     */
    boolean checkHouseDesignExist(String houseDesignId);

    /**
     * 获取房屋列表，带用户入住信息
     *
     * @param buildingId
     * @param unitId
     * @return
     */
    List<ProjectHouseDTO> listHouseWithPersonNum(String buildingId, String unitId);

    /**
     * 计算已入住的房屋数
     *
     * @param buildingId
     * @param unitId
     * @return
     */
    Integer countHouseUsed(String buildingId, String unitId);

    /**
     * 计算未入住的房屋数
     *
     * @param buildingId
     * @param unitId
     * @return
     */
    Integer countHouseUnuse(String buildingId, String unitId);

    /**
     * 删除房屋
     *
     * @param unitId
     * @return
     */
    boolean deleteHouseByUnitId(String unitId);

    /**
     * <p>
     * 批量删除房屋
     * </p>
     *
     * @param houseIdList 房屋ID列表
     * @return 是否删除成功
     */
    boolean removeBatch(List<String> houseIdList);

    /**
     * <p>
     * 批量修改房屋类型
     * </p>
     *
     * @param houseDesginVo 房屋ID列表
     */
    boolean putBatch(HouseDesginVo houseDesginVo);

    /**
     * 统计房屋总数
     *
     * @return
     */
    int countHouse();

    /**
     * 统计自住的住户
     *
     * @return
     */
    Integer countLive();

    /**
     * 统计租用的住户
     *
     * @return
     */
    Integer countSublet();

    /**
     * 统计商用的房屋
     *
     * @return
     */
    Integer countCommercial();

    /**
     * 统计闲置的房屋
     *
     * @return
     */
    Integer countFree();

    /**
     * 根据房屋名称查询房屋框架信息
     *
     * @param name
     * @return
     */
    IPage<ProjectDeviceSelectTreeVo> findIndoorByName(IPage page, String name);

    boolean saveBatchVo(ProjectBuildingBatchVo vo);

    List<ProjectHouseInfoVo> getByUnitId(String unitId, Integer projectId);
    /**
    * @Author hjj
    * @Description 根据楼栋ID获取楼层框架数据
    * @Date  2022/3/8
    * @Param
    * @return
    **/
    List<Integer> getFloorByBuildingId(String buildingId);

    /**
     * @Author hjj
     * @Description 根据项目ID获取楼层框架数据
     * @Date  2022/3/8
     * @Param
     * @return
     **/
    List<Integer> getFloorByProjectId(Integer projectId);

    List<ProjectHouseInfoVo> getByBuildingId(String buildingId);

    /**
     * 查询人员所在小区拥有的房屋信息
     *
     * @return
     */
    List<ProjectHouseInfoVo> getHouseInfo();

    /**
     * 获取房屋的地址
     *
     * @param houseId
     * @return
     */
    List<String> getAddress(String houseId);

    /**
     * 获取房屋属性
     *
     * @param personId
     * @return
     */
    List<String> getHouseParam(String personId);

    R<String> getHouseId(Integer projectId);

    /**
     * 根据人员、楼栋和房屋信息，获取首套房信息
     *
     * @param personId
     * @param buildingId
     * @param houseId
     * @return
     */
    ProjectHouseInfo getFirstHouse(String personId, String buildingId, String unitId, String houseId);
}
