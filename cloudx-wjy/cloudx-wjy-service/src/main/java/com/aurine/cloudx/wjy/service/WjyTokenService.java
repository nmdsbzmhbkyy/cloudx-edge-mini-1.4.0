package com.aurine.cloudx.wjy.service;

import com.aurine.cloudx.wjy.entity.Project;
import com.aurine.cloudx.wjy.pojo.AccessToken;
import com.aurine.cloudx.wjy.pojo.Ticket;

/**
 * @author ： huangjj
 * @date ： 2021/4/22
 * @description： 项目获取接口调用凭证
 */
public interface WjyTokenService {
    Ticket getTicket(Project project, Integer type);
    AccessToken getToken(Project project, Integer type);
    AccessToken getToken(Project project, Integer type, String phone);
    AccessToken refreshToken(Project project, Integer type, String phone);
}
