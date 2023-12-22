package com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.vo.param;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 二维码参数对象 json名：qrcodeParam
 * </p>
 *
 * @ClassName: QrcodeObj
 * @author: 王良俊 <>
 * @date: 2020年11月25日 下午04:16:38
 * @Copyright:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonRootName("qrcodeParam")
public class QrcodeObj {

    /**
     * 二维码类型
     * 0：扫码开门 1：蓝牙开门器
     */
    @NotNull
    Integer qrcodeType;

    /**
     * 扫码开门启用状态
     * 0：不启用 1：启用
     */
    Integer QrOpenEnable;

    /**
     * 设备注册ID
     * 蓝牙开门器的注册id
     */
    String deviceRegId;

}
