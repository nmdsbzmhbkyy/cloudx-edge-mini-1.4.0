

package com.aurine.cloudx.estate.mapper;

import com.aurine.cloudx.estate.entity.SysGroup;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.admin.api.entity.SysDept;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 项目组
 *
 * @author xull@aurine.cn
 * @date 2020-04-30 16:04:44
 */
@Mapper
public interface SysGroupMapper extends BaseMapper<SysGroup> {

    /**
     * 集团管理员分页查询项目组
     *
     * @param page
     * @param sysGroup
     *
     * @return
     */
    Page<SysGroup> pageGroup(Page page, @Param("query") SysGroup sysGroup);

    /**
     * 根据id获取当前项目组下的所有项目组id
     *
     * @param id
     *
     * @return
     */
    List<Integer> findAllById(@Param("id") Integer id);

    /**
     * 根据集团id获取下级项目组部门列表(含集团)
     *
     * @param id
     *
     * @return
     */
    List<SysDept> selectTree(@Param("id") Integer id);

    /**
     * 根据集团id获取次级项目组id
     *
     * @param id
     *
     * @return
     */
    List<Integer> findByCompanyId(@Param("id") Integer id);

    /**
     * 根据项目组id获取本级及下级项目组
     *
     * @param id
     *
     * @return
     */
    List<SysGroup> findById(@Param("id") Integer id);
}
