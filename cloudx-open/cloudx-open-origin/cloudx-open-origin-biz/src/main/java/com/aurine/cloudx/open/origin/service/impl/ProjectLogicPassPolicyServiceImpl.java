
package com.aurine.cloudx.open.origin.service.impl;

import com.aurine.cloudx.open.common.entity.vo.LogicPassPolicyVo;
import com.aurine.cloudx.open.origin.entity.ProjectLogicPassPolicy;
import com.aurine.cloudx.open.origin.mapper.ProjectLogicPassPolicyMapper;
import com.aurine.cloudx.open.origin.service.ProjectLogicPassPolicyService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * 逻辑策略
 *
 * @author pigx code generator
 * @date 2020-05-20 15:24:32
 */
@Service
public class ProjectLogicPassPolicyServiceImpl extends ServiceImpl<ProjectLogicPassPolicyMapper, ProjectLogicPassPolicy> implements ProjectLogicPassPolicyService {

    @Override
    public Page<LogicPassPolicyVo> page(Page page, LogicPassPolicyVo vo) {
        ProjectLogicPassPolicy po = new ProjectLogicPassPolicy();
        BeanUtils.copyProperties(vo, po);

        return baseMapper.page(page, po);
    }
}
