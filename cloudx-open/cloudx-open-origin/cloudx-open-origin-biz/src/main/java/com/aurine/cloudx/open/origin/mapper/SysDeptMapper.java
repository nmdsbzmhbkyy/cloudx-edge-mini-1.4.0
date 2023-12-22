

package com.aurine.cloudx.open.origin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pig4cloud.pigx.admin.api.entity.SysDept;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 系统部门
 *
 * @author : Qiu
 * @date : 2022 04 18 18:03
 */

@Mapper
public interface SysDeptMapper extends BaseMapper<SysDept> {

    /**
     * 通过projectId（项目id）查询第一个系统部门
     *
     * @param projectId
     * @return
     */
    SysDept getFirstByProjectId(@Param("projectId") Integer projectId);

}
