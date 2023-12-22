package com.aurine.cloudx.common.core.util.web;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @description: 上下文工具类
 * @author: lingang@linewell.com
 * @since: 2014年12月5日 上午11:20:04
 */
public class ContextHolderUtil {
    /**
     * SpringMvc下获取request
     *
     * @return
     */
    public static HttpServletRequest getRequest() {
        if (RequestContextHolder.getRequestAttributes() == null) {
            return null;
        }
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return request;

    }

}
