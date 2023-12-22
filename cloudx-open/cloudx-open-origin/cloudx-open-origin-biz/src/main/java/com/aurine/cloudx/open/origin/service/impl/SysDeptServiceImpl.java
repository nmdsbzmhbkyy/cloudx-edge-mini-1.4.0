
package com.aurine.cloudx.open.origin.service.impl;

import com.aurine.cloudx.open.origin.mapper.SysDeptMapper;
import com.aurine.cloudx.open.origin.service.SysDeptService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.admin.api.entity.SysDept;
import org.springframework.stereotype.Service;


/**
 * 系统部门实现
 *
 * @author : Qiu
 * @date : 2022 04 18 18:03
 */

@Service
public class SysDeptServiceImpl extends ServiceImpl<SysDeptMapper, SysDept> implements SysDeptService {

    @Override
    public SysDept getFirstByProjectId(Integer projectId) {
        return baseMapper.getFirstByProjectId(projectId);
    }
}
