
package com.aurine.cloudx.open.origin.service.impl;

import com.aurine.cloudx.open.common.entity.vo.PhysicalPassPolicyVo;
import com.aurine.cloudx.open.origin.entity.ProjectPhysicalPassPolicy;
import com.aurine.cloudx.open.origin.mapper.ProjectPhysicalPassPolicyMapper;
import com.aurine.cloudx.open.origin.service.ProjectPhysicalPassPolicyService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * 物理策略
 *
 * @author pigx code generator
 * @date 2020-05-20 15:44:19
 */
@Service
public class ProjectPhysicalPassPolicyServiceImpl extends ServiceImpl<ProjectPhysicalPassPolicyMapper, ProjectPhysicalPassPolicy> implements ProjectPhysicalPassPolicyService {

    @Override
    public Page<PhysicalPassPolicyVo> page(Page page, PhysicalPassPolicyVo vo) {
        ProjectPhysicalPassPolicy po = new ProjectPhysicalPassPolicy();
        BeanUtils.copyProperties(vo, po);

        return baseMapper.page(page, po);
    }
}
