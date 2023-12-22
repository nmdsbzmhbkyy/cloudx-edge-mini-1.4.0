package com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.param.entity.base;

import com.aurine.cloudx.estate.util.ObjectMapperUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * <p>
 * 基类
 * </p>
 * @author : 王良俊
 * @date : 2021-08-10 14:58:19
 */
@Data
public class BaseIotControl {

    @JsonIgnore
    protected ObjectMapper objectMapper = ObjectMapperUtil.instance();

    /**
     * 设备ID
     * */
    protected String deviceId;

    /**
     * 批量操作的设备ID
     * */
    protected List<String> deviceIdList;
}
