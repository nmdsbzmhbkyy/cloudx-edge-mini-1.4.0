package com.aurine.cloudx.wjy.service.impl;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.aurine.cloudx.wjy.entity.Project;
import com.aurine.cloudx.wjy.pojo.AccessToken;
import com.aurine.cloudx.wjy.vo.BuildingVo;
import com.aurine.cloudx.wjy.vo.UnitVo;
import com.aurine.cloudx.wjy.constant.ApiPathEnum;
import com.aurine.cloudx.wjy.nal.CommonNao;
import com.aurine.cloudx.wjy.pojo.Prams;
import com.aurine.cloudx.wjy.pojo.RDataRowsPager;
import com.aurine.cloudx.wjy.service.WjyBuildingService;
import com.aurine.cloudx.wjy.constant.Constant;
import com.aurine.cloudx.wjy.pojo.R;
import com.aurine.cloudx.wjy.service.WjyTokenService;
import com.aurine.cloudx.wjy.vo.WjyBuilding;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ： huangjj
 * @date ： 2021/4/22
 * @description： 我家云楼栋管理接口实现类
 */
@Service
public class WjyBuildingServiceImpl implements WjyBuildingService {

    @Resource
    WjyTokenService wjyTokenService;
    @Override
    public R<RDataRowsPager<WjyBuilding>> buildingGetByPage(int rowCount,
                                                            int currentPage,
                                                            Project project,
                                                            String queryName) {

        Prams p = null;
        p = Constant.getConfig(project, Constant.wyWebType);
        String url = p.getBaseUrl() + ApiPathEnum.BuildGetList.getPath();
        AccessToken accessToken = wjyTokenService.getToken(project,Constant.wyWebType);
        if(accessToken == null){
            return null;
        }
        String token = accessToken.getAccess_token();
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("access_token", token);
        params.put("rowCount", rowCount);
        params.put("current", currentPage);
        params.put("projectIDs", project.getPid());
        if(StringUtils.isNotBlank(queryName)){
            params.put("searchPhrase", queryName);
        }

        R<RDataRowsPager<WjyBuilding>> resp = CommonNao.doGet(url, null, params, new TypeReference<R<RDataRowsPager<WjyBuilding>>>() {});

        return resp;
    }

    @Override
    public boolean buildingSave(Project project, List<BuildingVo> builds) {

        Prams p = null;
        p = Constant.getConfig(project, Constant.wyWebType);
        String url = p.getBaseUrl() + ApiPathEnum.BuildSave.getPath();
        String token = null;
        try {
            token = wjyTokenService.getToken(project, Constant.wyWebType).getAccess_token();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("access_token", token);

        JSONArray jary = new JSONArray();
        for(BuildingVo one: builds){
            JSONObject jobj = new JSONObject();

            jobj.set("name", one.getName());
            jobj.set("number", one.getNumber());
            jobj.set("overgroundCount", one.getOvergroundCount());
            jobj.set("undergroundCount", one.getUndergroundCount());
            jobj.set("sourceID", one.getSourceID());
            jobj.set("sourceSystem", one.getSourceSystem());
            jobj.set("catalog", one.getCatalog());
            jobj.set("area",one.getArea());//非必要
            jobj.set("projectGroupName",one.getProjectGroupName());//非必要
            if(one.getBuildingUnitVoList() != null){
                jobj.set("unitCount",one.getBuildingUnitVoList().size());//非必要
            }
            jobj.set("imageUrl",one.getImageUrl());//非必要
            jobj.set("position",one.getPosition());//非必要

            // 单元信息
            JSONArray jSubAry = new JSONArray();
            if(one.getBuildingUnitVoList() != null)
                for(UnitVo unit: one.getBuildingUnitVoList()){
                    JSONObject jsub = new JSONObject();
                    jsub.set("name", unit.getName());
                    jsub.set("number", unit.getNumber());
                    jsub.set("Seq", unit.getSeq());
                    jSubAry.add(jsub);
                }
            jobj.set("buildingUnitVoList", jSubAry);

            jary.add(jobj);
        }

        params.put("list", jary.toJSONString(0));

        // 返回数据格式：Response: {"result":"success","data":null,"msg":"1条数据保存成功!","code":0,"success":true}
        R<String> resp = CommonNao.doPost(url, null, params, new TypeReference<R<String>>() {});

        return resp == null ? false : resp.isSuccess();
    }
}