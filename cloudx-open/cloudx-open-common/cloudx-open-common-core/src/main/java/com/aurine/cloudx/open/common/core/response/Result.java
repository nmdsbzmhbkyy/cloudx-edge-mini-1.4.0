package com.aurine.cloudx.open.common.core.response;

import com.aurine.cloudx.open.common.core.exception.CloudxOpenErrorEnum;
import com.pig4cloud.pigx.common.core.util.R;

public class Result<T> extends R<T> {

    public static <T> R<T> failed(T data, int code, String msg) {
        return restResult(data, code, msg);
    }

    public static <T> R<T> fail() {
        return failed();
    }

    public static <T> R<T> fail(T data, CloudxOpenErrorEnum cloudxOpenErrorEnum) {
        return restResult(data, cloudxOpenErrorEnum.getCode(), cloudxOpenErrorEnum.getMsg());
    }

    public static <T> R<T> fail(T data, int code, String msg) {
        return restResult(data, code, msg);
    }

    private static <T> R<T> restResult(T data, int code, String msg) {
        R apiResult = new R();
        apiResult.setCode(code);
        apiResult.setData(data);
        apiResult.setMsg(msg);
        return apiResult;
    }
}
