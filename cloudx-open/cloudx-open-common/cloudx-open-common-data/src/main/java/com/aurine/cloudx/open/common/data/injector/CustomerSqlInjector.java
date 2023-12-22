package com.aurine.cloudx.open.common.data.injector;

import com.aurine.cloudx.open.common.data.method.InsertIgnore;
import com.aurine.cloudx.open.common.data.method.InsertIgnoreBatch;
import com.aurine.cloudx.open.common.data.method.Replace;
import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;

import java.util.List;

/**
 * 自定义sql注入器，增加通用方法
 *
 * @author : Qiu
 * @date : 2022 04 21 15:21
 */
public class CustomerSqlInjector extends DefaultSqlInjector {

    @Override
    public List<AbstractMethod> getMethodList(Class<?> mapperClass) {
        List<AbstractMethod> methodList = super.getMethodList(mapperClass);
        // 插入数据，如果中已经存在相同的记录，则忽略当前新数据
        methodList.add(new InsertIgnore());
        // 批量插入数据，如果中已经存在相同的记录，则忽略当前新数据
        methodList.add(new InsertIgnoreBatch());
        // 替换数据，如果中已经存在相同的记录，则覆盖旧数据
        methodList.add(new Replace());
        return methodList;
    }
}
