

package com.aurine.cloudx.open.origin.service;


import com.aurine.cloudx.open.origin.entity.ProjectHouseDesign;
import com.aurine.cloudx.open.common.entity.vo.HouseDesignVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>户型设置</p>
 *
 * @ClassName: ProjectHouseDesignService
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020/5/9 14:59
 * @Copyright:
 */
public interface ProjectHouseDesignService extends IService<ProjectHouseDesign> {
    /**
     * @param houseDesign
     * @return
     * @author: 王伟 <wangwei@aurine.cn>
     */
    boolean add(ProjectHouseDesign houseDesign);

    /**
     * 删除
     *
     * @param houseDesignId
     * @return
     * @author: 王伟
     */
    boolean delete(String houseDesignId);

    /**
     * 修改
     *
     * @param projectHouseDesign
     * @return
     * @author: 张博玉
     */
    boolean update(ProjectHouseDesign projectHouseDesign);

    /**
     * 获取首个户型
     *
     * @param projectId
     * @return
     */
    ProjectHouseDesign getTopOne(int projectId);

    /**
     * 分页查询
     *
     * @param page
     * @param vo
     * @return
     */
    Page<HouseDesignVo> page(Page page, HouseDesignVo vo);

}
