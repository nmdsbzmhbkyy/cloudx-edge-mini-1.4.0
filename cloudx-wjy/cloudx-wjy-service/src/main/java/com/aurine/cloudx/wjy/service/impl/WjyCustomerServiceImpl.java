package com.aurine.cloudx.wjy.service.impl;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.aurine.cloudx.wjy.entity.Project;
import com.aurine.cloudx.wjy.vo.BindCustomerVo;
import com.aurine.cloudx.wjy.vo.CustomerStandardVo;
import com.aurine.cloudx.wjy.constant.ApiPathEnum;
import com.aurine.cloudx.wjy.nal.CommonNao;
import com.aurine.cloudx.wjy.pojo.*;
import com.aurine.cloudx.wjy.constant.Constant;
import com.aurine.cloudx.wjy.service.WjyCustomerService;
import com.aurine.cloudx.wjy.service.WjyTokenService;
import com.aurine.cloudx.wjy.vo.WjyCustomer;
import com.aurine.cloudx.wjy.vo.WjyRoomCustomer;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ： huangjj
 * @date ： 2021/4/26
 * @description： 我家云客户管理接口实现类
 */
@Service
public class WjyCustomerServiceImpl implements WjyCustomerService {
    @Resource
    WjyTokenService wjyTokenService;
    @Override
    public R<RDataRowsPager<WjyRoomCustomer>> roomGetInListCustomer(int current,
                                                                    int rowCount,
                                                                    Project project,
                                                                    String pids,
                                                                    String buildingIds,
                                                                    String roomIds,
                                                                    String roomNames,
                                                                    String searchPhrase,
                                                                    String idCardNos) {
        Prams p = null;
        p = Constant.getConfig(project, Constant.wyWebType);
        String url = p.getBaseUrl() + ApiPathEnum.CustomerQueryByRoom.getPath();
        AccessToken accessToken = wjyTokenService.getToken(project,Constant.wyWebType);
        if(accessToken == null){
            return null;
        }
        String token = accessToken.getAccess_token();

        Map<String, Object> params = new HashMap<String, Object>();

        params.put("access_token", token);
        params.put("current", current);
        params.put("rowCount", rowCount);
        if(StringUtils.isNotBlank(pids)){
            params.put("projectIDs", pids);
        }
        if(StringUtils.isNotBlank(buildingIds)){
            params.put("buildingIDs", buildingIds);
        }
        if(StringUtils.isNotBlank(roomIds)){
            params.put("roomIDs", roomIds);
        }
        if(StringUtils.isNotBlank(roomNames)){
            params.put("roomNames", roomNames);
        }
        if(StringUtils.isNotBlank(searchPhrase)){
            params.put("searchPhrase", searchPhrase);
        }
        if(StringUtils.isNotBlank(idCardNos)){
            params.put("idCardNos", idCardNos);
        }

        R<RDataRowsPager<WjyRoomCustomer>> resp = CommonNao.doGet(url, null, params,
                new TypeReference<R<RDataRowsPager<WjyRoomCustomer>>>() {});
        return resp;
    }

    @Override
    public boolean customerStandardAdd(Project project, List<CustomerStandardVo> customer) {
        Prams p = null;
        p = Constant.getConfig(project, Constant.wyWebType);
        String url = p.getBaseUrl() + ApiPathEnum.CustomerStandardNew.getPath();
        AccessToken accessToken = wjyTokenService.getToken(project,Constant.wyWebType);
        if(accessToken == null){
            return false;
        }
        String token = accessToken.getAccess_token();
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("access_token", token);
        params.put("projectID", project.getPid());

        JSONArray jary = new JSONArray();
        for(CustomerStandardVo item: customer){
            JSONObject jobj = new JSONObject();
            jobj.set("name", item.getName());
            jobj.set("type", item.getType());
            if(StringUtils.isNotBlank(item.getPhone())){
                jobj.set("phone", item.getPhone());
            }
            if(StringUtils.isNotBlank(item.getCertType())
                    && StringUtils.isNotBlank(item.getCertNo())){
                jobj.set("certType", item.getCertType());
                jobj.set("certNo", item.getCertNo());
            }
            if(StringUtils.isNotBlank(item.getSex())){
                jobj.set("sex", item.getSex());
            }
            if(StringUtils.isNotBlank(item.getTelephone())){
                jobj.set("telephone", item.getTelephone());
            }
            if(item.getBirthday() != null){
                jobj.set("birthday", item.getBirthday());
            }

            jary.add(jobj);
        }
        params.put("list", jary.toJSONString(0));

        R<RDataSuccessCount> resp = CommonNao.doPost(url, null, params, new TypeReference<R<RDataSuccessCount>>() {});

        return resp == null ? false : resp.isSuccess();
    }

    @Override
    public R<RDataList<WjyCustomer>> customerGetList(int current, int rowCount, Project project, Date updateBegin, Date updateEnd, String likeName, String areaName) {
        Prams p = null;
        p = Constant.getConfig(project, Constant.wyWebType);
        String url = p.getBaseUrl() + ApiPathEnum.CustomerQueryByNameArea.getPath();
        AccessToken accessToken = wjyTokenService.getToken(project,Constant.wyWebType);
        if(accessToken == null){
            return null;
        }
        String token = accessToken.getAccess_token();
        SimpleDateFormat fmter = new SimpleDateFormat("yyyy-MM-dd");
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("access_token", token);
        params.put("current", current);
        params.put("rowCount", rowCount);

        if(StringUtils.isNotBlank(likeName)){
            params.put("name", likeName);
        }
        if(updateBegin != null && updateEnd != null){
            params.put("updateStartDate", fmter.format(updateBegin));
            params.put("updateEndDate", fmter.format(updateEnd));
        }
        if(StringUtils.isNotBlank(areaName)){
            params.put("areaName", areaName);
        }else{
            params.put("pid", project.getPid());
        }

        R<RDataList<WjyCustomer>> resp = CommonNao.doGet(url, null, params, new TypeReference<R<RDataList<WjyCustomer>>>() {});

        return resp;
    }

    @Override
    public boolean roomBindCustomer(Project project, List<BindCustomerVo> customers) {
        Prams p = null;
        p = Constant.getConfig(project, Constant.wyWebType);
        String url = p.getBaseUrl() + ApiPathEnum.RoomBindCustomer.getPath();
        AccessToken accessToken = wjyTokenService.getToken(project,Constant.wyWebType);
        if(accessToken == null){
            return false;
        }
        String token = accessToken.getAccess_token();
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("access_token", token);
        params.put("projectID", project.getPid());

        params.put("list", JSONUtil.toJsonStr(customers));

        //Response: {"result":"success","data":null,"msg":"1条数据保存成功!","code":0,"success":true}
        R<RDataSuccessCount> resp = CommonNao.doPost(url, null, params, new TypeReference<R<RDataSuccessCount>>() {});

        return resp == null ? false : resp.isSuccess();
    }

    @Override
    public boolean customerStandardCheckout(Project project, String room, String phone) {
        Prams p = null;
        p = Constant.getConfig(project, Constant.wyWebType);
        String url = p.getBaseUrl() + ApiPathEnum.CustomerCheckout.getPath();
        AccessToken accessToken = wjyTokenService.getToken(project,Constant.wyWebType);
        if(accessToken == null){
            return false;
        }
        String token = accessToken.getAccess_token();
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("access_token", token);
        params.put("pid", project.getPid());
        params.put("room", room);
        params.put("phone", phone);

        R<String> resp = CommonNao.doPost(url, null, params, new TypeReference<R<String>>() {});
        return resp == null ? false : resp.isSuccess();
    }


}