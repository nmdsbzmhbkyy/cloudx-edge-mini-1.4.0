package com.aurine.cloudx.estate.mapper;

import com.aurine.cloudx.estate.entity.ProjectFloorPic;
import com.aurine.cloudx.estate.vo.ProjectFloorPicVo;
import com.aurine.cloudx.estate.vo.ProjectFloorPicSearchCondition;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 平面圖 Mapper * </p>
 * 
 * @ClassName: PlanMapper
 * @author: wangwei<wangwei@aurine.cn>
 * @date: 2020年1月16日
 * @Copyright:
 */
@Mapper
public interface ProjectFloorPicMapper extends BaseMapper<ProjectFloorPic> {

    IPage<ProjectFloorPicVo> select(IPage<?> page, @Param("searchCondition") ProjectFloorPicSearchCondition searchCondition);
}
