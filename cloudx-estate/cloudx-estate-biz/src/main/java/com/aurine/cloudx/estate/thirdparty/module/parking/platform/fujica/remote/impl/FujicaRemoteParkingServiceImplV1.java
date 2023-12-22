package com.aurine.cloudx.estate.thirdparty.module.parking.platform.fujica.remote.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.VersionEnum;
import com.aurine.cloudx.estate.thirdparty.module.parking.platform.fujica.remote.FujicaRemoteParkingService;
import com.aurine.cloudx.estate.thirdparty.transport.mq.MQDataTransporter;
import com.aurine.project.entity.Message;
import com.aurine.project.entity.constant.ServiceProviderEnum;
import com.aurine.project.entity.constant.TransportTopicConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @description:
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020/6/22
 * @Copyright:
 */
@Service
@Slf4j
public class FujicaRemoteParkingServiceImplV1 implements FujicaRemoteParkingService {

    @Resource
    private MQDataTransporter<Message, Message> mqDataTransporter;

    /**
     * 获取版本
     *
     * @return
     */
    @Override
    public String getVersion() {
        return VersionEnum.V1.code;
    }

    /**
     * 获取车场信息
     *
     * @param parkKey
     * @param appid
     * @param projectId
     * @param paramMap
     * @return
     */
    @Override
    public JSONObject getParkInfo(String parkKey, String appid, Integer projectId, Map<String, Object> paramMap) {

        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        String topic =  TransportTopicConstant.COMSUMER_TOPIC_PREFIX + appid + "_" + projectId;
        Message message = this.createMessage("com/aurine/cloudx/estate/test", projectId);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("test", "com/aurine/cloudx/estate/test");
        message.setBody(jsonObject);


        Message returnMessage = mqDataTransporter.syncRequest(topic, message.getMsgId(), message.getCallBackMessageId(), message, Message.class);
        log.info("[富士车场] 获取到车场信息：{}", returnMessage.getBody());
        return returnMessage.getBody();
    }

    /**
     * 获取车道摄像头设备列表
     *
     * @param gid 车场code
     * @return
     */
    @Override
    public JSONArray getLaneCameraList(String gid, String appid, Integer projectId, Map<String, Object> paramMap) {

        /**
         *      请求：
         *     {
         *         "PageSize":100,
         *             "CurrentPage":1,
         *             "OrderBy":"ID",
         *             "OrderType":true,
         *             "where":"Id > 0 and Gid='5DF389CE-695A-4D73-8607-407BD0AECAB5'",
         *             "TotalCount":100
         *     }
         */


        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        String topic =  TransportTopicConstant.COMSUMER_TOPIC_PREFIX + appid + "_" + projectId;


        JSONObject jsonObject = new JSONObject();
        jsonObject.put("PageSize", 1000);
        jsonObject.put("CurrentPage", 1);
        jsonObject.put("OrderBy", "ID");
        jsonObject.put("OrderType", true);
        jsonObject.put("where", "Id > 0 and Gid='" + gid + "'");
        jsonObject.put("TotalCount", 100);

        Message message = this.createMessage("postData", projectId, "PlateDevice/GetByCustom", jsonObject);

        Message returnMessage = mqDataTransporter.syncRequest(topic, message.getMsgId(), message.getCallBackMessageId(), message, Message.class);
        log.info("[富士车场] 获取到车道摄像头设备信息：{}", returnMessage.getBody());

        return returnMessage.getBody().getJSONArray("Records");
    }

    /**
     * 添加车辆
     *
     * @param gid
     * @param appid
     * @param projectId
     * @param paramMap
     * @param authDeviceList
     * @return
     */
    @Override
    public boolean addCar(String gid, String appid, Integer projectId, List<String> authDeviceList, String plate, String staffNo, String staffName, String telphoneNo, String orginazitionId, String tcm, LocalDateTime beginTime, LocalDateTime endTime, Double money, String parkingPlaceNo, Map<String, Object> paramMap) {
        /**
         * {
         *     "TokenType": 0,
         *     "IcNumber": null,
         *     "IdNumber": null,
         *     "Deposit": 0,
         *     "IsWrite": false,
         *     "StaffNo": null,
         *     "StaffName": null,
         *     "TelphoneNo": null,
         *     "Address": null,
         *     "OrginazitionId": null,
         *     "OperNo": null,
         *     "OperName": null,
         *     "Redate": "2021-12-24 18:02:41", //登记时间
         *     "Remark": null,
         *     "Gid": null,
         *     "ProjectGids": null,
         *     "ParkIssue": {
         *         "TokenOper": 1,
         *         "AlreadyIn": false,
         *         "TokenId": "苏E05EV2",
         *         "SerialNo": "苏E05EV2",
         *         "StaffNo": "S0002",
         *         "StaffName": "用户名称1",
         *         "TelphoneNo": "15011112213",
         *         "OrginazitionId": "8BCB2990-71AD-4EF2-B150-A58A0BE2C800",
         *         "Tcm": "C97611CE-C6EA-4582-9680-55934B79F9F7",
         *         "PayType": 3,
         *         "AccountBalance": 100,
         *         "BeginDate": "2018-12-24 00:00:00",
         *         "EndDate": "2022-02-01 00:00:00",
         *         "LotNo": "A1",
         *         "AuthDevice": [
         *             "1",
         *             "2"
         *         ],
         *         "Token": "苏E05EV2",
         *         "IsExistCard": true,
         *         "IsUpdateBeginDate": true,
         *         "IsUpdateEndDate": true,
         *         "TokenType": 0,
         *         "UseModel": 1,
         *         "Plate": "苏E05EV2",
         *         "Remark": "remak 车牌",
         *         "OperNo": "999999",
         *         "OperName": "超级管理员",
         *         "Redate": "2018-12-24 18:02:41",
         *         "Gid": "5DF389CE-695A-4D73-8607-407BD0AECAB5",
         *         "ProjectGids": [
         *             "5DF389CE-695A-4D73-8607-407BD0AECAB5"
         *         ],
         *         "OldAccountBlace": 0,
         *         "ID": 0,
         *         "Rid": null,
         *         "IsLegal": true
         *     },
         *     "DoorIssue": null,
         *     "ConsumIssue": null,
         *     "AttendanceIssue": null,
         *     "ID": 0,
         *     "Rid": null,
         *     "IsLegal": false
         * }
         */

        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        String topic =  TransportTopicConstant.COMSUMER_TOPIC_PREFIX + appid + "_" + projectId;

        JSONObject requestJson = new JSONObject();
        JSONObject carJson = new JSONObject();
        JSONArray deviceJsonArray = new JSONArray();
        for (String devId : authDeviceList) {
            deviceJsonArray.add(devId);
        }

        carJson.put("AuthDevice", deviceJsonArray);

        carJson.put("TokenOper", 1);
        carJson.put("AlreadyIn", false);

        carJson.put("TokenId", plate);
        carJson.put("SerialNo", plate);
        carJson.put("Token", plate);
        carJson.put("Plate", plate);

        carJson.put("StaffNo", staffNo);
        carJson.put("StaffName", staffName);
        carJson.put("TelphoneNo", telphoneNo);
        carJson.put("OrginazitionId", orginazitionId);
        carJson.put("Tcm", tcm);

        carJson.put("BeginDate", DateUtil.formatLocalDateTime(beginTime));
        carJson.put("EndDate", DateUtil.formatLocalDateTime(endTime));
        carJson.put("LotNo", parkingPlaceNo);
        carJson.put("AccountBalance", money == null ? 0 : money);

        carJson.put("Gid", gid);

        JSONArray jsonArray = new JSONArray();
        jsonArray.add(gid);
        carJson.put("ProjectGids", jsonArray);

        carJson.put("PayType", 3);
        carJson.put("IsExistCard", true);
        carJson.put("IsUpdateBeginDate", true);
        carJson.put("IsUpdateEndDate", true);
        carJson.put("TokenType", 0);
        carJson.put("UseModel", 1);
        carJson.put("Remark", "");
        carJson.put("OperNo", "999999");
        carJson.put("OperName", "超级管理员");
        carJson.put("Redate", DateUtil.now());
        carJson.put("OldAccountBlace", 0);
        carJson.put("ID", 0);
        carJson.put("Rid", null);
        carJson.put("IsLegal", true);


        requestJson.put("TokenType", 0);
        requestJson.put("IcNumber", null);
        requestJson.put("IdNumber", null);
        requestJson.put("Deposit", 0);
        requestJson.put("IsWrite", false);
        requestJson.put("StaffNo", null);
        requestJson.put("StaffName", null);
        requestJson.put("TelphoneNo", null);
        requestJson.put("Address", null);
        requestJson.put("OrginazitionId", null);
        requestJson.put("OperNo", null);
        requestJson.put("OperName", null);
        requestJson.put("Redate", DateUtil.now());
        requestJson.put("Remark", null);
        requestJson.put("Gid", null);
        requestJson.put("ProjectGids", null);
        requestJson.put("DoorIssue", null);
        requestJson.put("ConsumIssue", null);
        requestJson.put("AttendanceIssue", null);
        requestJson.put("ID", 0);
        requestJson.put("Rid", null);
        requestJson.put("IsLegal", false);

        requestJson.put("ParkIssue", carJson);


        Message message = this.createMessage("postData", projectId, "TokenService/Issue", requestJson);


        Message returnMessage = mqDataTransporter.syncRequest(topic, message.getMsgId(), message.getCallBackMessageId(), message, Message.class);
        log.info("[富士车场] 添加车辆返回信息：{}", returnMessage.getBody());
        this.handleError(returnMessage);

        return true;
    }

    /**
     * 延期与充值
     *
     * @param gid
     * @param appid
     * @param projectId
     * @param paramMap
     * @return
     */
    @Override
    public boolean extraDate(String gid, String appid, Integer projectId, Double money, LocalDateTime beginTime, LocalDateTime endTime, String plate, Map<String, Object> paramMap) {
        String topic =  TransportTopicConstant.COMSUMER_TOPIC_PREFIX + appid + "_" + projectId;

        /**
         * {
         *     "OperMoney":12222,
         *     "PayType":3,
         *     "DeferDate":"2021-06-27 00:00:00",
         *     "EndDate":"2018-12-28 00:00:00",
         *     "FreeMoney":0,
         *     "Token":"粤BFB222",
         *     "IsExistCard":true,
         *     "TokenType":0,
         *     "UseModel":0,
         *     "Plate":null,
         *     "TokenOper":4,
         *     "Remark":"remak 延期",
         *     "OperNo":"999999",
         *     "OperName":"超级管理员",
         *     "Redate":"2018-12-24 10:11:12",
         *     "Gid":"5DF389CE-695A-4D73-8607-407BD0AECAB5",
         *     "ProjectGids":[
         *         "5DF389CE-695A-4D73-8607-407BD0AECAB5"
         *     ],
         *     "OldAccountBlace":0,
         *     "ID":0,
         *     "Rid":null,
         *     "IsLegal":true
         * }
         */
        JSONObject requestJson = new JSONObject();
        requestJson.put("OperMoney", money);
        requestJson.put("PayType", 3);
        requestJson.put("DeferDate", DateUtil.formatLocalDateTime(endTime));
        requestJson.put("EndDate", DateUtil.formatLocalDateTime(beginTime));
        requestJson.put("FreeMoney", 0);
        requestJson.put("Token", plate);
        requestJson.put("IsExistCard", true);
        requestJson.put("TokenType", 0);
        requestJson.put("UseModel", 0);
        requestJson.put("Plate", null);
        requestJson.put("TokenOper", 4);
        requestJson.put("Remark", "");
        requestJson.put("OperNo", "999999");
        requestJson.put("OperName", "超级管理员");
        requestJson.put("Redate", DateTime.now());
        requestJson.put("OldAccountBlace", 0);
        requestJson.put("ID", 0);
        requestJson.put("Rid", 0);
        requestJson.put("IsLegal", true);

        requestJson.put("Gid", gid);
        JSONArray jsonArray = new JSONArray();
        jsonArray.add(gid);
        requestJson.put("ProjectGids", jsonArray);

        Message message = this.createMessage("postData", projectId, "TokenService/ParkDefer", requestJson);

        Message returnMessage = mqDataTransporter.syncRequest(topic, message.getMsgId(), message.getCallBackMessageId(), message, Message.class);
        log.info("[富士车场] 延期充值结果：{}", returnMessage.getBody());

        return this.handleError(returnMessage);
    }

    /**
     * 注销车辆
     *
     * @param gid       车场ID
     * @param appid
     * @param projectId
     * @param plate     车牌号
     * @param paramMap  扩展预留参数
     * @return
     */
    @Override
    public boolean removeMthCar(String gid, String appid, Integer projectId, String plate, Map<String, Object> paramMap) {
        String topic =  TransportTopicConstant.COMSUMER_TOPIC_PREFIX + appid + "_" + projectId;

        /**
         * {

         *     "Token":"京A11111",
         *     "IsExistCard":true,
         *     "TokenType":0,
         *     "UseModel":0,
         *     "Plate":null,
         *     "TokenOper":12,
         *     "Remark":"remark车牌注销",
         *     "OperNo":"999999",
         *     "OperName":"超级管理员",
         *     "Redate":"2018-12-25 09:41:42",
         *     "Gid":"5DF389CE-695A-4D73-8607-407BD0AECAB5",
         *     "ProjectGids":[
         *         "5DF389CE-695A-4D73-8607-407BD0AECAB5"
         *     ],
         *     "OldAccountBlace":0,
         *     "ID":0,
         *     "Rid":null,
         *     "IsLegal":true
         * }
         */
        JSONObject requestJson = new JSONObject();
        requestJson.put("Token", plate);
        requestJson.put("TokenType", 0);
        requestJson.put("UseModel", 0);
        requestJson.put("Plate", null);
        requestJson.put("TokenOper", 12);
        requestJson.put("Remark", "");
        requestJson.put("OperNo", "999999");
        requestJson.put("OperName", "超级管理员");
        requestJson.put("Redate", DateTime.now());
        requestJson.put("OldAccountBlace", 0);
        requestJson.put("ID", 0);
        requestJson.put("Rid", null);
        requestJson.put("IsLegal", true);

        requestJson.put("Gid", gid);
        JSONArray jsonArray = new JSONArray();
        jsonArray.add(gid);
        requestJson.put("ProjectGids", jsonArray);

        Message message = this.createMessage("postData", projectId, "TokenService/ParkUnregister", requestJson);

        Message returnMessage = mqDataTransporter.syncRequest(topic, message.getMsgId(), message.getCallBackMessageId(), message, Message.class);
        log.info("[富士车场] 注销结果：{}", returnMessage.getBody());

        return this.handleError(returnMessage);
    }

    /**
     * 获取默认组织
     *
     * @param gid
     * @param appid
     * @param projectId
     * @param paramMap
     * @return
     */
    @Override
    public JSONObject getDefaultOrg(String gid, String appid, Integer projectId, Map<String, Object> paramMap) {
        String topic =  TransportTopicConstant.COMSUMER_TOPIC_PREFIX + appid + "_" + projectId;
        Message message = this.createMessage("getData", projectId, "Organization/Get/1", new JSONObject());

        Message returnMessage = mqDataTransporter.syncRequest(topic, message.getMsgId(), message.getCallBackMessageId(), message, Message.class);
        log.info("[富士车场] 获取到默认组织信息：{}", returnMessage.getBody());
        this.handleError(returnMessage);

        return returnMessage.getBody();
    }

    /**
     * 获取支付类型
     * （卡类型）
     *
     * @param gid
     * @param appid
     * @param projectId
     * @param paramMap
     * @return
     */
    @Override
    public JSONObject getPayType(String gid, String appid, Integer projectId, Map<String, Object> paramMap) {
        String topic =  TransportTopicConstant.COMSUMER_TOPIC_PREFIX + appid + "_" + projectId;

        /**
         {
         "PageSize":100,
         "CurrentPage":1,
         "OrderBy":"",
         "OrderType":false,
         "where":"TcmType in (1,2,4) and SubSystem = 1 and Gid='5DF389CE-695A-4D73-8607-407BD0AECAB5'",
         "TotalCount":100
         }

         */
        JSONObject requestJson = new JSONObject();
        requestJson.put("PageSize", 100);
        requestJson.put("CurrentPage", 1);
        requestJson.put("OrderBy", "");
        requestJson.put("OrderType", false);
        requestJson.put("where", "TcmType in (1,2,4) and SubSystem = 1 and Gid='"+gid+"'");
        requestJson.put("TotalCount", 100);

        Message message = this.createMessage("postData", projectId, "TCM/GetByCustom", requestJson);

        Message returnMessage = mqDataTransporter.syncRequest(topic, message.getMsgId(), message.getCallBackMessageId(), message, Message.class);
        log.info("[富士车场] 获取支付类型（卡类型）：{}", returnMessage.getBody());

        this.handleError(returnMessage);

        return returnMessage.getBody();
    }


    private Message createMessage(String method, Integer projectId) {
        return this.createMessage(method, projectId, null, null);
    }

    private Message createMessage(String method, Integer projectId, String apiUrl, JSONObject body) {

        Message message = new Message();

        String uuid = UUID.randomUUID().toString().replaceAll("-", "");

        message.setMsgId(uuid);
        message.setTime(LocalDateTime.now());
        message.setProjectId(projectId);
        message.setServiceMethod(method);
        message.setServiceProducerName(ServiceProviderEnum.PARKING_FUJICA.code);
        message.setServiceVersion(VersionEnum.V1.code);
        message.setCallBackTopic(TransportTopicConstant.CALL_BACK_TOPIC);
        message.setCallBackMessageId("CALL_BACK_" + uuid);
        message.setApiUrl(apiUrl);
        message.setBody(body);

        return message;

    }

    private boolean handleError(Message message) {
        JSONObject jsonObject = message.getBody();
        JSONObject stateJson = jsonObject.getJSONObject("State");

        if (stateJson == null && jsonObject.get("IsSucess") != null) {
            stateJson = jsonObject;
        }
//            if (stateJson.getBoolean("IsError")) {
//                //异常信息获取
//                log.error("[富士车场] 数据获取异常：{},{}", stateJson.getString("Code"), stateJson.getString("Describe"));
//                return false;
//            }

        if (!stateJson.getBoolean("IsSucess")) {
            //失败信息获取
            log.error("[富士车场] 数据获取失败：{},{}", stateJson.getString("Code"), stateJson.getString("Describe"));
            return false;
        } else {
            return true;
        }


    }
}
