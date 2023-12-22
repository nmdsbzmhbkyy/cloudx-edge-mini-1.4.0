package com.aurine.cloudx.open.origin.vo;

import com.aurine.cloudx.open.origin.entity.SysServiceParamConf;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 平台设备参数定义表(SysDeviceParamConf)表实体类
 *
 * @author 王良俊
 * @since 2020-12-15 10:24:38
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysServiceParamConfVo extends SysServiceParamConf {

    /**
     * 现在这里额外存储了根服务ID(因为参数数据结构是如此的)
     * 具体格式如下（这种格式原本是想给jackson的RequiredAt方法使用但是现在已经没必要了）：
     * DeviceNoObj/devNoRule/stairNoLen
     * */
    String props;

    /**
     * 根参数节点serviceId（如：DeviceNoObj）
     * */
    String rootServiceId;

    /**
     * 父参数节点的参数ID（这里是各个参数的ID如：devNoRule/stairNoLen）
     * */
    String parParamIds;

}