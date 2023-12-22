

package com.aurine.cloudx.open.origin.mapper;

import com.aurine.cloudx.open.origin.entity.ProjectDeviceSubsystem;
import com.baomidou.mybatisplus.annotation.SqlParser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 设备信息表
 *
 * @author xull@aurine.cn
 * @date 2020-05-20 10:39:47
 */
@Mapper
public interface ProjectDeviceSubsystemMapper extends BaseMapper<ProjectDeviceSubsystem> {

    @SqlParser(filter = true)
    List<ProjectDeviceSubsystem> selectByTemplate();

    @SqlParser(filter=true)
    boolean initInsert(@Param("param") ProjectDeviceSubsystem po, @Param("tenantId") Integer tenantId);

    @SqlParser(filter=true)
    List<ProjectDeviceSubsystem> defaultSubsystem(@Param("projectId") Integer projectId, @Param("subsystemName") String subsystemName);

}
