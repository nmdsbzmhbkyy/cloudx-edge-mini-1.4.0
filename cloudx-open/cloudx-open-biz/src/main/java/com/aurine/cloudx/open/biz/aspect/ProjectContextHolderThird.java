package com.aurine.cloudx.open.biz.aspect;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.aurine.cloudx.common.data.project.ProjectConfig;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import lombok.experimental.UtilityClass;

/**
 * 项目工具类
 * 
 * @ClassName: ProjectContextHolder.java
 * @author: 林港 <ling@aurine.cn>
 * @date: 2020年5月7日 上午10:39:26
 * @Copyright:
 */
@UtilityClass
public class ProjectContextHolderThird {


    private final ThreadLocal<Integer> THREAD_LOCAL_PROJECT_THIRD = new TransmittableThreadLocal<>();
    ProjectConfig projectConfig;
    /**
     * @param projectId
     */
    public void setProjectId(Integer projectId) {
        THREAD_LOCAL_PROJECT_THIRD.set(projectId);
    }

    public void setProjectConfig(ProjectConfig projectConfig){
        ProjectContextHolderThird.projectConfig=projectConfig;
    }

    /**
     * @return 
     */
    public Integer getProjectId() {
        return THREAD_LOCAL_PROJECT_THIRD.get();
    }

    public void clear() {
        THREAD_LOCAL_PROJECT_THIRD.remove();
    }
}
