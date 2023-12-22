package com.aurine.cloudx.open.origin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.admin.api.entity.SysDept;
import org.apache.ibatis.annotations.Param;

/**
 * 系统部门
 *
 * @author : Qiu
 * @date : 2022 04 18 18:03
 */

public interface SysDeptService extends IService<SysDept> {

    /**
     * 通过projectId（项目id）查询第一个系统部门
     *
     * @param projectId
     * @return
     */
    SysDept getFirstByProjectId(@Param("projectId") Integer projectId);

}
