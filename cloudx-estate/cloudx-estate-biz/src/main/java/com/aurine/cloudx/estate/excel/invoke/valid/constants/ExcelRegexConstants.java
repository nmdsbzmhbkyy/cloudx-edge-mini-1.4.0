package com.aurine.cloudx.estate.excel.invoke.valid.constants;

/**
 * <p>正则表达式常量类</p>
 * @author : 王良俊
 * @date : 2021-09-14 16:54:58
 */
public interface ExcelRegexConstants {

    /**
     * IPV4格式校验的正则表达式
     */
    String IPV4 = "^(?:(?:1[0-9][0-9]\\.)|(?:2[0-4][0-9]\\.)|(?:25[0-5]\\.)|(?:[1-9][0-9]\\.)|(?:[0-9]\\.)){3}(?:(?:1[0-9][0-9])|(?:2[0-4][0-9])|(?:25[0-5])|(?:[1-9][0-9])|(?:[0-9]))$";

    /**
     * 随机生成IPV6地址 https://www.tooleyes.com/app/ipv6_generator.html
     * 198e:e3b8:28f6:ed07:52be:34a2:3030:9e95
     */
    String IPV6 = "(([0-9a-fA-F]{1,4}:){7,7}[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,7}:|([0-9a-fA-F]{1,4}:){1,6}:[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,5}(:[0-9a-fA-F]{1,4}){1,2}|([0-9a-fA-F]{1,4}:){1,4}(:[0-9a-fA-F]{1,4}){1,3}|([0-9a-fA-F]{1,4}:){1,3}(:[0-9a-fA-F]{1,4}){1,4}|([0-9a-fA-F]{1,4}:){1,2}(:[0-9a-fA-F]{1,4}){1,5}|[0-9a-fA-F]{1,4}:((:[0-9a-fA-F]{1,4}){1,6})|:((:[0-9a-fA-F]{1,4}){1,7}|:)|fe80:(:[0-9a-fA-F]{0,4}){0,4}%[0-9a-zA-Z]{1,}|::(ffff(:0{1,4}){0,1}:){0,1}((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])\\.){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])|([0-9a-fA-F]{1,4}:){1,4}:((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])\\.){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9]))";
    /**
     * 子网掩码
     */
    String SUBNET = "^((123|192)|2(24|4[08]|5[245]))(\\.(0|(128|192)|2((24)|(4[08])|(5[245])))){3}$";
    /**
     * 默认网关
     */
    //String GATEWAY = "^192\\.168(\\.(\\d|([1-9]\\d)|(1\\d{2})|(2[0-4]\\d)|(25[0-5]))){2}$";
    /**
     * 非零正整数
     */
    String NON_ZERO_POSITIVE_INTEGER = "^([1-9][0-9]*)$";
}