package com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

/***
 * @title RemoteOpenDoorModel
 * @description
 * @author cyw
 * @version 1.0.0
 * @create 2023/6/12 15:55
 **/
@Data
@NoArgsConstructor
public class RemoteOpenDoorResultModel {
    //校验结果
    //typedef enum
    //{
    //	PASS_RESULT_TYPE_VALID				=1，			//有效
    //	PASS_RESULT_TYPE_INVALID			=2，			//无效
    //	PASS_RESULT_TYPE_EXPIRE				=3，			//无效
    //} PASS_RESULT_E;
    private Integer result;
    //上报事件标志
    //设备是否上报开门事件
    //0：不上报事件
    //1：上报事件
    private final Integer report = 1;
    //检验数据
    //typedef enum {
    //VOUCHER_USER						=0x70,			// 住户
    //VOUCHER_VISITOR					=0x71,			// 访客
    //}VOUCHER_TYPE_E;
    private final Integer attri = 70;
    //住户房号
    private String roomNo;
    //姓名
    private String userName;

    @AllArgsConstructor
    @Getter
    public static enum ResultType {
        PASS_RESULT_TYPE_VALID(1),
        PASS_RESULT_TYPE_INVALID(2),
        PASS_RESULT_TYPE_EXPIRE(3),
        ;
        private Integer code;

    }
}
