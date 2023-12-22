

package com.aurine.cloudx.open.origin.service;

import com.aurine.cloudx.open.common.entity.vo.PhysicalPassPolicyVo;
import com.aurine.cloudx.open.origin.entity.ProjectPhysicalPassPolicy;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>物理策略服务</p>
 *
 * @ClassName: ProjectPhysicalPassPolicyService
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020/5/20 15:47
 * @Copyright:
 */
public interface ProjectPhysicalPassPolicyService extends IService<ProjectPhysicalPassPolicy> {

    /**
     * 分页查询
     *
     * @param page
     * @param vo
     * @return
     */
    Page<PhysicalPassPolicyVo> page(Page page, PhysicalPassPolicyVo vo);

}
