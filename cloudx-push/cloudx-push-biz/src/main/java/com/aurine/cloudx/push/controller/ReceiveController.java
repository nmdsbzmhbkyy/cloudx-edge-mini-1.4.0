package com.aurine.cloudx.push.controller;

import com.alibaba.fastjson.JSON;
import com.aurine.cloudx.push.constant.Constants;
import com.aurine.cloudx.push.util.MenuUtil;
import com.aurine.cloudx.push.util.MessageUtil;
import com.aurine.cloudx.push.util.WxUtil;
import com.aurine.cloudx.push.util.aes.WXBizMsgCrypt;
import com.pig4cloud.pigx.common.security.annotation.Inner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Random;

/**
 * @ClassName: ReceiveController
 * @author: 邹宇
 * @date: 2021-8-24 15:19:18
 * @Copyright:
 */
@RestController
@RequestMapping(value = "/wxp/wx")
@Inner(false)
@Slf4j
public class ReceiveController {


    /**
     * 微信接入服务器验证请求
     *
     * @param request
     * @return 返回echoster代表接入成功
     */
    @GetMapping(value = "/receive")
    public String receiveInfoGet(HttpServletRequest request) {
        String signature = request.getParameter("signature");
        String timestamp = request.getParameter("timestamp");
        String nonce = request.getParameter("nonce");
        String echostr = request.getParameter("echostr");
        //校验请求是否来自微信
        if (MenuUtil.check(timestamp, nonce, signature)) {
            log.info("微信验证接入成功");
            return echostr;
        } else {
            log.error("微信验证接入失败,请检查参数:{}", JSON.toJSONString(request));
            return "微信验证接入失败,请检查参数";
        }
    }

    /**
     * 接收微信发送的消息
     *
     * @param
     * @return
     */
    @PostMapping(value = "/receive")
    public String receiveInfoPost(@RequestBody String requestBody,
                                  @RequestParam(name = "timestamp", required = false) String timestamp,
                                  @RequestParam(name = "nonce", required = false) String nonce,
                                  @RequestParam(name = "msg_signature", required = false) String msgSignature) {
        log.info("收到消息");
        try {
            //消息解密
            WXBizMsgCrypt pc = new WXBizMsgCrypt(Constants.TOKEN, Constants.ENCODING_AES_KEY, Constants.APP_ID);
            //处理微信推送的xml数据包
            String decryptXml = pc.decryptMsg(msgSignature, timestamp, nonce, requestBody);
            Map<String, String> requestMap = WxUtil.xmlToMap(decryptXml);
            //准备回复的数据包
            String response = MessageUtil.getResponse(requestMap);
            log.info("准备回复的数据包:{}", response);
            if (response != null) {
                response = pc.encryptMsg(response, (System.currentTimeMillis() + "").substring(0, 10), new Random().nextInt(99999999) + "");
            }
            return response;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
