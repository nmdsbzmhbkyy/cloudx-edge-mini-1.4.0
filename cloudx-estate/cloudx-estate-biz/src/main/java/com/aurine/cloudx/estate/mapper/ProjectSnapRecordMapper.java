package com.aurine.cloudx.estate.mapper;

import com.aurine.cloudx.estate.entity.ProjectSnapRecord;
import com.aurine.cloudx.estate.openapi.ToOpenApi;
import com.aurine.cloudx.estate.openapi.enums.OpenApiServiceNameEnum;
import com.aurine.cloudx.estate.openapi.enums.OpenPushSubscribeCallbackTypeEnum;
import com.aurine.cloudx.estate.vo.ProjectSnapRecordVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


@Mapper
@ToOpenApi(serviceType = OpenPushSubscribeCallbackTypeEnum.EVENT, serviceName = OpenApiServiceNameEnum.SNAP_RECORD)
public interface ProjectSnapRecordMapper extends BaseMapper<ProjectSnapRecord> {
    IPage<ProjectSnapRecordVo> pageRecord(Page page, @Param("query") ProjectSnapRecordVo vo);
}