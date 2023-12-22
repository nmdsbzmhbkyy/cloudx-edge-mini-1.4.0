package com.aurine.cloudx.estate.util;

import com.alibaba.nacos.common.utils.UuidUtils;

import java.util.Random;
import java.util.UUID;

/**
 * @description:
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2021-04-23
 * @Copyright:
 */
public class ShortUUIDUtil {

    private static String[] chars = new String[]{"a", "b", "c", "d", "e", "f",
            "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s",
            "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z"};

    public static String shortUUID() {
//        StringBuffer shortBuffer = new StringBuffer();
//        String uuid = UUID.randomUUID().toString().replace("-", "");
//        for (int i = 0; i < 8; i++) {
//            String str = uuid.substring(i * 4, i * 4 + 4);
//            int x = Integer.parseInt(str, 16);
//            shortBuffer.append(chars[x % 0x3E]);
//        }
//        return shortBuffer.toString();

        return ShortUUIDUtil.timestampRandom();
    }

    /**
     * 获取9位以内顺序数
     *
     * @return
     */
    public static String timestampRandom() {
        String timestamp = String.valueOf(System.currentTimeMillis());
        Random random = new Random();
        int ends = random.nextInt(99);
        timestamp =  timestamp +String.format("%02d",ends);
        timestamp = timestamp.substring(timestamp.length()-9,timestamp.length());

        if(timestamp.startsWith("0")){
            timestamp = "1"+timestamp.substring(1,timestamp.length());
//            timestamp = timestamp.replace("0","1");
        }

        return timestamp;
    }

    /**
     * 获取16位随机数字
     *
     * @return
     */
    public static String timestampRandom16() {
        String timestamp = String.valueOf(System.currentTimeMillis());
        Random random = new Random();
        int ends = random.nextInt(999);
        timestamp =  timestamp +String.format("%03d",ends);

        if(timestamp.startsWith("0")){
            timestamp = "1"+timestamp.substring(1,timestamp.length());
//            timestamp = timestamp.replace("0","1");
        }

        return timestamp;
    }
}
