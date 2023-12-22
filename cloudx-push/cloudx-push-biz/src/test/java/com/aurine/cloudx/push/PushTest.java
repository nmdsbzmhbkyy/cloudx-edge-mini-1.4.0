package com.aurine.cloudx.push;


import com.aurine.cloudx.push.constant.PushSystemEnum;
import com.aurine.cloudx.push.dto.SmsDTO;
import com.aurine.cloudx.push.dto.WxDTO;
import com.aurine.cloudx.push.entity.ExtensionCallEntity;
import com.aurine.cloudx.push.entity.PushMessage;
import com.aurine.cloudx.push.feign.RemotePushService;
import com.aurine.cloudx.push.service.SendMessageService;
import com.cloudx.common.push.constant.OSTypeEnum;
import com.pig4cloud.pigx.common.core.util.R;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PushTest {

    private String url = "/sysBuilding/";

    @Autowired
    private SendMessageService sendMessageService;
//
//    @Autowired
//    private PushService pushService;

    @Resource
    private RemotePushService remotePushService;


//    @Test
//    @Ignore
//    public void emailMQTest() throws Exception {
//
//        R r = pushService.pushToEmail("123114964@qq.com", "测试标题", "邮件信息<p>换行信息</p>", EmailTypeEnum.text);
//        //断言
//        Assert.assertEquals(0, r.getCode());
//    }

//
//    @Test
//    public void emailsMQTest() throws Exception {
//        List<String> emailList = new ArrayList<>();
//        emailList.add("123114964@qq.com");
//        emailList.add("nanou@vip.qq.com");
//
//        R r = pushService.pushToEmails(emailList, "测试标题", "邮件信息<p>换行信息</p>", EmailTypeEnum.html);
//        //断言
//        Assert.assertEquals(0, r.getCode());
//    }
    @Test
    public void wxMQTest() throws Exception{
        WxDTO wxDTO = new WxDTO();
        List<String> list = new ArrayList<>();
        list.add("oOTpM5yk0XmkpWt0nbfm6iuGFzfA");
        wxDTO.setUnionId(list);
        wxDTO.setTemplateId("MrG5WxPytY5GaZ7imUg-yeo8DN3eDQ7wAP3v4_dXH68");
        Map<String,Object> map = new HashMap<>();
        ExtensionCallEntity extensionCallEntity = new ExtensionCallEntity();
        extensionCallEntity.setCallerTime(DateFormatUtils.format(System.currentTimeMillis(), "yyyy年MM月dd日 HH时mm分"));
        map.put("ExtensionCallEntity", extensionCallEntity);
        wxDTO.setData(map);
        R r = remotePushService.pushToWx(wxDTO);
        //断言
        Assert.assertEquals(0, r.getCode());
    }

    @Test
    public void smsMQTest() throws Exception {


        List<String> emailList = new ArrayList<>();
        emailList.add("123114964@qq.com");
        emailList.add("nanou@vip.qq.com");
        SmsDTO dto = new SmsDTO();
        dto.setMobile("15005082553");
        dto.setMessage("【米立科技】您的验证码是：231122，请尽快验证！ 如非本人操作请忽略本短信。");
        R r =  remotePushService.pushToSMS(dto);

//        R r = remotePushService.pushToSMS("15005082553", "【米立科技】您的验证码是：231122，请尽快验证！ 如非本人操作请忽略本短信。");
        //断言
        Assert.assertEquals(0, r.getCode());
    }

//    @Test
////    @Ignore
//    public void smssMQTest() throws Exception {
//        List<String> listist = new ArrayList<>();
//        listist.add("15005082553");
//        listist.add("18600365301");
//
//        R r = pushService.pushToSMSs(listist, "【米立科技】您的验证码是：121123，请尽快验证！ 如非本人操作请忽略本短信。");
//        //断言
//        Assert.assertEquals(0, r.getCode());
//    }
//
//    @Test
//    public void appsMQTest() throws Exception {
//
//        Map<String, Object> paramMap = new HashMap<>();
//
//        Map<String, String> map = new HashMap<>();
//        map.put("content", "这是内2222222容");
//        map.put("time", (System.currentTimeMillis() / 1000L) + "");
//        map.put("push_type", "C1");
//        map.put("message_type", "8");
//        map.put("community_id", "12");
//        map.put("item_id", "2314");
//        map.put("app_type", "MLink");
//
//        paramMap.put("cunstoms", map);
//        paramMap.put("intent", "intent:#Intent;action=android.intent.action.oppopush;package=com.smarthome.oem.nb;component=com.smarthome.oem.nb/.MainActivity;end");
//
//        PushMessage pushMessage = new PushMessage();
//
//        List<String> list = new ArrayList<>();
//        list.add("2fa9b5b4300273dc41dcdb25aff81daa");
//        list.add("a22b1890ff486f6b31daa7bbad7574ab");
//        pushMessage.setTargetIdList(list);
//
//        List<OSTypeEnum> typeList = new ArrayList<>();
//        typeList.add(OSTypeEnum.android);
//        typeList.add(OSTypeEnum.android);
//        pushMessage.setOsTypeList(typeList);
//
//        pushMessage.setMessage("Messagessssssssssssss");
//        pushMessage.setAppPushSystem(PushSystemEnum.GE_TUI); //个推
//        pushMessage.setAppName("test");//要推送的APP产品名
//
////        pushMessage.setParamMap(paramMap);
//
//
//        R r = pushService.pushToApps(list,typeList,"推送的消息",PushSystemEnum.GE_TUI,"test",paramMap);
//        //断言
//        Assert.assertEquals(0, r.getCode());
//    }
//

//    @Test
//    public void emailTest() throws Exception {
//        PushMessage pushMessage = new PushMessage();
//        pushMessage.setTargetId("123114964@qq.com");
//        pushMessage.setTitle("tititititit");
//        pushMessage.setMessage("Message");
//        boolean res = sendMessageService.sendToEmail(pushMessage);
//        //断言
//        Assert.assertEquals(true, res);
//    }
//
//    @Test
//    public void emailsTest() throws Exception {
//        PushMessage pushMessage = new PushMessage();
//        List<String> list = new ArrayList<>();
//        list.add("123114964@qq.com");
//        list.add("nanou@vip.qq.com");
//        pushMessage.setTargetIdList(list);
//        pushMessage.setTitle("群发消息");
//        pushMessage.setMessage("测试群发");
//        boolean res = sendMessageService.sendToEmails(pushMessage);
//        //断言
//        Assert.assertEquals(true, res);
//    }
//
//
//    @Test
//    public void smsTest() throws Exception {
//        PushMessage pushMessage = new PushMessage();
//        pushMessage.setTargetId("18600365301");
//        pushMessage.setMessage("【米立科技】您的验证码是：231122，请尽快验证！ 如非本人操作请忽略本短信。");
//        boolean res = sendMessageService.sendToSMS(pushMessage);
//        //断言
//        Assert.assertEquals(true, res);
//    }
//
//    @Test
//    public void smssTest() throws Exception {
//        PushMessage pushMessage = new PushMessage();
//        List<String> list = new ArrayList<>();
//        list.add("15005082553");
//        list.add("18600365301");
//        pushMessage.setTargetIdList(list);
//        pushMessage.setMessage("【米立科技】您的验证码是：122311，请尽快验证！ 如非本人操作请忽略本短信。");
//        boolean res = sendMessageService.sendToSMSs(pushMessage);
//        //断言
//        Assert.assertEquals(true, res);
//    }
//
    @Test
    public void smsApp() throws Exception {

        Map<String,Object> paramMap = new HashMap<>();

        Map<String,String> map = new HashMap<>();
        map.put("content", "这是内222容");
        map.put("time", (System.currentTimeMillis() / 1000L) + "");
        map.put("push_type", "C1");
        map.put("message_type", "8");
        map.put("community_id", "12");
        map.put("item_id", "2314");
        map.put("app_type", "MLink");

        paramMap.put("cunstoms",map);
        paramMap.put("intent","intent:#Intent;action=android.intent.action.oppopush;package=com.smarthome.oem.nb;component=com.smarthome.oem.nb/.MainActivity;end");

        PushMessage pushMessage = new PushMessage();
        pushMessage.setTargetId("a4f684bcb74b1466bc8ee48fdeee3762");
        pushMessage.setOsType(OSTypeEnum.android);
        pushMessage.setMessage("Messagessssssssssssss");
        pushMessage.setAppPushSystem(PushSystemEnum.GE_TUI); //个推
        pushMessage.setAppName("慧生活");//要推送的APP产品名

        pushMessage.setParamMap(paramMap);
        boolean res = sendMessageService.sendToApp(pushMessage);
        //断言
        Assert.assertEquals(true, res);
    }
//
//    @Test
    public void smsApps() throws Exception {

        Map<String,Object> paramMap = new HashMap<>();

        Map<String,String> map = new HashMap<>();
        map.put("content", "这是内222容");
        map.put("time", (System.currentTimeMillis() / 1000L) + "");
        map.put("push_type", "C1");
        map.put("message_type", "8");
        map.put("community_id", "12");
        map.put("item_id", "2314");
        map.put("app_type", "MLink");

        paramMap.put("cunstoms",map);
        paramMap.put("intent","intent:#Intent;action=android.intent.action.oppopush;package=com.smarthome.oem.nb;component=com.smarthome.oem.nb/.MainActivity;end");

        PushMessage pushMessage = new PushMessage();

        List<String> list = new ArrayList<>();
        list.add("2fa9b5b4300273dc41dcdb25aff81daa");
        list.add("a22b1890ff486f6b31daa7bbad7574ab");
        pushMessage.setTargetIdList(list);

        List<OSTypeEnum>  typeList = new  ArrayList<>();
        typeList.add(OSTypeEnum.android);
        typeList.add(OSTypeEnum.android);
        pushMessage.setOsTypeList(typeList);

        pushMessage.setMessage("Messagessssssssssssss");
        pushMessage.setAppPushSystem(PushSystemEnum.GE_TUI); //个推
        pushMessage.setAppName("test");//要推送的APP产品名

        pushMessage.setParamMap(paramMap);
        boolean res = sendMessageService.sendToApps(pushMessage);
        //断言
        Assert.assertEquals(true, res);
    }
//
//
//    @Test
//    public void smsAppJiGuang() throws Exception {
//
////        Map<String,Object> paramMap = new HashMap<>();
////
////        Map<String,String> map = new HashMap<>();
////        map.put("content", "这是内222容");
////        map.put("time", (System.currentTimeMillis() / 1000L) + "");
////        map.put("push_type", "C1");
////        map.put("message_type", "8");
////        map.put("community_id", "12");
////        map.put("item_id", "2314");
////        map.put("app_type", "MLink");
////
////        paramMap.put("cunstoms",map);
////        paramMap.put("intent","intent:#Intent;action=android.intent.action.oppopush;package=com.smarthome.oem.nb;component=com.smarthome.oem.nb/.MainActivity;end");
//
//        PushMessage pushMessage = new PushMessage();
//        pushMessage.setTargetId("2f7f808e22919e687ac4426a5960e18a");
//        pushMessage.setOsType(OSTypeEnum.android);
//        pushMessage.setMessage("Messagessssssssssssss");
//        pushMessage.setAppPushSystem(PushSystemEnum.JI_GUANG); //极光
//        pushMessage.setAppName("test");//要推送的APP产品名
//
////        pushMessage.setParamMap(paramMap);
//        boolean res = sendMessageService.sendToApp(pushMessage);
//        //断言
//        Assert.assertEquals(true, res);
//    }


}
