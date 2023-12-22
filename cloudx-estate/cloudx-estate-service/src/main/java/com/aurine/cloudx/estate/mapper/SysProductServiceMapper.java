package com.aurine.cloudx.estate.mapper;

import com.aurine.cloudx.estate.entity.SysProductService;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 以产品为维度进行平台设备参数配置项管理(SysProductParamCategory)表数据库访问层
 *
 * @author 王良俊
 * @since 2020-12-15 10:24:58
 */
@Mapper
public interface SysProductServiceMapper extends BaseMapper<SysProductService> {

}