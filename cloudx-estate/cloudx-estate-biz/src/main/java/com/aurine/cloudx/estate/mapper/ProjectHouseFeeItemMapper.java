package com.aurine.cloudx.estate.mapper;

import com.aurine.cloudx.estate.vo.ProjectHouseFeeItemConfVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.aurine.cloudx.estate.entity.ProjectHouseFeeItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 房屋费用设置(ProjectHouseFeeItem)表数据库访问层
 *
 * @author makejava
 * @since 2020-07-20 16:43:48
 */
@Mapper
public interface ProjectHouseFeeItemMapper extends BaseMapper<ProjectHouseFeeItem> {
    /**
     * 分页查询房屋费用配置相关信息
     *
     * @param page 分页
     * @return 房屋费用信息
     */
    Page<ProjectHouseFeeItemConfVo> selectHouseFeeItemConf(Page<ProjectHouseFeeItemConfVo> page, @Param("houseId") String houseId, @Param("ids") List<String> ids);

    /**
     * 根据房屋id和费用id查询房屋配置信息
     *
     * @param houseId 房屋id
     * @param ids     费用id列表
     * @param feeCycleTypes 付费类型(2临时,1固定)
     * @param feeTypes 费用类型
     * @return 房屋费用信息
     */
    List<ProjectHouseFeeItemConfVo> listHouseFeeItemConf(@Param("houseId") String houseId, @Param("ids") List<String> ids, @Param("feeCycleTypes") List<String> feeCycleTypes, @Param("feeTypes") List<String> feeTypes);

}