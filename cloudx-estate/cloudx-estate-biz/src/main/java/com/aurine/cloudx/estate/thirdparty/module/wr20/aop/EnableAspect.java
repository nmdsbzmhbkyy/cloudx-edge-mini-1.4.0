package com.aurine.cloudx.estate.thirdparty.module.wr20.aop;

import com.aurine.cloudx.estate.thirdparty.module.wr20.config.WR20ConfigProperties;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Pointcut;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @description: 接口可用性切面
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-08-10
 * @Copyright:
 */
//@Aspect
//@Component
public class EnableAspect {
    @Resource
    private WR20ConfigProperties wr20ConfigProperties;

    @Pointcut("execution(* com.aurine.cloudx.estate.thirdparty.module.wr20.service.impl.*.*(..))  && !execution(* com.aurine.cloudx.estate.thirdparty.module.wr20.service.impl.*.get*(..))")
    public void aroundV1Execute() {
    }


    @Around("aroundV1Execute()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] obj = joinPoint.getArgs();
        int projectId = Integer.valueOf(obj[0].toString());

        List<Map<String, String>> projectConfigList = wr20ConfigProperties.getProjectList();

        try {
            //验证当前项目是否已在配置信息中
            boolean haveConfig = projectConfigList.stream().filter(a -> Integer.valueOf(a.get("projectid")) == projectId &&
                    "true".equalsIgnoreCase(a.get("enable"))).count() >= 1;
            if (haveConfig) {
                return joinPoint.proceed();
            }else{
                return false;
            }
        } catch (Exception e) {
            throw new RuntimeException("WR20配置信息错误",e);

        }

//            return joinPoint.proceed(new Object[]{requestCache, JSONObject.toJSONString(payload)});
    }

}
