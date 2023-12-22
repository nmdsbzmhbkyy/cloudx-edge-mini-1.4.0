package com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.vo.param;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 事件服务对象 json名：eventReport
 * </p>
 *
 * @ClassName: EventReport
 * @author: 王良俊 <>
 * @date: 2020年11月25日 下午04:03:06
 * @Copyright:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonRootName("eventReport")
public class EventReport {

    /**
     * 事件源类型
     *
     * camera	监控点
     * subSys	子系统
     * acsDevice	门禁设备	梯口机
     * visDevice	可视对讲设备
     * roadway	车道
     * parkSpace	停车位
     * ecsDevice	梯控设备
     * roomDevice	室内设备	室内机
     */
    String eventSrc;

    /**
     * 事件系统类型
     * vms	视频
     * scpms	入侵报警
     * acs	门禁	梯口机
     * pms	停车场
     * vis	可视对讲
     * ecs	梯控
     * frs	人脸监控
     * fpms	消防
     * escpms	紧急报警
     */
    String evnetType;

    /**
     * 事件名称
     * DjOpenDoorEvent	人行进门事件服务
     * DjAbnormalEvent	异常事件服务
     * DjAlarmEvent	报警事件服务
     * DjTalkRecordEvent	通话记录事件服务
     * DjFQAlarmEvent	安防报警事件
     * FaceMonitorEvent	人脸监控事件
     */
    String eventName;

    /**
     * 事件类型编码
     * 人行进门事件服务 (OpenDoorEvent)	钥匙开门	401000
     * 密码开门	401001
     * 刷卡开门	401002
     * 指纹开门	401003
     * 人脸识别开门	401004
     * 访客开门	401006
     * 监视开门	401007
     * 手机一键开门	401008
     * 临时授权开锁	401009
     * 远程一键开门	401011
     * 手机蓝牙开门	401012
     * 分享密码开门	401013
     * 业主二维码开门	401020
     * 授权二维码开门	401021
     * 临时二维码开门	401022
     * 人证核验开门	401030
     * 访客呼叫室内机开门	401050
     * 访客呼叫手机开门	401051
     * 呼叫中心开门	401052
     * 出门按钮开门	401053
     * 异常事件 (AbnormalEvent)	无效卡刷卡	401102
     * 无效二维码	401103
     * 过期二维码	401104
     * 陌生人抓拍	401105
     * 无效指纹	401106
     * 过期指纹	401107
     * 过期卡	401108
     * 过期密码	401109
     * 过期人脸	401110
     * 异常操作	401199
     * 报警事件 (AlarmEvent )	挟持开门	401201
     * 3次密码错误告警	401202
     * 长时间逗留报警	401203
     * 3次刷卡错误告警	401204
     * 3次指纹错误告警	401205
     * 强行开门报警	401210
     * 门未关报警	401211
     * 防拆报警	401212
     * 黑名单进门	401220
     * 五次操作失败异常	401221
     * 锁舌故障	401222
     * 其他报警	401300
     * 防区报警	401400
     * SOS报警	401401
     * 通话记录	通话记录	401301
     */
    String eventCode;
    /**
     * 事件时间
     */
    LocalDateTime eventTime;

    /**
     * 事件对象信息
     * 可包含OpendoorEventObj，AlarmEventObj，RecordEventObj
     */
    JSONObject objInfo;


}
