package com.aurine.cloudx.dashboard.vo;

import com.aurine.cloudx.common.core.exception.ErrorType;
import com.pig4cloud.pigx.common.core.constant.CommonConstants;
import com.pig4cloud.pigx.common.core.util.R;
import lombok.Data;
import org.springframework.beans.BeanUtils;

/**
 * @description:
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2021-03-26
 * @Copyright:
 */
@Data
public class Result<T> extends R {

    private String projectId;
    private String serviceName;
    private String version;


    public static <T> Result<T> ok(T data, String projectId, String serviceName, String version) {
        return restResult(data, CommonConstants.SUCCESS, (String) null, projectId, serviceName, version);
    }


    public static <T> Result<T> failed(String msg, DashboardRequestVo vo) {
        return toResult(R.failed(msg), vo.getProjectId(), vo.getServiceName(), vo.getVersion());
    }

    public static <T> Result<T> failed(ErrorType errorType, DashboardRequestVo vo) {
        R r = R.failed();
        r.setCode(errorType.getCode());
        r.setMsg(errorType.getMsg());
        return toResult(r, vo.getProjectId(), vo.getServiceName(), vo.getVersion());
    }

    private static <T> Result<T> restResult(T data, int code, String msg, String projectId, String serviceName, String version) {
        Result<T> apiResult = new Result();
        apiResult.setCode(code);
        apiResult.setData(data);
        apiResult.setMsg(msg);
        apiResult.setProjectId(projectId);
        apiResult.setServiceName(serviceName);
        apiResult.setVersion(version);
        return apiResult;
    }

    private static <T> Result<T> toResult(R<T> r, String projectId, String serviceName, String version) {
        Result result = new Result();
        BeanUtils.copyProperties(r, result);

        result.setVersion(version);
        result.setServiceName(serviceName);
        result.setProjectId(projectId);
        return result;
    }
}
