package com.aurine.cloudx.estate.subscribe.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.google.common.reflect.TypeToken;
import com.aurine.cloudx.estate.entity.SysDecided;
import com.aurine.cloudx.estate.service.SysDecidedService;
import com.aurine.cloudx.estate.subscribe.service.EventSubscribeService;
import com.aurine.cloudx.estate.util.HttpClientUtil;
import com.aurine.cloudx.estate.util.RedisUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

import static com.aurine.cloudx.estate.constant.decidedKeyConstant.PEOPLE_KEY;
import static com.aurine.cloudx.estate.constant.decidedTypeConstant.PEOPLE_TYPE;

/**
 * 用于人行记录
 *
 * @version 1.0
 * @author： 林功鑫
 * @date： 2021-07-29 15:58
 */
@Component(value = PEOPLE_TYPE)
@Slf4j
public class PeopleEventSubscribeServiceImpl implements EventSubscribeService {

    @Resource
    private SysDecidedService sysDecidedService;

    @Override
    public Boolean send(JSONObject jsonObject, Integer projectId, String type) {

        //取缓存
        List<Object> peopleType = RedisUtil.lGet("subscribe:" + PEOPLE_KEY + ":" + projectId, 0, -1);

        List<String> addrList = new ArrayList<>();
        if (peopleType.size() == 0) {
            List<SysDecided> sysDecidedList = sysDecidedService.list(new LambdaQueryWrapper<SysDecided>()
                    .eq(SysDecided::getProjectid, projectId)
                    .eq(SysDecided::getType, type));

            for (SysDecided sysDecided : sysDecidedList) {
                addrList.add(sysDecided.getAddr());
                RedisUtil.lSet("subscribe:" + PEOPLE_KEY + ":" + projectId, sysDecided);
            }

        } else {
            addrList = new Gson().fromJson(new Gson().toJson(addrList), new TypeToken<List<String>>() {
            }.getType());
        }

        for (String addr : addrList) {
            try {
//                HttpUtil.post(addr,jsonObject);
                HttpClientUtil.doPostTestTwo(addr, jsonObject);
                log.info("[" + addr + "]:" + jsonObject.toJSONString());
            } catch (Exception e) {
                log.error(e.getMessage());
                return false;
            }

        }
        return true;
    }
}
