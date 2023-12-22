package com.aurine.cloudx.estate.mapper;

import com.aurine.cloudx.estate.entity.ProjectRightDeviceCache;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


/**
 * 凭证信息缓存
 * @author:zy
 * @data:2023/4/26 9:22 上午
 */
@Mapper
public interface ProjectRightDeviceCacheMapper extends BaseMapper<ProjectRightDeviceCache> {


    List<ProjectRightDeviceCache> getRightDeviceCache();

}
