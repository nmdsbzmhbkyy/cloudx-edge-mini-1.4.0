package com.aurine.cloudx.wjy.pojo;

import lombok.Data;

import java.io.Serializable;

@Data
public class AccessToken implements Serializable {
    private String access_token;
    private int expires_time;
    private Long time;
}
