package com.aurine.cloudx.wjy.pojo;

import lombok.Data;

import java.io.Serializable;

@Data
public class Ticket implements Serializable {
    private String ticket;
    private int expires;
}
