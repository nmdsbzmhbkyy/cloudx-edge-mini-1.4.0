package com.aurine.cloudx.wjy.pojo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class RDataList<T> implements Serializable {
    private int current;
    private int rowCount;
    private int totalCount;
    private List<T> list;
}
