package com.aurine.cloudx.common.data.project;

import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import com.aurine.cloudx.common.core.constant.WebConstants;

import cn.hutool.core.util.StrUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName:  ProjectContextHolderFilter   
 * @author: 林港 <ling@aurine.cn>
 * @date:   2020年5月7日 上午10:43:01      
 * @Copyright:
 */
@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ProjectContextHolderFilter extends GenericFilterBean {

    @Override
    @SneakyThrows
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String projId = request.getHeader(WebConstants.PROJECT_ID);
        log.debug("获取header中的项目id为:{}", projId);

        if (StrUtil.isNotBlank(projId)) {
            ProjectContextHolder.setProjectId(Integer.parseInt(projId));
        }

        filterChain.doFilter(request, response);
        ProjectContextHolder.clear();
    }
}
