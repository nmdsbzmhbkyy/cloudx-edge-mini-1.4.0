package com.aurine.cloudx.open.biz.aspect;

import com.aurine.cloudx.open.common.entity.util.HandleResult;
import com.aurine.cloudx.open.common.entity.base.OpenApiHeader;
import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.common.entity.vo.OpenAppInfoVo;
import com.aurine.cloudx.open.common.entity.vo.ProjectInfoVo;
import com.aurine.cloudx.open.biz.service.OpenAppInfoService;
import com.aurine.cloudx.open.biz.service.OpenProjectInfoService;
import com.aurine.cloudx.open.common.core.annotation.AutoInject;
import com.aurine.cloudx.open.common.core.annotation.SkipCheck;
import com.aurine.cloudx.open.common.core.constant.enums.OpenFieldEnum;
import com.aurine.cloudx.open.common.core.constant.RedisConstant;
import com.aurine.cloudx.open.common.core.constant.enums.RedisKeySuffixEnum;
import com.aurine.cloudx.open.common.core.exception.CloudxOpenErrorEnum;
import com.aurine.cloudx.open.common.core.exception.CloudxOpenException;
import com.aurine.cloudx.open.common.core.util.JSONConvertUtils;
import com.aurine.cloudx.open.common.core.util.RedisUtils;
import com.aurine.cloudx.open.common.core.util.ReflectUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * OpenApi切面
 *
 * @author : Qiu
 * @date : 2022 01 17 20:26
 */

@Component
@Aspect
@Slf4j
public class OpenApiAspect {

    /*
    // 本地缓存的方式

    // 应用信息Map，用于根据应用ID（appId）做映射
    private final Map<String, OpenAppInfoVo> appInfoMap = new HashMap<>();
    // 项目信息Map，用于根据项目UUID（projectUUID）做映射
    private final Map<String, ProjectInfoVo> projectInfoMap = new HashMap<>();
    */

    private static final String REDIS_KEY_APP_INFO = RedisConstant.getRedisKey(RedisKeySuffixEnum.APP_INFO.suffix);
    private static final String REDIS_KEY_PROJECT_INFO = RedisConstant.getRedisKey(RedisKeySuffixEnum.PROJECT_INFO.suffix);

    @Resource
    private OpenAppInfoService openAppInfoService;
    @Resource
    private OpenProjectInfoService openProjectInfoService;


    @Before("execution(* com.aurine.cloudx.open..*.controller..*.*(..))")
    public void before(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        log.info("[OpenAspect - before]: 参数列表, args={}", JSONConvertUtils.arrayToJSONString(args));

        // 获取到目标方法
        Method method = this.getTargetMethod(joinPoint);
        if (method == null) throw new CloudxOpenException(CloudxOpenErrorEnum.NOT_FOUND.getMsg());

        // 切面具体处理
        this.checkArgs(args, method);
        this.autoInject(args, method);
    }

    /**
     * 获取目标方法
     *
     * @param joinPoint
     * @return
     */
    private Method getTargetMethod(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();

        Method method;
        try {
            Class<?>[] argTypes = new Class[args.length];
            for (int i = 0; i < args.length; i++) {
                argTypes[i] = args[i].getClass();
            }
            method = joinPoint.getTarget().getClass().getMethod(joinPoint.getSignature().getName(), argTypes);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return method;
    }

    /**
     * 参数检测
     *
     * @param args   入参对象列表
     * @param method 目标方法
     * @return
     */
    private void checkArgs(Object[] args, Method method) {
        // 如果有跳过参数检测注解，如果是开启状态且排除数组没有数据，就直接return
        SkipCheck skipCheck = method.getAnnotation(SkipCheck.class);
        if (skipCheck != null && skipCheck.enable() && skipCheck.exclude().length <= 0) {
            log.info("[OpenAspect - checkArgs]: 跳过参数检测");
            return;
        }

        List<OpenFieldEnum> exclude = new ArrayList<>();
        if (skipCheck != null) exclude = Arrays.asList(skipCheck.exclude());

        HandleResult result = checkArgs(args, exclude);
        log.info("[OpenAspect - checkArgs]: 参数检测结束, result={}", JSONConvertUtils.objectToString(result));

        // 如果处理结果是false，就表示处理出问题了
        if (!result.getResult()) {
            // 如果错误消息message不为空，则错误信息就使用message，否则使用设定的错误信息
            String message = result.getMessage();
            if (StringUtils.isNotEmpty(message)) throw new CloudxOpenException(message);
            throw new CloudxOpenException(CloudxOpenErrorEnum.ARGUMENT_INVALID.getMsg());
        }
    }

    /**
     * 自动注入
     * 把请求头信息中的数据，注入到对象信息中（如果对象信息存在该属性的话）
     *
     * @param args   入参对象列表
     * @param method 目标方法
     * @return
     */
    private void autoInject(Object[] args, Method method) {
        // 如果有自动注入注解，就需要把请求头数据转入到对象数据中
        AutoInject autoInject = method.getAnnotation(AutoInject.class);
        if (autoInject == null || !autoInject.enable()) {
            log.info("[OpenAspect - autoInject]: 未开启自动注入");
            return;
        }
        boolean result = autoInject(args, Arrays.asList(autoInject.exclude()));
        log.info("[OpenAspect - autoInject]: 自动注入结束, result={}", result);
    }

    /**
     * 参数检测
     *
     * @param args 入参对象列表
     * @return
     */
    private HandleResult checkArgs(Object[] args, List<OpenFieldEnum> exclude) {
        HandleResult result = new HandleResult();

        // 没有参数就直接返回失败
        if (args == null || args.length <= 0) return result.failed();

        // 遍历参数数组，只要有一项检测成功，就直接返回成功
        for (Object arg : args) {
            HandleResult checkArgsResult = checkArgs(arg, exclude);
            if (checkArgsResult.getResult()) return result.success();

            // 优先级判断，优先返回优先级高的错误信息
            Integer priority = checkArgsResult.getPriority();
            if (priority <= result.getPriority()) result.failed(checkArgsResult.getMessage());
        }

        return result;
    }

    /**
     * 参数检测
     *
     * @param object 目标对象
     * @return
     */
    private HandleResult checkArgs(Object object, List<OpenFieldEnum> exclude) {
        HandleResult result = new HandleResult();

        // 对象为空就直接返回失败
        if (object == null) return result.failed();

        String appId = null;
        String projectUUID = null;
        Integer tenantId = null;
        if (object instanceof OpenApiHeader) {
            appId = ((OpenApiHeader) object).getAppId();
            projectUUID = ((OpenApiHeader) object).getProjectUUID();
            tenantId = ((OpenApiHeader) object).getTenantId();
        } else if (object instanceof OpenApiModel) {
            @Valid @NotNull(message = "请求头信息（header）不能为空") OpenApiHeader header = ((OpenApiModel) object).getHeader();
            appId = header.getAppId();
            projectUUID = header.getProjectUUID();
            tenantId = header.getTenantId();
        } else {
            // 如果object不是前几个类型，优先级就不需要设置太高
            result.setPriority(9);
        }

        // appId校验
        // 如果排除集合不存在appId属性，就需要校验
        if (!exclude.contains(OpenFieldEnum.APP_ID)) {
            HandleResult checkAppIdResult = checkAppId(appId);
            if (!checkAppIdResult.getResult()) return result.failed(checkAppIdResult.getMessage());
        }

        // projectUUID和tenantId校验
        // 如果排除集合不存在projectUUID并且不存在tenantId，就需要校验
        if (!exclude.contains(OpenFieldEnum.PROJECT_UUID) && !exclude.contains(OpenFieldEnum.TENANT_ID)) {
            HandleResult checkProjectUUIDAndTenantIdResult = checkProjectUUIDAndTenantId(projectUUID, tenantId);
            if (!checkProjectUUIDAndTenantIdResult.getResult())
                return result.failed(checkProjectUUIDAndTenantIdResult.getMessage());
        }

        // 目前没有多余校验规则，返回true
        return result.success();
    }

    /**
     * 自动注入
     * 把请求头信息中的数据，注入到对象信息中（如果对象信息存在该属性的话）
     *
     * @param args 目标对象列表
     * @return
     */
    private boolean autoInject(Object[] args, List<OpenFieldEnum> exclude) {
        if (args == null || args.length <= 0) return false;

        boolean result = false;

        // 遍历参数数组，只要有一项注入成功，结果就是成功
        for (Object arg : args) {
            result = result || autoInject(arg, exclude);
        }

        return result;
    }

    /**
     * 自动注入
     * 把请求头信息中的数据，注入到对象信息中（如果对象信息存在该属性的话）
     *
     * @param object 目标对象
     * @return
     */
    private boolean autoInject(Object object, List<OpenFieldEnum> exclude) {
        if (object == null) return false;

        OpenApiHeader header = null;
        Object data = null;

        String appId;
        String projectUUID;
        Integer tenantId;
        Integer projectId;

        if (object instanceof OpenApiHeader) {
            header = (OpenApiHeader) object;
        } else if (object instanceof OpenApiModel) {
            header = ((OpenApiModel) object).getHeader();
            data = ((OpenApiModel) object).getData();
        }

        if (header == null) return false;

        appId = header.getAppId();
        projectUUID = header.getProjectUUID();
        tenantId = header.getTenantId();

        projectId = this.getProjectIdByProjectUUID(projectUUID);
        header.setProjectId(projectId);
        ProjectContextHolderThird.setProjectId(projectId);

        if (data != null) {
            // 如果data是集合类型，则需要for循环的方式遍历注入
            if (data instanceof List) {
                for (Object o : ((List<Object>) data)) {
                    // 如果排除数组中不存在appId属性的话，就需要自动注入
                    if (!exclude.contains(OpenFieldEnum.APP_ID)) {
                        ReflectUtils.findAndSetFieldByName(o, OpenFieldEnum.APP_ID.name, appId);
                    }
                    // 如果排除数组中不存在projectUUID属性的话，就需要自动注入
                    if (!exclude.contains(OpenFieldEnum.PROJECT_UUID)) {
                        ReflectUtils.findAndSetFieldByName(o, OpenFieldEnum.PROJECT_UUID.name, projectUUID);
                    }
                    // 如果排除数组中不存在tenantId属性的话，就需要自动注入
                    if (!exclude.contains(OpenFieldEnum.TENANT_ID)) {
                        ReflectUtils.findAndSetFieldByName(o, OpenFieldEnum.TENANT_ID.name, tenantId);
                    }
                    // 如果排除数组中不存在projectId属性的话，就需要自动注入
                    if (!exclude.contains(OpenFieldEnum.PROJECT_ID)) {
                        ReflectUtils.findAndSetFieldByName(o, OpenFieldEnum.PROJECT_ID.name, projectId);
                    }
                }
            } else {
                // 如果排除数组中不存在appId属性的话，就需要自动注入
                if (!exclude.contains(OpenFieldEnum.APP_ID)) {
                    ReflectUtils.findAndSetFieldByName(data, OpenFieldEnum.APP_ID.name, appId);
                }
                // 如果排除数组中不存在projectUUID属性的话，就需要自动注入
                if (!exclude.contains(OpenFieldEnum.PROJECT_UUID)) {
                    ReflectUtils.findAndSetFieldByName(data, OpenFieldEnum.PROJECT_UUID.name, projectUUID);
                }
                // 如果排除数组中不存在tenantId属性的话，就需要自动注入
                if (!exclude.contains(OpenFieldEnum.TENANT_ID)) {
                    ReflectUtils.findAndSetFieldByName(data, OpenFieldEnum.TENANT_ID.name, tenantId);
                }
                // 如果排除数组中不存在projectId属性的话，就需要自动注入
                if (!exclude.contains(OpenFieldEnum.PROJECT_ID)) {
                    ReflectUtils.findAndSetFieldByName(data, OpenFieldEnum.PROJECT_ID.name, projectId);
                }
            }
        }

        return true;
    }

    /**
     * 应用ID（appId）校验
     *
     * @param appId 应用ID
     * @return 是否通过校验
     */
    private HandleResult checkAppId(String appId) {
        HandleResult result = new HandleResult();

        if (StringUtils.isEmpty(appId)) return result.failed("应用ID（appId）为空");

        // 根据应用id（appId）获取openAppInfoVo（开放平台应用信息Vo）
        OpenAppInfoVo vo = this.getOpenAppByAppId(appId);
        if (vo == null) return result.failed("不存在该应用ID（appId）的应用信息, appId=%s", appId);

        // 其他校验
        return result.success();
    }

    /**
     * 项目UUID（projectUUID）和租户ID（tenantId）校验
     *
     * @param projectUUID 项目UUID
     * @param tenantId    租户ID
     * @return 是否通过校验
     */
    private HandleResult checkProjectUUIDAndTenantId(String projectUUID, Integer tenantId) {
        HandleResult result = new HandleResult();

        if (StringUtils.isEmpty(projectUUID)) result.failed("项目UUID（projectUUID）为空");
        if (tenantId == null) result.failed("租户ID（tenantId）为空");

        // 判断是否存在某个项目的项目UUID（projectUUID）和租户ID（tenantId）匹配
        ProjectInfoVo vo = this.getProjectByProjectUUID(projectUUID);
        if (vo == null) return result.failed("不存在该项目UUID（projectUUID）的项目, projectUUID=%s", projectUUID);
        if (!tenantId.equals(vo.getTenantId()))
            return result.failed("项目UUID（projectUUID）或租户ID（tenantId）不存在, projectUUID=%s, tenantId=%s", projectUUID, tenantId);

        // 其他校验
        return result.success();
    }

    /**
     * 根据projectUUID（项目UUID）获取projectId（项目ID）
     *
     * @param projectUUID 项目UUID
     * @return projectId（项目ID）
     */
    private Integer getProjectIdByProjectUUID(String projectUUID) {
        ProjectInfoVo project = this.getProjectByProjectUUID(projectUUID);
        return project == null ? null : project.getProjectId();
    }

    /**
     * 根据应用id（appId）获取openAppInfoVo（开放平台应用信息Vo）
     *
     * @param appId 应用id
     * @return OpenAppInfoVo（开放平台应用信息Vo）
     */
    private OpenAppInfoVo getOpenAppByAppId(String appId) {
        OpenAppInfoVo openAppInfoVo;

        Map<String, OpenAppInfoVo> appInfoMap = RedisUtils.hmget(REDIS_KEY_APP_INFO, String.class, OpenAppInfoVo.class);
        // 判断应用map中是否已存在对应的应用id
        if (appInfoMap.containsKey(appId)) {
            // 若存在则直接取出并赋值
            openAppInfoVo = appInfoMap.get(appId);
        } else {
            // 若不存在则重新查询一次，如果不为空就保存到map
            openAppInfoVo = openAppInfoService.getByAppId(appId).getData();
            if (openAppInfoVo != null) {
                appInfoMap.put(appId, openAppInfoVo);

                RedisUtils.hmsetBySerializable(REDIS_KEY_APP_INFO, appInfoMap);
            }
        }

        return openAppInfoVo;
    }

    /**
     * 根据projectUUID（项目UUID）获取projectInfoVo（项目信息Vo）
     *
     * @param projectUUID 项目UUID
     * @return ProjectInfoVo（项目信息Vo）
     */
    private ProjectInfoVo getProjectByProjectUUID(String projectUUID) {
        ProjectInfoVo projectInfoVo;

        Map<String, ProjectInfoVo> projectInfoMap = RedisUtils.hmget(REDIS_KEY_PROJECT_INFO, String.class, ProjectInfoVo.class);
        // 判断项目map中是否已存在对应的项目UUID
        if (projectInfoMap.containsKey(projectUUID)) {
            // 若存在则直接取出并赋值
            projectInfoVo = projectInfoMap.get(projectUUID);
        } else {
            // 若不存在则重新查询一次，如果不为空就保存到map
            projectInfoVo = openProjectInfoService.getByProjectUUID(projectUUID).getData();
            if (projectInfoVo != null) {
                projectInfoMap.put(projectUUID, projectInfoVo);

                RedisUtils.hmsetBySerializable(REDIS_KEY_PROJECT_INFO, projectInfoMap);
            }
        }

        return projectInfoVo;
    }

//    /**
//     * 根据projectUUID（项目UUID）获取projectInfoVo（项目信息Vo）
//     * 本地缓存的方式
//     *
//     * @param projectUUID 项目UUID
//     * @return ProjectInfoVo（项目信息Vo）
//     */
//    private ProjectInfoVo getProjectByProjectUUID(String projectUUID) {
//        ProjectInfoVo projectInfoVo;
//
//        // 判断项目map中是否已存在对应的项目UUID
//        if (projectInfoMap.containsKey(projectUUID)) {
//            // 若存在则直接取出并赋值
//            projectInfoVo = projectInfoMap.get(projectUUID);
//        } else {
//            // 若不存在则重新查询一次，如果不为空就保存到map
//            projectInfoVo = openProjectInfoService.getByProjectUUID(projectUUID).getData();
//            if (projectInfoVo != null) {
//                projectInfoMap.put(projectUUID, projectInfoVo);
//            }
//        }
//
//        return projectInfoVo;
//    }
//
//    /**
//     * 根据应用id（appId）获取openAppInfoVo（开放平台应用信息Vo）
//     * 本地缓存的方式
//     *
//     * @param appId 应用id
//     * @return OpenAppInfoVo（开放平台应用信息Vo）
//     */
//    private OpenAppInfoVo getOpenAppByAppId(String appId) {
//        OpenAppInfoVo openAppInfoVo;
//
//        // 判断应用map中是否已存在对应的应用id
//        if (appInfoMap.containsKey(appId)) {
//            // 若存在则直接取出并赋值
//            openAppInfoVo = appInfoMap.get(appId);
//        } else {
//            // 若不存在则重新查询一次，如果不为空就保存到map
//            openAppInfoVo = openAppInfoService.getByAppId(appId).getData();
//            if (openAppInfoVo != null) {
//                appInfoMap.put(appId, openAppInfoVo);
//            }
//        }
//
//        return openAppInfoVo;
//    }
}
