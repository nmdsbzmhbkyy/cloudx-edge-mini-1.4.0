package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.entity.SysUserLock;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @date 2021-03-17 17:29:48
 */
public interface SysUserLockService extends IService<SysUserLock> {
    /**
     *
     * @param phone
     * @return
     */
    SysUserLock checkUser(String phone);
}
