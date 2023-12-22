package com.aurine.cloudx.dashboard.config;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @description:
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2021-03-02
 * @Copyright:
 */
@RefreshScope
@Configuration
@ConfigurationProperties("test")
@Data
public class TestConfig {
    private List<JSONObject> entranceEvent = new ArrayList<>();
    private List<JSONObject> alarmEvent = new ArrayList<>();
    private List<JSONObject> parkIn = new ArrayList<>();
    private List<JSONObject> parkOut = new ArrayList<>();


    public JSONObject getEntranceEvent(String id) {
        return get(entranceEvent, id);
    }

    public JSONObject getAlarmEvent(String id) {
        return get(alarmEvent, id);
    }

    public JSONObject getParkingIn(String id) {
        return get(parkIn, id);
    }

    public JSONObject getParkingOut(String id) {
        return get(parkOut, id);
    }


    /**
     * 获取列表中的指定id
     *
     * @param list
     * @return
     */
    private JSONObject get(List<JSONObject> list, String id) {

        if (StringUtils.isEmpty(id)) {
            Random random = new Random();
            int n = random.nextInt(list.size());
            return list.get(n);
        } else {
            for (JSONObject json : list) {
                if (StringUtils.isNotEmpty(json.getString("id"))) {
                    if (StringUtils.equals(json.getString("id"), id)) {
                        return json;
                    }
                }
            }
        }

        return null;
    }
}
