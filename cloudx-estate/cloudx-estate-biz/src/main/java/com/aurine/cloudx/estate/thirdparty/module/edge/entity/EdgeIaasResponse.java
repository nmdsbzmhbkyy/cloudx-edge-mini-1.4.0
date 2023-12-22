package com.aurine.cloudx.estate.thirdparty.module.edge.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EdgeIaasResponse {

    /*
     * 0 成功 1 失败
     **/
    private Integer errorCode;

    /*
     * 操作结果 操作成功、操作失败...
     **/
    private String errorMsg;

    /*
     * 中台返回的数据 可能为null
     **/
    private String body;


}
