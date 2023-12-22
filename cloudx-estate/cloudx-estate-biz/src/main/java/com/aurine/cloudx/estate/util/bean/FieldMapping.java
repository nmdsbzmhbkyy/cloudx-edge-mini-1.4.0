package com.aurine.cloudx.estate.util.bean;


import com.aurine.cloudx.estate.util.bean.convert.ConvertHandler;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.property.PropertyNamer;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.*;
import java.util.function.Consumer;

/**
 * <p>Bean字段映射类</p>
 *
 * @author : 王良俊
 * @date : 2021-11-29 11:25:38
 */
@Slf4j
@NoArgsConstructor
public class FieldMapping<T, S> {


    private static final Map<String, Class<?>> classMap = new HashMap<>();
    private static final Map<String, WeakReference<Class>> otherClassMap = new HashMap<>();

    static {
        classMap.put("()[I", Integer[].class);
        classMap.put("()[C", Character[].class);
        classMap.put("()[Z", Boolean[].class);
        classMap.put("()[F", Float[].class);
        classMap.put("()[D", Double[].class);

        classMap.put("()I", Integer.class);
        classMap.put("()C", Character.class);
        classMap.put("()Z", Boolean.class);
        classMap.put("()F", Float.class);
        classMap.put("()D", Double.class);
    }
    /**
     * 字段映射集合
     */
    private final List<FieldMappingItem> fieldMappingItemList = new ArrayList<>();

    public FieldMapping<T, S> add(SFunction<T, ?> targetField, SFunction<S, ?> sourceField, Object defaultValue) {
        FieldMappingItem fieldMappingItem = new FieldMappingItem(targetField, sourceField, defaultValue);
        if (fieldMappingItem.isCorrect) {
            fieldMappingItemList.add(fieldMappingItem);
        }
        return this;
    }

    public FieldMapping<T, S> add(SFunction<T, ?> targetField, SFunction<S, ?> sourceField) {
        FieldMappingItem fieldMappingItem = new FieldMappingItem(targetField, sourceField);
        if (fieldMappingItem.isCorrect) {
            fieldMappingItemList.add(fieldMappingItem);
        }
        return this;
    }

    public FieldMapping<T, S> add(SFunction<T, ?> targetField, SFunction<S, ?> sourceField, Object defaultValue, ConvertHandler<?, ?> handler) {
        FieldMappingItem fieldMappingItem = new FieldMappingItem(targetField, sourceField, defaultValue, handler);
        if (fieldMappingItem.isCorrect) {
            fieldMappingItemList.add(fieldMappingItem);
        }
        return this;
    }

    public FieldMapping<T, S> add(SFunction<T, ?> targetField, SFunction<S, ?> sourceField, ConvertHandler<?, ?> handler) {
        FieldMappingItem fieldMappingItem = new FieldMappingItem(targetField, sourceField, handler);
        if (fieldMappingItem.isCorrect) {
            fieldMappingItemList.add(fieldMappingItem);
        }
        return this;
    }

    public void foreach(Consumer<FieldMappingItem> item) {
        for (FieldMappingItem fieldMappingItem : fieldMappingItemList) {
            item.accept(fieldMappingItem);
        }
    }

    @Data
    public class FieldMappingItem {

        /**
         * 数据复制源字段名
         */
        public String sourceField;

        /**
         * 数据复制目标字段名
         */
        public String targetField;

        /**
         * 源字段类
         */
        public Class sClass;

        /**
         * 目标字段类
         */
        public Class tClass;

        public boolean isCorrect = true;

        /**
         * 默认值
         */
        public Object defaultValue;

        /**
         * 默认值
         */
        public ConvertHandler handler = BeanPropertyUtil.DEFAULT_HANDLER;

        Throwable throwable = new Throwable();

        public FieldMappingItem(SFunction<?, ?> targetField, SFunction<?, ?> sourceField, ConvertHandler<?, ?> convertHandler) {
            this.sourceField = getFieldName(sourceField, aClass -> this.sClass = aClass);
            this.targetField = getFieldName(targetField, aClass -> this.tClass = aClass);
            this.handler = convertHandler;
        }

        public FieldMappingItem(SFunction<?, ?> targetField, SFunction<?, ?> sourceField) {
            this.sourceField = getFieldName(sourceField, aClass -> this.sClass = aClass);
            this.targetField = getFieldName(targetField, aClass -> this.tClass = aClass);
            if (sClass != null && !canCopy(sClass, tClass)) {
                logError("[属性复制] 目标属性类型（{}）和源属性类型（{}）不一致，且无继承关系无法复制", tClass, sClass);
                isCorrect = false;
            }
        }

        public FieldMappingItem(SFunction<?, ?> targetField, SFunction<?, ?> sourceField, Object defaultValue) {
            this(targetField, sourceField);
            if (defaultValue != null && !canCopy(defaultValue.getClass(), tClass)) {
                logError("[属性复制] 默认值的类型和目标属性的类型不一致，且无继承关系无法复制");
                isCorrect = false;
            }
            this.defaultValue = defaultValue;
        }

        public FieldMappingItem(SFunction<?, ?> targetField, SFunction<?, ?> sourceField, Object defaultValue, ConvertHandler<?, ?> handler) {
            this(targetField, sourceField, handler);
            if (defaultValue != null && !canCopy(defaultValue.getClass(), tClass)) {
                logError("[属性复制] 默认值的类型和目标属性的类型不一致，且无继承关系无法复制");
                isCorrect = false;
            }
            this.defaultValue = defaultValue;
        }

        private String getFieldName(SFunction<?, ?> SFunction, Consumer<Class<?>> clazz) {
            SerializedLambda resolve = SerializedLambda.resolve(SFunction);

            String implMethodSignature = resolve.getImplMethodSignature();
            Class<?> currClass = classMap.get(implMethodSignature);
            if (currClass == null) {
                currClass = Optional.ofNullable(otherClassMap.get(implMethodSignature)).map(WeakReference::get).orElseGet(() -> {
                    String name;
                    if ("()[L".equals(implMethodSignature.substring(0, 4))) {
                        //()[Lxx.xx.xx;
                        name = implMethodSignature.replace("()[L", "[L").replace("/", ".");
                    } else {
                        //xx.xx.xx
                        name = implMethodSignature.replace("()L", "").replace("/", ".").replace(";", "");
                    }
                    try {
                        Class<?> tmpClazz = Class.forName(name);
                        otherClassMap.put(implMethodSignature, new WeakReference<>(tmpClazz));
                        return tmpClazz;
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                        return Object.class;
                    }
                });
            }
            clazz.accept(currClass);
            return PropertyNamer.methodToProperty(resolve.getImplMethodName());
        }


        public boolean canCopy(Class<?> child, Class<?> father) {
            return father.isAssignableFrom(child);
        }

        public Object convert(Object value) {
            return handler.convert(value);
        }

        private void logError(String errorReason, Object... args) {
            log.error(errorReason, args);
            throwable.printStackTrace(System.out);
        }
    }


}
