package com.aurine.cloudx.estate.excel.invoke.valid;

import com.aurine.cloudx.estate.excel.invoke.entity.RowInvokeResult;

/**
 * <p>Excel字段校验类</p>
 * @author : 王良俊
 * @date : 2021-09-18 09:16:31
 */
public abstract class ExcelCustomizeValid<T> {

    /**
     * <p>对导入的数据进行校验</p>
     *
     * @param value 要校验的数据
     * @return 校验结果对象
     */
    public abstract RowInvokeResult valid(T value);

    public abstract static class None extends ExcelCustomizeValid {
        public None() {}
    }
}
