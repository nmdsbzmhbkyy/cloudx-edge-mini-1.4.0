package com.aurine.cloudx.estate.mapper;

import com.aurine.cloudx.estate.entity.ProjectCardHis;
import com.aurine.cloudx.estate.openapi.ToOpenApi;
import com.aurine.cloudx.estate.openapi.enums.OpenApiServiceNameEnum;
import com.aurine.cloudx.estate.openapi.enums.OpenPushSubscribeCallbackTypeEnum;
import com.aurine.cloudx.estate.vo.ProjectCardHisVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 卡操作记录
 *
 * @author zy
 *
 * @date 2022-10-18 08:40:49
 */
@Mapper
@ToOpenApi(serviceType = OpenPushSubscribeCallbackTypeEnum.OPERATE, serviceName = OpenApiServiceNameEnum.CARD_HIS)
public interface ProjectCardHisMapper extends BaseMapper<ProjectCardHis> {


    Page<ProjectCardHisVo> pageVo(Page page, @Param("query") ProjectCardHisVo projectCardVo);

    void handleCardHis();

}
