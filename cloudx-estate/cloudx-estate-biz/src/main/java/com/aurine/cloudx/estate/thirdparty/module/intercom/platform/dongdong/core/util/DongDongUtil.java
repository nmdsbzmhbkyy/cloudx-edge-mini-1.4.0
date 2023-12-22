package com.aurine.cloudx.estate.thirdparty.module.intercom.platform.dongdong.core.util;

import cn.hutool.crypto.SecureUtil;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

/**
 * 咚咚 工具类
 *
 * @description:
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-11-17
 * @Copyright:
 */
public class DongDongUtil {

    /**
     * 签名算法、
     *  获取请求的 http method；
     *  获取请求的 url，不包括 query_string 的部分；
     *  将所有参数（包括 POST 的参数，但不包含签名字段）格式化为“key=value”格式，如“k1=v1”、“k2=v2”、“k3=v3”；
     *  将格式化好的参数键值对以字典序升序排列后，拼接在一起，如“k1：v1，k2 ：v2，k3：v3”，并将 http method 和 url
     * 按顺序拼接在这个字符串前面；
     *  在拼接好的字符串末尾追加上应用的 secret_key，并进行 urlencode，形成 base_string；
     * 上述字符串的 MD5 值即为签名的值：
     * 即可简单描述为： sign = MD5( urlencode( $http_method$url$k1=$v1$k2=$v2$k3=$v3$secret_key ));
     *
     * @param method
     * @param url
     * @param secretKey
     * @param paramMap
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String getSign(String method, String url, String secretKey, Map<String, String> paramMap) throws UnsupportedEncodingException {
        String signString = "POST" + url;
        Collection<String> keyset = paramMap.keySet();
        List<String> keyList = new ArrayList<String>(keyset);
        // 对 key 键值按字典升序排序
        Collections.sort(keyList);
        for (int i = 0; i < keyList.size(); i++) {
            String key = keyList.get(i);
            String value = paramMap.get(keyList.get(i));
            signString += (key + "=" + value);
        }
        signString += secretKey;
        System.out.println("signString:" + signString);
        String encode = URLEncoder.encode(signString, "UTF-8");
        String encodeParams = SecureUtil.md5(encode).toLowerCase();
        return encodeParams;
    }


    /**
     * 将unicode \uAAAA 转换为 字符串
     * @param str
     * @return
     */
    public static String unicodeToString(String str) {
        StringBuilder sb = new StringBuilder();
        int begin = 0;
        int index = str.indexOf("\\u");
        while (index != -1) {
            sb.append(str.substring(begin, index));
            sb.append(ascii2Char(str.substring(index, index + 6)));
            begin = index + 6;
            index = str.indexOf("\\u", begin);
        }
        sb.append(str.substring(begin));
        return sb.toString();
    }

    private static char ascii2Char(String str) {
        if (str.length() != 6) {
            throw new IllegalArgumentException("Ascii string of a native character must be 6 character.");
        }
        if (!"\\u".equals(str.substring(0, 2))) {
            throw new IllegalArgumentException("Ascii string of a native character must start with \"\\u\".");
        }
        String tmp = str.substring(2, 4);
        // 将十六进制转为十进制
        int code = Integer.parseInt(tmp, 16) << 8; // 转为高位，后与地位相加
        tmp = str.substring(4, 6);
        code += Integer.parseInt(tmp, 16); // 与低8为相加
        return (char) code;
    }
}
