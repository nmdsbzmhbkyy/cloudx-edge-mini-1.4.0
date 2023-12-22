package com.aurine.cloudx.estate.mapper;

import com.aurine.cloudx.estate.entity.SysServiceParamConf;
import com.aurine.cloudx.estate.vo.SysServiceParamConfVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 平台设备参数定义表(SysDeviceParamConf)表数据库访问层
 *
 * @author 王良俊
 * @since 2020-12-15 10:24:39
 */
@Mapper
public interface SysServiceParamConfMapper extends BaseMapper<SysServiceParamConf> {



    /**
     * <p>
     * 根据最高级别的serviceId查询出包含其子参数的serviceId和其与父参数的对应关系
     * </p>
     *
     * @param serviceIdList 需要插叙你的serviceId
     */
    List<SysServiceParamConfVo> listServiceIdByParServiceId(@Param("serviceIdList") List<String> serviceIdList);



    /**
     * <p>
     * 查询出所有的表单项数据
     * </p>
     *
     * @param serviceIdList 需要插叙你的serviceId
     */
    List<SysServiceParamConfVo> listParamConf(@Param("serviceIdList") List<String> serviceIdList);

    /**
     * <p>
     * 查询出所有的表单项数据
     * </p>
     *
     * @param serviceIdList 需要插叙你的serviceId
     */
    List<SysServiceParamConfVo> listParamConfV2(@Param("serviceIdList") List<String> serviceIdList);

    List<String> getRebootServiceIdList();
}