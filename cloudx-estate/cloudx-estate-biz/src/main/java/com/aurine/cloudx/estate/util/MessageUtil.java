package com.aurine.cloudx.estate.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * (MessageUtil)信息内容工具类
 *
 * @author xull
 * @version 1.0.0
 * @date 2020/11/4 17:02
 */
public class MessageUtil {

    private static final String REG = "\\{\\{(.+?)\\}\\}";

    /**
     * 根据正则表达式获取文本中的变量名列表
     *
     * @param pattern
     * @param content
     * @return
     */
    public static List<String> getParams(String pattern, String content) {
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(content);

        List<String> result = new ArrayList<String>();
        while (m.find()) {
            result.add(m.group(1));
        }
        return result;
    }

    /**
     * 根据正则表达式将文本中的变量使用实际的数据替换成无变量的文本
     *
     * @param pattern
     * @param content
     * @param data
     * @return
     */
    public static String parse(String pattern, String content, Map<String, String> data) {
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(content);

        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            String key = m.group(1);
            String value = data.get(key);
            m.appendReplacement(sb, value == null ? "" : value);
        }
        m.appendTail(sb);
        return sb.toString();
    }

    /**
     * 默认占位符替换方法 使用 {{参数名}} 格式替换
     *
     * @param content 需要替换的内容 如：”信息:{{参数1}}，{{参数2}}“
     * @param data    map值 传入如：第一条参数 key:”参数1" value: “信息1”  第二条参数 key:”参数2" value: “信息2”
     * @return 返回 如 ”信息:信息1，信息2“
     */
    public static String parse(String content, Map<String, String> data) {
        return parse(REG, content, data);
    }

//    public static void main(String[] args) {
//        Map<String, String> map = new HashMap<>();
//        map.put("test", "测试");
//        map.put("test2", "测试2");
//        System.out.println(parse("消息:{{test}},{{test2}}", map));
//    }
}
