package com.aurine.cloudx.common.data.project;

import com.alibaba.ttl.TransmittableThreadLocal;
import lombok.experimental.UtilityClass;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;

/**
 * 项目工具类
 * 
 * @ClassName: ProjectContextHolder.java
 * @author: 林港 <ling@aurine.cn>
 * @date: 2020年5月7日 上午10:39:26
 * @Copyright:
 */
@UtilityClass
public class ProjectContextHolder {


    private final ThreadLocal<Integer> THREAD_LOCAL_PROJECT = new TransmittableThreadLocal<>();
    ProjectConfig projectConfig;
    /**
     * @param projectId
     */
    public void setProjectId(Integer projectId) {
        THREAD_LOCAL_PROJECT.set(projectId);
    }

    public void setProjectConfig(ProjectConfig projectConfig){
        ProjectContextHolder.projectConfig=projectConfig;
    }

    /**
     * @return 
     */
    public Integer getProjectId() {
        //TODO: 如果当前线程没有projectId，则获取默认配置的ProjectId值 @since 20210926
        if(THREAD_LOCAL_PROJECT.get() == null) {
            ProjectContextHolder.setProjectId(projectConfig.getProjectId());
        }
        return THREAD_LOCAL_PROJECT.get();
    }

    public void clear() {
        THREAD_LOCAL_PROJECT.remove();
    }
}
