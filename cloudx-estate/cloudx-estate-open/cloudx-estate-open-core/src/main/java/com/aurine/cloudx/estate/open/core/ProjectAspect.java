package com.aurine.cloudx.estate.open.core;

import com.pig4cloud.pigx.admin.api.feign.RemoteUserService;
import com.pig4cloud.pigx.common.core.constant.SecurityConstants;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.security.util.SecurityUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Aspect
public class ProjectAspect {

    @Resource
    private RemoteUserService remoteUserService;

    @Resource
    private CacheManager cacheManager;

    private final static String CACHE_NAME = "auth_user";
    private final static String CACHE_NAME_PROJECT = "auth_project";

    @Before("execution(* com.aurine.cloudx.estate.open.*.controller.*.*(..))")
    public void before(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        Class<?>[] argTypes = new Class[joinPoint.getArgs().length];
        for (int i = 0; i < args.length; i++) {
            argTypes[i] = args[i].getClass();
        }

        // 是否忽略操作
        Method method = null;
        try {
            method = joinPoint.getTarget().getClass()
                    .getMethod(joinPoint.getSignature().getName(), argTypes);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        }

        UnCheckProject cp = method.getAnnotation(UnCheckProject.class);

        if (cp == null || !cp.isEnable()) {
            RequestAttributes ra = RequestContextHolder.getRequestAttributes();
            ServletRequestAttributes sra = (ServletRequestAttributes) ra;
            HttpServletRequest request = sra.getRequest();

            String projectId = request.getHeader("PROJECT-ID");

            String authorization = request.getHeader("Authorization");

            Integer userId = SecurityUtils.getUser().getId();

            List<Integer> data = new ArrayList<Integer>();

            if (!StringUtils.isEmpty(authorization) && !StringUtils.isEmpty(projectId)) {
                String auth = cacheManager.getCache(CACHE_NAME).get(userId, String.class);
                if (auth == null) {
                    cacheManager.getCache(CACHE_NAME).put(userId, authorization);
                } else if (auth.equals(authorization)) {
                    data = cacheManager.getCache(CACHE_NAME_PROJECT).get(auth, List.class);
                } else {
                    cacheManager.getCache(CACHE_NAME).put(userId, authorization);
                    cacheManager.getCache(CACHE_NAME_PROJECT).evict(auth);

                    R ur = remoteUserService.getUserDeptRole();

                    if (ur.getCode() == 0) {
                        List<Map> list = (List) ur.getData();
                        Map keys = new HashMap();

                        for (Map m : list) {
                            if ("3".equals(m.get("deptTypeId"))) {
                                int deptId = (int) m.get("deptId");
                                if (!keys.containsKey(deptId)) {
                                    data.add(deptId);
                                    keys.put(deptId, true);
                                }
                            }
                        }

                        // 验证是否存在项目id
                        cacheManager.getCache(CACHE_NAME_PROJECT).put(authorization, data);
                    }
                }

                if (!data.contains(Integer.parseInt(projectId))) {
                    throw new RuntimeException("没有PROJECT-ID " + projectId + " 的操作权限");
                }
            }

            // 判断是否进行忽略
            if (StringUtils.isEmpty(projectId)) {
                throw new RuntimeException("PROJECT-ID 不能为空");
            }
        }
    }
}
