package com.aurine.cloudx.open.origin.dto;

import com.aurine.cloudx.open.origin.constant.enums.IntoCloudEventCodeEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class IntoCloudResponse {

    public IntoCloudResponse(String serviceId, IntoCloudEventCodeEnum resultCode, String data) {
        this.serviceId = serviceId;
        this.setResultCode(resultCode.code);
        this.data = data;
    }

    public IntoCloudResponse(String serviceId, IntoCloudEventCodeEnum resultCode) {
        this.serviceId = serviceId;
        this.setResultCode(resultCode.code);
    }

    /**
     * 服务ID：比如入云申请服务
     */
    private String serviceId;

    /**
     * 对应的结果
     */
    private String resultCode;

    /**
     * 消息
     */
    private String msg;

    /**
     * 存放各种数据
     */
    private String data;

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String code) {
        IntoCloudEventCodeEnum enumByCode = IntoCloudEventCodeEnum.getEnumByCode(code);
        if (enumByCode != null) {
            this.msg = enumByCode.desc;
            this.resultCode = enumByCode.code;
        } else {
            this.msg = "其他错误";
            this.resultCode = "";
        }
    }
}
