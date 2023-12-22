package com.aurine.cloudx.estate.excel.invoke.service;

import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.context.AnalysisContext;
import com.aurine.cloudx.estate.excel.invoke.valid.ExcelCustomizeValid;
import com.aurine.cloudx.estate.excel.invoke.valid.annotation.ExcelValid;
import com.aurine.cloudx.estate.excel.invoke.entity.RowInvokeResult;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * <p>用于设备Excel导入每行解析</p>
 *
 * @author : 王良俊
 * @date : 2021-09-02 10:48:38
 */
@Slf4j
public abstract class BaseExcelRowInvokeService<T> {


    /**
     * <p>解析每行Excel数据</p>
     *
     * @param data         Excel每一行的数据
     * @param context      包含当前行号等信息
     * @param enablePolice 是否开启公安
     * @return 该行处理结果
     */
    public RowInvokeResult invoke(String batchId, T data, AnalysisContext context, boolean enablePolice) {

        try {
            RowInvokeResult result = checkFormatByAnnotation(data, enablePolice);
            if (result.isFailed()) {
                return result;
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return RowInvokeResult.failed("导入失败需要联系管理员解决");
        }
        return this.excelRowInvoke(batchId, data, context, enablePolice);
    }

    /**
     * <p>解析每行Excel数据</p>
     *
     * @param data         Excel每一行的数据
     * @param context      包含当前行号等信息
     * @param enablePolice 是否开启公安
     * @return 该行处理结果
     */
    public RowInvokeResult invoke(String batchId, T data, AnalysisContext context, boolean enablePolice, boolean isCover) {
        try {
            RowInvokeResult result = checkFormatByAnnotation(data, enablePolice);
            if (result.isFailed()) {
                return result;
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return RowInvokeResult.failed("导入失败需要联系管理员解决");
        }
        return this.excelRowInvoke(batchId, data, context, enablePolice, isCover);
    }


    /**
     * <p>解析每行Excel数据</p>
     *
     * @param data         Excel每一行的数据
     * @param context      包含当前行号等信息
     * @param enablePolice 是否开启公安
     * @return 该行处理结果
     */
    public abstract RowInvokeResult excelRowInvoke(String batchId, T data, AnalysisContext context, boolean enablePolice);

    /**
     * <p>解析每行Excel数据</p>
     *
     * @param data         Excel每一行的数据
     * @param context      包含当前行号等信息
     * @param enablePolice 是否开启公安
     * @return 该行处理结果
     */
    public abstract RowInvokeResult excelRowInvoke(String batchId, T data, AnalysisContext context, boolean enablePolice, boolean isCover);

    /**
     * <p>获取适用的设备类型ID列表</p>
     *
     * @return 设备类型ID Set集合
     */
    public abstract Set<String> getDeviceTypeSet();

    /**
     * <p>获取要删除redis缓存的key</p>
     *
     * @param batchId 导入的批次ID（导入日志的ID）
     * @return key的set集合
     */
    public abstract Set<String> getRedisKeys(String batchId);

    private RowInvokeResult checkFormatByAnnotation(T t, boolean enabledPolice) throws IllegalAccessException {
        Field[] declaredFields = t.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            field.setAccessible(true);
            ExcelValid annotation = field.getAnnotation(ExcelValid.class);
            if (annotation == null) {
                continue;
            }
            if (annotation.using() != ExcelCustomizeValid.None.class) {
                try {
                    ExcelCustomizeValid excelCustomizeValid = annotation.using().getConstructor().newInstance();
                    RowInvokeResult valid = excelCustomizeValid.valid(field.get(t));
                    if (valid.isFailed()) {
                        return valid;
                    }
                } catch (InstantiationException | InvocationTargetException | NoSuchMethodException e) {
                    log.error("[Excel导入] 自定义校验类存在问题，字段名{}", annotation.fieldName());
                    e.printStackTrace();
                    return RowInvokeResult.failed("导入失败需要联系管理员解决");
                }
            }
            if (field.getType().equals(String.class)) {
                String value = field.get(t) == null ? null : (String) field.get(t);
                if (StrUtil.isEmpty(value)) {
                    if ((enabledPolice && annotation.requiredPolice()) || annotation.required()) {
                        return RowInvokeResult.failed(annotation.fieldName() + "未填写");
                    }
                } else {
                    if (StrUtil.isNotEmpty(annotation.regex())) {
                        try {
                            Pattern compile = Pattern.compile(annotation.regex());
                            if (!compile.matcher(value).matches()) {
                                return RowInvokeResult.failed(annotation.regexTip());
                            }
                        } catch (Exception e) {
                            log.warn("[Excel导入] {}字段的正则表达式填写错误，这里跳过判断", annotation.fieldName());
                            e.printStackTrace();
                        }
                    }
                    if (annotation.minLength() != 0 && value.length() < annotation.minLength()) {
                        return RowInvokeResult.failed(annotation.fieldName() + "长度未达到" + annotation.minLength() + "个字符");
                    }
                    if (annotation.maxLength() != 0 && value.length() > annotation.maxLength()) {
                        return RowInvokeResult.failed(annotation.fieldName() + "长度过长，不应超过" + annotation.maxLength() + "个字符");
                    }
                }
            } else if (field.getType().equals(Integer.class)) {
                Integer value = field.get(t) == null ? null : (Integer) field.get(t);
                if (value == null) {
                    if ((enabledPolice && annotation.requiredPolice()) || annotation.required()) {
                        return RowInvokeResult.failed(annotation.fieldName() + "未填写");
                    }
                }
               /* else {
                }*/
            }
        }
        return RowInvokeResult.success();
    }

    public abstract void clearCache();
}
