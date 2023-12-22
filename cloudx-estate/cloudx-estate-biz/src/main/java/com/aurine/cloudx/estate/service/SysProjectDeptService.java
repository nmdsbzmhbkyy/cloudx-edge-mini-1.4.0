

package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.entity.SysProjectDept;

import com.aurine.cloudx.estate.vo.SysProjectDeptVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 项目内部门信息
 *
 * @author lingang
 * @date 2020-05-07 18:44:46
 */
public interface SysProjectDeptService extends IService<SysProjectDept> {

    /**
     * 初始化部门
     * @param projectId
     * @param tenantId
     * @return
     */
    boolean initDept(Integer projectId,Integer tenantId);

    IPage<SysProjectDeptVo> page(Page page,  SysProjectDeptVo sysProjectDeptVo);
}
