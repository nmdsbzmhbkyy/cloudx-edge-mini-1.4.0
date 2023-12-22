package com.aurine.cloudx.common.data.mybatis;

import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds; 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

/**
 * 自定义拦截器方法，处理模糊查询中包含特殊字符（_、%、\）
 */
@Intercepts(@Signature(type = Executor.class, method = "query", args = { MappedStatement.class, Object.class,
        RowBounds.class, ResultHandler.class }))
public class EscapeInterceptor implements Interceptor {

    Logger LOGGER = LoggerFactory.getLogger(EscapeInterceptor.class);

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        // 拦截sql
        Object[] args = invocation.getArgs();
        MappedStatement statement = (MappedStatement) args[0];
        Object parameterObject = args[1];
        BoundSql boundSql = statement.getBoundSql(parameterObject);
        String sql = boundSql.getSql();
        // 处理特殊字符
        modifyLikeSql(sql, parameterObject, boundSql);
        // 返回
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }

    @SuppressWarnings("unchecked")
    public static String modifyLikeSql(String sql, Object parameterObject, BoundSql boundSql) {
        if (parameterObject instanceof HashMap) {
        } else {
            return sql;
        }
        if (!sql.toLowerCase().contains(" like ") || !sql.toLowerCase().contains("?")) {
            return sql;
        }
        // 获取关键字的个数（去重）
        String[] strList = sql.split("\\?");
        Set<String> keyNames = new HashSet<>();
        for (int i = 0; i < strList.length; i++) {
            if (strList[i].toLowerCase().contains(" like ")) {
                String keyName = boundSql.getParameterMappings().get(i).getProperty();
                keyNames.add(keyName);
            }
        }
        // 对关键字进行特殊字符“清洗”，如果有特殊字符的，在特殊字符前添加转义字符（\）
        for (String keyName : keyNames) {
            HashMap parameter = (HashMap) parameterObject;
            if (keyName.contains("ew.paramNameValuePairs.") && sql.toLowerCase().contains(" like ?")) {
                // 第一种情况：在业务层进行条件构造产生的模糊查询关键字
                AbstractWrapper wrapper = (AbstractWrapper) parameter.get("ew");
                parameter = (HashMap) wrapper.getParamNameValuePairs();

                String[] keyList = keyName.split("\\.");
                // ew.paramNameValuePairs.MPGENVAL1，截取字符串之后，获取第三个，即为参数名
                Object a = parameter.get(keyList[2]);
                if (a instanceof String
                        && (a.toString().contains("_") || a.toString().contains("\\") || a.toString().contains("%"))) {
                    parameter.put(keyList[2],
                            "%" + EscapeUtil.escapeChar(a.toString().substring(1, a.toString().length() - 1)) + "%");
                }
            } else if (!keyName.contains("ew.paramNameValuePairs.") && sql.toLowerCase().contains(" like ?")) {
                // 第二种情况：未使用条件构造器，但是在service层进行了查询关键字与模糊查询符`%`手动拼接
                Object a = parameter.get(keyName);
                if (a instanceof String
                        && (a.toString().contains("_") || a.toString().contains("\\") || a.toString().contains("%"))) {
                    parameter.put(keyName,
                            "%" + EscapeUtil.escapeChar(a.toString().substring(1, a.toString().length() - 1)) + "%");
                }
            } else {
                // 第三种情况：在Mapper类的注解SQL中进行了模糊查询的拼接
                getParameterKey(parameter, keyName);
            }
        }
        return sql;
    }
    
    private static void getParameterKey(HashMap parameter ,String keyName) {
        String[] keyList = keyName.split("\\.");
        
        if (keyList.length == 1) {
            Object a = parameter.get(keyName);
            
            if (a instanceof String
                    && (a.toString().contains("_") || a.toString().contains("\\") || a.toString().contains("%"))) {
                parameter.put(keyName, EscapeUtil.escapeChar(a.toString()));
            }
        } else {
            Object a = getter(parameter.get(keyList[0]), keyList[1]);
            
            if (a instanceof String
                    && (a.toString().contains("_") || a.toString().contains("\\") || a.toString().contains("%"))) {
                
                setter(parameter.get(keyList[0]), keyList[1], EscapeUtil.escapeChar(a.toString()), String.class);
            }
        }
    }
    
    private static Object getter(Object obj, String attr) {// 调用getter方法  
        try {  
            Method method = obj.getClass().getMethod("get" + initStr(attr));// 此方法不需要参数，如：getName(),getAge()  
            return method.invoke(obj);// 接收方法的返回值,输出结果  
        } catch (Exception e) {  
            e.printStackTrace();  
        }
        return null;  
    }
    
    private static void setter(Object obj, String attr, Object value, Class<?> type) {  
        try {  
            // 第一个参数表示方法名称，setAge、setName,第二个参数表示类型，如int.class,String.class  
            Method method = obj.getClass().getMethod("set" + initStr(attr),  
                    type);  
            method.invoke(obj, value);// 调用方法  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }
    
    private static String initStr(String old) {// 单词首字母大写  
        String str = old.substring(0, 1).toUpperCase() + old.substring(1);  
        return str;  
    }

    public static class EscapeUtil {
        // mysql的模糊查询时特殊字符转义
        public static String escapeChar(String before) {
            if (StringUtils.isNotBlank(before)) {
                before = before.replaceAll("\\\\", "\\\\\\\\");
                before = before.replaceAll("_", "\\\\_");
                before = before.replaceAll("%", "\\\\%");
            }
            return before;
        }
    }
}