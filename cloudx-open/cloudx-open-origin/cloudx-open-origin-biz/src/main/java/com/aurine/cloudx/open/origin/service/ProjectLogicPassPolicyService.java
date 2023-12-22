

package com.aurine.cloudx.open.origin.service;

import com.aurine.cloudx.open.common.entity.vo.LogicPassPolicyVo;
import com.aurine.cloudx.open.origin.entity.ProjectLogicPassPolicy;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p> 逻辑策略服务 </p>
 *
 * @ClassName: ProjectLogicPassPolicyService
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020/5/20 15:39
 * @Copyright:
 */
public interface ProjectLogicPassPolicyService extends IService<ProjectLogicPassPolicy> {

    /**
     * 分页查询
     *
     * @param page
     * @param vo
     * @return
     */
    Page<LogicPassPolicyVo> page(Page page, LogicPassPolicyVo vo);

}
