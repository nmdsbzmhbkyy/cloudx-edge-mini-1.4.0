package com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.model;

import lombok.Data;

/***
 * @title RemoteValidDataModel
 * @description
 * @author cyw
 * @version 1.0.0
 * @create 2023/6/12 18:57
 **/
@Data
public class RemoteValidDataModel {
    //开门方式
    //{
    //	PASS_TYPE_CARD			=1，			//卡号
    //	PASS_TYPE_PASSWD			=2，			//密码
    //	PASS_TYPE_FACE			=3，			//人脸
    //	PASS_TYPE_FINGER			=4，			//指纹
    //	OPEN_MODE_QRCODE		=5，			//二维码
    //} PASS_TYPE_E;
    private Integer mode;
    //mfrs
    //{
    //	MANUFACTURER_TYPE_MILI				=0，			//米立
    //	MANUFACTURER_TYPE_POLY			=10，			//保利
    //MANUFACTURER_TYPE_TIANHENG		=11，			//天恒地产
    //} MANUFACTURER_TYPE_E;
    private Integer mfrs;
    //检验数据
    private String data;
    //预留：data编码方式：base64
    private String encrypt;
}
