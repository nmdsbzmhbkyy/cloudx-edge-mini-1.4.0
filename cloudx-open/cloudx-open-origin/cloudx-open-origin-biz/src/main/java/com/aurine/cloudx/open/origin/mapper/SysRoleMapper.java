package com.aurine.cloudx.open.origin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pig4cloud.pigx.admin.api.entity.SysRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 系统角色
 *
 * @author : Qiu
 * @date : 2022 04 18 18:03
 */

@Mapper
public interface SysRoleMapper extends BaseMapper<SysRole> {

    /**
     * 通过projectId（项目id）查询第一个系统角色
     *
     * @param projectId
     * @return
     */
    SysRole getFirstByProjectId(@Param("projectId") Integer projectId);

}
