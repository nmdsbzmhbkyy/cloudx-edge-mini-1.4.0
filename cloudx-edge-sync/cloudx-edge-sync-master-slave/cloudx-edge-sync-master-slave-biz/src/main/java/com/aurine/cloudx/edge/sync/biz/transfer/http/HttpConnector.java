package com.aurine.cloudx.edge.sync.biz.transfer.http;

import cn.hutool.core.io.IORuntimeException;
import cn.hutool.http.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author: wrm
 * @Date: 2022/01/24 9:15
 * @Package: com.aurine.cloudx.edge.sync.biz.transfer.http
 * @Version: 1.0
 * @Remarks:
 **/
@Component
@Slf4j
public class HttpConnector {

    public static void sendJsonPost(String url, String jsonBody) {
        log.info("转发url:{},message：{}", url, jsonBody);
        try {
            String postRes = HttpUtil.post(url, jsonBody);
            log.info("Transfer Event postRes = {}", postRes);
        }catch (IORuntimeException e) {
            log.info("连接被拒绝，url为{}", url);
        }
    }
}
