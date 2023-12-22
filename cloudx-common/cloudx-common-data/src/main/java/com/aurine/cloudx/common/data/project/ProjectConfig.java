package com.aurine.cloudx.common.data.project;


import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;


/**
 * @description:
 * @ClassName: CanalConfig
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2021-03-24 8:37
 * @Copyright:
 */
@Component
@Data
@Configuration
public class ProjectConfig {
    // 这是一个Java代码片段，用于设置服务器上的本地项目ID,{server.local-project-id:1000000708}：分别是设置的属性名和属性值
    @Value("${server.local-project-id:1000000708}")
    private Integer projectId;
}