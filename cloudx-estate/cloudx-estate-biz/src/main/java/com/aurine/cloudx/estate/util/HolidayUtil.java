package com.aurine.cloudx.estate.util;

import cn.hutool.core.io.resource.ClassPathResource;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.common.core.util.CommUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 获取节假日时间
 *
 * @description:
 * @author: guhl
 * @date: 2021-03-10
 * @Copyright:
 */
@Component
@Slf4j
public class HolidayUtil {
    /**
     * 存储的json位置
     */
    public static final String PATH = "json/holiday.json";

    public static void setHoliday() {
        OutputStream out = null;
        CloseableHttpClient httpclient = HttpClients.createDefault();
        //获取节假日的api地址
        String url = "http://api.tianapi.com/txapi/jiejiari/index";
        //获取每年的节假日所需携带的参数
        Integer year = LocalDate.now().getYear();
        String param = "key=6bc3eb0cb2d5f98bc7e64dfb05230d40&date=" + year + "&type=1";
        try {

            StringEntity stringEntity = new StringEntity(param);

            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(stringEntity);
            httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded");
            HttpResponse response = httpclient.execute(httpPost);
            //返回的json字符串
            String result = EntityUtils.toString(response.getEntity(), "utf-8");

            log.info("节假日api获取的节假日信息：{}", result);

            JSONObject jsonObject = JSONObject.parseObject(result);
            JSONArray newslist = jsonObject.getJSONArray("newslist");
            ClassPathResource classPathResource = new ClassPathResource(PATH);
            out = new BufferedOutputStream(new FileOutputStream(classPathResource.getFile()));
            out.write(newslist.toString().getBytes(StandardCharsets.UTF_8));

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取所有节假日
     *
     * @return
     */
    public static List<String> getVacations() {
        String s = null;
        try {
            ClassPathResource classPathResource = new ClassPathResource(PATH);
            BufferedReader br = null;
            StringBuffer data = new StringBuffer();
            br = new BufferedReader(new InputStreamReader(classPathResource.getStream(), "utf-8"));
            for (String temp = br.readLine(); temp != null; temp = br.readLine()) {
                data.append(temp);
            }
            br.close();
            s = data.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        JSONArray jsonArray = JSON.parseArray(s);
        List<String> vacations = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            String vacationStr = jsonArray.getJSONObject(i).getString("vacation");
            String[] vacationArr = vacationStr.split("\\|");
            vacations.addAll(Arrays.asList(vacationArr));

        }
        log.info("获取的节假日集合: {}", vacations.toString());
        return vacations;
    }

}
