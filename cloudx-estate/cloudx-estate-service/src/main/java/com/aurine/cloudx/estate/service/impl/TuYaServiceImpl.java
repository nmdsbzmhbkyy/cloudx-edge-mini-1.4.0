package com.aurine.cloudx.estate.service.impl;

import com.aurine.cloudx.estate.service.TuyaService;
import com.aurine.cloudx.estate.util.UserClient;
import com.tuya.api.client.user.Models.SyncUserVO;
import com.tuya.api.common.ClientConfig;
import com.tuya.api.common.RegionEnum;
import com.tuya.api.common.TuyaResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 涂鸦业务接口
 */
@Service
@Slf4j
public class TuYaServiceImpl implements TuyaService {

    @Value("${TuYa.ACCESS_ID:49q9mkq8af6kduy6r51x}")
    private String ACCESS_ID;

    @Value("${TuYa.ACCESS_KEY:26c8bbf6a20640789215d48b556cbf58}")
    private String ACCESS_KEY;

    @Value("${TuYa.SCHEMA:smartcloudlive}")
    private String SCHEMA;

    @Override
    public String syncUser(SyncUserVO user) {
        ClientConfig.init(ACCESS_ID, ACCESS_KEY, RegionEnum.URL_CN);

//        SyncUserVO vo = new SyncUserVO("86", "18259551683", "18259551683", "123456", 1);
        TuyaResult<Map> tr = UserClient.syncUser(SCHEMA, user);

        if (tr.getSuccess()) {
            return (String) tr.getResult().get("uid");
        } else {
            return null;
        }
    }

    @Override
    public String syncUser(SyncUserVO user, String schema) {
        ClientConfig.init(ACCESS_ID, ACCESS_KEY, RegionEnum.URL_CN);

//        SyncUserVO vo = new SyncUserVO("86", "18259551683", "18259551683", "123456", 1);
        TuyaResult<Map> tr = UserClient.syncUser(schema, user);

        if (tr.getSuccess()) {
            return (String) tr.getResult().get("uid");
        } else {
            return null;
        }
    }
}
