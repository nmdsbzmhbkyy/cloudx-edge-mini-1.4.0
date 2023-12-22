package com.cloudx.common.push.util;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * <p>推送信息到短信工具类</p>
 *
 * @ClassName: PushSMSUtil
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020/4/29 16:59
 * @Copyright:
 */
@Slf4j
public class PushSMSUtil {
    /**
     * 发送手机短信
     *
     * @param phone
     * @param message
     * @return
     * @throws Exception
     */
    public static boolean sendSmsMessage(String phone, String message,String serverUrl, String userName, String password) throws Exception {

        HttpURLConnection connection = null;
        BufferedReader in = null;
        try {
            StringBuffer sb = new StringBuffer(serverUrl).append("?");
            // 向StringBuffer追加用户名
            // 59515
            sb.append("uid=").append(userName);

            // 向StringBuffer追加密码（密码采用MD5 32位 小写）
            // 3il3r0
            sb.append("&pwd=" + CommUtil.hash(password));

            // 向StringBuffer追加手机号码
            sb.append("&mobile=" + phone);

            // 向StringBuffer追加消息内容转URL标准码
            sb.append("&content=" + URLEncoder.encode(message, "GBK"));
            URL url = new URL(sb.toString());
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            // 发送
            in = new BufferedReader(new InputStreamReader(url.openStream()));
            // 返回发送结果
            String inputline = in.readLine();

            log.debug(inputline);
            if ("100".equals(inputline)) {
                return true;
            } else {
                return false;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (connection != null) {
                    connection.disconnect();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return false;
    }

    public static void main(String[] args) throws Exception {
        String msg = "【米立科技】您的验证码是：" + 123221 + "，请尽快验证！ 如非本人操作请忽略本短信。";
        boolean result = PushSMSUtil.sendSmsMessage("15005082553",msg,"http://http.yunsms.cn/tx/","2205001","kpqpq8");
        log.info(String.valueOf(result));
    }
}
