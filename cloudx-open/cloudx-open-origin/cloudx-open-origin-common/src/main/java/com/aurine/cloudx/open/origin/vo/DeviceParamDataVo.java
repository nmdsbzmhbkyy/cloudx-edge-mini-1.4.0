package com.aurine.cloudx.open.origin.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * <p>
 * 存放设备的参数数据，包括了设备拥有的参数类型（用于前端回显时进行判断），以及具体的参数数据
 * </p>
 *
 * @ClassName: DeviceParamDataVo
 * @author: 王良俊 <>
 * @date: 2020年12月17日 上午11:31:21
 * @Copyright:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceParamDataVo {
    /**
     * 这里存放的是生成好对格式的json数据
     * 对应的json结构应该如下 ↓
     * <br> {
     * <br>
     * <br>    "netparam": {
     * <br>        "faceServer": "0.0.0.0",
     * <br>        "elevator": "0.0.0.0",
     * <br>        "netmask": "255.255.255.0",
     * <br>        "managerIp": "0.0.0.0",
     * <br>        "dns": "58.22.96.66",
     * <br>        "rtspServer": "0.0.0.0",
     * <br>        "centerIp": "0.0.0.0",
     * <br>        "ipAddr": "192.168.1.251",
     * <br>        "gateway": "192.168.1.1"
     * <br>    },
     * <br>    "faceParam": {
     * <br>        "securityLevel": 1,
     * <br>        "livenessEnable": 0,
     * <br>        "faceEnable": 1,
     * <br>        "inductionEnable": 0
     * <br>    },
     * <br>    "volumeParam": {
     * <br>        "keyTone": 1,
     * <br>        "talkVolume": 5,
     * <br>        "tipTone": 0,
     * <br>        "mediaMute": 1
     * <br>    }
     * <br>}
     */
    String paramData;

    /**
     * 这里存放的不是具体参数名而是网络参数这种“netparam”参数类别名
     */
    List<String> paramNameList;

    /**
     * 这里存放的是已有的serviceId集合 这里serviceId和上面的参数名对应 ”netparam“ 对应 ”NetworkObj“
     */
    List<String> serviceIdList;
}
