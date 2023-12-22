package com.aurine.cloudx.dashboard.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @description:
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2021-03-29
 * @Copyright:
 */
@Mapper
public interface DeviceDataMapper extends BaseMapper {

    /**
     * 查询设备数据信息
     *
     * @param tableName
     * @param deviceCode
     * @return
     */
    List<LinkedHashMap<String, Object>> getDataByCode(@Param("tableName")String tableName, @Param("deviceCode")String deviceCode, @Param("count")int count);

    List<LinkedHashMap<String, Object>> getDataCountOverview(@Param("projectId")String projectId);

    List<LinkedHashMap<String, Object>> getDataTypeOverview(@Param("projectId")String projectId);
//    LinkedHashMap<String, Object> getDataBySn(String sn);
}
