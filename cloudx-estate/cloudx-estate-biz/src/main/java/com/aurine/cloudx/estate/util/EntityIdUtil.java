package com.aurine.cloudx.estate.util;

import com.aurine.cloudx.estate.constant.enums.TableNameEnum;
import com.aurine.cloudx.estate.entity.ProjectHouseDesign;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Author: wrm
 * @Date: 2021/12/29 19:11
 * @Package: com.aurine.cloudx.open.intocloud.biz.util
 * @Version: 1.0
 * @Remarks:
 **/
public class EntityIdUtil {
    public static String getId(Object o) {
        String name = null;
        Class<?> aClass = o.getClass();
        Class<?> bClass = o.getClass();
        Field[] fields = aClass.getFields();

        for (int i = 0; i < fields.length; i++) {
            fields[i].setAccessible(true);
            System.out.println(fields[i].getName());
        }
        List<Field> fieldList = new ArrayList<>();
        while (bClass != null) {
            fieldList.addAll(Arrays.asList(bClass.getDeclaredFields()));
            bClass = bClass.getSuperclass();
        }
        for (int i = 0; i < fieldList.size(); i++) {
            TableId annotation = fieldList.get(i).getAnnotation(TableId.class);
            if (annotation != null) {
                name = fieldList.get(i).getName();
                fieldList.get(i).setAccessible(true);
                String o1 = null;
                try {
                    o1 = fieldList.get(i).get(o).toString();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                return o1;
            }
        }
        return null;
    }
    public static String getTableName(Object o) {
        TableName tableName = o.getClass().getAnnotation(TableName.class);
        return tableName.value();
    }
    public static String getTableName(String sql){
        for (TableNameEnum value : TableNameEnum.values()) {
            if(sql.contains(value.code)){
                return value.code;
            }
        }
        return null;
    }
}
