package com.aurine.cloudx.estate.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 *  设备拓展属性
 * </p>
 *
 * @author 王良俊
 * @since 2022/6/1 16:01
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceExtendAttr {

    /*
     * 拓展属性名 对应平台的 attrCode 根据这个查询出拓展属性ID
     **/
    private String attrName;

    /*
     * 拓展属性值 对应平台的 attrValue
     **/
    private String attrValue;
}
