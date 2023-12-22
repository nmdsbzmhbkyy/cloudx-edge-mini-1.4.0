package com.aurine.cloudx.estate.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 项目设备产品名Vo
 *
 * @author 邹宇
 * @date 2021-7-23 10:11:17
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectDeviceProductNameVo {

    /**
     * 第三方类型名称
     */
    private String productName;


    /**
     * 产品型号
     */
    private String productModel;

    /**
     * 控制器IMEL
     */
    private String attrValue;

    /**
     * 产品id
     */
    private String productId;

}
