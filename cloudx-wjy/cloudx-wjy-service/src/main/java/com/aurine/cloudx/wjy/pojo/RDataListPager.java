package com.aurine.cloudx.wjy.pojo;

import lombok.Data;

import java.io.Serializable;

@Data
public class RDataListPager<T> implements Serializable {
    private int pageCount;
    private int pages;
    private int pageSize;
    private int pageNum;
    private T list;
}
