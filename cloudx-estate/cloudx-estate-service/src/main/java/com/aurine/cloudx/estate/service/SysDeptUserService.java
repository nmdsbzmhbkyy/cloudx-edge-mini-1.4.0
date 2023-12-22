package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.vo.SysUserVo;
import com.pig4cloud.pigx.common.core.util.R;

/**
 * Title: SysDeptUserService
 * Description:
 *
 * @author xull@aurine.cn
 * @version 1.0.0
 * @date 2020/5/7 17:59
 */
public interface SysDeptUserService {
    /**
     * 新增pigx用户
     *
     * @param sysUserVo
     */
    Integer addDeptUser(SysUserVo sysUserVo);

    /**
     * 编辑pigx用户
     *
     * @param sysUserVo
     */
    void updateDeptUser(SysUserVo sysUserVo);

    R removeDeptUser(SysUserVo sysUserVo);
}
