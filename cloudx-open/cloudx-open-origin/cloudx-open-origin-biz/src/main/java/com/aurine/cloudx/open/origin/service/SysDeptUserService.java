package com.aurine.cloudx.open.origin.service;

import com.aurine.cloudx.open.origin.vo.SysUserVo;
import io.seata.spring.annotation.GlobalTransactional;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


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

    @GlobalTransactional
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
