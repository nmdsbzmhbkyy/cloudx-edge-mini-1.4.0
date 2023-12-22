package com.aurine.cloudx.wjy.pojo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class RDataRowsPager<T> implements Serializable {
    //"current":1,"total":221,"pages":3,"rowCount":100,"rows":[]
    private int current;
    private int total;
    private int pages;
    private int rowCount;
    //private T rows;
    private List<T> rows;
}
