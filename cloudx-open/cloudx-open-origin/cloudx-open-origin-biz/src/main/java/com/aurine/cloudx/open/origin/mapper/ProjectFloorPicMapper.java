package com.aurine.cloudx.open.origin.mapper;

import com.aurine.cloudx.open.origin.entity.ProjectFloorPic;
import com.aurine.cloudx.open.origin.vo.ProjectFloorPicSearchCondition;
import com.aurine.cloudx.open.origin.vo.ProjectFloorPicVo;
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


    IPage<ProjectFloorPicVo> selectLocation(IPage<?> page, @Param("deviceId") String deviceId, @Param("regionId") String regionId, @Param("projectId") Integer projectId);
}
