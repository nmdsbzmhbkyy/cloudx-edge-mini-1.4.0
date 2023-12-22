package com.aurine.cloudx.estate.util;

import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.constant.enums.DockModuleEnum;
import com.aurine.cloudx.estate.service.ProjectDockModuleConfService;
import com.aurine.cloudx.estate.vo.ProjectDockModuleConfWR20Vo;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 业务模块第三方对接配置参数工具类
 *
 * @description:
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-12-15
 * @Copyright:
 */
@Component
public class DockModuleConfigUtil {
    @Resource
    ProjectDockModuleConfService projectDockModuleConfService;


    public boolean isWr20(int projectId) {
        return getWr20Config(projectId) != null;
    }

    /**
     * 当前业务是否为WR20
     * 该方法只适用于具体项目业务调用，项目组、平台业务禁止使用
     * @return
     */
    public boolean isWr20() {
        return getWr20Config(ProjectContextHolder.getProjectId()) != null;
    }

    /**
     * 获取WR20配置参数
     * @param projectId
     * @return
     */
    public ProjectDockModuleConfWR20Vo getWr20Config(int projectId) {
        return projectDockModuleConfService.getConfigByProjectId(projectId, DockModuleEnum.WR20.code, ProjectDockModuleConfWR20Vo.class);
    }
}
