package com.aurine.cloudx.estate.util;

import com.tuya.api.client.user.Models.GetUsersReq;
import com.tuya.api.client.user.Models.SyncUserVO;
import com.tuya.api.common.ClientConfig;
import com.tuya.api.common.RegionEnum;
import com.tuya.api.common.RequestHandler;
import com.tuya.api.common.TuyaResult;

import java.util.Map;

public class UserClient {
    /**
     * 同步用户
     *
     * @param schema 渠道标识符
     * @param user 用户数据
     * @return
     */
    public static TuyaResult syncUser(String schema, SyncUserVO user) {
        return RequestHandler.sendRequest(new SyncUserReq(schema, user));
    }

    /**
     * 获取用户
     *
     * @param schema 渠道标识符
     * @param pageNo 当前页, 从1开始
     * @param pageSize 每页大小
     * @return
     */
    public static TuyaResult getUsers(String schema, int pageNo, int pageSize) {
        return RequestHandler.sendRequest(new GetUsersReq(schema, pageNo, pageSize));
    }

    /**
     * 开发者账号
     */
    public static final String ACCESS_ID = "49q9mkq8af6kduy6r51x";
    /**
     * 开发者密钥
     */
    public static final String ACCESS_KEY = "26c8bbf6a20640789215d48b556cbf58";

    /**
     * 涂鸦应用标识符
     */
    public static final String SCHEMA="smartcloudlive";

    public static void main(String[] args) {
        ClientConfig.init(ACCESS_ID, ACCESS_KEY, RegionEnum.URL_CN);

        SyncUserVO vo = new SyncUserVO("86", "18259551685", "18259551685", "123456", 1);
        TuyaResult tr = UserClient.syncUser(SCHEMA, vo);

        System.out.println(tr);
//        System.out.println(tr.getResult().get("uid"));
    }
}
