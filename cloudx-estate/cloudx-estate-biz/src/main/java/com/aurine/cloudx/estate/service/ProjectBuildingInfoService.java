package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.entity.ProjectBuildingInfo;
import com.aurine.cloudx.estate.entity.ProjectDeviceInfo;
import com.aurine.cloudx.estate.vo.ProjectBuildingBatchVo;
import com.aurine.cloudx.estate.vo.ProjectBuildingInfoVo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.common.core.util.R;

import java.util.List;

/**
 * 楼栋
 *
 * @author 王伟
 * @date 2020-05-07 16:52:22
 */
public interface ProjectBuildingInfoService extends IService<ProjectBuildingInfo> {
    /**
     * 批量添加房屋
     *
     * @param buildingBatchVo
     * @return
     */
    boolean saveBatch(ProjectBuildingBatchVo buildingBatchVo);


    /**
     * 保存楼栋
     *
     * @param projectBuildingInfo
     * @return
     */
    boolean saveBuilding(ProjectBuildingInfo projectBuildingInfo,String buildingCode,String buidingFrameNo);

    /**
     * 根据框架号和项目号，保存或修改楼栋信息
     */
    boolean saveOrUpdateByThirdCode(ProjectBuildingInfoVo projectBuildingInfoVo);

    /**
     * 保存楼栋
     *
     * @param projectBuildingInfoVo
     * @return
     */
    boolean saveBuildingAndUnit(ProjectBuildingInfoVo projectBuildingInfoVo);



    /**
     * 批量保存楼栋
     *
     * @param buildingList
     * @return
     */
    boolean saveBatchBuilding(List<ProjectBuildingInfo> buildingList);

    /**
     * 修改楼栋，如果楼栋已存在房屋，则修改失败
     *
     * @param projectBuildingInfoVo
     * @return
     */
    boolean updateById(ProjectBuildingInfoVo projectBuildingInfoVo);

    /**
     * 获取楼栋信息
     *
     * @param id
     * @return
     */
    ProjectBuildingInfoVo getById(String id);

    /**
     * 获取一个楼栋的房屋数量
     *
     * @param buildingId
     * @return
     */
    int countHouseInBuilding(String buildingId);

    /**
     * 删除楼栋
     *
     * @param buildingId
     * @return
     */
    R deleteBuilding(String buildingId);

    /**
     * 获取带组团信息的楼栋列表
     *
     * @return
     */
    List<ProjectBuildingInfo> listBuildingWithGroup(String name);

    /**
     * 统计楼栋总数
     *
     * @return
     */
    int countBuilding();


    /**
     * 通过设备的第三方ID,添加整栋楼、单元的框架号
     *
     * @param deviceInfo
     * @param thirdCode
     * @return
     */
    boolean addThirdCode(ProjectDeviceInfo deviceInfo, String thirdCode);

    /**
     * 查看公安模式是否开启
     * @return
     */
    boolean getPoliceIsEnable();


}
