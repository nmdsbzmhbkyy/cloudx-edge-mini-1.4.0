package com.aurine.cloudx.wjy.service.impl;

import cn.hutool.core.lang.TypeReference;
import com.aurine.cloudx.wjy.entity.Project;
import com.aurine.cloudx.wjy.constant.ApiPathEnum;
import com.aurine.cloudx.wjy.nal.CommonNao;
import com.aurine.cloudx.wjy.pojo.AccessToken;
import com.aurine.cloudx.wjy.pojo.Ticket;
import com.aurine.cloudx.wjy.constant.Constant;
import com.aurine.cloudx.wjy.pojo.Prams;
import com.aurine.cloudx.wjy.pojo.R;
import com.aurine.cloudx.wjy.service.WjyTokenService;
import com.aurine.cloudx.wjy.util.CodecUtils;
import com.aurine.cloudx.wjy.util.SHA1;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ： huangjj
 * @date ： 2021/4/22
 * @description： 项目获取接口调用凭证实现类
 */
@Service
public class WjyTokenServiceImpl implements WjyTokenService {


    @Override
    public Ticket getTicket(Project project, Integer type){

        Prams p = null;
        p = Constant.getConfig(project, type);
        String url = p.getBaseUrl() + ApiPathEnum.GetTicket.getPath();
        System.out.println("url:"+url);
        Long time = System.currentTimeMillis();

        String sign = makeSign(p.getAppKey(), p.getAppSecret(), time, Constant.ver);

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("appKey", p.getAppKey());
        params.put("clientTime", time);
        params.put("version", Constant.ver);
        params.put("signature", sign);
        R<Ticket> ticket = CommonNao.doGet(url, null, params, new TypeReference<R<Ticket>>(){});
        if(ticket == null){
            return null;
        }
        return ticket.getData();
    }
    @Override
    public AccessToken getToken(Project project, Integer type) {

        return getToken(project,type,project.getPhone());
    }

    @Override
    public AccessToken getToken(Project project, Integer type, String phone) {

        AccessToken token = Constant.getToken(project.getPid(),type);
        if(token == null){
            token = refreshToken(project,type,phone);
        }
        long  expires_time = token.getExpires_time()/2*1000;
        if(Calendar.getInstance().getTimeInMillis()-token.getTime()>expires_time){
            token = refreshToken(project,type,phone);
        }

        return token;
    }

    @Override
    public AccessToken refreshToken(Project project, Integer type, String phone) {

        Ticket ticket = getTicket(project,type);
        if(ticket == null){
            return null;
        }
        Prams p = null;
        p = Constant.getConfig(project, type);
        String url = p.getBaseUrl() + ApiPathEnum.GetAccessToken.getPath();
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ticket", ticket.getTicket());
        if(StringUtils.isBlank(phone)){
            params.put("username", project.getPhone());
        }else {
            params.put("username", phone);
        }

        params.put("pid", project.getPid());
        params.put("type", type);

        R<AccessToken> tokenResp = CommonNao.doPost(url, null, params, new TypeReference<R<AccessToken>>() {});
        if(tokenResp == null || tokenResp.getData() == null){
            return null;
        }
        tokenResp.getData().setTime(Calendar.getInstance().getTimeInMillis());
        Constant.setToken(project.getPid(), tokenResp.getData(), type);
        return tokenResp.getData();
    }

    @SneakyThrows
    public static String makeSign(String key, String secret, Long time, String ver){
        //Long time = System.currentTimeMillis();
        String pathString = "appKey=" + key + "&clientTime=" + time+ "&version=" + ver + "&" + secret;
        String md5Val = CodecUtils.MD5(pathString);
        String digest = SHA1.genWithAmple(md5Val);
        return digest;
    }
}