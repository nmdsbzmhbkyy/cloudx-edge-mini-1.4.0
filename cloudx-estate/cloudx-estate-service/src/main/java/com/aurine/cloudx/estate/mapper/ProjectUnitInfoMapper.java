

package com.aurine.cloudx.estate.mapper;

import com.aurine.cloudx.estate.entity.ProjectUnitInfo;
import com.aurine.cloudx.estate.vo.ProjectUnitInfoVo;
import com.baomidou.mybatisplus.annotation.SqlParser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 单元
 *
 * @author 王伟
 * @date 2020-06-10 11:10:40
 */
@Mapper
public interface ProjectUnitInfoMapper extends BaseMapper<ProjectUnitInfo> {

    /**
     * 通过框架号获取楼栋对象
     *
     * @param unitCode
     * @param projectId
     * @return
     */
    @SqlParser(filter = true)
    ProjectUnitInfoVo getByCode(@Param("unitCode") String unitCode, @Param("projectId") Integer projectId);

    @SqlParser(filter = true)
    Integer saveUnit(@Param("unitInfo") ProjectUnitInfoVo unitInfo);
}
