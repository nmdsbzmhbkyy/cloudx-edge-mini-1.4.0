package com.aurine.cloudx.wjy.service.impl;

import com.aurine.cloudx.wjy.config.WjyConfig;
import com.aurine.cloudx.wjy.entity.Project;
import com.aurine.cloudx.wjy.constant.Constant;
import com.aurine.cloudx.wjy.pojo.AccessToken;
import com.aurine.cloudx.wjy.service.WjyH5Service;
import com.aurine.cloudx.wjy.service.WjyTokenService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author ： huangjj
 * @date ： 2021/5/8
 * @description： 我家云H5接口实现类
 */
@Service
public class WjyH5ServiceImpl implements WjyH5Service {
    @Resource
    WjyTokenService wjyTokenService;
    @Resource
    WjyConfig wjyConfig;
    @Override
    public String getWyUrl(Project project, String moduleType) {

        AccessToken accessToken = wjyTokenService.getToken(project,Constant.wyWebType);
        String token = "";
        if(accessToken != null){
            token = accessToken.getAccess_token();
        }

        String path = wjyConfig.getWyWebUrl() + "?access_token=" + token
                + "&moduleType=" + moduleType + "&appId=" + project.getGjAppid();

        return path;
    }

    @Override
    public String getWjUrl(Project project, String moduleType, String phone) {
        AccessToken accessToken = wjyTokenService.getToken(project,Constant.wjWebType,phone);
        String token = "";
        if(accessToken != null){
            token = accessToken.getAccess_token();
        }
        String path = wjyConfig.getWjWebUrl() + "?access_token=" + token
                + "&menu=" + moduleType + "&appId=" + project.getWjAppid() + "#" + moduleType;

        return path;
    }

    @Override
    public String getgjUrl(Project project, String moduleType, String phone) {
        AccessToken accessToken = wjyTokenService.getToken(project,Constant.gjWebType,phone);
        String token = "";
        if(accessToken != null){
            token = accessToken.getAccess_token();
        }
        String path = wjyConfig.getGjWebUrl() + "?access_token=" + token
                + "#" + moduleType ;
        return path;
    }
}