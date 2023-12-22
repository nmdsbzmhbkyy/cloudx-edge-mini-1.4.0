package com.aurine.cloudx.push.thread;

import com.aurine.cloudx.push.config.AppConfig;
import com.aurine.cloudx.push.constant.Constants;
import com.aurine.cloudx.push.util.SpringUtil;
import com.aurine.cloudx.push.util.UserUtil;
import com.aurine.cloudx.push.util.WxUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName: ThreadPools
 * @author: 邹宇
 * @date: 2021-8-26 09:29:15
 * @Copyright:
 */
@Component
@Slf4j
public class ThreadPools implements CommandLineRunner {

    /**
     * 线程池
     */
    private ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(100);

    public ScheduledFuture<?> futureGetToken;

    /**
     * 刷新微信token
     */
    public void refreshToken() {
        futureGetToken = scheduledThreadPoolExecutor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    //如果未获取token则记录日志
                    try {
                        String accessToken = WxUtil.getAccessToken();
                        log.info("accessToken:{}", accessToken);
                        if (accessToken == null) {
                            log.error("accessToken为空");
                            // 休眠3s后重新获取
                            try {
                                Thread.sleep(3000);
                            } catch (InterruptedException e1) {
                                e1.printStackTrace();
                            }
                        } else {
                            //初始化数据
                            initializationData();
                            log.info("获取token成功:{}", Constants.ACCESS_TOKEN);
                            break;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }, 0, 7000, TimeUnit.SECONDS);
    }

    @Override
    public void run(String... args) throws Exception {
        //注入默认参数
        initAppConfig();
        //获取微信token
        refreshToken();
    }

    private void initAppConfig() {
        AppConfig appConfig = SpringUtil.getApplicationContext().getBean(AppConfig.class);
        String appId = appConfig.getAppId();
        if (appId != null && !"".equals(appId)) {
            Constants.APP_ID = appId;
        }
        String appSecret = appConfig.getAppSecret();
        if (appSecret != null && !"".equals(appSecret)) {
            Constants.APP_SECRET = appSecret;
        }
        String serverToken = appConfig.getServerToken();
        if (serverToken != null && !"".equals(serverToken)) {
            Constants.TOKEN = serverToken;
        }
        log.info("初始化成功");
        log.info("Constants.APP_ID：{}",Constants.APP_ID);
        log.info("Constants.APP_SECRET：{}",Constants.APP_SECRET);
        log.info("Constants.TOKEN：{}",Constants.TOKEN);
    }

    /**
     * 初始化数据
     */
    private void initializationData() {
        // 获取token之后去初始化openId和UnionId关系map
        UserUtil.updateUserInfoToMap();
    }
}
