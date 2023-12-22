package com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.exception.entity;

import com.aurine.cloudx.estate.constant.enums.device.DeviceRegParamEnum;
import com.aurine.cloudx.estate.entity.ProjectDeviceAbnormal;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * <p>设备异常信息对象</p>
 *
 * @author : 王良俊
 * @date : 2021-09-24 17:36:30
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceAbnormalHandleInfo extends ProjectDeviceAbnormal {

    /**
     * 错误代码
     */
    private String failCode;

    /**
     * 第三方设备ID
     */
    private String thirdpartyCode;

    /**
     * 项目id
     */
    private Integer projectId;


    /**
     * 用来存放各种参数
     */
    private Map<DeviceRegParamEnum, Object> paramContainer = new HashMap<>();

    /**
     * <p>存放设备参数到容器中</p>
     *
     * @param paramEnum 参数键值枚举类
     * @param o         具体参数数据
     * @return 当前信息对象
     */
    public DeviceAbnormalHandleInfo addParam(DeviceRegParamEnum paramEnum, Object o) {
        if (o != null) {
            paramContainer.put(paramEnum, o);
        }
        return this;
    }

    /**
     * <p>存放设备参数到容器中</p>
     *
     * @param paramEnum 参数键值枚举类
     * @return 参数数据（可能为null）
     */
    public Object getParam(DeviceRegParamEnum paramEnum) {
        return paramContainer.get(paramEnum);
    }

    /**
     * <p>获取所有参数key</p>
     *
     * @return 参数key的set集合
     */
    public Set<DeviceRegParamEnum> getParamKeySet() {
        return paramContainer.keySet();
    }

}
