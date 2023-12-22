package com.aurine.cloudx.open.origin.entity;

import com.alibaba.fastjson.JSON;
import com.aurine.cloudx.open.origin.constant.enums.EdgeCascadeEventCodeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : 王良俊
 * @date : 2021-12-17 09:25:12
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EdgeCascadeResponse {

    /**
     * 服务ID：比如级联申请服务
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
        EdgeCascadeEventCodeEnum enumByCode = EdgeCascadeEventCodeEnum.getEnumByCode(code);
        if (enumByCode != null) {
            this.msg = enumByCode.desc;
            this.resultCode = enumByCode.code;
        } else {
            this.msg = "其他错误";
            this.resultCode = "";
        }
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
