package com.aurine.cloudx.estate.thirdparty.main.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.common.core.constant.DeviceTypeConstants;
import com.aurine.cloudx.common.core.constant.enums.DeviceTypeEnum;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.constant.enums.AlarmEventStatusEnum;
import com.aurine.cloudx.estate.constant.enums.CertmediaTypeEnum;
import com.aurine.cloudx.estate.constant.enums.PersonTypeEnum;
import com.aurine.cloudx.estate.entity.*;
import com.aurine.cloudx.estate.mapper.ProjectDeviceInfoMapper;
import com.aurine.cloudx.estate.service.*;
import com.aurine.cloudx.estate.subscribe.factory.EventFactory;
import com.aurine.cloudx.estate.subscribe.service.EventSubscribeService;
import com.aurine.cloudx.estate.thirdparty.main.entity.PersonInfo;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.EventTypeEnum;
import com.aurine.cloudx.estate.thirdparty.main.entity.dto.EventDeviceGatePassDTO;
import com.aurine.cloudx.estate.thirdparty.main.entity.dto.EventWarningErrorDTO;
import com.aurine.cloudx.estate.thirdparty.main.service.EventService;
import com.aurine.cloudx.estate.util.ImgConvertUtil;
import com.aurine.cloudx.estate.vo.ProjectDeviceInfoProxyVo;
import com.aurine.cloudx.estate.vo.ProjectEntranceAlarmEventVo;
import com.aurine.cloudx.estate.vo.ProjectEventVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.pig4cloud.pigx.common.data.tenant.TenantContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import static com.aurine.cloudx.estate.constant.decidedTypeConstant.PEOPLE_TYPE;

/**
 * @description:
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-08-10
 * @Copyright:
 */
@Service
@Slf4j
public class EventServiceImpl implements EventService {

    @Resource
    private ProjectDeviceInfoProxyService projectDeviceInfoProxyService;
    @Resource
    private ProjectPersonInfoService projectPersonInfoService;
    @Resource
    private ProjectEntranceEventService projectEntranceEventService;
    @Resource
    private ProjectEntranceAlarmEventService projectEntranceAlarmEventService;
    @Resource
    private ProjectCardService projectCardService;
    @Resource
    private ProjectFingerprintsService projectFingerprintsService;
    @Resource
    private ProjectFaceResourcesService projectFaceResourcesService;
    @Resource
    private ProjectRightDeviceService projectRightDeviceService;
    @Resource
    private ProjectPasswdService projectPasswdService;
    @Resource
    private ProjectStaffService projectStaffService;
    @Resource
    private SysEventTypeConfService sysEventTypeConfService;

    @Resource
    private ProjectVisitorHisService projectVisitorHisService;

    @Resource
    private ImgConvertUtil imgConvertUtil;

    @Resource
    private ProjectPerimeterAlarmEventService projectPerimeterAlarmEventService;

    @Resource
    private ProjectDeviceInfoService projectDeviceInfoService;

    @Resource
    private EventFactory eventFactory;

    @Resource
    private ProjectDeviceInfoMapper projectDeviceInfoMapper;
    @Resource
    private  ProjectLiftEventService projectLiftEventService;
    @Resource
    private ProjectFrameInfoService projectFrameInfoService;
    @Resource
    private ProjectEpidemicEventService projectEpidemicEventService;
    @Resource
    private ProjectVisitorService projectVisitorService;
    @Resource
    private ProjectHouseInfoService projectHouseInfoService;
    /**
     * 门禁开门事件
     *
     * @param eventDeviceGatePassDTO
     */
    @Override
//    @SysLog("记录通行事件")
    @Async
    public void passGate(EventDeviceGatePassDTO eventDeviceGatePassDTO) {
        //获取设备信息
        String thirdPartyCode = eventDeviceGatePassDTO.getThirdPartyCode();
        String deviceSn = eventDeviceGatePassDTO.getDeviceSn();
        ProjectDeviceInfoProxyVo deviceInfo;
        Integer projectId = null;
        PersonInfo personInfo = null;


        if (StringUtils.isNotEmpty(thirdPartyCode)) {
            deviceInfo = projectDeviceInfoProxyService.getByThirdPartyCode(thirdPartyCode);
            projectId = deviceInfo.getProjectId();
        } else {
            deviceInfo = projectDeviceInfoProxyService.getByDeviceSn(deviceSn);
            if (deviceInfo == null) {
                log.error("统一接口 事件处理失败：设备不存在 {}", deviceSn);
                return;
//                throw new RuntimeException("设备不存在");
            }
            projectId = deviceInfo.getProjectId();
        }


        //获取住户信息
        String relaCode = eventDeviceGatePassDTO.getPersonCode();

//        //事件不带有住户第三方信息，则过通行凭证查询
//        if (StringUtils.isEmpty(relaCode)) {
//
//        } else {
//            //通过住户第三方编码获取 住户信息
//            ProjectHousePersonRel housePersonRel = projectHousePersonRelService.getByRelaCode(relaCode);
//            personInfo = projectPersonInfoService.getById(housePersonRel.getPersonId());
//        }

        //获取通行人员的类型与人员id
        if (deviceInfo != null) {
            personInfo = getPersonTypeAndPersonId(eventDeviceGatePassDTO, projectId);
        }


        if (personInfo != null && deviceInfo != null) {
            ProjectEventVo eventVo = this.createProjectEvent(eventDeviceGatePassDTO, personInfo, deviceInfo);

            /**
             * 将图片转存至本地服务器
             * @author: 王伟
             * @since: 2020-11-05 9:14
             */
//            MultipartFile file = MultipartFileUtil.createFile(eventDeviceGatePassDTO.getImgUrl(), UUID.randomUUID().toString().replaceAll("-", ""));


//            String result = HttpUtil.postFile("https://icloud.aurine.cn/admin/sys-file/upload", file);

//            log.info(result);
//
//            if (file != null) {
//                R r = remoteFileService.upload(file);
//               String imgUrl = r.getMsg();
//                eventDeviceGatePassDTO.setImgUrl(imgUrl);
//            }

            eventDeviceGatePassDTO.setImgUrl(imgConvertUtil.convertToLocalUrl(eventDeviceGatePassDTO.getImgUrl()));

            //发送订阅消息
            EventSubscribeService eventSubscribeService = eventFactory.GetProduct(PEOPLE_TYPE);
            String poString = JSONObject.toJSONString(eventVo);
            eventSubscribeService.send(JSONObject.parseObject(poString), deviceInfo.getProjectId(), PEOPLE_TYPE);
            //判断为乘梯识别终端设备，保存到乘梯记录表
            if(deviceInfo.getDeviceType().equals(DeviceTypeConstants.ELEVATOR_RECOGNIZER_DEVICE)){
                projectLiftEventService.addEvent(eventVo);
            }else {
                projectEntranceEventService.addEvent(eventVo);
            }

            //添加疫情记录
            if(StrUtil.isNotEmpty(eventDeviceGatePassDTO.getTemperature())){
                projectEpidemicEventService.saveEpidemicEvent(eventVo,eventDeviceGatePassDTO.getTemperature());
            }

            //如果是访客访问，还需将事件事件记录到访客表中
            projectVisitorHisService.saveVisitTime(eventVo.getPersonId(), eventVo.getEventTime());

        } else {
            log.info("通行事件中使用的通行凭证 {} 未找到有效持有人或设备", eventDeviceGatePassDTO.getJsonObject());
            this.passGateWithOutPerson(eventDeviceGatePassDTO);
        }

    }

    /**
     * 异常通行事件
     *
     * @param eventDeviceGatePassDTO
     */
    @Override
    @Async
    public void passGateError(EventDeviceGatePassDTO eventDeviceGatePassDTO) {
        //获取设备信息
        String thirdPartyCode = eventDeviceGatePassDTO.getThirdPartyCode();
        String deviceSn = eventDeviceGatePassDTO.getDeviceSn();
        ProjectDeviceInfoProxyVo deviceInfo;

        if (StringUtils.isNotEmpty(thirdPartyCode)) {
            deviceInfo = projectDeviceInfoProxyService.getByThirdPartyCode(thirdPartyCode);
        } else {
            deviceInfo = projectDeviceInfoProxyService.getByDeviceSn(deviceSn);
        }


        if (deviceInfo != null) {
            ProjectEventVo eventVo = new ProjectEventVo();
            eventVo.setDeviceId(deviceInfo.getDeviceId());
            eventVo.setDeviceName(deviceInfo.getDeviceName());

            eventVo.setEventTime(eventDeviceGatePassDTO.getEventTime());
            eventVo.setEventDesc(eventDeviceGatePassDTO.getDesc());

            eventVo.setPersonType(eventDeviceGatePassDTO.getPersonType());

            eventVo.setEventType("2");//通行事件, 异常事件
            eventVo.setEventStatus("0");//未处理
            eventVo.setCertMedia(eventDeviceGatePassDTO.getCertMediaType());

            eventVo.setTenantId(deviceInfo.getTenantId());
            eventVo.setProjectId(deviceInfo.getProjectId());
            String picUrl = imgConvertUtil.convertToLocalUrl(eventDeviceGatePassDTO.getImgUrl());
            eventVo.setPicUrl(picUrl);
            eventVo.setSmallPicUrl(picUrl);
            eventVo.setThirdCertMediaId(eventDeviceGatePassDTO.getThirdCertMediaId());
            //发送订阅消息
            EventSubscribeService eventSubscribeService = eventFactory.GetProduct(PEOPLE_TYPE);
            String poString = JSONObject.toJSONString(eventVo);
            eventSubscribeService.send(JSONObject.parseObject(poString), deviceInfo.getProjectId(), PEOPLE_TYPE);

            //判断为乘梯识别终端设备，保存到乘梯记录表
            if(deviceInfo.getDeviceType().equals(DeviceTypeConstants.ELEVATOR_RECOGNIZER_DEVICE)){
                projectLiftEventService.addEvent(eventVo);
            }else {
                projectEntranceEventService.addEvent(eventVo);
            }

        } else {
            log.info("通行事件中使用的通行凭证 {} 未找到有效持有人或设备", eventDeviceGatePassDTO.getJsonObject());
        }
    }

    /**
     * 门禁开门事件(不含用户信息，如系统开门，远程开门等)
     *
     * @param eventDeviceGatePassDTO
     */
    @Override
//    @SysLog("记录系统开门事件")
    @Async
    public void passGateWithOutPerson(EventDeviceGatePassDTO eventDeviceGatePassDTO) {

        //获取设备信息
        String thirdPartyCode = eventDeviceGatePassDTO.getThirdPartyCode();
        String deviceSn = eventDeviceGatePassDTO.getDeviceSn();
        ProjectDeviceInfoProxyVo deviceInfo;

        if (StringUtils.isNotEmpty(thirdPartyCode)) {
            deviceInfo = projectDeviceInfoProxyService.getByThirdPartyCode(thirdPartyCode);
        } else {
            deviceInfo = projectDeviceInfoProxyService.getByDeviceSn(deviceSn);
        }


        if (deviceInfo != null) {
            ProjectEventVo eventVo = new ProjectEventVo();
            eventVo.setDeviceId(deviceInfo.getDeviceId());
            eventVo.setDeviceName(deviceInfo.getDeviceName());

            eventVo.setEventTime(eventDeviceGatePassDTO.getEventTime());
            eventVo.setEventDesc(eventDeviceGatePassDTO.getDesc());

            if (StrUtil.equals(eventDeviceGatePassDTO.getDeviceType(), DeviceTypeConstants.INDOOR_DEVICE)) {
                eventVo.setPersonName(eventDeviceGatePassDTO.getPersonName());
//                eventVo.setPersonName(projectDeviceInfoMapper.getPersonAddressByDeviceId(deviceInfo.getHouseId(),deviceInfo.getUnitId(),deviceInfo.getBuildingId()));
                eventVo.setPersonType(PersonTypeEnum.PROPRIETOR.code);
            } else if (StrUtil.equals(eventDeviceGatePassDTO.getDeviceType(), DeviceTypeConstants.CENTER_DEVICE)) {
                eventVo.setPersonName(eventDeviceGatePassDTO.getPersonName());
                eventVo.setPersonType(PersonTypeEnum.STAFF.code);
            } else {
                eventVo.setPersonType(eventDeviceGatePassDTO.getPersonType());
            }


            eventVo.setEventType("1");//通行事件
            eventVo.setEventStatus("0");//未处理
            eventVo.setCertMedia(eventDeviceGatePassDTO.getCertMediaType());
            eventVo.setThirdCertMediaId(eventDeviceGatePassDTO.getThirdCertMediaId());
            log.info("guwh   eventVo =============="+eventVo.getThirdCertMediaId() );

            eventVo.setTenantId(deviceInfo.getTenantId());
            eventVo.setProjectId(deviceInfo.getProjectId());
            String picUrl = imgConvertUtil.convertToLocalUrl(eventDeviceGatePassDTO.getImgUrl());
            eventVo.setPicUrl(picUrl);
            eventVo.setSmallPicUrl(picUrl);
            eventVo.setOpenType(eventDeviceGatePassDTO.getOpenMode());
            eventVo.setQrcode(eventDeviceGatePassDTO.getQrcode());
            //发送订阅消息
            EventSubscribeService eventSubscribeService = eventFactory.GetProduct(PEOPLE_TYPE);
            String poString = JSONObject.toJSONString(eventVo);
            eventSubscribeService.send(JSONObject.parseObject(poString), deviceInfo.getProjectId(), PEOPLE_TYPE);
            //判断为乘梯识别终端设备，保存到乘梯记录表
            if(deviceInfo.getDeviceType().equals(DeviceTypeConstants.ELEVATOR_RECOGNIZER_DEVICE)){
                projectLiftEventService.addEvent(eventVo);
            }else {
                projectEntranceEventService.addEvent(eventVo);
            }
            //如果是访客访问，需要判断自动延期业务；项目自动延期功能开启，访客自动延期开启同时满足
            projectVisitorHisService.autoDelay(eventVo.getPersonId());
        } else {
            log.info("通行事件中使用的通行凭证 {} 未找到有效设备", eventDeviceGatePassDTO.getJsonObject());
        }

    }

    /**
     * 异常警告事件
     *
     * @param eventWarningErrorDTO
     */
    @Override
//    @SysLog("记录通行异常信息")
    @Async
    public void warning(EventWarningErrorDTO eventWarningErrorDTO) {

        //获取设备信息
        String thirdPartyCode = eventWarningErrorDTO.getThirdPartyCode();
        String deviceSn = eventWarningErrorDTO.getDeviceSn();
        ProjectDeviceInfoProxyVo deviceInfo;
        String eventLevel = null;
        if (StringUtils.isNotEmpty(thirdPartyCode)) {
            deviceInfo = projectDeviceInfoProxyService.getByThirdPartyCode(thirdPartyCode);
        } else {
            deviceInfo = projectDeviceInfoProxyService.getByDeviceSn(deviceSn);
        }


        ProjectContextHolder.setProjectId(deviceInfo.getProjectId());
        TenantContextHolder.setTenantId(1);
        if (deviceInfo != null && eventWarningErrorDTO.getEventTypeId() != null) {
            eventLevel = sysEventTypeConfService.getOne(new LambdaQueryWrapper<SysEventTypeConf>()
                    .eq(SysEventTypeConf::getEventTypeId, eventWarningErrorDTO.getEventTypeId())).getEventLevel();
            String eventDesc = eventWarningErrorDTO.getDesc();
            if (eventWarningErrorDTO.getEventTypeId().equals(EventTypeEnum.A001001.eventTypeId) ||
                    eventWarningErrorDTO.getEventTypeId().equals(EventTypeEnum.A001003.eventTypeId) ||
                    eventWarningErrorDTO.getEventTypeId().equals(EventTypeEnum.A001004.eventTypeId)) {
                eventDesc = eventWarningErrorDTO.getDesc() + "(" + eventWarningErrorDTO.getAreaNo() + "防区)";
            }

            ProjectEntranceAlarmEventVo projectEntranceAlarmEventVo = new ProjectEntranceAlarmEventVo();
            projectEntranceAlarmEventVo.setProjectId(deviceInfo.getProjectId());
            projectEntranceAlarmEventVo.setTenantId(deviceInfo.getTenantId());
            log.info("设备类型：{},{}", deviceInfo.getDeviceType(), DeviceTypeEnum.getByCode(deviceInfo.getDeviceType()).getNote());
            projectEntranceAlarmEventVo.setDeviceTypeName(DeviceTypeEnum.getByCode(deviceInfo.getDeviceType()).getNote());
            projectEntranceAlarmEventVo.setAreaNo(eventWarningErrorDTO.getAreaNo());
            projectEntranceAlarmEventVo.setDeviceId(deviceInfo.getDeviceId());
            projectEntranceAlarmEventVo.setEventTime(eventWarningErrorDTO.getEventTime());
            projectEntranceAlarmEventVo.setEventDesc(eventDesc);
            projectEntranceAlarmEventVo.setStatus(AlarmEventStatusEnum.UNPROCESSED.code);
            projectEntranceAlarmEventVo.setEventTypeId(eventWarningErrorDTO.getEventTypeId());
            projectEntranceAlarmEventVo.setEventLevel(eventLevel);
            projectEntranceAlarmEventVo.setEventCode(eventWarningErrorDTO.getEventCode());
            if (eventWarningErrorDTO.getImgUrl() != null) {
                projectEntranceAlarmEventVo.setPicUrl(imgConvertUtil.convertToLocalUrl(eventWarningErrorDTO.getImgUrl()));
            }

            //获取4.0的告警ID
//            projectEntranceAlarmEventVo.setEventId(eventWarningErrorDTO.getEventTypeId());

            //获取第三方原生告警ID
            eventWarningErrorDTO.getThirdEventTypeId();

            projectEntranceAlarmEventService.save(projectEntranceAlarmEventVo);
        }

    }

    /**
     * 周界告警事件
     *
     * @param eventWarningErrorDTO
     */
    @Override
    @Async
    public void channelAlarm(EventWarningErrorDTO eventWarningErrorDTO) {
        String sn = eventWarningErrorDTO.getDeviceSn();
        ProjectDeviceInfoProxyVo device = projectDeviceInfoProxyService.getByDeviceSn(sn);
        if (device == null) {
            log.info("周界告警主机未找到");
        } else {
            ProjectDeviceInfo deviceInfoServiceOne = projectDeviceInfoService.getOne(Wrappers.lambdaQuery(ProjectDeviceInfo.class).eq(ProjectDeviceInfo::getSn, sn));
            ProjectContextHolder.setProjectId(deviceInfoServiceOne.getProjectId());
            TenantContextHolder.setTenantId(1);
            String channelNo = eventWarningErrorDTO.getErrorDeviceId();
            String errorType = eventWarningErrorDTO.getErrorType();
            projectPerimeterAlarmEventService.saveAlarm(sn, channelNo, errorType);
        }
    }


    /**********************************************通行事件内部方法****************************************************/

    private ProjectEventVo createProjectEvent(EventDeviceGatePassDTO eventDeviceGatePassDTO, PersonInfo personInfo, ProjectDeviceInfoProxyVo deviceInfo) {
        ProjectEventVo eventVo = new ProjectEventVo();

        //根据人员信息，获取对应的人员信息
        if (StringUtils.isEmpty(personInfo.getPersonName())) {
            if (personInfo.getPersonTypeEnum() == PersonTypeEnum.PROPRIETOR) {
                ProjectPersonInfo projectPersonInfo = projectPersonInfoService.getById(personInfo.getPersonId());
                if (projectPersonInfo == null) {
                    eventVo.setPersonName("未知住户");
                    log.error("未知住户访问 {}", personInfo.getPersonId());
                } else {
                    ProjectHouseInfo houseInfo = null;
                    eventVo.setPersonName(projectPersonInfo.getPersonName());
                    eventVo.setTelephone(projectPersonInfo.getTelephone());
                    //为区口
                    if(DeviceTypeEnum.getByCode(deviceInfo.getDeviceType()) == DeviceTypeEnum.GATE_DEVICE){
                        houseInfo = projectHouseInfoService.getFirstHouse(personInfo.getPersonId(),null,null,null);
                        //梯口
                    }else if(DeviceTypeEnum.getByCode(deviceInfo.getDeviceType()) == DeviceTypeEnum.LADDER_WAY_DEVICE){
                        houseInfo = projectHouseInfoService.getFirstHouse(personInfo.getPersonId(),deviceInfo.getBuildingId(),deviceInfo.getUnitId(),null);
                    }
                    eventVo.setAddrDesc(houseInfo==null?"":houseInfo.getHouseName());

                }
            } else if (personInfo.getPersonTypeEnum() == PersonTypeEnum.STAFF) {
                ProjectStaff projectStaff = projectStaffService.getById(personInfo.getPersonId());
                if (projectStaff == null) {
                    eventVo.setPersonName("未知员工");
                    log.error("未知员工访问 {}", personInfo.getPersonId());
                } else {
                    eventVo.setPersonName(projectStaff.getStaffName());
                    eventVo.setTelephone(projectStaff.getMobile());
                }

            } else if (personInfo.getPersonTypeEnum() == PersonTypeEnum.VISITOR) {
                ProjectVisitorHis visitor = projectVisitorHisService.getById(personInfo.getPersonId());
                if(StrUtil.isNotEmpty(eventDeviceGatePassDTO.getTemperature())){
                    ProjectVisitor projectVisitor = projectVisitorService.getById(visitor.getVisitorId());
                    eventVo.setTelephone(projectVisitor.getMobileNo());
                }
                if (visitor == null) {
                    eventVo.setPersonName("未知访客");
                    log.error("未知访客访问 {}", personInfo.getPersonId());
                } else {
                    eventVo.setPersonName(visitor.getVisitorName());
                    //是否存在被访房屋
                    if(StringUtils.isNotEmpty(visitor.getVisitHouseId())){
                        ProjectHouseInfo  houseInfo = projectHouseInfoService.getFirstHouse(null,null,null,visitor.getVisitHouseId());
                        eventVo.setAddrDesc(houseInfo==null?"":houseInfo.getHouseName());
                    }
                }
            }
        } else {
            //已存在personName，不再查询人员名称
            eventVo.setPersonName(personInfo.getPersonName());
            if (personInfo.getPersonTypeEnum() == PersonTypeEnum.PROPRIETOR) {
                ProjectPersonInfo projectPersonInfo = projectPersonInfoService.getById(personInfo.getPersonId());
                if (projectPersonInfo == null) {
//                    eventVo.setPersonName("未知住户");
                    log.error("未知住户访问 {}", personInfo.getPersonId());
                } else {
                    ProjectHouseInfo houseInfo = null;
//                    eventVo.setPersonName(projectPersonInfo.getPersonName());
                    //为区口
                    if(DeviceTypeEnum.getByCode(deviceInfo.getDeviceType()) == DeviceTypeEnum.GATE_DEVICE){
                        houseInfo = projectHouseInfoService.getFirstHouse(personInfo.getPersonId(),null,null,null);
                        //梯口
                    }else if(DeviceTypeEnum.getByCode(deviceInfo.getDeviceType()) == DeviceTypeEnum.LADDER_WAY_DEVICE){
                        houseInfo = projectHouseInfoService.getFirstHouse(personInfo.getPersonId(),deviceInfo.getBuildingId(),deviceInfo.getUnitId(),null);
                    }
                    eventVo.setAddrDesc(houseInfo==null?"":houseInfo.getHouseName());
                }
            } else if (personInfo.getPersonTypeEnum() == PersonTypeEnum.STAFF) {

            } else if (personInfo.getPersonTypeEnum() == PersonTypeEnum.VISITOR) {
                ProjectVisitorHis visitor = projectVisitorHisService.getById(personInfo.getPersonId());
                if (visitor == null) {
//                    eventVo.setPersonName("未知访客");
                    if(personInfo.getPersonId() == null){
                        eventVo.setAddrDesc(eventDeviceGatePassDTO.getAddrDesc());
                    }
                    log.error("未知访客访问 {}", personInfo.getPersonId());
                } else {
//                    eventVo.setPersonName(visitor.getVisitorName());
                    //是否存在被访房屋
                    if(StringUtils.isNotEmpty(visitor.getVisitHouseId())){
                        ProjectHouseInfo  houseInfo = projectHouseInfoService.getFirstHouse(null,null,null,visitor.getVisitHouseId());
                        eventVo.setAddrDesc(houseInfo==null?"":houseInfo.getHouseName());
                    }
                }
            }
        }
        if(StrUtil.isEmpty(eventVo.getAddrDesc())){
            eventVo.setAddrDesc(eventDeviceGatePassDTO.getAddrDesc());
        }
        eventVo.setDeviceId(deviceInfo.getDeviceId());
        eventVo.setDeviceName(deviceInfo.getDeviceName());
        eventVo.setEventTime(eventDeviceGatePassDTO.getEventTime());
        eventVo.setEventDesc(eventDeviceGatePassDTO.getDesc());
        eventVo.setPersonId(personInfo.getPersonId());//根据不同的情况，可以是房间号人员id等
        eventVo.setPersonType(personInfo.getPersonTypeEnum().code);
        String pirUrl = imgConvertUtil.convertToLocalUrl(eventDeviceGatePassDTO.getImgUrl());
        eventVo.setPicUrl(pirUrl);
        eventVo.setSmallPicUrl(pirUrl);
        eventVo.setOpenType(eventDeviceGatePassDTO.getOpenMode());
        eventVo.setUserId(eventDeviceGatePassDTO.getUserId());
        eventVo.setExtStr(eventDeviceGatePassDTO.getExtStr());

        eventVo.setEventType("1");//通行事件
        eventVo.setEventStatus("0");//未处理
        eventVo.setCertMedia(eventDeviceGatePassDTO.getCertMediaType());

        eventVo.setTenantId(deviceInfo.getTenantId());
        eventVo.setProjectId(deviceInfo.getProjectId());

        return eventVo;
    }

    /**
     * 通过凭证获取人员类型与人员ID
     * 通行凭证如果不带有第三方id，则通过凭证值进行查询
     *
     * @param eventDeviceGatePassDTO
     * @return
     */
    @Override
    public PersonInfo getPersonTypeAndPersonId(EventDeviceGatePassDTO eventDeviceGatePassDTO, Integer projectId) {

        //位置用户类型，通过凭证判断
        String certMediaType = eventDeviceGatePassDTO.getCertMediaType();
        String certMediaCode = eventDeviceGatePassDTO.getCertMediaCode();//第三方id
        String certMediaValue = eventDeviceGatePassDTO.getCertMediaValue();//凭证值

        PersonInfo personInfo = null;

        //非云系统管控的凭证通行
        if (certMediaType == null) {

            if (StringUtils.isNotEmpty(eventDeviceGatePassDTO.getPersonType()) && StringUtils.isNotEmpty(eventDeviceGatePassDTO.getPersonCode())) {
                personInfo = new PersonInfo(PersonTypeEnum.getEnum(eventDeviceGatePassDTO.getPersonType()), eventDeviceGatePassDTO.getPersonCode());
                personInfo.setPersonName(eventDeviceGatePassDTO.getPersonName());
            } else if (StringUtils.isNotEmpty(eventDeviceGatePassDTO.getPersonType()) && StringUtils.isNotEmpty(eventDeviceGatePassDTO.getPersonName())) {
                personInfo = new PersonInfo(PersonTypeEnum.getEnum(eventDeviceGatePassDTO.getPersonType()), eventDeviceGatePassDTO.getPersonCode());
                personInfo.setPersonName(eventDeviceGatePassDTO.getPersonName());
            }

        } else {

            //卡片
            if (certMediaType.equals(CertmediaTypeEnum.Card.code)) {

                ProjectCard card = null;
                if (StringUtils.isNotEmpty(certMediaCode)) {
                    card = projectCardService.getByCode(certMediaCode, projectId);
                } else {
                    card = projectCardService.getByCardNo(certMediaValue, projectId);
                }

                if (card != null) {
                    personInfo = new PersonInfo(PersonTypeEnum.getEnum(card.getPersonType()), card.getPersonId());
                }

                //面部通行
            } else if (certMediaType.equals(CertmediaTypeEnum.Face.code)) {

                ProjectFaceResources face = null;
                if (StringUtils.isNotEmpty(certMediaCode)) {
                    face = projectFaceResourcesService.getByCode(certMediaCode, projectId);
                } else {
                    face = projectFaceResourcesService.getByFaceId(certMediaValue, projectId);
                }

                if (face != null) {
                    personInfo = new PersonInfo(PersonTypeEnum.getEnum(face.getPersonType()), face.getPersonId());
                }

                //密码通行
            } else if (certMediaType.equals(CertmediaTypeEnum.Password.code)) {

                ProjectPasswd passwd = projectPasswdService.getByCode(certMediaCode, projectId);
                if (passwd != null) {
                    personInfo = new PersonInfo(PersonTypeEnum.getEnum(passwd.getPersonType()), passwd.getPersonId());
                }

                //指纹通行
            } else if (certMediaType.equals(CertmediaTypeEnum.Finger.code)) {

                ProjectFingerprints fingerprints = projectFingerprintsService.getByCode(certMediaCode, projectId);
                if (fingerprints != null) {
                    personInfo = new PersonInfo(PersonTypeEnum.getEnum(fingerprints.getPersonType()), fingerprints.getPersonId());
                }
            }
            if(personInfo == null){
                ProjectRightDevice rightDevice = null;
                if (StringUtils.isNotEmpty(certMediaCode)) {
                    rightDevice = projectRightDeviceService.getOne(Wrappers.lambdaQuery(ProjectRightDevice.class)
                            .eq(ProjectRightDevice::getCertMediaCode,certMediaCode)
                            .last("LIMIT 1"));
                } else {
                    rightDevice = projectRightDeviceService.getOne(Wrappers.lambdaQuery(ProjectRightDevice.class)
                            .eq(ProjectRightDevice::getCertMediaId,certMediaValue)
                            .last("LIMIT 1"));
                }
                if(rightDevice != null){
                    personInfo = new PersonInfo(PersonTypeEnum.getEnum(rightDevice.getPersonType()), rightDevice.getPersonId());
                }
            }
        }


        return personInfo;
    }

//    /**
//     * 人员类型与ID信息
//     */
//    @Data
//    class PersonInfo {
//        private PersonTypeEnum personTypeEnum;
//        private String personId;
//        private String personName;
//
//        public PersonInfo(PersonTypeEnum personTypeEnum, String personId, String personName) {
//            this.personTypeEnum = personTypeEnum;
//            this.personId = personId;
//            this.personId = personId;
//            this.personName = personName;
//        }
//
//        public PersonInfo(PersonTypeEnum personTypeEnum, String personId) {
//            this.personTypeEnum = personTypeEnum;
//            this.personId = personId;
//            this.personId = personId;
//            this.personName = null;
//        }
//    }

}
