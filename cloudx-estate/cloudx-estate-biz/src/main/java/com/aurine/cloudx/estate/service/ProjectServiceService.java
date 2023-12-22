package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.entity.ProjectService;
import com.aurine.cloudx.estate.entity.SysServiceCfg;
import com.aurine.cloudx.estate.vo.ProjectServiceInfoVo;
import com.aurine.cloudx.estate.vo.ServiceProjectSaveVo;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 项目增值服务
 *
 * @author guhl@aurine.cn
 * @date 2020-06-04 11:18:39
 */
public interface ProjectServiceService extends IService<ProjectService> {



    /**
     * 根据项目Id查询增值服务
     *
     * @param
     *
     * @return
     */
    List<ProjectServiceInfoVo> getHouseServiceByProjectId(Integer projectId);

    /**
     * 关闭到期的项目增值服务
     */
    void removeExpireProjectService();

    /**
     * 关闭远端的项目增值服务
     *
     * @param projectId
     * @param serviceIds 本地要添加的增值服务id集合
     */
    void removeRemoteProjectService(Integer projectId, List<String> serviceIds);

    /**
     *
     * 项目配置增值服务
     * @param serviceProjectSaveVo
     * @return
     */
    Boolean saveByServiceIds(ServiceProjectSaveVo serviceProjectSaveVo);

    /**
     *
     * 当前项目所有增值服务
     * @param
     * @return
     */
    List<SysServiceCfg> getHouseServiceList();


}
