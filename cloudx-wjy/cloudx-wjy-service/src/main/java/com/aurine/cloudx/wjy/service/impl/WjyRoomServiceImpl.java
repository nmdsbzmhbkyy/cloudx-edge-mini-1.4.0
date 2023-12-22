package com.aurine.cloudx.wjy.service.impl;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.aurine.cloudx.wjy.entity.Project;
import com.aurine.cloudx.wjy.vo.RoomStandardVo;
import com.aurine.cloudx.wjy.vo.RoomVo;
import com.aurine.cloudx.wjy.constant.ApiPathEnum;
import com.aurine.cloudx.wjy.constant.Constant;
import com.aurine.cloudx.wjy.nal.CommonNao;
import com.aurine.cloudx.wjy.service.WjyRoomService;
import com.aurine.cloudx.wjy.service.WjyTokenService;
import com.aurine.cloudx.wjy.vo.WjyRoom;
import com.aurine.cloudx.wjy.pojo.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ： huangjj
 * @date ： 2021/4/25
 * @description： 我家云房间接口
 */
@Service
public class WjyRoomServiceImpl implements WjyRoomService {
    @Resource
    WjyTokenService wjyTokenService;
    @Override
    public R<RDataListPager<List<WjyRoom>>> roomGetByPage(int rowCount, int currentPage, Project project, String queryName) {
        Prams p = null;
        p = Constant.getConfig(project, Constant.wyWebType);
        String url = p.getBaseUrl() + ApiPathEnum.RoomGetList.getPath();
        Ticket ticket = wjyTokenService.getTicket(project,Constant.wyWebType);
        if(ticket == null){
            return null;
        }
        Map<String, Object> params = new HashMap<>();

        params.put("rowCount", rowCount);
        params.put("current", currentPage);
        params.put("ticket", ticket.getTicket());
        params.put("pid", project.getPid());

        if(StringUtils.isNotBlank(queryName)){
            params.put("name", queryName);
        }
        R<RDataListPager<List<WjyRoom>>> resp = CommonNao.doGet(url, null, params, new TypeReference<R<RDataListPager<List<WjyRoom>>>>() {});

        return resp;
    }

    @Override
    public boolean roomSave(Project project, List<RoomVo> rooms) {
        return false;
    }

    @Override
    public boolean roomSaveStandard(Project project, List<RoomStandardVo> rooms) {
        Prams p = null;
        p = Constant.getConfig(project, Constant.wyWebType);
        String url = p.getBaseUrl() + ApiPathEnum.RoomNew.getPath();
        String token = null;
        try {
            token = wjyTokenService.getToken(project, Constant.wyWebType).getAccess_token();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        Map<String, Object> params = new HashMap<>();

        params.put("access_token", token);
        params.put("projectID", project.getPid());

        JSONArray jary = new JSONArray();
        for(RoomStandardVo room: rooms){
            JSONObject jobj = new JSONObject();
            jobj.set("buildingName", room.getBuildingName());
            jobj.set("buildUnitName", room.getBuildUnitName());
            jobj.set("floor", room.getFloor());
            jobj.set("roomName", room.getRoomName());
            jobj.set("roomNumber", room.getRoomNumber());
            jobj.set("property", room.getProperty());

            jobj.set("buildingArea", room.getBuildingArea());
            jobj.set("roomArea", room.getRoomArea());
            jobj.set("productName", room.getProductName());
//            jobj.set("feeStatus", room.getFeeStatus());
//            jobj.set("status", room.getStatus());
//            jobj.set("sourceID", room.getSourceID());
//            jobj.set("sourceSystem", room.getSourceSystem());
//            jobj.set("roomAttributeStr", room.getRoomAttributeStr());

            jary.add(jobj);
        }
        params.put("list", jary.toJSONString(0));
        //System.out.println("{{{{  " + jary.toJSONString(0));

        //Response: {"result":"success","data":null,"msg":"1条数据保存成功!","code":0,"success":true}
        R<RDataSuccessCount> resp = CommonNao.doPost(url, null, params, new TypeReference<R<RDataSuccessCount>>() {});

        return resp == null ? false : resp.isSuccess();
    }
}