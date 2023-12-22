

package com.aurine.cloudx.open.origin.mapper;

import com.aurine.cloudx.open.common.entity.vo.LogicPassPolicyVo;
import com.aurine.cloudx.open.origin.entity.ProjectLogicPassPolicy;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 逻辑策略
 *
 * @author pigx code generator
 * @date 2020-05-20 15:24:32
 */
@Mapper
public interface ProjectLogicPassPolicyMapper extends BaseMapper<ProjectLogicPassPolicy> {

    /**
     * 多条件分页查询
     *
     * @param page
     * @param po
     * @return
     */
    Page<LogicPassPolicyVo> page(Page page, @Param("query") ProjectLogicPassPolicy po);
}
