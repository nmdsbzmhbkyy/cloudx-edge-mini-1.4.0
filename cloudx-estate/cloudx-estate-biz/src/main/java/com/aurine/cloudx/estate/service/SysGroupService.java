

package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.entity.SysGroup;
import com.aurine.cloudx.estate.vo.ProjectGroupTreeVo;
import com.aurine.cloudx.estate.vo.SysGroupFormVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 项目组
 *
 * @author xull@aurine.cn
 * @date 2020-04-30 16:04:44
 */
public interface SysGroupService extends IService<SysGroup> {

    /**
     * 新增项目组
     *
     * @param entity
     * @return
     */
    Integer saveReturnId(SysGroupFormVo entity);

    /**
     * 更新项目组
     *
     * @param sysGroupFormVo
     * @return
     */
    boolean updateGroupAndUser(SysGroupFormVo sysGroupFormVo);

    /**
     * 分页查询项目组
     *
     * @param page
     * @param sysGroup
     *
     * @return
     */
    Page<SysGroup> pageGroup(Page page, @Param("query") SysGroup sysGroup);

    /**
     * 查询所有项目组及子项目组
     *
     * @param id
     *
     * @return
     */
    List<Integer> findAllById(Integer id);

    /**
     * 获取当前集团下所有项目组(含集团)
     *
     * @param id
     *
     * @return
     */
    List<ProjectGroupTreeVo> selectTree(Integer id);

    /**
     * 根据当前集团id获取次级所有项目组
     *
     * @param id
     *
     * @return
     */
    List<Integer> findByCompanyId(Integer id);

    List<SysGroup> findById(Integer id);
}
