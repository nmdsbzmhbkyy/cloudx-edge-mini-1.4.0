

package com.aurine.cloudx.open.origin.mapper;

import com.aurine.cloudx.open.common.entity.vo.PhysicalPassPolicyVo;
import com.aurine.cloudx.open.origin.entity.ProjectPhysicalPassPolicy;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 物理策略
 *
 * @author pigx code generator
 * @date 2020-05-20 15:44:19
 */
@Mapper
public interface ProjectPhysicalPassPolicyMapper extends BaseMapper<ProjectPhysicalPassPolicy> {

    /**
     * 多条件分页查询
     *
     * @param page
     * @param po
     * @return
     */
    Page<PhysicalPassPolicyVo> page(Page page, @Param("query") ProjectPhysicalPassPolicy po);
}
