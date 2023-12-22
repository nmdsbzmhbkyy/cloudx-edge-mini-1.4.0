package com.aurine.cloudx.estate.service.impl;

import com.aurine.cloudx.estate.entity.SysUserLock;
import com.aurine.cloudx.estate.service.SysUserLockService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aurine.cloudx.estate.mapper.SysUserLockMapper;
import org.springframework.stereotype.Service;

/**
 * @author pigx code generator
 * @date 2021-03-17 17:29:48
 */
@Service
public class SysUserLockServiceImpl extends ServiceImpl<SysUserLockMapper, SysUserLock> implements SysUserLockService {

    @Override
    public SysUserLock checkUser(String phone) {
        return baseMapper.checkUser(phone);
    }
}
