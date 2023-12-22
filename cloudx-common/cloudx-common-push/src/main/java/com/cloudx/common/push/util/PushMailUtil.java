package com.cloudx.common.push.util;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.SimpleEmail;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>推送消息到Email工具类</p>
 *
 * @ClassName: EMailUtil
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020/4/29 15:05
 * @Copyright:
 */
@Slf4j
public class PushMailUtil {


    public static boolean sendSimpleEmail(String toMailAddr, String subject, String message, String mailHost, String userName, String password, String mailFrom, String mailFormName) {
        Email email = new SimpleEmail();
        try {
            email.setHostName(mailHost);
//			email.setSmtpPort(465);
            email.setAuthentication(userName, password);
//			email.setSSLOnConnect(true);
            email.setFrom(mailFrom, mailFormName);
            email.setSubject(subject);
            email.setMsg(message);
            email.addTo(toMailAddr);
            email.send();
            log.error("邮件发送成功：{}", toMailAddr);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("邮件发送失败：{}", toMailAddr);
            return false;
        }
    }

    /**
     * 注意，使用该模式群发邮件，接收方在邮件地址可以看到所有的群发对象
     * @param toMailAddrList
     * @param subject
     * @param message
     * @param mailHost
     * @param userName
     * @param password
     * @param mailFrom
     * @param mailFormName
     * @return
     */
    public static boolean sendSimpleEmails(List<String> toMailAddrList, String subject, String message, String mailHost, String userName, String password, String mailFrom, String mailFormName) {
        Email email = new SimpleEmail();
        try {
            email.setHostName(mailHost);
//			email.setSmtpPort(465);
            email.setAuthentication(userName, password);
//			email.setSSLOnConnect(true);
            email.setFrom(mailFrom, mailFormName);
            email.setSubject(subject);
            for (String mailAddr : toMailAddrList) {
                email.addTo(mailAddr);
            }
            email.setMsg(message);
            email.send();
            log.error("邮件发送成功");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("邮件发送失败");
            return false;
        }
    }

    /**
     * 发送html邮件
     *
     * @param toMailAddr 收信人地址
     * @param subject    email主题
     * @param message    发送email信息
     */
    public static boolean sendHtmlMail(String toMailAddr, String subject, String message, String mailHost, String userName, String password, String mailFrom, String mailFormName) {
        HtmlEmail hemail = new HtmlEmail();
        try {
            hemail.setHostName(mailHost);
            //			hemail.setSmtpPort(getSmtpPort(from));
            //						hemail.setCharset(charSet);
            hemail.addTo(toMailAddr);
            hemail.setAuthentication(userName, password);
            hemail.setFrom(mailFrom, mailFormName);
            hemail.setSubject(subject);
//            hemail.setMsg(message);
            hemail.setContent(message, "text/html;charset=UTF-8");
            hemail.send();
            log.error("邮件发送成功：{}", toMailAddr);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("邮件发送失败：{}", toMailAddr);
            return false;
        }

    }

    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        list.add("wangwei@aurine.cn");
        list.add("123114964@qq.com");
        String htmlMess= "亲爱的{name}<br/><br/>欢迎您使用IOT系统，请点击下面的链接激活您的帐户<br/>如果您无法点击链接，请复制以下网址到浏览器里直接打开。<br/><br/><a href='{url}'>{url}</a><br/><br/>XXXX信息技术有限公司<br/>www.xxxxx.net";

//        boolean result = PushMailUtil.sendSimpleEmail("wangwei@aurine.cn", "主题", "messages", "smtp.qq.com", "ahomelife", "bsmmzjfvptdadaah", "ahomelife@qq.com", "智能生活");
//        boolean result = PushMailUtil.sendSimpleEmails(list, "主题", "messages", "smtp.qq.com", "ahomelife", "bsmmzjfvptdadaah", "ahomelife@qq.com", "智能生活");
        boolean result = PushMailUtil.sendHtmlMail("123114964@qq.com", "主题", htmlMess, "smtp.qq.com", "ahomelife", "bsmmzjfvptdadaah", "ahomelife@qq.com", "智能生活");
        log.info(String.valueOf(result));
    }
	/*public static void main(String args[])
	{
		String htmlMess= "亲爱的{name}<br/><br/>欢迎您使用IOT系统，请点击下面的链接激活您的帐户<br/>如果您无法点击链接，请复制以下网址到浏览器里直接打开。<br/><br/><a href='{url}'>{url}</a><br/><br/>XXXX信息技术有限公司<br/>www.xxxxx.net";
		String subject = mailConfig.getMail_title();
		sendHtmlMail("chenshulong123@qq.com","1213",htmlMess);
	}*/
}
