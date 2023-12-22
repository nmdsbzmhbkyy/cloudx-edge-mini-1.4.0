package com.aurine.cloudx.wjy.service;


import com.aurine.cloudx.wjy.entity.Project;

/**
 * @author ： huangjj
 * @date ： 2021/5/8
 * @description： 我家云H5对接
 */
public interface WjyH5Service {
    String getWyUrl(Project project, String moduleType);

    String getWjUrl(Project project, String moduleType, String phone);

    String getgjUrl(Project project, String moduleType, String phone);
}
