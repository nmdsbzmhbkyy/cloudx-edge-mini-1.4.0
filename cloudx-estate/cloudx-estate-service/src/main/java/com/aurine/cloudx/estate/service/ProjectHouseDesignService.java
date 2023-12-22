

package com.aurine.cloudx.estate.service;


import com.aurine.cloudx.estate.entity.ProjectHouseDesign;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>户型设置</p>
 * @ClassName: ProjectHouseDesignService
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020/5/9 14:59
 * @Copyright:
 */
public interface ProjectHouseDesignService extends IService<ProjectHouseDesign> {
    /**
     * @author: 王伟 <wangwei@aurine.cn>
     * @param houseDesign
     * @return
     */
    boolean add(ProjectHouseDesign houseDesign);

    /**
     * 删除
     * @author: 王伟
     * @param houseDesignId
     * @return
     */
    boolean delete(String houseDesignId);

    /**
     * 获取首个户型
     * @param projectId
     * @return
     */
    ProjectHouseDesign getTopOne(int projectId);

}
