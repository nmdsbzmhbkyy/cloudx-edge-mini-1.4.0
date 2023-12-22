package com.aurine.cloudx.estate.excel.invoke.valid;

import com.aurine.cloudx.estate.excel.invoke.entity.RowInvokeResult;

/**
 * @Author cyw
 * @description <description class purpose>
 * @Date 2023/04/25 17:21
 **/
public class DoorControllerCustomizeValid extends ExcelCustomizeValid{
    @Override
    public RowInvokeResult valid(Object value) {
        return RowInvokeResult.success();
    }
}
