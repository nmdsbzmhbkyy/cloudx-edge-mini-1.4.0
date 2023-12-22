package com.aurine.cloudx.common.core.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Auther: hjj
 * @Date: 2022/6/23 09:52
 * @Description: 格式化MAC
 */
public class MacUtil {
    /**
    * @Author hjj
    * @Description  mac码加分隔符
    * 类似12ae5bac34c4中间无分隔符，需要在中间加入分隔符(:或者-),最终效果：12:ae:5b:ac:34:c4
    * @Date  2022/6/23
    * @Param mac
    * @Param split
    * @return
    **/
    public static String formatMac(String mac, String split) {

        String regex = "[0-9a-fA-F]{12}";

        Pattern pattern = Pattern.compile(regex);

        Matcher matcher = pattern.matcher(mac);

        if (!matcher.matches()) {
            throw new IllegalArgumentException("mac format is error");

        }

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 12; i++) {
            char c = mac.charAt(i);

            sb.append(c);

            if ((i & 1) == 1 && i <= 9) {
                sb.append(split);

            }

        }

        return sb.toString();

    }
    /**
    * @Author hjj
    * @Description  mac码去除分隔符
    * 类似12:ae:5b:ac:34:c4或者12-ae-5b-ac-34-c4中间有分隔符，需要去掉分隔符(:或者-),最终效果：12ae5bac34c4
    * @Date  2022/6/23
    * @Param mac
    * @return
    **/
    public static String formatMac(String mac) {
        if(mac.length() != 17){
            return mac.replace(":","").replace("-","");
        }
        String regex = "(([a-f0-9]{2}:)|([a-f0-9]{2}-)){5}[a-f0-9]{2}";

        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);

        Matcher matcher = pattern.matcher(mac);

        if (!matcher.matches()) {
            throw new IllegalArgumentException("mac format is error");

        }

        return mac.replaceAll("[:-]", "");

    }

    public static void main(String[] args) {
        System.out.println(formatMac("00-55-53-39:18:03"));
        //System.out.println(formatMac("005553391803","-"));
    }
}
