package com.aurine.cloudx.wjy.service.impl;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.aurine.cloudx.wjy.constant.ApiPathEnum;
import com.aurine.cloudx.wjy.constant.Constant;
import com.aurine.cloudx.wjy.entity.Project;
import com.aurine.cloudx.wjy.nal.CommonNao;
import com.aurine.cloudx.wjy.pojo.AccessToken;
import com.aurine.cloudx.wjy.pojo.Prams;
import com.aurine.cloudx.wjy.pojo.R;
import com.aurine.cloudx.wjy.pojo.Ticket;
import com.aurine.cloudx.wjy.service.WjyOrgService;
import com.aurine.cloudx.wjy.service.WjyTokenService;
import com.aurine.cloudx.wjy.vo.Organization;
import com.aurine.cloudx.wjy.vo.Worker;
import com.aurine.cloudx.wjy.vo.WorkerVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WjyOrgServiceImpl implements WjyOrgService {
    @Resource
    WjyTokenService wjyTokenService;
    @Override
    public boolean orgSyncSave(Project project, List<Organization> orgs) {
        Prams p = null;
        p = Constant.getConfig(project, Constant.wyWebType);
        String url = p.getBaseUrl() + ApiPathEnum.OrgSyncSave.getPath();
        Ticket ticket = wjyTokenService.getTicket(project,Constant.wyWebType);
        if(ticket == null){
            return false;
        }
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ticket", ticket.getTicket());
        JSONArray jary = new JSONArray();
        for(Organization item: orgs){
            JSONObject jobj = new JSONObject();
            jobj.set("name", item.getName());
            jobj.set("number", item.getName());
            jobj.set("simpleName", item.getSimpleName());
            jobj.set("isLeaf", item.getIsLeaf());
            jobj.set("level", item.getLevel());
            jobj.set("longNumber", item.getLongNumber());
            jobj.set("isCompany", item.getIsCompany());
            jobj.set("sourceID", item.getSourceID());
            jobj.set("sourceSystem", item.getSourceSystem());
            jobj.set("parentID",item.getParentID());
            jobj.set("description", item.getDescription());
            jary.add(jobj);
        }
        params.put("list", jary.toJSONString(0));

        R<Object> resp = CommonNao.doPost(url, null, params, new TypeReference<R<Object>>() {});
        return resp == null ? false : resp.isSuccess();
    }

    @Override
    public boolean workerAdd(Project project, List<WorkerVo> workerVos) {
        Prams p = null;
        p = Constant.getConfig(project, Constant.wyWebType);
        String url = p.getBaseUrl() + ApiPathEnum.WorkerSyncSave.getPath();
        Ticket ticket = wjyTokenService.getTicket(project,Constant.wyWebType);
        if(ticket == null){
            return false;
        }
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ticket", ticket.getTicket());
        JSONArray jary = new JSONArray();
        for(WorkerVo item: workerVos){
            JSONObject jobj = new JSONObject();
            jobj.set("name", item.getName());
            jobj.set("number", item.getNumber());
            jobj.set("phone", item.getPhone());
            jobj.set("sourceID", item.getSourceID());
            jobj.set("sourceSystem", item.getSourceSystem());
            jobj.set("orgUnitNames", item.getOrgUnitNames());
            jary.add(jobj);
        }
        params.put("list", jary.toJSONString(0));

        R<Object> resp = CommonNao.doPost(url, null, params, new TypeReference<R<Object>>() {});

        return resp == null ? false : resp.isSuccess();
    }

    @Override
    public boolean workerDelete(Project project, String phones) {
        Prams p = null;
        p = Constant.getConfig(project, Constant.wyWebType);
        String url = p.getBaseUrl() + ApiPathEnum.WorkerDelete.getPath();
        AccessToken accessToken = wjyTokenService.getToken(project,Constant.wyWebType);
        if(accessToken == null){
            return false;
        }
        String token = accessToken.getAccess_token();

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("access_token", token);
        params.put("phones", phones);

        R<String> resp = CommonNao.doPost(url, null, params, new TypeReference<R<String>>() {});

        return resp == null ? false : resp.isSuccess();
    }
}
