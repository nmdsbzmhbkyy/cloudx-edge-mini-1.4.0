

package com.aurine.cloudx.open.origin.mapper;


import com.aurine.cloudx.open.origin.dto.ProjectParkingInfoDto;
import com.aurine.cloudx.open.origin.entity.ProjectParkingInfo;
import com.baomidou.mybatisplus.annotation.SqlParser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 停车区域
 *
 * @author 王伟
 * @date 2020-05-07 09:13:25
 */
@Mapper
public interface ProjectParkingInfoMapper extends BaseMapper<ProjectParkingInfo> {

    IPage<ProjectParkingInfoDto> select(IPage<?> page, @Param("query") ProjectParkingInfoDto parkingInfo, @Param("projectId") Integer projectId);

    @SqlParser(filter = true)
    List<ProjectParkingInfoDto> getAllParks(@Param("projectId") Integer projectId);

    List<ProjectParkingInfoDto> list(@Param("projectId") Integer projectId, @Param("query") ProjectParkingInfoDto projectParkingInfoDto);

    ProjectParkingInfoDto getByParkId(@Param("parkId") String parkId);

    boolean setParkNum(@Param("parkId") String parkId, @Param("parkNum") Integer parkNum);

    boolean parkGlobalSetting(@Param("projectId") Integer projectId, @Param("item") ProjectParkingInfoDto projectParkingInfoDto);
}
