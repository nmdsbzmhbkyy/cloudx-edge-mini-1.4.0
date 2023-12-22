package com.aurine.cloudx.estate.openapi.aspect;

import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.entity.OpenApiEntity;
import com.aurine.cloudx.estate.openapi.ToOpenApi;
import com.aurine.cloudx.estate.openapi.enums.OpenApiCascadeTypeEnum;
import com.aurine.cloudx.estate.openapi.enums.OpenApiServiceNameEnum;
import com.aurine.cloudx.estate.openapi.enums.OpenPushSubscribeCallbackTypeEnum;
import com.aurine.cloudx.estate.openapi.service.OpenApiMessageService;
import com.aurine.cloudx.estate.service.ProjectInfoService;
import com.pig4cloud.pigx.common.core.util.R;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 操作日志使用spring event异步入库
 *
 * @author L.cm
 */
@Aspect
@Component
@Slf4j
public class OpenApiAspect {

    @Resource
    private OpenApiMessageService openApiMessageService;

    @Resource
    @Lazy
    private ProjectInfoService projectInfoService;

    /**
     * service层切面
     */
    @Pointcut("execution(* com.aurine.cloudx.estate.service..*(..)) " +
            "&& @annotation(com.aurine.cloudx.estate.openapi.ToOpenApi)")
    public void toOpenApi() {
    }

    @Around(value = "toOpenApi()")
    public Object handle(ProceedingJoinPoint joinPoint) throws Throwable {
        //1.1获取目标对象对应的字节码对象
        Class<?> targetCls = joinPoint.getTarget().getClass();

        //1.2获取目标方法对象

        //1.2.1 获取方法签名信息从而获取方法名和参数类型
        Signature signature = joinPoint.getSignature();

        //1.2.1.1将方法签名强转成MethodSignature类型，方便调用
        MethodSignature ms = (MethodSignature) signature;

        //1.2.2通过字节码对象以及方法签名获取目标方法对象
        Method targetMethod = targetCls.getDeclaredMethod(ms.getName(), ms.getParameterTypes());

        //1.3获取目标方法对象上注解中的属性值

        //1.2.3 获取方法上的自定义requiredLog注解
        ToOpenApi annotation = targetMethod.getAnnotation(ToOpenApi.class);

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        //IP地址
        String ipAddr = getRemoteHost(request);
        String url = request.getRequestURL().toString();

        Object reqParams = preHandle(joinPoint);
        log.info("请求源IP:【{}】,请求URL:【{}】,请求参数:【{}】", ipAddr, url, reqParams);
        R respParams = (R) joinPoint.proceed();
        log.info("请求源IP:【{}】,请求URL:【{}】,返回参数:【{}】", ipAddr, url, respParams);
        System.err.println("respParams = " + JSONObject.toJSONString(respParams));
        // 向OpenApi发送消息
        if (respParams.getCode() == 0) {
            if (annotation.cascadeType().name != null && annotation.serviceType() != OpenPushSubscribeCallbackTypeEnum.CONFIG) {
                openApiMessageService.sendOpenApiMessage(buildOpenApiEntity(annotation, respParams));
            } else {
                openApiMessageService.sendOpenApiMessage(buildOpenApiEntity(annotation, respParams.getData()));
            }
        }
        return respParams;
    }

    /**
     * 入参数据
     *
     * @param joinPoint
     * @return
     */
    private Object preHandle(ProceedingJoinPoint joinPoint) {

        //1.这里获取到所有的参数值的数组
        Object[] args = joinPoint.getArgs();
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        //2.最关键的一步:通过这获取到方法的所有参数名称的字符串数组
        String[] parameterNames = methodSignature.getParameterNames();
        Map<String, Object> params = new ConcurrentHashMap<>();
        for (int i = 0; i < parameterNames.length; i++) {
            if (args[i] != null) {
                params.put(parameterNames[i], args[i]);
            }
        }
        return params;
    }

    /**
     * 获取目标主机的ip
     *
     * @param request
     * @return
     */
    private String getRemoteHost(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : ip;
    }

    /**
     * 设置openApiEntity
     *
     * @param annotation
     * @return
     */
    private OpenApiEntity buildOpenApiEntity(ToOpenApi annotation, Object data) {
        OpenApiEntity openApiEntity = new OpenApiEntity();
        openApiEntity.setServiceType(annotation.serviceType().name);
        openApiEntity.setServiceName(annotation.serviceName().name);
        openApiEntity.setOperateType(annotation.operateType().name);
        openApiEntity.setCascadeType(annotation.cascadeType().name);
        openApiEntity.setCommandType(annotation.commandType().name);
        openApiEntity.setData(buildOpenApiEntityData(annotation, data));
        System.err.println("openApiEntity = " + openApiEntity);
        return openApiEntity;
    }

    /**
     * 业务处理，需要发送非service的返回结果的data内容的方法在此方法中定义发送的内容
     *
     * @param annotation
     * @param data
     * @return
     */
    private Object buildOpenApiEntityData(ToOpenApi annotation, Object data) {
        if (OpenPushSubscribeCallbackTypeEnum.CASCADE.equals(annotation.serviceType())
                && OpenApiServiceNameEnum.CASCADE_APPLY.equals(annotation.serviceName())
                && OpenApiCascadeTypeEnum.APPLY.equals(annotation.cascadeType())) {
            // 级联申请操作需要发送额外数据
            Integer projectId = ProjectContextHolder.getProjectId();
            //边缘网关syncType获取
            return projectInfoService.getProjectInfoVoById(projectId);

        }
        return data;
    }

}
