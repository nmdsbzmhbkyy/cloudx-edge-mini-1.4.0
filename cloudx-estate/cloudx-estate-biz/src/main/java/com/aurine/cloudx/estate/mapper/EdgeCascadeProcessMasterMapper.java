package com.aurine.cloudx.estate.mapper;

import com.aurine.cloudx.estate.entity.EdgeCascadeProcessMaster;
import com.aurine.cloudx.estate.openapi.ToOpenApi;
import com.aurine.cloudx.estate.openapi.enums.OpenApiCommandTypeEnum;
import com.aurine.cloudx.estate.openapi.enums.OpenApiServiceNameEnum;
import com.aurine.cloudx.estate.openapi.enums.OpenPushSubscribeCallbackTypeEnum;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author：zouyu
 * @data: 2022/3/22 9:56
 */
@Mapper
public interface EdgeCascadeProcessMasterMapper extends BaseMapper<EdgeCascadeProcessMaster> {
}
