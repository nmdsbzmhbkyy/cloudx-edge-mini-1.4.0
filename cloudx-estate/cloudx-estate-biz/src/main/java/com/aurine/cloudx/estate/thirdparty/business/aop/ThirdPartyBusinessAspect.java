package com.aurine.cloudx.estate.thirdparty.business.aop;

import com.aurine.cloudx.estate.thirdparty.business.entity.constant.ThirdPartyBusinessPlatformEnum;
import com.aurine.cloudx.estate.thirdparty.business.entity.constant.ThirdPartyBusinessServiceNameEnum;
import com.baomidou.mybatisplus.extension.service.IService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * 第三方业务实现切面
 * 用于对第三方业务模块的水平扩展
 *
 * @ClassName: ThirdpartyImplAspect
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-12-10 16:36
 * @Copyright:
 */
@Aspect
@Component
@Slf4j
public class ThirdPartyBusinessAspect {
    /**
     * 系统服务缓存
     */
    @Resource
    private Map<String, IService> serviceMap;

//    /**
//     * 迁入住户业务切面
//     */
//    @Pointcut("execution(* com.aurine.cloudx.estate.service.impl.WebProjectHousePersonRelServiceImpl.saveRel(..))")
//    public void saveHousePersonRelPointcut() {
//    }


    @Around(value = "@annotation(annotation)")
    public Object thirdPartyBusinessApiAround(ProceedingJoinPoint joinPoint, ThirdPartyBusinessApi annotation) throws Throwable {

        String methodName = joinPoint.getSignature().getName();//要反射调用的方法名
//        Class implClass = joinPoint.getTarget().getClass();//被调用实现的类名
        Class interfaceClass = joinPoint.getTarget().getClass().getInterfaces()[annotation.interfaceIndex()];//动态获取用实现的接口
        Class[] paramsTypeArray = this.objsToClasses(joinPoint.getArgs());


        //TODO： 获取项目第三方配置，配置信息缓存策略需要在数据库缓存、redis缓存和session缓存中选择
        ThirdPartyBusinessPlatformEnum platformEnum = ThirdPartyBusinessPlatformEnum.getByCode("WR20");

        //无配置信息,直接调用原有方法
        if (platformEnum == null) {
            return proceed(joinPoint);
        }

        //通过配置查询是否存在缓存实例, 不存在则执行
        IService service = serviceMap.get(ThirdPartyBusinessServiceNameEnum.getByPlatformAndInterface(interfaceClass, platformEnum).serviceName);
        if (service == null) {
            return proceed(joinPoint);
        }



        //获取要执行的接口方法
        Method method = null;
        try {
            //spring service使用了代理方式管理，获取到的实例需要获取原有的对象class
            method = this.getMethodByName(service.getClass().getSuperclass(), methodName, paramsTypeArray);
        } catch (NoSuchMethodException e) {
            return proceed(joinPoint);
        }

        log.info("[第三方业务对接] 业务方法动态调用 接入平台：{}, 调用实例:{},调用方法:{}", platformEnum, service.getClass().getGenericSuperclass(), method);
        return method.invoke(service, joinPoint.getArgs());
    }

    /**
     * 执行原有方法
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    private Object proceed(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("执行原有方法");
        return null;
//        return joinPoint.proceed();
    }

    /**
     * 根据名称 方法、参数类型 获取方法 实体
     *
     * @param classes
     * @param methodName
     * @param paramClass
     * @return
     */
    private Method getMethodByName(Class classes, String methodName, Class[] paramClass) throws NoSuchMethodException {
        Method returnMethod = null;
        if (paramClass != null) {
            returnMethod = classes.getDeclaredMethod(methodName, paramClass);

            if (returnMethod.getAnnotation(ThirdPartyBusinessApiEnable.class) == null) {
                throw new NoSuchMethodException("不存在带有ThirdPartyBusinessApiEnable注解的第三方方法实现");
            }

            return returnMethod;
        }

        for (Method method : classes.getDeclaredMethods()) {
            //返回同名且带有启用注解的方法
            if (method.getName().equals(methodName) && method.getAnnotation(ThirdPartyBusinessApiEnable.class) != null) {
                return method;
            }
        }
        throw new NoSuchMethodException("不存在带有ThirdPartyBusinessApiEnable注解的第三方方法实现");
    }

    /**
     * 获取obj数组的对象类型数组
     *
     * @param objectArray
     * @return
     */
    private Class[] objsToClasses(Object[] objectArray) {

        if (objectArray == null || objectArray.length == 0) {
            return null;
        }

        Class[] classArray = new Class[objectArray.length];

        for (int i = 0; i < classArray.length; i++) {
            classArray[i] = objectArray[i].getClass();
        }

        return classArray;
    }
}
