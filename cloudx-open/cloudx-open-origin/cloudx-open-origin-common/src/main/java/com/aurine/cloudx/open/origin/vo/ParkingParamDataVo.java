package com.aurine.cloudx.open.origin.vo;

import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 参数数据对象
 * </p>
 *
 * @author 王良俊
 * @since 2022/5/25 9:43
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParkingParamDataVo {

    /*
     * 需要配置参数的设备ID
     **/
    private String serviceId;

    /*
     * 参数json
     **/
    private JSONObject param;
}
