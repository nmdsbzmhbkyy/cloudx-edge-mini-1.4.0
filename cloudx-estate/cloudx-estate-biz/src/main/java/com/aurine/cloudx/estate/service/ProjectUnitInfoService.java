

package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.entity.ProjectUnitInfo;
import com.aurine.cloudx.estate.vo.ProjectUnitInfoCountVo;
import com.aurine.cloudx.estate.vo.ProjectUnitInfoVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 单元
 *
 * @author 王伟
 * @date 2020-06-10 11:10:40
 */
public interface ProjectUnitInfoService extends IService<ProjectUnitInfo> {

    /**
     * 保存单元极其框架信息
     * @param unitInfo
     * @return
     */
    boolean saveUnit(ProjectUnitInfo unitInfo,String buildingId,String unitNameType);

    /**
     * 根据框架号和项目号，保存或修改单元信息
     */
    boolean saveOrUpdateByThirdCode(ProjectUnitInfoVo projectUnitInfoVo);

    /**
     * 批量保存单元信息极其框架
     * @param unitInfoList
     * @return
     */
    boolean saveBatchUnit(List<ProjectUnitInfo> unitInfoList,String buildingId,String unitNameType);

    /**
     * 保存修改极其框架信息
     * @param unitInfo
     * @return
     */
    boolean updateUnit(ProjectUnitInfo unitInfo);

    /**
     * 批量修改单元信息极其框架
     * @param unitInfoList
     * @return
     */
    boolean updateBatchUnit(List<ProjectUnitInfoVo> unitInfoList);

    /**
     * 通过楼栋ID获取所属的单元
     * @param buildingId
     * @return
     */
    List<ProjectUnitInfoVo> listUnit(String buildingId);

    /**
     * 根据楼栋ID删除单元极其框架数据
     * @param buildingId
     * @return
     */
    boolean deleteByBuildingId(String buildingId);

    List<ProjectUnitInfoCountVo> listUnitInfo(String buildingId);

    String getBuildingIdByUnitId(String unitId);
}
