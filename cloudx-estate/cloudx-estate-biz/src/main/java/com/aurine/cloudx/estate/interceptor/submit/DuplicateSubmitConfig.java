package com.aurine.cloudx.estate.interceptor.submit;

import com.pig4cloud.pigx.common.data.resolver.WebMvcConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2021-02-19
 * @Copyright:
 */
@Configuration
public class DuplicateSubmitConfig extends WebMvcConfig {


    /**
     * 防止重复提交拦截器
     */
    @Resource
    private DuplicateSubmitInterceptor duplicateSubmitInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 避开静态资源
        List<String> resourcePaths = defineResourcePaths();
        registry.addInterceptor(duplicateSubmitInterceptor).addPathPatterns("/**").excludePathPatterns(resourcePaths);// 重复请求
    }

    /**
     * 自定义静态资源路径
     *
     * @return
     */
    public List<String> defineResourcePaths() {
        List<String> patterns = new ArrayList<>();
        patterns.add("/excel/**");
        patterns.add("/static/**");
        return patterns;
    }
}
