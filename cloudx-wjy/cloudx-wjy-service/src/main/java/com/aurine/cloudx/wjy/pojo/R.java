package com.aurine.cloudx.wjy.pojo;

import lombok.Data;

import java.io.Serializable;

@Data
public class R<T> implements Serializable {
    private boolean success;
    private int code;
    private String msg;
    private String result;
    private T data;
}
