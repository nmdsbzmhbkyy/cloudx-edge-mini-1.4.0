package com.aurine.cloudx.estate.excel.invoke;

import cn.hutool.core.util.StrUtil;
import com.aurine.cloudx.estate.excel.invoke.valid.ExcelCustomizeValid;
import com.aurine.cloudx.estate.excel.invoke.valid.constants.ExcelRegexConstants;
import com.aurine.cloudx.estate.excel.invoke.entity.RowInvokeResult;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>设备端口号校验类</p>
 * @author : 王良俊
 * @date : 2021-09-18 09:46:41
 */
public class DevicePortCustomizeValid extends ExcelCustomizeValid<String> {

    @Override
    public RowInvokeResult valid(String value) {

        // 已经先进行了正整数判断
        if (StrUtil.isNotEmpty(value)) {
            Matcher matcher = Pattern.compile(ExcelRegexConstants.NON_ZERO_POSITIVE_INTEGER).matcher(value);
            if (!matcher.matches()) {
                return RowInvokeResult.failed("端口号只能是非零正整数，且不能为“01”这种格式");
            }
            if (value.length() > 5 || Integer.parseInt(value) > 65535) {
                return RowInvokeResult.failed("端口号最大不能超过65535");
            }
        }
        return RowInvokeResult.success();
    }
}
