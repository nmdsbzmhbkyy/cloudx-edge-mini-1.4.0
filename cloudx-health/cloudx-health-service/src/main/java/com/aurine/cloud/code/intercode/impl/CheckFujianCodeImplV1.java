package com.aurine.cloud.code.intercode.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloud.code.entity.IdCard;
import com.aurine.cloud.code.entity.QrCard;
import com.aurine.cloud.code.entity.dto.ResultCode;
import com.aurine.cloud.code.entity.enums.VersionEnum;
import com.aurine.cloud.code.platform.PassWayHealthService;
import com.aurine.cloud.code.util.CheckHealthUtils;
import org.apache.http.conn.ConnectTimeoutException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 福建接口实现
 *
 * @ClassName: AbstractHealthFactory
 * @author: yz <wangwei@aurine.cn>
 */
@Component
public class CheckFujianCodeImplV1 implements PassWayHealthService {




    @Value("${connectTimeout}")
    private  Integer connectTimeout;

    @Value("${url}")
    private  String url;


    /**
     * 二维码校验
     *
     * @param qrCard 二维码对象
     * @return
     */
    @Override
    public ResultCode CheckHealth(QrCard qrCard) {

        //根据二维码获取通行结果调用例子开始------------ 测试地址
        //String url = "https://mztapp.fujian.gov.cn:9089/mztsfrz/dataset/AppSerController/invokeservice.do";
        //String url = "https://www.google.com/?hl=zh_CN";
        Map<String, Object> paramsMap = new HashMap<String, Object>();
        paramsMap.put("INVOKESERVICE_CODE", CheckHealthUtils.INVOKESERVICE_CODE_QR);
        paramsMap.put("INVOKECALLER_CODE", CheckHealthUtils.INVOKECALLER_CODE);
        paramsMap.put("QR_CONTENT", qrCard.getQrString());
        paramsMap.put("USER_MOBILE", qrCard.getUserMobile());
        paramsMap.put("CEHCK_TWNR", qrCard.getBodyTemperature());
        String POSTPARAM_JSON = JSON.toJSONString(paramsMap);
        Map<String, Object> clientParam = new HashMap<String, Object>();
        clientParam.put("POSTPARAM_JSON", POSTPARAM_JSON);
        return req(url,clientParam,connectTimeout);
    }

    /**
     * 身份证校验
     *
     * @param idCard 身份证对象
     * @return
     */
    @Override
    public ResultCode CheckHealthByCode(IdCard idCard) {
        //测试地址
        //String url = "https://mztapp.fujian.gov.cn:9089/mztsfrz/dataset/AppSerController/invokeservice.do";
        //正是地址
        //String url = "https://mztapp.fujian.gov.cn:8502/dataset/AppSerController/invokeservice.do";
        Map<String, Object> paramsMap = new HashMap<>(6);
        paramsMap.put("INVOKESERVICE_CODE", CheckHealthUtils.INVOKESERVICE_CODE_ID);
        paramsMap.put("INVOKECALLER_CODE", CheckHealthUtils.INVOKECALLER_CODE);
        paramsMap.put("USER_MOBILE", idCard.getUserMobile());
        paramsMap.put("USER_IDCARD", CheckHealthUtils.getSm4EncryptByCBC(idCard.getIdentityNumber()));
        paramsMap.put("USER_NAME", CheckHealthUtils.getSm4EncryptByCBC(idCard.getUsername()));
        paramsMap.put("CEHCK_TWNR", idCard.getBodyTemperature());
        String POSTPARAM_JSON = JSON.toJSONString(paramsMap);
        Map<String, Object> clientParam = new HashMap<>(1);
        clientParam.put("POSTPARAM_JSON", POSTPARAM_JSON);

        return req(url,clientParam,connectTimeout);
    }

    /**
     * 发送请求并且获取相应结果
     * @param url 请求地址
     * @param clientParam  请求参数
     * @param connectTimeout 连接超时
     * @return
     */
    private ResultCode req(String url, Map<String, Object> clientParam,Integer connectTimeout) {
        try {
            JSONObject  jsonObject = JSONObject.parseObject(CheckHealthUtils.sendPostParams(url, clientParam,connectTimeout));
            return new ResultCode(jsonObject.getBoolean("success"),jsonObject.getString("msg"));
        } catch (ConnectTimeoutException e) {
            e.printStackTrace();
            return new ResultCode(false,"请求超时") ;
        } catch (IOException ioException){
            ioException.printStackTrace();
            return new ResultCode(false,"未知错误联系管理员") ;
        }
    }


    @Override
    public String getVersion() {
        return VersionEnum.V1.code;
    }

    /**
     * 获取版本
     *
     * @return
     */
    public VersionEnum getVer() {
        return VersionEnum.V1;
    }

}
