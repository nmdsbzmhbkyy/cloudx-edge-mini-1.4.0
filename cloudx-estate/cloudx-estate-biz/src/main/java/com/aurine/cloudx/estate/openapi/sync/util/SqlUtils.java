package com.aurine.cloudx.estate.openapi.sync.util;
 
import cn.hutool.core.collection.ListUtil;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;

@Slf4j
public class SqlUtils {
 
    private static final String UNDERLINE = "_";
 
    private static final String APOSTROPHE = "'";
 
    private static final String COMMA = ", ";
 
    private static final String LEFT_BRACKET = "(";
 
    private static final String RIGHT_BRACKET = ")";
 
    private static final String SEMICOLON = ";";

    private static final List<String> paramInfoList = ListUtil.toList("serialVersionUID","seq","projectId","tenant_id","createTime","updateTime");

    /**
     * 批量插入
     *
     * @param list      对象集合
     * @param tableName 表名
     */
    public static String joinBatchInsert(List list, String tableName) {
        StringBuilder br = new StringBuilder(joinSqlColumnName(list.get(0).getClass(), tableName));
        for (int i = 0; i < list.size(); i++) {
            br.append(LEFT_BRACKET);
            Object o = list.get(i);
            Field[] fields = o.getClass().getDeclaredFields();
 
            for (int j = 0; j < fields.length; j++) {
                Field field = fields[j];
                try {
                    if(paramInfoList.contains(field.getName())) continue;
                    field.setAccessible(Boolean.TRUE);
                    Object value = field.get(o);
                    String name = field.getType().getSimpleName();
                    if ("String".equals(name)) {
                        br.append(Objects.isNull(value) ? "NULL" + COMMA : APOSTROPHE + value + APOSTROPHE + COMMA);
                    } else if ("Integer".equals(name) || "BigDecimal".equals(name)) {
                        br.append(value + COMMA);
                    } else if ("LocalDateTime".equals(name)) {
                        br.append(value.toString() + COMMA);
                    } else {
                        log.info("===========缺少类型===========" + name);
                        break;
                    }
                } catch (IllegalAccessException e) {
                    log.info("===========Exception===========" + e);
                    break;
                }
            }
            br.replace(br.length() - 2, br.length() - 2, RIGHT_BRACKET).append("\n");
        }
        StringBuilder stringBuilder = br.replace(br.length() - 3, br.length(), SEMICOLON);
        return stringBuilder.toString();
    }
 
    /**
     * 拼接sql插入字段名
     *
     * @param clazz     实体类类型
     * @param tableName 表名
     * @return
     */
    private static String joinSqlColumnName(Class clazz, String tableName) {
        StringBuilder br = new StringBuilder("INSERT INTO " + tableName + LEFT_BRACKET);
        Field[] fields = clazz.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            if(paramInfoList.contains(fields[i].getName())) continue;
            br.append(fields[i].getName()).append(COMMA);
        }
        return br.substring(0, br.length() - 2) + ") VALUES";
    }
}