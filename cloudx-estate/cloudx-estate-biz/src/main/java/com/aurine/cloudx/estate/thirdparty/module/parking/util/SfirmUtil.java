package com.aurine.cloudx.estate.thirdparty.module.parking.util;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.util.DigestUtils;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * @description: 赛菲姆车场 连接对接工具类
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-07-23
 * @Copyright:
 */
@Slf4j
public class SfirmUtil {
    private static Random r = new Random();


    /**
     * sign生成
     * 生成规则：所有参数小写后，按照ASCII顺序排列，拼接成字符串后，还原原有大小，并MD5加密，大写
     *
     * @param json      报文参数
     * @param appSecret 授权码
     * @return
     */
    public static String signGenerator(JSONObject json, String appSecret) {
        List<String> keyList = new ArrayList<>();
        String signStr = "";
        String sign = "";

        //参数进入迭代器，并最小化
        for (String keyStr : json.keySet()) {
            if (!"sign".equalsIgnoreCase(keyStr)) {
                keyList.add(keyStr.toLowerCase());
            }
        }

        //排序，小到大，ascii顺序
        Collections.sort(keyList);

        //将顺序映射到原数据，并拼接为字符串
        signStr = "";
        boolean math = false;
        for (String lowerKey : keyList) {
            for (String originKey : json.keySet()) {
                if (lowerKey.equalsIgnoreCase(originKey)) {
                    signStr += originKey + "=" + json.getString(originKey) + "&";
                    break;
                }
            }
        }
        signStr += appSecret;

        //MD5加密
        sign = DigestUtils.md5DigestAsHex(signStr.getBytes()).toUpperCase();
        return sign;
    }



//    public static void main(String[] args) {
//        JSONObject json = new JSONObject();
//        json.put("parkKey", "84w88ujr");
//        json.put("payOrderNo", "OT20200413092822736721484w88ujr");
//        json.put("payedSN", "201808251340300001");
//        json.put("payedMoney", "10.00");
//        json.put("version", "v1.0");
//        json.put("appid", "ym91f172631e1f8353");
//        json.put("rand", "4.197383447");
//        json.put("sign", "B06405A1A083CF8A5F6F8AE2599D1D14");
//
//        System.out.println(SfirmUtil.signGenerator(json, "aef0ae6d9f094013b245aa45f772a0d4"));
//        LocalDate date = LocalDate.now();
//        ;
//
//        System.out.println(DateUtil.format(date.atStartOfDay(), "yyyy-MM-dd HH:mm:ss"));
//    }

    /**
     * 生成接口用随机参数
     *
     * @return
     */
    public static String random() {
        int i = r.ints(100000, (999899 + 1)).limit(1).findFirst().getAsInt();
        double d = Double.valueOf(i) / 10000;
        return String.valueOf(d);
    }


}
