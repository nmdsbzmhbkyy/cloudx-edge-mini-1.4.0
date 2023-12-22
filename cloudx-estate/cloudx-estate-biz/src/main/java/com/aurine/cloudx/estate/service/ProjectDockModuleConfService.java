package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.entity.ProjectDockModuleConf;
import com.aurine.cloudx.estate.vo.ProjectDockModuleConfBaseVo;
import com.aurine.cloudx.estate.vo.ProjectDockModuleConfWR20Vo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>配置第三方对接数据</p>
 *
 * @ClassName: WebProjectDockModuleConfService
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-12-15 13:50
 * @Copyright:
 */
public interface ProjectDockModuleConfService extends IService<ProjectDockModuleConf> {


    /**
     * 保存WR20配置信息
     *
     * @param dockModuleConfWR20Vo
     * @return
     */
    boolean saveWR20(ProjectDockModuleConfWR20Vo dockModuleConfWR20Vo);

    /**
     * 删除WR20配置
     *
     * @return
     */
    boolean delWR20(Integer projectId);

    /**
     * 同步WR20数据
     *
     * @param projectId
     * @param type      1：框架 2：设备 3：住户员工权限
     * @return
     */
    boolean syncWr20(Integer projectId, String type);


    /**
     * 通过projectId和配置对象，获取组装好的配置参数
     *
     * @param projectId
     * @param moduleId
     * @param t
     * @param <T>
     * @return
     */
    <T extends ProjectDockModuleConfBaseVo> T getConfigByProjectId(int projectId, String moduleId, Class<T> t);


    /**
     * 通过第三方编号，获取对应的配置信息
     *
     * @param moduleId
     * @param t
     * @param <T>
     * @return
     */
    <T extends ProjectDockModuleConfBaseVo> T getConfigByThirdCode(String moduleId, String thirdCode, Class<T> t);

}
