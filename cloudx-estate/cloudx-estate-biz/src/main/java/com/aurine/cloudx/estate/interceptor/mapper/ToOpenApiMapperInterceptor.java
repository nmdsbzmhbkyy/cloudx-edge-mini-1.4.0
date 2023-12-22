package com.aurine.cloudx.estate.interceptor.mapper;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.common.core.util.StringUtil;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.constant.enums.IntoCloudStatusEnum;
import com.aurine.cloudx.estate.entity.EdgeCloudRequest;
import com.aurine.cloudx.estate.entity.OpenApiDeleteIdEntity;
import com.aurine.cloudx.estate.entity.OpenApiEntity;
import com.aurine.cloudx.estate.openapi.ToOpenApi;
import com.aurine.cloudx.estate.openapi.enums.OpenApiServiceNameEnum;
import com.aurine.cloudx.estate.openapi.enums.OpenPushSubscribeCallbackTypeEnum;
import com.aurine.cloudx.estate.openapi.service.OpenApiMessageService;
import com.aurine.cloudx.estate.service.EdgeCloudRequestService;
import com.aurine.cloudx.estate.util.EntityIdUtil;
import com.aurine.cloudx.estate.util.ObjectMapperUtil;
import com.aurine.cloudx.estate.util.RedisUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.enums.IEnum;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ParameterMode;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Mapper拦截器
 * 目的是为了对部分开放平台（OpenApi）需要表进行拦截，并转发给开放平台（OpenApi）
 *
 * @author : Qiu
 * @date : 2022 02 07 10:32
 */

@Component
@Intercepts({
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
})
@Slf4j
public class ToOpenApiMapperInterceptor implements Interceptor, ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Resource
    private OpenApiMessageService openApiMessageService;
    @Lazy
    @Resource
    private EdgeCloudRequestService edgeCloudRequestService;

    private final List<String> serviceNameDeleteIgnoreList = Arrays.asList(OpenApiServiceNameEnum.DEVICE_INFO.name, OpenApiServiceNameEnum.HOUSE_PERSON_INFO.name);

    private static ObjectMapper objectMapper = ObjectMapperUtil.instance();

    // 执行器的方法名称，无需修改
    private static final String METHOD_NAME_UPDATE = "update";
    // 前缀，数据库名称，用于在SQL语句中的表名添加前缀
    private static final String PREFIX_DATABASE_AURINE = "aurine.";
    private static final String CLOUD_REQUEST_COUNT_KEY = "IN_TO_CLOUD_REQUEST_COUNT";

    // MappedStatement对象中resource相关常量属性
    // mapper对象的分隔符
    private static final String RESOURCE_SEPARATOR_MAPPER_CLASS = "/";
    // mapper对象的后缀
    private static final String RESOURCE_SUFFIX_MAPPER_CLASS = ".java (best guess)";
    // xml文件的分隔符
    private static final String RESOURCE_SEPARATOR_MAPPER_XML = "\\\\";
    // xml文件的后缀
    private static final String RESOURCE_SUFFIX_MAPPER_XML = ".xml]";

    private static final String RESOURCE_URL = "URL [jar:file:";

    private final static String pattern = "^[1-9]\\d*$";

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        // 获取方法名称
        String methodName = invocation.getMethod().getName();
        // 如果不是update就不处理直接返回
        if (!METHOD_NAME_UPDATE.equals(methodName)) return invocation.proceed();

        // 获取参数列表
        Object[] args = invocation.getArgs();
        // 如果参数列表小于2就不处理直接返回
        if (args.length < 2) return invocation.proceed();

        MappedStatement mappedStatement = null;
        Object object = null;

        for (Object arg : args) {
            // 不为空就不需要继续执行了
            if (mappedStatement != null && object != null) break;

            // 如果参数是MappedStatement类型，就赋值给mappedStatement，否则就赋值给object
            if (arg instanceof MappedStatement) {
                mappedStatement = (MappedStatement) arg;
            } else {
                object = arg;
            }
        }

        // 其中有一项为空就不处理直接返回
        if (mappedStatement == null || object == null) return invocation.proceed();

        Class mapperClass = getMapperClass(mappedStatement);
        ToOpenApi toOpenApi = (ToOpenApi) mapperClass.getAnnotation(ToOpenApi.class);
        if (toOpenApi == null) return invocation.proceed();
        log.debug("触发mybatis切面");
//        System.err.println("触发mybatis切面");

        Integer projectId;
        if (OpenApiServiceNameEnum.CASCADE_APPLY.name.equals(toOpenApi.serviceName().name)) {
            ObjectNode objectNode = objectMapper.readValue(objectMapper.writeValueAsString(object), ObjectNode.class);
            JsonNode projectIdNode = objectNode.findPath("projectId");
            projectId = projectIdNode.asInt();
        } else {
            projectId = ProjectContextHolder.getProjectId();
        }

        Object countObj = RedisUtil.get(CLOUD_REQUEST_COUNT_KEY);
        long count = 0;

        if (countObj == null) {
            count = edgeCloudRequestService.count(new LambdaQueryWrapper<EdgeCloudRequest>().eq(EdgeCloudRequest::getProjectId, projectId)
                    .in(EdgeCloudRequest::getCloudStatus, IntoCloudStatusEnum.INTO_CLOUD.code, IntoCloudStatusEnum.UNBINDING.code));
            RedisUtil.set(CLOUD_REQUEST_COUNT_KEY,count,1);//设置1秒的缓存，规避高并发查询；该问题后期需要入云服务和应用服务使用同一个redis状态位，或发送解绑通知给业务系统解决。
        }else{
            count = (Long) countObj;
        }

        if (count == 0) {
            log.debug("mybatis切面，项目未入云，不进行后续处理");
//            System.err.println("mybatis切面，项目未入云，不进行后续处理");
            return invocation.proceed();
        }

        OpenApiEntity openApiEntity = new OpenApiEntity();
        // 要发送的对象
        Object o;
        // 操作类型（因为是用户型模拟，所以这里只写一个操作类型，可能还有其他类型）
        String operateType;
        // uuid
        String entityId = null;
        // 获取sql类型并判断，如果不是增删改就不处理直接返回，否则执行对应处理（转发给开放平台）
        SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();

        switch (sqlCommandType) {
            case INSERT:
                // 如果是INSERT，object就是对应的对象，所以不能直接根据object去查询map
                o = object;
                operateType = "add";
                break;
            case UPDATE:
                o = ((Map) object).values().toArray()[0];
                operateType = "update";
                break;
            case DELETE:
                // 过滤不需要发送的serviceName
                if (deleteIgnore(toOpenApi.serviceName().name)) {
                    return invocation.proceed();
                }
                // 如果是DELETE，object就是id，所以不能直接根据object去查询map
                entityId = object.toString();
                o = new OpenApiDeleteIdEntity(object);
                operateType = "delete";
                break;
            default:
                return invocation.proceed();
        }
        Object proceed = invocation.proceed();
        openApiEntity.setServiceType(toOpenApi.serviceType().name);
        openApiEntity.setServiceName(toOpenApi.serviceName().name);
        openApiEntity.setOperateType(operateType);
        try {
            openApiEntity.setEntityId(entityId == null ? EntityIdUtil.getId(o) : entityId);
        } catch (NullPointerException e) {
            openApiEntity.setEntityId(null);
        }
        openApiEntity.setData(o);
        boolean flag = false;
        if (StringUtil.isEmpty(openApiEntity.getEntityId()) || (openApiEntity.getEntityId().length() != 32 && !Pattern.matches(pattern, openApiEntity.getEntityId())) || openApiEntity.getServiceName().equals(OpenApiServiceNameEnum.DEVICE_PARAM_INFO.name)) {
            BoundSql boundSql = mappedStatement.getBoundSql(args[1]);
            String originalSql = boundSql.getSql();
            Object parameterObject = boundSql.getParameterObject();
            List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
            for (ParameterMapping parameterMapping : parameterMappings) {
                //获取property属性
                String propertyName = parameterMapping.getProperty();
            }
            originalSql = showSql(mappedStatement.getConfiguration(), boundSql, operateType);

            if (parameterObject != null) {
                originalSql = showSql(mappedStatement.getConfiguration(), boundSql, operateType);
            }
            Map map = new HashMap();
            map.put("sql", originalSql);
            openApiEntity.setServiceType(OpenPushSubscribeCallbackTypeEnum.OTHER.name);
            openApiEntity.setData(map);
            openApiEntity.setEntityId(null);
            log.info(originalSql);
//            System.err.println(originalSql);


        }
        // 发送openApiEntity给OpenApi
        openApiMessageService.sendOpenApiMessage(openApiEntity);
        return proceed;
    }

    public String showSql(Configuration configuration, BoundSql boundSql, String operateType) {
        //获取参数对象
        Object parameterObject = boundSql.getParameterObject();
        //获取当前的sql语句有绑定的所有parameterMapping属性
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        //去除空格
        String sql = boundSql.getSql().replaceAll("[\\s]+", " ");
        if (parameterMappings.size() > 0 && parameterObject != null) {
            TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
            /* 如果参数满足：org.apache.ibatis.type.TypeHandlerRegistry#hasTypeHandler(java.lang.Class<?>)
            org.apache.ibatis.type.TypeHandlerRegistry#TYPE_HANDLER_MAP
            * 即是不是属于注册类型(TYPE_HANDLER_MAP...等/有没有相应的类型处理器)
             * */
            if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
                sql = sql.replaceFirst("\\?", getParameterValue(parameterObject));
            } else {
                //装饰器，可直接操作属性值 ---》 以parameterObject创建装饰器
                //MetaObject 是 Mybatis 反射工具类，通过 MetaObject 获取和设置对象的属性值
                MetaObject metaObject = configuration.newMetaObject(parameterObject);
                String parameter = "NULL";
                //循环 parameterMappings 所有属性
                for (ParameterMapping parameterMapping : parameterMappings) {
                    if (parameterMapping.getMode() == ParameterMode.OUT) {
                        continue;
                    }
                    String propertyName = parameterMapping.getProperty();
                    MetaObject newMetaObject = configuration.newMetaObject(parameterObject);

                    if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
                        parameter = getParameterValue(parameterObject);
                    } else if (newMetaObject.hasGetter(propertyName)) {
                        parameter = getParameterValue(newMetaObject.getValue(propertyName));
                    } else if (boundSql.hasAdditionalParameter(propertyName)) {
                        parameter = getParameterValue(boundSql.getAdditionalParameter(propertyName));
                    }
                    sql = sql.replaceFirst("\\?", parameter);
                }
                if (operateType.equals("add")) {
                    sql = handleSql(sql);
                    log.info("处理后的sql{}", sql);
                }


            }

            String tableName = EntityIdUtil.getTableName(sql);
            if (StringUtil.isNotBlank(tableName)) {
                // 如果表名的前缀不是以"aurine."开头，则补上"aurine."的前缀
                if (!tableName.startsWith(PREFIX_DATABASE_AURINE)) {
                    sql = sql.replace(tableName, PREFIX_DATABASE_AURINE + tableName);
                }
            }
        }

        // 为了防止出现"aurine.aurine.tableName"的情况，这里再做一层过滤
        String abnormalDoubleDB = PREFIX_DATABASE_AURINE + PREFIX_DATABASE_AURINE;
        if (sql.contains(abnormalDoubleDB)) {
            sql = sql.replace(abnormalDoubleDB, PREFIX_DATABASE_AURINE);
        }

        return sql;
    }

    public static String handleSql(String sql) {
        boolean projectId = sql.indexOf("projectId") > 0;
        boolean tenant_id = sql.indexOf("tenant_id") > 0;
        if (projectId || tenant_id) {
            int i = sql.indexOf("(");
            String insert = sql.substring(0, i);
            int i1 = sql.indexOf(")");
            String paramName = sql.substring(i + 1, i1);
            List<String> paramNameList = Arrays.stream(paramName.split(", ")).collect(Collectors.toList());
            int indexOf = sql.lastIndexOf("(");
            int lastIndexOf = sql.lastIndexOf(")");
            String param = sql.substring(indexOf + 1, lastIndexOf);
            List<String> paramList = Arrays.stream(param.split(", ")).collect(Collectors.toList());
            Map<String, String> map = new HashMap();

            if (paramNameList.size() == paramList.size()) {
                for (int j = 0; j < paramNameList.size(); j++) {
                    map.put(paramNameList.get(j).trim(), paramList.get(j).trim());
                }
            }
            if (projectId) {
                map.remove("projectId");
            }
            if (tenant_id) {
                map.remove("tenant_id");
            }

            paramNameList = new ArrayList<>();
            paramList = new ArrayList<>();
            for (Map.Entry<String, String> map2 : map.entrySet()) {
                paramNameList.add(map2.getKey());
                paramList.add(map2.getValue());
            }
            String paramNameResult = paramNameList.toString().replace("[", "(").replace("]", ")");
            String substring = paramList.toString().substring(1);
            String paramResult = substring.substring(0, substring.length() - 1);

            StringBuilder sb = new StringBuilder();
            return sb.append(insert).append(paramNameResult).append(" VALUES ").append("( " + paramResult + " )").toString();

        }
        return sql;
    }


    private static String getParameterValue(Object param) {
        if (param == null) {
            return "null";
        }
        if (param instanceof Number) {
            return param.toString();
        }
        String value = null;
        if (param instanceof String) {
            value = param.toString();
        } else if (param instanceof Date) {
            DateUtil.format((Date) param, "yyyy-MM-dd HH:mm:ss");
        } else if (param instanceof IEnum) {
            value = String.valueOf(((IEnum) param).getValue());
        } else {
            value = param.toString();
        }
        return StringUtils.quotaMark(value);
    }

    private static Class getMapperClass(MappedStatement mappedStatement) throws Throwable {
//        String resource = mappedStatement.getResource();
//        String id = mappedStatement.getId();
//
//
//        // 默认resource是mapperClass处理，将source后缀替换为空再替换分隔符
//        String mapperClassName = resource.replace(RESOURCE_SUFFIX_MAPPER_CLASS, "").replace(RESOURCE_SEPARATOR_MAPPER_CLASS, ".");
//
//        // 判断resource文件后缀是否是xml文件类型
//        if (resource.endsWith(RESOURCE_SUFFIX_MAPPER_XML)) {
//            if(resource.indexOf(RESOURCE_URL)!=-1) {
//                String[] resourceSplit = resource.replace(RESOURCE_SUFFIX_MAPPER_XML, "").replace(
//                        RESOURCE_URL,""
//                ).split("/");
//                String simpleMapperClass = lowerCaseFirstChar(resourceSplit[resourceSplit.length - 1]);
//                Object bean = SpringUtil.getBean(simpleMapperClass);
//                return applicationContext.getBean(simpleMapperClass).getClass();
//            }else {
//                // 将source后缀替换为空再以分隔符切割
//                String[] resourceSplit = resource.replace(RESOURCE_SUFFIX_MAPPER_XML, "").split(RESOURCE_SEPARATOR_MAPPER_XML);
//                // 获取数组最后一个字符串（即Mapper的文件名），并将首字母转为小写
//                String simpleMapperClass = lowerCaseFirstChar(resourceSplit[resourceSplit.length - 1]);
//                Class bean = SpringUtil.getBean(simpleMapperClass).getClass();
//                // 根据数组最后一个字符串（即Mapper的文件名）查找对象并返回对象类型
//                return applicationContext.getBean(simpleMapperClass).getClass();
//            }
//
//        }
//
//        return Class.forName(mapperClassName);
        return Class.forName(mappedStatement.getId().substring(0, mappedStatement.getId().lastIndexOf(".")));
    }


    private static String lowerCaseFirstChar(String s) {
        return s.substring(0, 1).toLowerCase() + s.substring(1);
    }

    /**
     * 获取mapper类的泛型类型
     *
     * @param mappedStatement
     * @return
     * @throws Throwable
     */
    private static Class getMapperClassParameterizedType(MappedStatement mappedStatement) throws Throwable {
        Class clazz = getMapperClass(mappedStatement);

        Class parameterizedTypeClass = null;
        try {
            //getGenericInterfaces()获得带有泛型的父接口
            //Type是 Java 编程语言中所有类型的公共高级接口。它们包括原始类型、参数化类型、数组类型、类型变量和基本类型。
            Type[] genericInterfaces = clazz.getGenericInterfaces();
            //ParameterizedType参数化类型，即泛型
            ParameterizedType p = (ParameterizedType) genericInterfaces[0];
            //getActualTypeArguments获取参数化类型的数组，泛型可能有多个
            parameterizedTypeClass = (Class) p.getActualTypeArguments()[0];
        } catch (Exception e) {
            return null;
        }

        return parameterizedTypeClass;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ToOpenApiMapperInterceptor.applicationContext = applicationContext;
    }

    private boolean deleteIgnore(String serviceName) {
        return serviceNameDeleteIgnoreList.contains(serviceName);
    }
}
