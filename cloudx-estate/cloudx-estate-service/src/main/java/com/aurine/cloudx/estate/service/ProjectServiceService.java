

package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.entity.ProjectService;
import com.aurine.cloudx.estate.entity.SysServiceCfg;
import com.aurine.cloudx.estate.vo.ProjectServiceInfoVo;
import com.aurine.cloudx.estate.vo.ServiceProjectSaveVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 项目增值服务
 *
 * @author guhl@aurine.cn
 * @date 2020-06-04 11:18:39
 */
public interface ProjectServiceService extends IService<ProjectService> {

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
