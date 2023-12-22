package com.aurine.cloudx.open.origin.mapper;

import com.aurine.cloudx.open.origin.entity.ProjectDeviceParamInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 设备参数信息表，存储设备的参数信息(ProjectDeviceParamInfo)表数据库访问层
 *
 * @author 王良俊
 * @since 2020-12-15 10:24:16
 */
@Mapper
public interface ProjectDeviceParamInfoMapper extends BaseMapper<ProjectDeviceParamInfo> {

    /**
     * <p>
     *  这里根据设备ID只获取到有表单的参数数据
     * </p>
     *
     * @param deviceId 设备ID
    */
    List<ProjectDeviceParamInfo> listValidDeviceParamInfo(@Param("deviceId") String deviceId, @Param("productId") String productId);
}