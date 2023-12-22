package com.aurine.cloudx.open.origin.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * <p>
 *  多台设备的参数设置结果
 * </p>
 *
 * @author 王良俊
 * @since 2022/5/23 9:47
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceParamBatchConfResultVo {

    /*
     * 参数配置结果
     **/
    List<DeviceParamConfResultVo> resultInfoList;
}
