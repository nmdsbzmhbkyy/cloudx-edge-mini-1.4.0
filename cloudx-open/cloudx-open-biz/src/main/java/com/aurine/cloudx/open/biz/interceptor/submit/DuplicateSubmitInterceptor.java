package com.aurine.cloudx.open.biz.interceptor.submit;

/**
 * @description:
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2021-02-19
 * @Copyright:
 */

import com.alibaba.fastjson.JSONObject;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Description: 将用户访问的url和参数结合token存入redis，每次访问进行验证是否重复请求接口
 * @Auther: 王伟 <wangwei@aurine.cn>
 * @create 2021-02-23 15:28
 */
@Component
@RefreshScope
@Slf4j
public class DuplicateSubmitInterceptor extends HandlerInterceptorAdapter {

    @Resource
    RedisTemplate<String, String> redisTemplate;

    /**
     * 需要校验的方法类型
     */
    @Value("${server.duplicate-submit-interceptor.methods:POST,PUT,DELETE}")
    private String methods;

    @Value("${server.duplicate-submit-interceptor.enable:true}")
    private boolean enable;

    /**
     * 全部校验
     * 当选择为全部校验时，只有@IgnoreDuplicateSubmit 标记的方法不会被校验，否则，只有被@DuplicateSubmit 的方法才会被校验
     */
    @Value("${server.duplicate-submit-interceptor.validate-all:true}")
    private boolean validateAll;


    /**
     * 重复提交间隔
     */
    @Value("${server.duplicate-submit-interceptor.ttl:200}")
    private long ttl;

    /**
     * 是否阻止提交
     *
     * @return false阻止, true放行
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (enable && handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();

            //校验传输方法核对，POST，GET，等
            if (StringUtils.equals(methods, "ALL") || methods.indexOf(request.getMethod()) >= 0) {
                //校验模式
                if (checkNeedValidate(method)) {
                    if (repeatDataValidator(request)) {
                        //请求数据相同
                        log.warn("请勿重复提交请求,url:" + request.getServletPath());
                        return false;
                    } else {//如果不是重复相同数据
                        return true;
                    }
                }
            }


            return true;
        } else {
            return super.preHandle(request, response, handler);
        }
    }


    /**
     * 是否需要执行重复校验
     *
     * @param method
     * @return
     */
    private boolean checkNeedValidate(Method method) {

        //配置为默认校验全部，配置了忽略重复提交注解的方法不进行校验
        if (validateAll) {
            if (method.getAnnotation(IgnoreDuplicateSubmit.class) == null) {
                return true;
            }
        } else {
            //只有配置了重复提交注解的方法才进行校验
            if (method.getAnnotation(DuplicateSubmit.class) != null) {
                return true;
            }
        }
        return false;

    }

    /**
     * 验证同一个url数据是否相同提交,相同返回true
     *
     * @param httpServletRequest
     * @return
     */
    public boolean repeatDataValidator(HttpServletRequest httpServletRequest) {

        //获取query参数
        String paramsString = JSONObject.toJSONString(httpServletRequest.getParameterMap());
        //获取body参数
        String bodyString = getBodyString(httpServletRequest);
//        String bodyString = "";

        String token = httpServletRequest.getHeader("authorization");

        if (StringUtils.isBlank(token)) {
            //如果没有token，直接放行（微服务调用等）
            return false;
        }

        //请求内容
//        log.debug("paramsString = {}", paramsString);
//        log.debug("bodyString = {}", bodyString);

        String url = httpServletRequest.getRequestURI();

        Map<String, String> map = new HashMap<>();
        map.put(url, paramsString + bodyString);
        String nowUrlParams = map.toString();

        String redisKey = token + url;
        String preUrlParams = redisTemplate.opsForValue().get(redisKey);
//        log.debug("preUrlParams = {}", preUrlParams);
        if (preUrlParams == null) {
            //如果上一个数据为null,表示还没有访问页面
            redisTemplate.opsForValue().set(redisKey, nowUrlParams, ttl, TimeUnit.MILLISECONDS);
            return false;
        } else {//否则，已经访问过页面
            if (preUrlParams.equals(nowUrlParams)) {
                //如果上次url+数据和本次url+数据相同，则表示重复添加数据
                log.error("重复请求已被过滤：{}", nowUrlParams);
                return true;
            } else {//如果上次 url+数据 和本次url加数据不同，则不是重复提交
                redisTemplate.opsForValue().set(redisKey, nowUrlParams, ttl, TimeUnit.MILLISECONDS);
                return false;
            }
        }
    }

    /**
     * 通过token + post长度进行双重校验
     *
     * @param request
     * @return
     */
    @SneakyThrows
    public String getBodyString(HttpServletRequest request) {

        return String.valueOf(request.getContentLengthLong());
//
//        BufferedReader bufferReader = new BufferedReader(request.getReader());
//        StringBuilder sb = new StringBuilder();
//        String line = null;
//        while ((line = bufferReader.readLine()) != null) {
//            sb.append(line);
//        }
//        return sb.toString();

    }
}