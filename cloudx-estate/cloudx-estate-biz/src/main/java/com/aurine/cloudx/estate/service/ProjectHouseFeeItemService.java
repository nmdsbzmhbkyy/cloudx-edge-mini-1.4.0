package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.entity.ProjectHouseFeeItem;
import com.aurine.cloudx.estate.vo.ProjectHouseFeeItemConfVo;
import com.aurine.cloudx.estate.vo.ProjectHouseFeeItemUpdateBatchVo;
import com.aurine.cloudx.estate.vo.ProjectHouseFeeItemUpdateVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.common.core.util.R;

import java.util.List;

/**
 * 房屋费用设置(ProjectHouseFeeItem)表服务接口
 *
 * @author makejava
 * @since 2020-07-20 16:43:48
 */
public interface ProjectHouseFeeItemService extends IService<ProjectHouseFeeItem> {
    /**
     * 分页查询房屋费用配置相关信息
     * @param page 分页
     * @return 房屋费用信息
     */
    Page<ProjectHouseFeeItemConfVo> selectHouseFeeItemConf(Page<ProjectHouseFeeItemConfVo> page);

    /**
     * 获取房屋费用列表
     * @param houseId
     * @param ids
     * @param feeCycleTypes
     * @param feeTypes
     * @return
     */
    List<ProjectHouseFeeItemConfVo> listHouseFeeItemConf(String houseId, List<String> ids, List<String> feeCycleTypes, List<String> feeTypes);


    /**
     * 新增或删除房屋关联费用
     * @param projectHouseFeeItem 操作视图
     * @return
     */
    R updateVo(ProjectHouseFeeItemUpdateVo projectHouseFeeItem);

    /**
     * 批量新增房屋关联费用
     * @param projectHouseFeeItemUpdateBatchVo 操作视图
     * @return
     */
    R updateBatchVo(ProjectHouseFeeItemUpdateBatchVo projectHouseFeeItemUpdateBatchVo);
}