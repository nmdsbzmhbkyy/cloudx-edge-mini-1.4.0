package com.aurine.cloudx.estate.thirdparty.module.device.platform.aurine.handle;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.entity.ProjectCard;
import com.aurine.cloudx.estate.entity.ProjectFaceResources;
import com.aurine.cloudx.estate.entity.ProjectPasswd;
import com.aurine.cloudx.estate.entity.ProjectRightDevice;
import com.aurine.cloudx.estate.mapper.ProjectFaceResourcesMapper;
import com.aurine.cloudx.estate.service.ProjectCardService;
import com.aurine.cloudx.estate.service.ProjectFaceResourcesService;
import com.aurine.cloudx.estate.service.ProjectPasswdService;
import com.aurine.cloudx.estate.service.ProjectRightDeviceService;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.VersionEnum;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.aurine.callback.AurineCallbackService;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.aurine.config.AurineConfigDTO;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.aurine.entity.constant.AurineCacheConstant;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.aurine.entity.constant.AurineErrorEnum;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.aurine.entity.vo.AurineCertVo;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.aurine.entity.vo.AurineDeviceVo;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.aurine.remote.factory.AurineRemoteDeviceServiceFactory;
import com.aurine.cloudx.estate.util.RedisUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 冠林中台 V2.1 版本  执行人脸下发异步转同步业务类
 *
 * @ClassName: PassWayDeviceServiceImplByAurineV1
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-08-12 14:00
 * @Copyright:
 */
@Component
@Slf4j
@RefreshScope
public class AurineCertSendHandleV2_1 {
    @Resource
    private ProjectFaceResourcesService projectFaceResourcesService;
    @Resource
    private ProjectFaceResourcesMapper projectFaceResourcesMapper;
    @Resource
    private ProjectPasswdService projectPasswdService;
    @Resource
    private ProjectCardService projectCardService;
    @Resource
    private AurineCallbackService aurineCallbackServiceImplV2_1;
    @Resource
    private ProjectRightDeviceService projectRightDeviceService;

    @Async
    public void addCert(AurineConfigDTO configDTO, AurineDeviceVo deviceVo, List<AurineCertVo> certList) {
        ProjectContextHolder.setProjectId(configDTO.getProjectId());
        JSONObject resultJson = AurineRemoteDeviceServiceFactory.getInstance(VersionEnum.V2_1).addCert(configDTO, deviceVo, certList);

        //1、同步调用中台下发，等待返回第三方id和会话id，存入redis形成会话
        String asyncMsgId = this.resetThirdCodeToCert(resultJson, certList);//获取到异步的消息ID
//        long startTime;
//        long endTime;

        //2、等待redis会话消费完毕，执行结束
//        if (certList.get(0).getKeytype().equals("3")) {
        log.info("[冠林中台] Redis同步消费 msgId：{} 已下发 开始等待回调", asyncMsgId);
//        startTime = System.currentTimeMillis();   //获取开始时间

        //当异步id被消费，或者已过期（硬件没有响应），再发送下一条数据到硬件
//        while (RedisUtil.hasKey(asyncMsgId)) {
//            try {
//                Thread.sleep(100);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//        endTime = System.currentTimeMillis(); //获取结束时间
//        if ((endTime - startTime) >= (AurineCacheConstant.AURINE_MESSAGE_CACHE_TTL * 1000)) {
//
//            log.info("冠林中台 Kafka同步消费 msgId：{} 回调执行超时，耗时{} ms，跳过并销毁会话", asyncMsgId, endTime - startTime);
//        }else{
//            log.info("冠林中台 Kafka同步消费 msgId：{} 回调执行成功，耗时{} ms", asyncMsgId, endTime - startTime);
//
//        }
//        }
    }


    //回调下发
    @Async
    public void delCert(AurineConfigDTO configDTO, AurineDeviceVo deviceVo, List<AurineCertVo> certList) {
        ProjectContextHolder.setProjectId(configDTO.getProjectId());
        JSONObject resultJson = AurineRemoteDeviceServiceFactory.getInstance(VersionEnum.V2_1).delCert(configDTO, deviceVo, certList);
        this.cacheToRedis(resultJson, certList);


        /**
         *验证删除时是否发生异常，如果返回 “对象不存在” 异常，直接删除凭证
         * 当前方法仅适用于中台2.0 一次只能调用一个凭证操作的情况
         * @author: 王伟
         * @since 2020-09-23
         */
        Object errorObj = resultJson.get("errorCode");
        if (errorObj != null && errorObj.toString().equals(AurineErrorEnum.WRONG_OBJECT.code)) {
            //伪造json请求，调用回调，出触发删除业务

            /**
             * {
             *     "eventCode": "0",
             *     "eventData": {
             *         "devsn": "PROQK20M0802905XQX0V",
             *         "errorcode": "0",
             *         "modelid": "020302",
             *         "msgid": "1597835726771"
             *     },
             *     "eventType": "1",
             *     "time": "2020-08-19 19:15:26",
             *     "accessToken": "NfSgqZzdshTrrVLHKSfagy42bP2L2hFk",
             *     "communityId": "S1000000272"
             * }
             */
            JSONObject delRespJson = new JSONObject();
            delRespJson.put("eventCode", "0");
            delRespJson.put("eventType", "1");
            delRespJson.put("time", "2020-08-19 19:15:26");
            delRespJson.put("accessToken", "NfSgqZzdshTrrVLHKSfagy42bP2L2hFk");
            delRespJson.put("communityId", "S1000000272");

            JSONObject delRespDataJson = new JSONObject();
            delRespDataJson.put("devsn", resultJson.getString("devsn"));
            delRespDataJson.put("errorcode", "0");
            delRespDataJson.put("modelid", "020302");
            delRespDataJson.put("msgid", resultJson.getString("msgid"));

            delRespJson.put("eventData", delRespDataJson);

            aurineCallbackServiceImplV2_1.deviceCommandResponse(delRespJson);
        }
    }

    /**
     * 将获取的第三方code设置到系统通行凭证中
     *
     * @param json
     * @param certVoList
     * @return
     */
    public String resetThirdCodeToCert(JSONObject json, List<AurineCertVo> certVoList) {
        if (json == null || CollUtil.isEmpty(certVoList)){
            return "";
        }

        /**
         *  "keys": [
         *             {
         *                 "keyvalue": "06621733",
         *                 "keyid": "1",
         *                 "keytype": "1"
         *             },
         *             {
         *                 "keyvalue": "11111111",
         *                 "keyid": "4",
         *                 "keytype": "1"
         *             }
         *         ],
         *         "msgid": "1597816041574"
         */

        JSONArray keyArray = json.getJSONArray("keys");
        String msgId = "AURINE_MIDDLE_REQUEST_" + json.getString("msgid");

        //因中台2.0机制，目前buffer只能为1

        //存入redis，等待异步回调处理
        RedisUtil.set(msgId, JSONObject.toJSONString(certVoList), AurineCacheConstant.AURINE_MESSAGE_CACHE_TTL);

        List<AurineCertVo> keyList = keyArray.toJavaList(AurineCertVo.class);

        //设置第三方key(确定与传入顺序完全一致)
        for (int i = 0; i < keyList.size(); i++) {
            certVoList.get(i).setKeyid(keyList.get(i).getKeyid());
        }

        //根据类型，批量修改通行凭证
        if ("1".equals(certVoList.get(0).getKeytype())) {//card

            ProjectCard card;
            List<ProjectCard> cardList = new ArrayList<>();
            for (AurineCertVo certVo : certVoList) {
                card = new ProjectCard();
                card.setCardId(certVo.getUid());
                card.setCardCode(certVo.getKeyid());
                cardList.add(card);
            }

//            return projectCardService.updateBatchById(cardList);
            projectCardService.updateBatchById(cardList);
            return msgId;

        } else if ("2".equals(certVoList.get(0).getKeytype())) {//password

            ProjectPasswd passwd;
            List<ProjectPasswd> passwdList = new ArrayList<>();
            for (AurineCertVo certVo : certVoList) {
                passwd = new ProjectPasswd();
                passwd.setPassId(certVo.getUid());
                passwd.setPassCode(certVo.getKeyid());
                passwdList.add(passwd);
            }

//            return projectPasswdService.updateBatchById(passwdList);
            if (CollUtil.isNotEmpty(passwdList)) {

                projectPasswdService.updateBatchById(passwdList);
            }
            return msgId;

        } else if ("3".equals(certVoList.get(0).getKeytype())) {//face

            ProjectFaceResources face;
            List<ProjectFaceResources> faceResourcesList = new ArrayList<>();
            List<ProjectRightDevice> rightDeviceList = new ArrayList<>();
            List<ProjectRightDevice> rightDeviceFullList = new ArrayList<>();

            for (AurineCertVo certVo : certVoList) {
                face = new ProjectFaceResources();
                face.setFaceId(certVo.getUid());
                face.setFaceCode(certVo.getKeyid());
                faceResourcesList.add(face);

                /**
                 * 将第三方id写入rightDevice记录表
                 * @since 2021-04-12
                 */
                rightDeviceList = projectRightDeviceService.list(new QueryWrapper<ProjectRightDevice>().lambda().eq(ProjectRightDevice::getCertMediaId, certVo.getUid()));
                if (CollUtil.isNotEmpty(rightDeviceList)) {
                    for (ProjectRightDevice rightDevice : rightDeviceList) {
                        rightDevice.setCertMediaCode(certVo.getKeyid());
                        rightDeviceFullList.add(rightDevice);
                    }
                }
            }
            if (CollUtil.isNotEmpty(faceResourcesList)) {
//                projectFaceResourcesMapper.updateFaceCodeBatch(faceResourcesList);
                projectFaceResourcesService.updateBatchById(faceResourcesList);
            }

            /**
             * 将第三方id写入rightDevice记录表
             * @since 2021-04-12
             */
            if (CollUtil.isNotEmpty(rightDeviceFullList)) {
                projectRightDeviceService.updateBatchById(rightDeviceFullList);
            }

            return msgId;
        }
        return msgId;

    }

    /**
     * @param json
     * @param certVoList
     * @return
     */
    private boolean cacheToRedis(JSONObject json, List<AurineCertVo> certVoList) {
        if (json == null || CollUtil.isEmpty(certVoList)){
            return true;
        }


//        JSONArray keyArray = json.getJSONArray("keys");
        String msgId = json.getString("msgid");
        //因中台2.0机制，目前buffer只能为1
        //存入redis，等待异步回调处理
        RedisUtil.set("AURINE_MIDDLE_REQUEST_" + msgId, JSONObject.toJSONString(certVoList), AurineCacheConstant.AURINE_MESSAGE_CACHE_TTL);


        return true;
    }

}
