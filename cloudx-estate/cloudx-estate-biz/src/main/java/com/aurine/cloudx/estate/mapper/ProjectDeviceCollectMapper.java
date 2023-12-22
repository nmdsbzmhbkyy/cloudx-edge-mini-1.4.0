

package com.aurine.cloudx.estate.mapper;

import com.aurine.cloudx.estate.entity.ProjectDeviceCollect;
import com.aurine.cloudx.estate.vo.ProjectDeviceCollectListVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 项目设备采集参数
 *
 * @author xull@aurine.cn
 * @date 2020-06-12 11:43:43
 */
@Mapper
public interface ProjectDeviceCollectMapper extends BaseMapper<ProjectDeviceCollect> {

    List<ProjectDeviceCollectListVo> getDeviceCollectListVo(@Param("projectId") Integer projectId, @Param("type") String type,@Param("param")String param);
}
