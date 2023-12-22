package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.vo.SysUserVo;
import com.codingapi.tx.annotation.TxTransaction;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;



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
    @Deprecated
    Integer addDeptUser(SysUserVo sysUserVo);


    Integer addUserAndRole(SysUserVo sysUserVo);

    Integer addUserAndRoleInner(SysUserVo sysUserVo);

    @TxTransaction(isStart = true)
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    void updateUserAndRole(SysUserVo sysUserVo);

    /**
     * 编辑pigx用户
     *
     * @param sysUserVo
     */
    void updateDeptUser(SysUserVo sysUserVo);

    @Deprecated
    R removeDeptUser(SysUserVo sysUserVo);

    R removeUserAndStaff(Integer userId, Integer deptId);



}
