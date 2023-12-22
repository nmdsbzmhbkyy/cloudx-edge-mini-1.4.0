package com.aurine.cloudx.estate.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.aurine.cloudx.common.core.constant.DeviceTypeConstants;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.config.LiftCardConfig;
import com.aurine.cloudx.estate.constant.enums.LiftCardModelEnum;
import com.aurine.cloudx.estate.constant.enums.LiftCardTypeEnum;
import com.aurine.cloudx.estate.dto.ProjectHouseDTO;
import com.aurine.cloudx.estate.entity.ProjectCardIssueRecord;
import com.aurine.cloudx.estate.entity.ProjectPersonInfo;
import com.aurine.cloudx.estate.service.*;
import com.aurine.cloudx.estate.util.CodecUtils;
import com.aurine.cloudx.estate.util.DataChangeUtil;
import com.aurine.cloudx.estate.util.MK_S0xEncryptUtil;
import com.aurine.cloudx.estate.util.NumberUtil;
import com.aurine.cloudx.estate.util.security.CRCUtil;
import com.aurine.cloudx.estate.vo.*;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.primitives.Bytes;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

/**
 * @Auther: hjj
 * @Date: 2022/3/31 15:37
 * @Description: 电梯卡管理
 */
@Service
@AllArgsConstructor
public class ProjectLiftCardServiceImpl implements ProjectLiftCardService {
    private static final String GB2312 = "GB2312";
    private static final String ASCII = "ASCII";
    private static final String ISO_8859_1 = "ISO-8859-1";
    private static final String DEFAULT_PROJECT_SECRET = "6F2C6D095D9E";
    @Resource
    LiftCardConfig liftCardConfig;
    @Resource
    ProjectPersonInfoService projectPersonInfoService;
    @Resource
    ProjectStaffService projectStaffService;
    @Resource
    private ProjectHousePersonRelService projectHousePersonRelService;
    @Resource
    private ProjectDeviceRelService projectDeviceRelService;
    @Resource
    private ProjectDeviceAttrService projectDeviceAttrService;
    @Resource
    private ProjectCardIssueRecordService projectCardIssueRecordService;


    @Override
    public String getAbilityCardHex(Integer cardType, Object object) {

        return null;
    }

    @Override
    public LiftCardVo getProprietorCardHex(Integer cardType, LiftProprietorCardVo liftProprietorCardVo) {
        //住户信息
        ProjectPersonInfo projectPersonInfo = projectPersonInfoService.getById(liftProprietorCardVo.getPersonId());
        if (projectPersonInfo == null) {
            return null;
        }
        liftProprietorCardVo.setPersonName(projectPersonInfo.getPersonName());
        liftProprietorCardVo.setUserId(projectPersonInfo.getUserId());
        liftProprietorCardVo.setMobile(projectPersonInfo.getTelephone());
        //召梯方式默认为手动
        if (liftProprietorCardVo.getCallLiftType() == null) {
            liftProprietorCardVo.setCallLiftType(0);
        }
        List<ProjectDeviceLiftVo> liftList = liftProprietorCardVo.getLiftList();
        if (liftList.size() > 1) {
            liftProprietorCardVo.setCardLiftRel(1);
        } else {
            liftProprietorCardVo.setCardLiftRel(0);
        }
        if (liftProprietorCardVo.getPrerogative() == null) {
            liftProprietorCardVo.setPrerogative(0);
        }
        //设置默认开始时段
        if (StringUtils.isBlank(liftProprietorCardVo.getStartTime())) {
            liftProprietorCardVo.setStartTime("00:00");
        }
        //设置默认结束时段
        if (StringUtils.isBlank(liftProprietorCardVo.getEndTime())) {
            liftProprietorCardVo.setEndTime("23:59");
        }
        //默认为一整周
        if (liftProprietorCardVo.getDays() == null) {
            List<String> days = new ArrayList<>();
            days.add("1");
            days.add("2");
            days.add("3");
            days.add("4");
            days.add("5");
            days.add("6");
            days.add("7");
            liftProprietorCardVo.setDays(days);
        }

        //读取电梯下分层器的编号
        for (ProjectDeviceLiftVo projectDeviceLiftVo :
                liftList) {
            List<String> liftNos = new ArrayList<>();
            List<ProjectDeviceRelVo> deviceRelVoList = projectDeviceRelService.ListByDeviceId(projectDeviceLiftVo.getDeviceId());
            if (deviceRelVoList != null) {
                for (ProjectDeviceRelVo child :
                        deviceRelVoList) {
                    List<ProjectDeviceAttrListVo> attrListVos = projectDeviceAttrService.getDeviceAttrListVo(ProjectContextHolder.getProjectId(), DeviceTypeConstants.ELEVATOR_LAYER_CONTROL_DEVICE, child.getDeviceId());
                    if (attrListVos != null) {
                        for (ProjectDeviceAttrListVo attr :
                                attrListVos) {
                            if (attr.getAttrCode().equals("liftNo")) {
                                if (attr.getAttrValue() != null) {
                                    liftNos.add(attr.getAttrValue());
                                }
                                break;
                            }
                        }
                    }
                }

            }
            projectDeviceLiftVo.setLiftNos(liftNos);
        }

        List<ProjectHouseDTO> houseDTOList = projectHousePersonRelService.listHouseByPersonId(liftProprietorCardVo.getPersonId());

        liftProprietorCardVo.setHouseCode(houseDTOList.get(0).getHouseCode());
        liftProprietorCardVo.setHouseRule("224");
        liftProprietorCardVo.setOpenDoorMode(0);


        LiftCardVo liftCardVo = new LiftCardVo();
        initLiftCard(liftCardVo);
        //时段卡
        if (LiftCardTypeEnum.durationCard.code.equals(cardType)) {
            liftCardVo.setData(getProprietorCardHexWithDuration(liftProprietorCardVo));
            liftCardVo.setBytes(liftCardVo.getData().length() / 2);
            return liftCardVo;
        }
        return liftCardVo;
    }

    @Override
    public LiftCardVo getInitDeviceData() {
        LiftCardVo liftCardVo = new LiftCardVo();
        liftCardVo.setAppid(liftCardConfig.getAppId());
        String timestamp = Calendar.getInstance().getTimeInMillis() / 1000 + "";
        liftCardVo.setTimestamp(timestamp);
        liftCardVo.setType(LiftCardModelEnum.m1.code);
        liftCardVo.setSign(getSign(liftCardConfig.getAppId(), liftCardConfig.getAppSecret(), timestamp).toLowerCase());
        return liftCardVo;
    }

    @Override
    public LiftCardVo getInitRFData() {
        LiftCardVo liftCardVo = new LiftCardVo();
        initLiftCard(liftCardVo);
        return liftCardVo;
    }

    @Override
    public LiftCardVo getReadRFData(Integer type) {
        LiftCardVo liftCardVo = new LiftCardVo();
        initLiftCard(liftCardVo);
        liftCardVo.setBytes(1);
        return liftCardVo;
    }

    @Override
    public LiftCardVo getStaffCardHex(Integer cardType, LiftStaffCardVo staffCardVo) {
        //员工信息
        ProjectStaffVo projectStaffVo = projectStaffService.getStaffAttrById(staffCardVo.getPersonId());
        if (projectStaffVo == null) {
            return null;
        }
        staffCardVo.setPersonName(projectStaffVo.getStaffName());
        staffCardVo.setUserId(projectStaffVo.getUserId());
        staffCardVo.setMobile(projectStaffVo.getMobile());
        //召梯方式默认为手动
        if (staffCardVo.getCallLiftType() == null) {
            staffCardVo.setCallLiftType(0);
        }
        //设置默认开始时段
        if (StringUtils.isBlank(staffCardVo.getStartTime())) {
            staffCardVo.setStartTime("00:00");
        }
        //设置默认结束时段
        if (StringUtils.isBlank(staffCardVo.getEndTime())) {
            staffCardVo.setEndTime("23:59");
        }
        //默认为一整周
        if (staffCardVo.getDays() == null) {
            List<String> days = new ArrayList<>();
            days.add("1");
            days.add("2");
            days.add("3");
            days.add("4");
            days.add("5");
            days.add("6");
            days.add("7");
            staffCardVo.setDays(days);
        }
        LiftCardVo liftCardVo = new LiftCardVo();
        initLiftCard(liftCardVo);
        //管理员卡
        if (LiftCardTypeEnum.staffCard.code.equals(cardType)) {
            liftCardVo.setData(getStaffCardHex(staffCardVo));
            liftCardVo.setBytes(liftCardVo.getData().length() / 2);
            return liftCardVo;
        }
        return liftCardVo;
    }

    @Override
    public LiftCardVo getFuctionCardHex(LiftFunctionCardVo liftFunctionCardVo) {

        //读取电梯下分层器的编号
        if (liftFunctionCardVo.getLiftList() != null) {
            for (ProjectDeviceLiftVo projectDeviceLiftVo :
                    liftFunctionCardVo.getLiftList()) {
                List<String> liftNos = new ArrayList<>();
                List<ProjectDeviceRelVo> deviceRelVoList = projectDeviceRelService.ListByDeviceId(projectDeviceLiftVo.getDeviceId());
                if (deviceRelVoList != null) {
                    for (ProjectDeviceRelVo child :
                            deviceRelVoList) {
                        List<ProjectDeviceAttrListVo> attrListVos = projectDeviceAttrService.getDeviceAttrListVo(ProjectContextHolder.getProjectId(), DeviceTypeConstants.ELEVATOR_LAYER_CONTROL_DEVICE, child.getDeviceId());
                        if (attrListVos != null) {
                            for (ProjectDeviceAttrListVo attr :
                                    attrListVos) {
                                if (attr.getAttrCode().equals("liftNo")) {
                                    if (attr.getAttrValue() != null) {
                                        liftNos.add(attr.getAttrValue());
                                    }
                                    break;
                                }
                            }
                        }
                    }

                }
                projectDeviceLiftVo.setLiftNos(liftNos);
            }
        }


        LiftCardVo liftCardVo = new LiftCardVo();
        initLiftCard(liftCardVo);

        if (LiftCardTypeEnum.shieldRoomCard.code.equals(liftFunctionCardVo.getCardType())) {
            liftCardVo.setData(getShieldRoomCardHex(liftFunctionCardVo));
        } else if (LiftCardTypeEnum.setButtonRespTimeCard.code.equals(liftFunctionCardVo.getCardType())) {
            liftCardVo.setData(getButtonRespTimeCardHex(liftFunctionCardVo));
        } else if (LiftCardTypeEnum.setCtrlClockCard.code.equals(liftFunctionCardVo.getCardType())) {
            liftCardVo.setData(getCtrlClockCardHex(liftFunctionCardVo));
        } else if (LiftCardTypeEnum.readUserDataCard.code.equals(liftFunctionCardVo.getCardType())) {
            liftCardVo.setData(getUserDataCardHex(liftFunctionCardVo));
        } else if (LiftCardTypeEnum.setVisitorButtonRespTimeCard.code.equals(liftFunctionCardVo.getCardType())) {
            liftCardVo.setData(getVisitorButtonRespTimeCardHex(liftFunctionCardVo));
        } else if (LiftCardTypeEnum.setRunDurationCard.code.equals(liftFunctionCardVo.getCardType())) {
            liftCardVo.setData(getRunDurationCardHex(liftFunctionCardVo));
        } else if (LiftCardTypeEnum.setLayerSystemSwitchCard.code.equals(liftFunctionCardVo.getCardType())) {
            liftCardVo.setData(getLayerSystemSwitchCardHex(liftFunctionCardVo));
        } else if (LiftCardTypeEnum.setBatchShieldCard.code.equals(liftFunctionCardVo.getCardType())) {
            liftCardVo.setData(getBatchShieldCardHex(liftFunctionCardVo));
        } else if (LiftCardTypeEnum.setLRDurationCard.code.equals(liftFunctionCardVo.getCardType())) {
            liftCardVo.setData(getLRDurationCardHex(liftFunctionCardVo));
        } else if (LiftCardTypeEnum.setSystemSwitchCard.code.equals(liftFunctionCardVo.getCardType())) {
            liftCardVo.setData(getSystemSwitchCardHex(liftFunctionCardVo));
        } else if (LiftCardTypeEnum.restartCard.code.equals(liftFunctionCardVo.getCardType())) {
            liftCardVo.setData(getRestartCardHex(liftFunctionCardVo));
        } else if (LiftCardTypeEnum.floorDebugCard.code.equals(liftFunctionCardVo.getCardType())) {
            liftCardVo.setData(getFloorDebugCardHex(liftFunctionCardVo));
        } else if (LiftCardTypeEnum.setCodeCard.code.equals(liftFunctionCardVo.getCardType())) {
            liftCardVo.setData(getCodeCardHex(liftFunctionCardVo));
        } else if (LiftCardTypeEnum.setCodeAddCard.code.equals(liftFunctionCardVo.getCardType())) {
            liftCardVo.setData(getCodeAddCardHex(liftFunctionCardVo));
        } else if (LiftCardTypeEnum.setCodeReduceCard.code.equals(liftFunctionCardVo.getCardType())) {
            liftCardVo.setData(getCodeReduceCardHex(liftFunctionCardVo));
        } else if (LiftCardTypeEnum.restoreCard.code.equals(liftFunctionCardVo.getCardType())) {
            liftCardVo.setData(getRestoreCardHex(liftFunctionCardVo));
        } else if (LiftCardTypeEnum.readFaultCodeCard.code.equals(liftFunctionCardVo.getCardType())) {
            liftCardVo.setData(getFaultCodeCardHex(liftFunctionCardVo));
        } else if (LiftCardTypeEnum.readCtrlStatusCard.code.equals(liftFunctionCardVo.getCardType())) {
            liftCardVo.setData(getCtrlStatusCardHex(liftFunctionCardVo));
        } else if (LiftCardTypeEnum.readVersionInfoCard.code.equals(liftFunctionCardVo.getCardType())) {
            liftCardVo.setData(getVersionInfoCardHex(liftFunctionCardVo));
        } else if (LiftCardTypeEnum.setNetworkCard.code.equals(liftFunctionCardVo.getCardType())) {
            if (liftFunctionCardVo.getNetMode().equals("1")) {
                liftCardVo.setKey(getKey(DEFAULT_PROJECT_SECRET, liftCardConfig.getAppSecret()));
            }
            liftFunctionCardVo.setCardEncryptionKey(liftCardConfig.getProjectSecret());
            liftCardVo.setData(getNetworkParamsCardHex(liftFunctionCardVo));
        } else {
            liftCardVo.setData("");
        }
        liftCardVo.setBytes(liftCardVo.getData().length() / 2);
        return liftCardVo;
    }

    @Override
    public Boolean saveCardIssueRecord(LiftFunctionCardVo liftFunctionCardVo) {
        ProjectCardIssueRecord projectCardIssueRecord = BeanUtil.copyProperties(liftFunctionCardVo, ProjectCardIssueRecord.class);
        return projectCardIssueRecordService.saveCardIssueRecord(projectCardIssueRecord);
    }

    @Override
    public Page<ProjectCardIssueRecord> pageCardIssueRecord(Page page, LiftFunctionCardVo liftFunctionCardVo) {
        ProjectCardIssueRecord projectCardIssueRecord = BeanUtil.copyProperties(liftFunctionCardVo, ProjectCardIssueRecord.class);
        return projectCardIssueRecordService.pageCardIssueRecord(page, projectCardIssueRecord);
    }

    private void initLiftCard(LiftCardVo liftCardVo) {
        liftCardVo.setAppid(liftCardConfig.getAppId());
        String timestamp = Calendar.getInstance().getTimeInMillis() / 1000 + "";
        liftCardVo.setTimestamp(timestamp);
        liftCardVo.setType(LiftCardModelEnum.m1.code);
        liftCardVo.setSign(getSign(liftCardConfig.getAppId(), liftCardConfig.getAppSecret(), timestamp).toLowerCase());
        liftCardVo.setKey(getKey(liftCardConfig.getProjectSecret(), liftCardConfig.getAppSecret()));
        liftCardVo.setOffset(1);
    }

    public static String getKey(String projectSecret, String appSecret) {
        return MK_S0xEncryptUtil.bytesToHexFun2(MK_S0xEncryptUtil.xorEncode(MK_S0xEncryptUtil.toBytes(projectSecret), appSecret.getBytes()));
    }


    public static String getSign(String appid, String appSecret, String timestamp) {
        return CodecUtils.MD5(MK_S0xEncryptUtil.xorEncode((appid + timestamp).getBytes(), appSecret));
    }

    //时段卡
    public static String getProprietorCardHexWithDuration(LiftProprietorCardVo liftProprietorCardVo) {
        List<Byte> infoBytes = new ArrayList<>();
        //卡类型 1B
        infoBytes.add(LiftCardTypeEnum.durationCard.code.byteValue());
        System.out.println("卡类型：" + DataChangeUtil.byte2Hex(Bytes.toArray(infoBytes)));
        //用户名 10B
        infoBytes.addAll(getStringByteList(liftProprietorCardVo.getPersonName(), 10, GB2312));
        System.out.println("用户名：" + DataChangeUtil.byte2Hex(Bytes.toArray(getStringByteList(liftProprietorCardVo.getPersonName(), 10, GB2312))));
        //人员ID 4B
        infoBytes.addAll(getNumberByteList(liftProprietorCardVo.getUserId(), 4));
        System.out.println("人员ID：" + DataChangeUtil.byte2Hex(Bytes.toArray(getNumberByteList(liftProprietorCardVo.getUserId(), 4))));
        //手机号 6B
        infoBytes.addAll(getHexByteList(liftProprietorCardVo.getMobile(), 6));
        System.out.println("手机号：" + DataChangeUtil.byte2Hex(Bytes.toArray(getHexByteList(liftProprietorCardVo.getMobile(), 6))));
        //卡参数 2B
        byte cardParam1 = 0;
        cardParam1 |= (byte) (liftProprietorCardVo.getCallLiftType() << 6);
        cardParam1 |= (byte) (liftProprietorCardVo.getCardLiftRel() << 5);
        cardParam1 |= liftProprietorCardVo.getPrerogative().byteValue();
        infoBytes.add(cardParam1);
        System.out.println("卡参数1：" + DataChangeUtil.byte2Hex(new byte[]{cardParam1}));
        byte cardParam2 = 0;
        cardParam2 |= (byte) (liftProprietorCardVo.getLiftList().size() << 5);
        cardParam2 |= liftProprietorCardVo.getPrerogative().byteValue();
        infoBytes.add(cardParam2);
        System.out.println("卡参数2：" + DataChangeUtil.byte2Hex(new byte[]{cardParam2}));
        //允许时段 5B
        infoBytes.addAll(getPeriodByteList(liftProprietorCardVo.getStartTime()));
        System.out.println("开始时段：" + DataChangeUtil.byte2Hex(Bytes.toArray(getPeriodByteList(liftProprietorCardVo.getStartTime()))));
        infoBytes.addAll(getPeriodByteList(liftProprietorCardVo.getEndTime()));
        System.out.println("结束时段：" + DataChangeUtil.byte2Hex(Bytes.toArray(getPeriodByteList(liftProprietorCardVo.getEndTime()))));
        infoBytes.addAll(getWeekDaysByteList(liftProprietorCardVo.getDays()));
        System.out.println("时段周期：" + DataChangeUtil.byte2Hex(Bytes.toArray(getWeekDaysByteList(liftProprietorCardVo.getDays()))));
        //卡有效期 12B
        infoBytes.addAll(getTermOfValidityByteList(liftProprietorCardVo.getEffTime()));
        System.out.println("有效期开始时间：" + DataChangeUtil.byte2Hex(Bytes.toArray(getTermOfValidityByteList(liftProprietorCardVo.getEffTime()))));
        infoBytes.addAll(getTermOfValidityByteList(liftProprietorCardVo.getExpTime()));
        System.out.println("有效期结束时间：" + DataChangeUtil.byte2Hex(Bytes.toArray(getTermOfValidityByteList(liftProprietorCardVo.getExpTime()))));

        Integer buildingLen = Integer.valueOf(liftProprietorCardVo.getHouseRule().substring(0, 1));
        Integer unitLen = Integer.valueOf(liftProprietorCardVo.getHouseRule().substring(1, 2));
        Integer houseLen = Integer.valueOf(liftProprietorCardVo.getHouseRule().substring(2, 3));
        List<ProjectDeviceLiftVo> liftList = liftProprietorCardVo.getLiftList();
        for (ProjectDeviceLiftVo liftVo :
                liftList) {
            //楼栋号 1B
            String bulidingCode = liftProprietorCardVo.getHouseCode().substring(0, buildingLen);
            infoBytes.addAll(getHexByteList(bulidingCode, 1));
            System.out.println("楼栋号：" + DataChangeUtil.byte2Hex(Bytes.toArray(getHexByteList(bulidingCode, 1))));
            //单元号 1B
            String unitCode = liftProprietorCardVo.getHouseCode().substring(buildingLen, buildingLen + unitLen);
            infoBytes.addAll(getHexByteList(unitCode, 1));
            System.out.println("单元号：" + DataChangeUtil.byte2Hex(Bytes.toArray(getHexByteList(unitCode, 1))));
            //房间号 6B
            String houseCode = liftProprietorCardVo.getHouseCode().substring(buildingLen + unitLen, buildingLen + unitLen + houseLen);
            houseCode = String.format("%06d", Integer.valueOf(houseCode));
            infoBytes.addAll(getStringByteList(houseCode, 6, ASCII));
            System.out.println("房间号：" + DataChangeUtil.byte2Hex(Bytes.toArray(getStringByteList(houseCode, 6, ASCII))));
            List<String> liftNos = liftVo.getLiftNos();
            //电梯数量 1B
            infoBytes.add(Integer.valueOf(liftNos.size()).byteValue());
            System.out.println("电梯数量：" + DataChangeUtil.byte2Hex(new byte[]{Integer.valueOf(liftNos.size()).byteValue()}));
            //电梯编号 8B
            infoBytes.addAll(getLiftNoByteList(liftNos));
            System.out.println("电梯编号：" + DataChangeUtil.byte2Hex(Bytes.toArray(getLiftNoByteList(liftNos))));
            //可用楼层 24B
            infoBytes.addAll(getOneDoorFloorByteList(liftVo.getChecked()));
            System.out.println("可用楼层：" + DataChangeUtil.byte2Hex(Bytes.toArray(getOneDoorFloorByteList(liftVo.getChecked()))));
            //单/双开门 1B
            infoBytes.add(liftProprietorCardVo.getOpenDoorMode().byteValue());
            System.out.println("卡类型：" + DataChangeUtil.byte2Hex(new byte[]{liftProprietorCardVo.getOpenDoorMode().byteValue()}));
        }

        //数据长度 2B
        Integer dataLength = infoBytes.size();
        //校验 2B
        byte[] bytes = Bytes.toArray(infoBytes);
        String crc = CRCUtil.CRC16_MODBUS(bytes);
        System.err.println(crc);
        infoBytes.addAll(0, getHexByteList(crc, 2));
        System.out.println("CRC：" + DataChangeUtil.byte2Hex(Bytes.toArray(getHexByteList(crc, 2))));
        infoBytes.addAll(0, Bytes.asList(NumberUtil.unsignedShortToByte2(dataLength)));
        System.out.println("数据长度：" + DataChangeUtil.byte2Hex(NumberUtil.unsignedShortToByte2(dataLength)));
        bytes = Bytes.toArray(infoBytes);
        return DataChangeUtil.byte2Hex(bytes);
    }

    //管理员卡
    public static String getStaffCardHex(LiftStaffCardVo staffCardVo) {
        List<Byte> infoBytes = new ArrayList<>();
        //卡类型 1B
        infoBytes.add(LiftCardTypeEnum.staffCard.code.byteValue());
        System.out.println("卡类型：" + DataChangeUtil.byte2Hex(Bytes.toArray(infoBytes)));
        //用户名 10B
        infoBytes.addAll(getStringByteList(staffCardVo.getPersonName(), 10, GB2312));
        System.out.println("用户名：" + DataChangeUtil.byte2Hex(Bytes.toArray(getStringByteList(staffCardVo.getPersonName(), 10, GB2312))));
        //人员ID 4B
        infoBytes.addAll(getNumberByteList(staffCardVo.getUserId(), 4));
        System.out.println("人员ID：" + DataChangeUtil.byte2Hex(Bytes.toArray(getNumberByteList(staffCardVo.getUserId(), 4))));
        //手机号 6B
        infoBytes.addAll(getHexByteList(staffCardVo.getMobile(), 6));
        System.out.println("手机号：" + DataChangeUtil.byte2Hex(Bytes.toArray(getHexByteList(staffCardVo.getMobile(), 6))));
        //卡参数 1B
        byte cardParam1 = 0;
        cardParam1 |= (byte) (staffCardVo.getCallLiftType() << 6);
        infoBytes.add(cardParam1);
        System.out.println("卡参数：" + DataChangeUtil.byte2Hex(new byte[]{cardParam1}));
        //可用楼层 24B
        infoBytes.addAll(getOneDoorFloorByteList(staffCardVo.getFloors()));
        System.out.println("可用楼层：" + DataChangeUtil.byte2Hex(Bytes.toArray(getOneDoorFloorByteList(staffCardVo.getFloors()))));
        //允许时段 5B
        infoBytes.addAll(getPeriodByteList(staffCardVo.getStartTime()));
        System.out.println("开始时段：" + DataChangeUtil.byte2Hex(Bytes.toArray(getPeriodByteList(staffCardVo.getStartTime()))));
        infoBytes.addAll(getPeriodByteList(staffCardVo.getEndTime()));
        System.out.println("结束时段：" + DataChangeUtil.byte2Hex(Bytes.toArray(getPeriodByteList(staffCardVo.getEndTime()))));
        infoBytes.addAll(getWeekDaysByteList(staffCardVo.getDays()));
        System.out.println("时段周期：" + DataChangeUtil.byte2Hex(Bytes.toArray(getWeekDaysByteList(staffCardVo.getDays()))));
        //卡有效期 12B
        infoBytes.addAll(getTermOfValidityByteList(staffCardVo.getEffTime()));
        System.out.println("有效期开始时间：" + DataChangeUtil.byte2Hex(Bytes.toArray(getTermOfValidityByteList(staffCardVo.getEffTime()))));
        infoBytes.addAll(getTermOfValidityByteList(staffCardVo.getExpTime()));
        System.out.println("有效期结束时间：" + DataChangeUtil.byte2Hex(Bytes.toArray(getTermOfValidityByteList(staffCardVo.getExpTime()))));
        //数据长度 2B
        Integer dataLength = infoBytes.size();
        //校验 2B
        byte[] bytes = Bytes.toArray(infoBytes);
        String crc = CRCUtil.CRC16_MODBUS(bytes);
        System.err.println(crc);
        infoBytes.addAll(0, getHexByteList(crc, 2));
        System.out.println("CRC：" + DataChangeUtil.byte2Hex(Bytes.toArray(getHexByteList(crc, 2))));
        infoBytes.addAll(0, Bytes.asList(NumberUtil.unsignedShortToByte2(dataLength)));
        System.out.println("数据长度：" + DataChangeUtil.byte2Hex(NumberUtil.unsignedShortToByte2(dataLength)));
        bytes = Bytes.toArray(infoBytes);
        return DataChangeUtil.byte2Hex(bytes);
    }

    //屏蔽房间卡
    public static String getShieldRoomCardHex(LiftFunctionCardVo functionCardVo) {
        List<Byte> infoBytes = new ArrayList<>();
        //卡类型 1B
        infoBytes.add(LiftCardTypeEnum.shieldRoomCard.code.byteValue());
        System.out.println("卡类型：" + DataChangeUtil.byte2Hex(Bytes.toArray(infoBytes)));
        List<ProjectDeviceLiftVo> liftList = functionCardVo.getLiftList();
        for (ProjectDeviceLiftVo liftVo :
                liftList) {
            List<String> liftNos = liftVo.getLiftNos();
            //电梯数量 1B
            infoBytes.add(Integer.valueOf(liftNos.size()).byteValue());
            System.out.println("电梯数量：" + DataChangeUtil.byte2Hex(new byte[]{Integer.valueOf(liftNos.size()).byteValue()}));
            //电梯编号 8B
            infoBytes.addAll(getLiftNoByteList(liftNos));
            System.out.println("电梯编号：" + DataChangeUtil.byte2Hex(Bytes.toArray(getLiftNoByteList(liftNos))));
        }
        List<ShieldRoomVo> shieldRoomList = functionCardVo.getShieldRoomList();
        Integer size = 0;
        if (shieldRoomList != null) {
            size = shieldRoomList.size();
            for (ShieldRoomVo room :
                    shieldRoomList) {
                //卡类型 1B
                infoBytes.add(room.getShield().byteValue());
                System.out.println("卡类型：" + DataChangeUtil.byte2Hex(new byte[]{room.getShield().byteValue()}));
                //房间编号
                infoBytes.addAll(getStringByteList(room.getRoomCode(), 6, ASCII));
                System.out.println("房间号：" + DataChangeUtil.byte2Hex(Bytes.toArray(getStringByteList(room.getRoomCode(), 6, ASCII))));
            }
        }
        if (size < 10) {
            infoBytes.addAll(Bytes.asList(new byte[(10 - size) * 7]));
        }
        //数据长度 2B
        Integer dataLength = infoBytes.size();
        //校验 2B
        byte[] bytes = Bytes.toArray(infoBytes);
        String crc = CRCUtil.CRC16_MODBUS(bytes);
        System.err.println(crc);
        infoBytes.addAll(0, getHexByteList(crc, 2));
        System.out.println("CRC：" + DataChangeUtil.byte2Hex(Bytes.toArray(getHexByteList(crc, 2))));
        infoBytes.addAll(0, Bytes.asList(NumberUtil.unsignedShortToByte2(dataLength)));
        System.out.println("数据长度：" + DataChangeUtil.byte2Hex(NumberUtil.unsignedShortToByte2(dataLength)));
        bytes = Bytes.toArray(infoBytes);
        return DataChangeUtil.byte2Hex(bytes);
    }

    //设定按钮响应时间卡
    public static String getButtonRespTimeCardHex(LiftFunctionCardVo functionCardVo) {
        List<Byte> infoBytes = new ArrayList<>();
        //卡类型 1B
        infoBytes.add(LiftCardTypeEnum.setButtonRespTimeCard.code.byteValue());
        System.out.println("卡类型：" + DataChangeUtil.byte2Hex(Bytes.toArray(infoBytes)));

        //手动召梯响应时间  2B
        infoBytes.addAll(Bytes.asList(NumberUtil.unsignedShortToByte2(functionCardVo.getManualCallTime())));
        System.out.println("手动召梯响应时间：" + DataChangeUtil.byte2Hex(NumberUtil.unsignedShortToByte2(functionCardVo.getManualCallTime())));
        //自动召梯自动按键触发间隔时间  2B
        //autoCallTime默认为0
        if (functionCardVo.getAutoCallTime() == null) {
            functionCardVo.setAutoCallTime(0);
        }
        infoBytes.addAll(Bytes.asList(NumberUtil.unsignedShortToByte2(functionCardVo.getAutoCallTime())));
        System.out.println("自动召梯自动按键触发间隔时间：" + DataChangeUtil.byte2Hex(NumberUtil.unsignedShortToByte2(functionCardVo.getAutoCallTime())));
        //数据长度 2B
        Integer dataLength = infoBytes.size();
        //校验 2B
        byte[] bytes = Bytes.toArray(infoBytes);
        String crc = CRCUtil.CRC16_MODBUS(bytes);
        System.err.println(crc);
        infoBytes.addAll(0, getHexByteList(crc, 2));
        System.out.println("CRC：" + DataChangeUtil.byte2Hex(Bytes.toArray(getHexByteList(crc, 2))));
        infoBytes.addAll(0, Bytes.asList(NumberUtil.unsignedShortToByte2(dataLength)));
        System.out.println("数据长度：" + DataChangeUtil.byte2Hex(NumberUtil.unsignedShortToByte2(dataLength)));
        bytes = Bytes.toArray(infoBytes);
        return DataChangeUtil.byte2Hex(bytes);
    }

    //设定控制器时钟卡
    public static String getCtrlClockCardHex(LiftFunctionCardVo functionCardVo) {
        List<Byte> infoBytes = new ArrayList<>();
        //卡类型 1B
        infoBytes.add(LiftCardTypeEnum.setCtrlClockCard.code.byteValue());
        System.out.println("卡类型：" + DataChangeUtil.byte2Hex(Bytes.toArray(infoBytes)));
        //默认为未使用
        infoBytes.add((byte) 0);
        //时间
        infoBytes.addAll(getTermOfValidityByteList(functionCardVo.getDateTime()));
        System.out.println("时间：" + DataChangeUtil.byte2Hex(Bytes.toArray(getDateByteListWithDayOfWeek(functionCardVo.getDateTime()))));
        //数据长度 2B
        Integer dataLength = infoBytes.size();
        //校验 2B
        byte[] bytes = Bytes.toArray(infoBytes);
        String crc = CRCUtil.CRC16_MODBUS(bytes);
        System.err.println(crc);
        infoBytes.addAll(0, getHexByteList(crc, 2));
        System.out.println("CRC：" + DataChangeUtil.byte2Hex(Bytes.toArray(getHexByteList(crc, 2))));
        infoBytes.addAll(0, Bytes.asList(NumberUtil.unsignedShortToByte2(dataLength)));
        System.out.println("数据长度：" + DataChangeUtil.byte2Hex(NumberUtil.unsignedShortToByte2(dataLength)));
        bytes = Bytes.toArray(infoBytes);
        return DataChangeUtil.byte2Hex(bytes);
    }

    //读取用户数据卡
    public static String getUserDataCardHex(LiftFunctionCardVo functionCardVo) {
        List<Byte> infoBytes = new ArrayList<>();
        //卡类型 1B
        infoBytes.add(LiftCardTypeEnum.readUserDataCard.code.byteValue());
        System.out.println("卡类型：" + DataChangeUtil.byte2Hex(Bytes.toArray(infoBytes)));
        //起始记录编号 2B
        infoBytes.addAll(Bytes.asList(NumberUtil.unsignedShortToByte2(functionCardVo.getStartNum())));
        System.out.println("起始记录编号：" + DataChangeUtil.byte2Hex(NumberUtil.unsignedShortToByte2(functionCardVo.getStartNum())));
        //读取记录数量 1B
        infoBytes.add(Integer.valueOf(functionCardVo.getUserDataNum()).byteValue());
        System.out.println("读取记录数量：" + DataChangeUtil.byte2Hex(new byte[]{Integer.valueOf(functionCardVo.getUserDataNum()).byteValue()}));
        //数据长度 2B
        Integer dataLength = infoBytes.size();
        //校验 2B
        byte[] bytes = Bytes.toArray(infoBytes);
        String crc = CRCUtil.CRC16_MODBUS(bytes);
        System.err.println(crc);
        infoBytes.addAll(0, getHexByteList(crc, 2));
        System.out.println("CRC：" + DataChangeUtil.byte2Hex(Bytes.toArray(getHexByteList(crc, 2))));
        infoBytes.addAll(0, Bytes.asList(NumberUtil.unsignedShortToByte2(dataLength)));
        System.out.println("数据长度：" + DataChangeUtil.byte2Hex(NumberUtil.unsignedShortToByte2(dataLength)));
        bytes = Bytes.toArray(infoBytes);
        return DataChangeUtil.byte2Hex(bytes);
    }

    //访客按钮响应时间卡
    public static String getVisitorButtonRespTimeCardHex(LiftFunctionCardVo functionCardVo) {
        List<Byte> infoBytes = new ArrayList<>();
        //卡类型 1B
        infoBytes.add(LiftCardTypeEnum.setVisitorButtonRespTimeCard.code.byteValue());
        System.out.println("卡类型：" + DataChangeUtil.byte2Hex(Bytes.toArray(infoBytes)));
        //响应时间 2B
        infoBytes.addAll(Bytes.asList(NumberUtil.unsignedShortToByte2(functionCardVo.getVisitRespTime())));
        System.out.println("响应时间：" + DataChangeUtil.byte2Hex(NumberUtil.unsignedShortToByte2(functionCardVo.getVisitRespTime())));
        //数据长度 2B
        Integer dataLength = infoBytes.size();
        //校验 2B
        byte[] bytes = Bytes.toArray(infoBytes);
        String crc = CRCUtil.CRC16_MODBUS(bytes);
        System.err.println(crc);
        infoBytes.addAll(0, getHexByteList(crc, 2));
        System.out.println("CRC：" + DataChangeUtil.byte2Hex(Bytes.toArray(getHexByteList(crc, 2))));
        infoBytes.addAll(0, Bytes.asList(NumberUtil.unsignedShortToByte2(dataLength)));
        System.out.println("数据长度：" + DataChangeUtil.byte2Hex(NumberUtil.unsignedShortToByte2(dataLength)));
        bytes = Bytes.toArray(infoBytes);
        return DataChangeUtil.byte2Hex(bytes);
    }

    //电梯运行时段卡 TODO
    public static String getRunDurationCardHex(LiftFunctionCardVo functionCardVo) {
        List<Byte> infoBytes = new ArrayList<>();
        //卡类型 1B
        infoBytes.add(LiftCardTypeEnum.setRunDurationCard.code.byteValue());
        System.out.println("卡类型：" + DataChangeUtil.byte2Hex(Bytes.toArray(infoBytes)));
        List<ProjectDeviceLiftVo> liftList = functionCardVo.getLiftList();
        for (ProjectDeviceLiftVo liftVo :
                liftList) {
            List<String> liftNos = liftVo.getLiftNos();
            //电梯数量 1B
            infoBytes.add(Integer.valueOf(liftNos.size()).byteValue());
            System.out.println("电梯数量：" + DataChangeUtil.byte2Hex(new byte[]{Integer.valueOf(liftNos.size()).byteValue()}));
            //电梯编号 8B
            infoBytes.addAll(getLiftNoByteList(liftNos));
            System.out.println("电梯编号：" + DataChangeUtil.byte2Hex(Bytes.toArray(getLiftNoByteList(liftNos))));
        }
        List<LiftDurationVo> durationObj = functionCardVo.getDurationObj();
        //时段个数 1B
        infoBytes.add(Integer.valueOf(durationObj.size()).byteValue());
        System.out.println("时段个数：" + DataChangeUtil.byte2Hex(new byte[]{Integer.valueOf(durationObj.size()).byteValue()}));
        Integer durationNo = 1;
        for (LiftDurationVo liftDurationVo :
                durationObj) {
            //时段1参数 1B
            byte cardParam1 = 0;
            cardParam1 |= (byte) (liftDurationVo.getDurationSwitch() << 7);
            cardParam1 |= (byte) (liftDurationVo.getDurationType() << 6);
            cardParam1 |= durationNo.byteValue();
            infoBytes.add(cardParam1);
            System.out.println("时段1参数：" + DataChangeUtil.byte2Hex(new byte[]{cardParam1}));
            //可用楼层 24B
            infoBytes.addAll(getOneDoorFloorByteList(liftDurationVo.getCheckedFloors()));
            System.out.println("可用楼层：" + DataChangeUtil.byte2Hex(Bytes.toArray(getOneDoorFloorByteList(liftDurationVo.getCheckedFloors()))));
            //允许时段 5B
            infoBytes.addAll(getPeriodByteList(liftDurationVo.getStartTime()));
            System.out.println("开始时段：" + DataChangeUtil.byte2Hex(Bytes.toArray(getPeriodByteList(liftDurationVo.getStartTime()))));
            infoBytes.addAll(getPeriodByteList(liftDurationVo.getEndTime()));
            System.out.println("结束时段：" + DataChangeUtil.byte2Hex(Bytes.toArray(getPeriodByteList(liftDurationVo.getEndTime()))));
            infoBytes.addAll(getWeekDaysByteList(liftDurationVo.getCheckedDays()));
            System.out.println("时段周期：" + DataChangeUtil.byte2Hex(Bytes.toArray(getWeekDaysByteList(liftDurationVo.getCheckedDays()))));
            //卡有效期 8B
            infoBytes.addAll(getTermOfValidityByteList4B(liftDurationVo.getEffTime()));
            System.out.println("有效期开始时间：" + DataChangeUtil.byte2Hex(Bytes.toArray(getTermOfValidityByteList4B(liftDurationVo.getEffTime()))));
            infoBytes.addAll(getTermOfValidityByteList4B(liftDurationVo.getExpTime()));
            System.out.println("有效期结束时间：" + DataChangeUtil.byte2Hex(Bytes.toArray(getTermOfValidityByteList4B(liftDurationVo.getExpTime()))));
            durationNo++;
        }
        if (durationObj.size() < 4) {
            infoBytes.addAll(Bytes.asList(new byte[(4 - durationObj.size()) * 13]));
        }
        //数据长度 2B
        Integer dataLength = infoBytes.size();
        //校验 2B
        byte[] bytes = Bytes.toArray(infoBytes);
        String crc = CRCUtil.CRC16_MODBUS(bytes);
        System.err.println(crc);
        infoBytes.addAll(0, getHexByteList(crc, 2));
        System.out.println("CRC：" + DataChangeUtil.byte2Hex(Bytes.toArray(getHexByteList(crc, 2))));
        infoBytes.addAll(0, Bytes.asList(NumberUtil.unsignedShortToByte2(dataLength)));
        System.out.println("数据长度：" + DataChangeUtil.byte2Hex(NumberUtil.unsignedShortToByte2(dataLength)));
        bytes = Bytes.toArray(infoBytes);
        return DataChangeUtil.byte2Hex(bytes);
    }

    //分层系统开关卡
    public static String getLayerSystemSwitchCardHex(LiftFunctionCardVo functionCardVo) {
        List<Byte> infoBytes = new ArrayList<>();
        //卡类型 1B
        infoBytes.add(LiftCardTypeEnum.setLayerSystemSwitchCard.code.byteValue());
        System.out.println("卡类型：" + DataChangeUtil.byte2Hex(Bytes.toArray(infoBytes)));
        List<ProjectDeviceLiftVo> liftList = functionCardVo.getLiftList();
        for (ProjectDeviceLiftVo liftVo :
                liftList) {
            List<String> liftNos = liftVo.getLiftNos();
            //电梯数量 1B
            infoBytes.add(Integer.valueOf(liftNos.size()).byteValue());
            System.out.println("电梯数量：" + DataChangeUtil.byte2Hex(new byte[]{Integer.valueOf(liftNos.size()).byteValue()}));
            //电梯编号 8B
            infoBytes.addAll(getLiftNoByteList(liftNos));
            System.out.println("电梯编号：" + DataChangeUtil.byte2Hex(Bytes.toArray(getLiftNoByteList(liftNos))));
        }
        //可用楼层 24B
        infoBytes.addAll(getOneDoorFloorByteList(functionCardVo.getFloors()));
        System.out.println("可用楼层：" + DataChangeUtil.byte2Hex(Bytes.toArray(getOneDoorFloorByteList(functionCardVo.getFloors()))));
        //数据长度 2B
        Integer dataLength = infoBytes.size();
        //校验 2B
        byte[] bytes = Bytes.toArray(infoBytes);
        String crc = CRCUtil.CRC16_MODBUS(bytes);
        System.err.println(crc);
        infoBytes.addAll(0, getHexByteList(crc, 2));
        System.out.println("CRC：" + DataChangeUtil.byte2Hex(Bytes.toArray(getHexByteList(crc, 2))));
        infoBytes.addAll(0, Bytes.asList(NumberUtil.unsignedShortToByte2(dataLength)));
        System.out.println("数据长度：" + DataChangeUtil.byte2Hex(NumberUtil.unsignedShortToByte2(dataLength)));
        bytes = Bytes.toArray(infoBytes);
        return DataChangeUtil.byte2Hex(bytes);
    }

    //批量屏蔽卡
    public static String getBatchShieldCardHex(LiftFunctionCardVo functionCardVo) {
        List<Byte> infoBytes = new ArrayList<>();
        //卡类型 1B
        infoBytes.add(LiftCardTypeEnum.setBatchShieldCard.code.byteValue());
        System.out.println("卡类型：" + DataChangeUtil.byte2Hex(Bytes.toArray(infoBytes)));
        List<ProjectDeviceLiftVo> liftList = functionCardVo.getLiftList();
        for (ProjectDeviceLiftVo liftVo :
                liftList) {
            List<String> liftNos = liftVo.getLiftNos();
            //电梯数量 1B
            infoBytes.add(Integer.valueOf(liftNos.size()).byteValue());
            System.out.println("电梯数量：" + DataChangeUtil.byte2Hex(new byte[]{Integer.valueOf(liftNos.size()).byteValue()}));
            //电梯编号 8B
            infoBytes.addAll(getLiftNoByteList(liftNos));
            System.out.println("电梯编号：" + DataChangeUtil.byte2Hex(Bytes.toArray(getLiftNoByteList(liftNos))));
        }
        //屏蔽/取消屏蔽 1B
        infoBytes.add(Integer.valueOf(functionCardVo.getShield()).byteValue());
        System.out.println("屏蔽/取消屏蔽：" + DataChangeUtil.byte2Hex(new byte[]{Integer.valueOf(functionCardVo.getShield()).byteValue()}));
        List<ShieldCardVo> shieldCardList = functionCardVo.getShieldCardList();
        Integer size = 0;
        if (shieldCardList != null) {
            size = shieldCardList.size();
            //屏蔽卡数量 1B
            infoBytes.add(Integer.valueOf(size).byteValue());
            System.out.println("屏蔽卡数量：" + DataChangeUtil.byte2Hex(new byte[]{Integer.valueOf(size).byteValue()}));
            for (ShieldCardVo card :
                    shieldCardList) {
                //卡类型 1B
                infoBytes.add(card.getShieldCardType().byteValue());
                System.out.println("卡类型：" + DataChangeUtil.byte2Hex(new byte[]{card.getShieldCardType().byteValue()}));
                //卡号
                infoBytes.addAll(getHexCardNo(Long.valueOf(card.getShieldCardNo())));
                System.out.println("卡号：" + DataChangeUtil.byte2Hex(Bytes.toArray(getHexCardNo(Long.valueOf(card.getShieldCardNo())))));
            }
        }
        //数据长度 2B
        Integer dataLength = infoBytes.size();
        //校验 2B
        byte[] bytes = Bytes.toArray(infoBytes);
        String crc = CRCUtil.CRC16_MODBUS(bytes);
        System.err.println(crc);
        infoBytes.addAll(0, getHexByteList(crc, 2));
        System.out.println("CRC：" + DataChangeUtil.byte2Hex(Bytes.toArray(getHexByteList(crc, 2))));
        infoBytes.addAll(0, Bytes.asList(NumberUtil.unsignedShortToByte2(dataLength)));
        System.out.println("数据长度：" + DataChangeUtil.byte2Hex(NumberUtil.unsignedShortToByte2(dataLength)));
        bytes = Bytes.toArray(infoBytes);
        return DataChangeUtil.byte2Hex(bytes);
    }

    //设置丽人时段卡
    public static String getLRDurationCardHex(LiftFunctionCardVo functionCardVo) {
        List<Byte> infoBytes = new ArrayList<>();
        //卡类型 1B
        infoBytes.add(LiftCardTypeEnum.setLRDurationCard.code.byteValue());
        System.out.println("卡类型：" + DataChangeUtil.byte2Hex(Bytes.toArray(infoBytes)));
        List<ProjectDeviceLiftVo> liftList = functionCardVo.getLiftList();
        for (ProjectDeviceLiftVo liftVo :
                liftList) {
            List<String> liftNos = liftVo.getLiftNos();
            //电梯数量 1B
            infoBytes.add(Integer.valueOf(liftNos.size()).byteValue());
            System.out.println("电梯数量：" + DataChangeUtil.byte2Hex(new byte[]{Integer.valueOf(liftNos.size()).byteValue()}));
            //电梯编号 8B
            infoBytes.addAll(getLiftNoByteList(liftNos));
            System.out.println("电梯编号：" + DataChangeUtil.byte2Hex(Bytes.toArray(getLiftNoByteList(liftNos))));
        }
        //特定时段 4B
        infoBytes.addAll(getPeriodByteList(functionCardVo.getStartTime()));
        System.out.println("开始时段：" + DataChangeUtil.byte2Hex(Bytes.toArray(getPeriodByteList(functionCardVo.getStartTime()))));
        infoBytes.addAll(getPeriodByteList(functionCardVo.getEndTime()));
        System.out.println("结束时段：" + DataChangeUtil.byte2Hex(Bytes.toArray(getPeriodByteList(functionCardVo.getEndTime()))));
        //时段开关 1B
        infoBytes.add(Integer.valueOf(functionCardVo.getDurationSwitch()).byteValue());
        System.out.println("时段开关：" + DataChangeUtil.byte2Hex(new byte[]{Integer.valueOf(functionCardVo.getDurationSwitch()).byteValue()}));
        //数据长度 2B
        Integer dataLength = infoBytes.size();
        //校验 2B
        byte[] bytes = Bytes.toArray(infoBytes);
        String crc = CRCUtil.CRC16_MODBUS(bytes);
        System.err.println(crc);
        infoBytes.addAll(0, getHexByteList(crc, 2));
        System.out.println("CRC：" + DataChangeUtil.byte2Hex(Bytes.toArray(getHexByteList(crc, 2))));
        infoBytes.addAll(0, Bytes.asList(NumberUtil.unsignedShortToByte2(dataLength)));
        System.out.println("数据长度：" + DataChangeUtil.byte2Hex(NumberUtil.unsignedShortToByte2(dataLength)));
        bytes = Bytes.toArray(infoBytes);
        return DataChangeUtil.byte2Hex(bytes);
    }

    //系统开关卡
    public static String getSystemSwitchCardHex(LiftFunctionCardVo functionCardVo) {
        List<Byte> infoBytes = new ArrayList<>();
        //卡类型 1B
        infoBytes.add(LiftCardTypeEnum.setSystemSwitchCard.code.byteValue());
        System.out.println("卡类型：" + DataChangeUtil.byte2Hex(Bytes.toArray(infoBytes)));
        //数据长度 2B
        Integer dataLength = infoBytes.size();
        //校验 2B
        byte[] bytes = Bytes.toArray(infoBytes);
        String crc = CRCUtil.CRC16_MODBUS(bytes);
        System.err.println(crc);
        infoBytes.addAll(0, getHexByteList(crc, 2));
        System.out.println("CRC：" + DataChangeUtil.byte2Hex(Bytes.toArray(getHexByteList(crc, 2))));
        infoBytes.addAll(0, Bytes.asList(NumberUtil.unsignedShortToByte2(dataLength)));
        System.out.println("数据长度：" + DataChangeUtil.byte2Hex(NumberUtil.unsignedShortToByte2(dataLength)));
        bytes = Bytes.toArray(infoBytes);
        return DataChangeUtil.byte2Hex(bytes);
    }

    //重启设备卡
    public static String getRestartCardHex(LiftFunctionCardVo functionCardVo) {
        List<Byte> infoBytes = new ArrayList<>();
        //卡类型 1B
        infoBytes.add(LiftCardTypeEnum.restartCard.code.byteValue());
        System.out.println("卡类型：" + DataChangeUtil.byte2Hex(Bytes.toArray(infoBytes)));
        //数据长度 2B
        Integer dataLength = infoBytes.size();
        //校验 2B
        byte[] bytes = Bytes.toArray(infoBytes);
        String crc = CRCUtil.CRC16_MODBUS(bytes);
        System.err.println(crc);
        infoBytes.addAll(0, getHexByteList(crc, 2));
        System.out.println("CRC：" + DataChangeUtil.byte2Hex(Bytes.toArray(getHexByteList(crc, 2))));
        infoBytes.addAll(0, Bytes.asList(NumberUtil.unsignedShortToByte2(dataLength)));
        System.out.println("数据长度：" + DataChangeUtil.byte2Hex(NumberUtil.unsignedShortToByte2(dataLength)));
        bytes = Bytes.toArray(infoBytes);
        return DataChangeUtil.byte2Hex(bytes);
    }

    //楼层调试卡
    public static String getFloorDebugCardHex(LiftFunctionCardVo functionCardVo) {
        List<Byte> infoBytes = new ArrayList<>();
        //卡类型 1B
        infoBytes.add(LiftCardTypeEnum.floorDebugCard.code.byteValue());
        System.out.println("卡类型：" + DataChangeUtil.byte2Hex(Bytes.toArray(infoBytes)));
        List<ProjectDeviceLiftVo> liftList = functionCardVo.getLiftList();
        for (ProjectDeviceLiftVo liftVo :
                liftList) {
            List<String> liftNos = liftVo.getLiftNos();
            //电梯数量 1B
            infoBytes.add(Integer.valueOf(liftNos.size()).byteValue());
            System.out.println("电梯数量：" + DataChangeUtil.byte2Hex(new byte[]{Integer.valueOf(liftNos.size()).byteValue()}));
            //电梯编号 8B
            infoBytes.addAll(getLiftNoByteListWithSize(liftNos));
            System.out.println("电梯编号：" + DataChangeUtil.byte2Hex(Bytes.toArray(getLiftNoByteListWithSize(liftNos))));
        }
        //数据长度 2B
        Integer dataLength = infoBytes.size();
        //校验 2B
        byte[] bytes = Bytes.toArray(infoBytes);
        String crc = CRCUtil.CRC16_MODBUS(bytes);
        System.err.println(crc);
        infoBytes.addAll(0, getHexByteList(crc, 2));
        System.out.println("CRC：" + DataChangeUtil.byte2Hex(Bytes.toArray(getHexByteList(crc, 2))));
        infoBytes.addAll(0, Bytes.asList(NumberUtil.unsignedShortToByte2(dataLength)));
        System.out.println("数据长度：" + DataChangeUtil.byte2Hex(NumberUtil.unsignedShortToByte2(dataLength)));
        bytes = Bytes.toArray(infoBytes);
        return DataChangeUtil.byte2Hex(bytes);
    }

    //设置电梯编号
    public static String getCodeCardHex(LiftFunctionCardVo functionCardVo) {
        List<Byte> infoBytes = new ArrayList<>();
        //卡类型 1B
        infoBytes.add(LiftCardTypeEnum.setCodeCard.code.byteValue());
        System.out.println("卡类型：" + DataChangeUtil.byte2Hex(Bytes.toArray(infoBytes)));
        //电梯编号 1B
        infoBytes.add(Integer.valueOf(functionCardVo.getLiftNo()).byteValue());
        System.out.println("电梯编号：" + DataChangeUtil.byte2Hex(new byte[]{Integer.valueOf(functionCardVo.getLiftNo()).byteValue()}));
        //数据长度 2B
        Integer dataLength = infoBytes.size();
        //校验 2B
        byte[] bytes = Bytes.toArray(infoBytes);
        String crc = CRCUtil.CRC16_MODBUS(bytes);
        System.err.println(crc);
        infoBytes.addAll(0, getHexByteList(crc, 2));
        System.out.println("CRC：" + DataChangeUtil.byte2Hex(Bytes.toArray(getHexByteList(crc, 2))));
        infoBytes.addAll(0, Bytes.asList(NumberUtil.unsignedShortToByte2(dataLength)));
        System.out.println("数据长度：" + DataChangeUtil.byte2Hex(NumberUtil.unsignedShortToByte2(dataLength)));
        bytes = Bytes.toArray(infoBytes);
        return DataChangeUtil.byte2Hex(bytes);
    }

    //电梯编号加卡
    public static String getCodeAddCardHex(LiftFunctionCardVo functionCardVo) {
        List<Byte> infoBytes = new ArrayList<>();
        //卡类型 1B
        infoBytes.add(LiftCardTypeEnum.setCodeAddCard.code.byteValue());
        System.out.println("卡类型：" + DataChangeUtil.byte2Hex(Bytes.toArray(infoBytes)));
        //数据长度 2B
        Integer dataLength = infoBytes.size();
        //校验 2B
        byte[] bytes = Bytes.toArray(infoBytes);
        String crc = CRCUtil.CRC16_MODBUS(bytes);
        System.err.println(crc);
        infoBytes.addAll(0, getHexByteList(crc, 2));
        System.out.println("CRC：" + DataChangeUtil.byte2Hex(Bytes.toArray(getHexByteList(crc, 2))));
        infoBytes.addAll(0, Bytes.asList(NumberUtil.unsignedShortToByte2(dataLength)));
        System.out.println("数据长度：" + DataChangeUtil.byte2Hex(NumberUtil.unsignedShortToByte2(dataLength)));
        bytes = Bytes.toArray(infoBytes);
        return DataChangeUtil.byte2Hex(bytes);
    }

    //电梯编号减卡
    public static String getCodeReduceCardHex(LiftFunctionCardVo functionCardVo) {
        List<Byte> infoBytes = new ArrayList<>();
        //卡类型 1B
        infoBytes.add(LiftCardTypeEnum.setCodeReduceCard.code.byteValue());
        System.out.println("卡类型：" + DataChangeUtil.byte2Hex(Bytes.toArray(infoBytes)));
        //数据长度 2B
        Integer dataLength = infoBytes.size();
        //校验 2B
        byte[] bytes = Bytes.toArray(infoBytes);
        String crc = CRCUtil.CRC16_MODBUS(bytes);
        System.err.println(crc);
        infoBytes.addAll(0, getHexByteList(crc, 2));
        System.out.println("CRC：" + DataChangeUtil.byte2Hex(Bytes.toArray(getHexByteList(crc, 2))));
        infoBytes.addAll(0, Bytes.asList(NumberUtil.unsignedShortToByte2(dataLength)));
        System.out.println("数据长度：" + DataChangeUtil.byte2Hex(NumberUtil.unsignedShortToByte2(dataLength)));
        bytes = Bytes.toArray(infoBytes);
        return DataChangeUtil.byte2Hex(bytes);
    }

    //恢复出厂卡
    public static String getRestoreCardHex(LiftFunctionCardVo functionCardVo) {
        List<Byte> infoBytes = new ArrayList<>();
        //卡类型 1B
        infoBytes.add(LiftCardTypeEnum.restoreCard.code.byteValue());
        System.out.println("卡类型：" + DataChangeUtil.byte2Hex(Bytes.toArray(infoBytes)));
        List<ProjectDeviceLiftVo> liftList = functionCardVo.getLiftList();
        for (ProjectDeviceLiftVo liftVo :
                liftList) {
            List<String> liftNos = liftVo.getLiftNos();
            //电梯数量 1B
            infoBytes.add(Integer.valueOf(liftNos.size()).byteValue());
            System.out.println("电梯数量：" + DataChangeUtil.byte2Hex(new byte[]{Integer.valueOf(liftNos.size()).byteValue()}));
            //电梯编号 8B
            infoBytes.addAll(getLiftNoByteListWithSize(liftNos));
            System.out.println("电梯编号：" + DataChangeUtil.byte2Hex(Bytes.toArray(getLiftNoByteListWithSize(liftNos))));
        }
        //数据长度 2B
        Integer dataLength = infoBytes.size();
        //校验 2B
        byte[] bytes = Bytes.toArray(infoBytes);
        String crc = CRCUtil.CRC16_MODBUS(bytes);
        System.err.println(crc);
        infoBytes.addAll(0, getHexByteList(crc, 2));
        System.out.println("CRC：" + DataChangeUtil.byte2Hex(Bytes.toArray(getHexByteList(crc, 2))));
        infoBytes.addAll(0, Bytes.asList(NumberUtil.unsignedShortToByte2(dataLength)));
        System.out.println("数据长度：" + DataChangeUtil.byte2Hex(NumberUtil.unsignedShortToByte2(dataLength)));
        bytes = Bytes.toArray(infoBytes);
        return DataChangeUtil.byte2Hex(bytes);
    }

    //读取故障代码卡
    public static String getFaultCodeCardHex(LiftFunctionCardVo functionCardVo) {
        List<Byte> infoBytes = new ArrayList<>();
        //卡类型 1B
        infoBytes.add(LiftCardTypeEnum.readFaultCodeCard.code.byteValue());
        System.out.println("卡类型：" + DataChangeUtil.byte2Hex(Bytes.toArray(infoBytes)));
        //数据长度 2B
        Integer dataLength = infoBytes.size();
        //校验 2B
        byte[] bytes = Bytes.toArray(infoBytes);
        String crc = CRCUtil.CRC16_MODBUS(bytes);
        System.err.println(crc);
        infoBytes.addAll(0, getHexByteList(crc, 2));
        System.out.println("CRC：" + DataChangeUtil.byte2Hex(Bytes.toArray(getHexByteList(crc, 2))));
        infoBytes.addAll(0, Bytes.asList(NumberUtil.unsignedShortToByte2(dataLength)));
        System.out.println("数据长度：" + DataChangeUtil.byte2Hex(NumberUtil.unsignedShortToByte2(dataLength)));
        bytes = Bytes.toArray(infoBytes);
        return DataChangeUtil.byte2Hex(bytes);
    }

    //读取控制器状态
    public static String getCtrlStatusCardHex(LiftFunctionCardVo functionCardVo) {
        List<Byte> infoBytes = new ArrayList<>();
        //卡类型 1B
        infoBytes.add(LiftCardTypeEnum.readCtrlStatusCard.code.byteValue());
        System.out.println("卡类型：" + DataChangeUtil.byte2Hex(Bytes.toArray(infoBytes)));
        //数据长度 2B
        Integer dataLength = infoBytes.size();
        //校验 2B
        byte[] bytes = Bytes.toArray(infoBytes);
        String crc = CRCUtil.CRC16_MODBUS(bytes);
        System.err.println(crc);
        infoBytes.addAll(0, getHexByteList(crc, 2));
        System.out.println("CRC：" + DataChangeUtil.byte2Hex(Bytes.toArray(getHexByteList(crc, 2))));
        infoBytes.addAll(0, Bytes.asList(NumberUtil.unsignedShortToByte2(dataLength)));
        System.out.println("数据长度：" + DataChangeUtil.byte2Hex(NumberUtil.unsignedShortToByte2(dataLength)));
        bytes = Bytes.toArray(infoBytes);
        return DataChangeUtil.byte2Hex(bytes);
    }

    //读取版本信息
    public static String getVersionInfoCardHex(LiftFunctionCardVo functionCardVo) {
        List<Byte> infoBytes = new ArrayList<>();
        //卡类型 1B
        infoBytes.add(LiftCardTypeEnum.readVersionInfoCard.code.byteValue());
        System.out.println("卡类型：" + DataChangeUtil.byte2Hex(Bytes.toArray(infoBytes)));
        //数据长度 2B
        Integer dataLength = infoBytes.size();
        //校验 2B
        byte[] bytes = Bytes.toArray(infoBytes);
        String crc = CRCUtil.CRC16_MODBUS(bytes);
        System.err.println(crc);
        infoBytes.addAll(0, getHexByteList(crc, 2));
        System.out.println("CRC：" + DataChangeUtil.byte2Hex(Bytes.toArray(getHexByteList(crc, 2))));
        infoBytes.addAll(0, Bytes.asList(NumberUtil.unsignedShortToByte2(dataLength)));
        System.out.println("数据长度：" + DataChangeUtil.byte2Hex(NumberUtil.unsignedShortToByte2(dataLength)));
        bytes = Bytes.toArray(infoBytes);
        return DataChangeUtil.byte2Hex(bytes);
    }

    //网络参数配置卡
    public static String getNetworkParamsCardHex(LiftFunctionCardVo functionCardVo) {
        List<Byte> infoBytes = new ArrayList<>();
        //卡类型 1B
        infoBytes.add(LiftCardTypeEnum.setNetworkCard.code.byteValue());
        System.out.println("卡类型：" + DataChangeUtil.byte2Hex(Bytes.toArray(infoBytes)));
        //默认为未使用
        infoBytes.add((byte) 0);
        //楼栋号 1B
        String bulidingCode = functionCardVo.getBuildingNo();
        infoBytes.addAll(getHexByteList(bulidingCode, 1));
        System.out.println("楼栋号：" + DataChangeUtil.byte2Hex(Bytes.toArray(getHexByteList(bulidingCode, 1))));
        //单元号 1B
        String unitCode = functionCardVo.getUnitNo();
        infoBytes.addAll(getHexByteList(unitCode, 1));
        System.out.println("单元号：" + DataChangeUtil.byte2Hex(Bytes.toArray(getHexByteList(unitCode, 1))));
        //楼号长度 1B
        String buildingNoLength = functionCardVo.getBuildingNoLength();
        infoBytes.addAll(getHexByteList(buildingNoLength, 1));
        System.out.println("楼号长度：" + DataChangeUtil.byte2Hex(Bytes.toArray(getHexByteList(buildingNoLength, 1))));
        //房号长度 1B
        String roomNoLength = functionCardVo.getRoomNoLength();
        infoBytes.addAll(getHexByteList(roomNoLength, 1));
        System.out.println("房号长度：" + DataChangeUtil.byte2Hex(Bytes.toArray(getHexByteList(roomNoLength, 1))));
        //设备编号 10B
        String deviceNo = functionCardVo.getDeviceNo();
        infoBytes.addAll(getHexByteList(deviceNo, 10));
        System.out.println("设备编号：" + DataChangeUtil.byte2Hex(Bytes.toArray(getHexByteList(deviceNo, 10))));
        //电梯编号 1B
        infoBytes.add(Integer.valueOf(functionCardVo.getLiftNo()).byteValue());
        System.out.println("电梯编号：" + DataChangeUtil.byte2Hex(new byte[]{Integer.valueOf(functionCardVo.getLiftNo()).byteValue()}));
        //梯控IP 4B
        infoBytes.addAll(getHexIp(functionCardVo.getIpAddr()));
        System.out.println("梯控IP：" + DataChangeUtil.byte2Hex(Bytes.toArray(getHexIp(functionCardVo.getIpAddr()))));
        //子网掩码 4B
        infoBytes.addAll(getHexIp(functionCardVo.getNetMask()));
        System.out.println("子网掩码：" + DataChangeUtil.byte2Hex(Bytes.toArray(getHexIp(functionCardVo.getNetMask()))));
        //默认网关 4B
        infoBytes.addAll(getHexIp(functionCardVo.getGateway()));
        System.out.println("默认网关：" + DataChangeUtil.byte2Hex(Bytes.toArray(getHexIp(functionCardVo.getGateway()))));
        //中心服务器IP 4B
        infoBytes.addAll(getHexIp(functionCardVo.getCenterIP()));
        System.out.println("默认网关：" + DataChangeUtil.byte2Hex(Bytes.toArray(getHexIp(functionCardVo.getCenterIP()))));
        //DNS 4B
        infoBytes.addAll(getHexIp(functionCardVo.getDns1()));
        System.out.println("DNS1：" + DataChangeUtil.byte2Hex(Bytes.toArray(getHexIp(functionCardVo.getDns1()))));
        //DNS 4B
        infoBytes.addAll(getHexIp(functionCardVo.getDns2()));
        System.out.println("DNS2：" + DataChangeUtil.byte2Hex(Bytes.toArray(getHexIp(functionCardVo.getDns2()))));
        //卡密钥 6B
        infoBytes.addAll(getHexByteList(functionCardVo.getCardEncryptionKey(), 6));
        System.out.println("卡密钥：" + DataChangeUtil.byte2Hex(Bytes.toArray(getHexByteList(functionCardVo.getCardEncryptionKey(), 6))));
        //卡扇区偏置
        infoBytes.add((byte) 1);
        List<String> exitIps = functionCardVo.getExitIps() == null ? new ArrayList<>() : functionCardVo.getExitIps();
        //分机数量 1B
        infoBytes.add(Integer.valueOf(exitIps.size()).byteValue());
        System.out.println("分机数量：" + DataChangeUtil.byte2Hex(new byte[]{Integer.valueOf(exitIps.size()).byteValue()}));
        for (String ip :
                exitIps) {
            //分机IP 4B
            infoBytes.addAll(getHexIp(ip));
            System.out.println("分机IP：" + DataChangeUtil.byte2Hex(Bytes.toArray(getHexIp(ip))));
        }
        //数据长度 2B
        Integer dataLength = infoBytes.size();
        //校验 2B
        byte[] bytes = Bytes.toArray(infoBytes);
        String crc = CRCUtil.CRC16_MODBUS(bytes);
        System.err.println(crc);
        infoBytes.addAll(0, getHexByteList(crc, 2));
        System.out.println("CRC：" + DataChangeUtil.byte2Hex(Bytes.toArray(getHexByteList(crc, 2))));
        infoBytes.addAll(0, Bytes.asList(NumberUtil.unsignedShortToByte2(dataLength)));
        System.out.println("数据长度：" + DataChangeUtil.byte2Hex(NumberUtil.unsignedShortToByte2(dataLength)));
        bytes = Bytes.toArray(infoBytes);
        return DataChangeUtil.byte2Hex(bytes);
    }

    //字符串转List<Byte>
    private static List<Byte> getStringByteList(String src, Integer length, String charsetName) {
        if (src == null) {
            src = "";
        }
        List<Byte> infoBytes = new ArrayList<>();
        byte[] bytes = new byte[length];
        try {
            byte[] srcBytes = src.getBytes(charsetName);
            Integer cutLength = 0;
            if (srcBytes.length >= length) {
                cutLength = length;
            } else {
                cutLength = srcBytes.length;
            }
            System.arraycopy(srcBytes, 0, bytes, 0, cutLength);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        infoBytes.addAll(Bytes.asList(bytes));
        return infoBytes;
    }

    //Integer转List<Byte>
    private static List<Byte> getNumberByteList(Integer src, Integer length) {
        if (src == null) {
            src = 0;
        }
        List<Byte> infoBytes = new ArrayList<>();
        if (length.equals(4)) {
            byte[] btyes = NumberUtil.intToByte4(src);
            infoBytes = Bytes.asList(btyes);
        } else if (length.equals(1)) {
            infoBytes.add(Integer.valueOf(src).byteValue());
        }
        //Collections.reverse(infoBytes);
        return infoBytes;
    }

    //16进制字符串转List<Byte>
    private static List<Byte> getHexByteList(String src, Integer length) {
        if (src == null) {
            src = "";
        }
        List<Byte> infoBytes = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(src);
        int srcLength = stringBuilder.length();
        if (srcLength < length * 2) {
            for (int i = 0; i < length * 2 - srcLength; i++) {
                stringBuilder.insert(0, "0");
            }
        }
        srcLength = stringBuilder.length();
        for (int i = 0; i < srcLength / 2; i++) {
            String str = stringBuilder.substring(i * 2, (i + 1) * 2);
            infoBytes.add((byte) Long.parseLong(str, 16));
        }
        return infoBytes;
    }

    //时间段字符串转List<Byte>,hh:mm，2B
    private static List<Byte> getPeriodByteList(String src) {
        if (src == null) {
            src = "";
        }
        List<Byte> infoBytes = new ArrayList<>();
        String[] periodArr = src.split(":");
        if (periodArr != null && periodArr.length == 2) {
            infoBytes.add(Integer.valueOf(periodArr[0]).byteValue());
            infoBytes.add(Integer.valueOf(periodArr[1]).byteValue());
        } else {
            //无特定时段
            infoBytes.add((byte) 0);
            infoBytes.add((byte) 0);
        }
        return infoBytes;
    }

    //时间段字符串转List<Byte>,yyyy-MM-dd hh:mm:ss，8B
    private static List<Byte> getDateByteListWithDayOfWeek(String src) {
        List<Byte> infoBytes = new ArrayList<>();
        if (StringUtils.isBlank(src)) {
            infoBytes.addAll(Bytes.asList(new byte[6]));
            return infoBytes;
        }
        Calendar calendar = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        try {
            calendar.setTime(dateFormat.parse(src));
            Integer year = calendar.get(Calendar.YEAR);
            byte[] yearBtyes = NumberUtil.unsignedShortToByte2(year);
            infoBytes.addAll(Bytes.asList(yearBtyes));
            Integer month = calendar.get(Calendar.MONTH) + 1;
            infoBytes.add(month.byteValue());
            Integer day = calendar.get(Calendar.DAY_OF_MONTH);
            infoBytes.add(day.byteValue());
            Integer hour = calendar.get(Calendar.HOUR);
            infoBytes.add(hour.byteValue());
            Integer min = calendar.get(Calendar.MINUTE);
            infoBytes.add(min.byteValue());
            Integer second = calendar.get(Calendar.SECOND);
            infoBytes.add(second.byteValue());
            switch (calendar.get(Calendar.DAY_OF_WEEK)) {
                case 1:
                    infoBytes.add((byte) 1);
                    break;
                case 2:
                    infoBytes.add((byte) 2);
                    break;
                case 3:
                    infoBytes.add((byte) 4);
                    break;
                case 4:
                    infoBytes.add((byte) 8);
                    break;
                case 5:
                    infoBytes.add((byte) 16);
                    break;
                case 6:
                    infoBytes.add((byte) 32);
                    break;
                case 7:
                    infoBytes.add((byte) 64);
                    break;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            infoBytes.addAll(Bytes.asList(new byte[8]));
            return infoBytes;
        }

        return infoBytes;
    }

    //时间段字符串转List<Byte>,yyyy-MM-dd hh:mm，6B
    private static List<Byte> getTermOfValidityByteList(String src) {
        List<Byte> infoBytes = new ArrayList<>();
        if (StringUtils.isBlank(src)) {
            infoBytes.addAll(Bytes.asList(new byte[6]));
            return infoBytes;
        }
        Calendar calendar = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        try {
            calendar.setTime(dateFormat.parse(src));
            Integer year = calendar.get(Calendar.YEAR);
            byte[] yearBtyes = NumberUtil.unsignedShortToByte2(year);
            infoBytes.addAll(Bytes.asList(yearBtyes));
            Integer month = calendar.get(Calendar.MONTH) + 1;
            infoBytes.add(month.byteValue());
            Integer day = calendar.get(Calendar.DAY_OF_MONTH);
            infoBytes.add(day.byteValue());
            Integer hour = calendar.get(Calendar.HOUR_OF_DAY);
            infoBytes.add(hour.byteValue());
            Integer min = calendar.get(Calendar.MINUTE);
            infoBytes.add(min.byteValue());
        } catch (ParseException e) {
            e.printStackTrace();
            infoBytes.addAll(Bytes.asList(new byte[6]));
            return infoBytes;
        }

        return infoBytes;
    }

    //时间段字符串转List<Byte>,yyyy-MM-dd，4B
    private static List<Byte> getTermOfValidityByteList4B(String src) {
        List<Byte> infoBytes = new ArrayList<>();
        if (StringUtils.isBlank(src)) {
            infoBytes.addAll(Bytes.asList(new byte[6]));
            return infoBytes;
        }
        Calendar calendar = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            calendar.setTime(dateFormat.parse(src));
            Integer year = calendar.get(Calendar.YEAR);
            byte[] yearBtyes = NumberUtil.unsignedShortToByte2(year);
            infoBytes.addAll(Bytes.asList(yearBtyes));
            Integer month = calendar.get(Calendar.MONTH) + 1;
            infoBytes.add(month.byteValue());
            Integer day = calendar.get(Calendar.DAY_OF_MONTH);
            infoBytes.add(day.byteValue());
        } catch (ParseException e) {
            e.printStackTrace();
            infoBytes.addAll(Bytes.asList(new byte[4]));
            return infoBytes;
        }

        return infoBytes;
    }

    //周期 dayOfWeek
    private static List<Byte> getWeekDaysByteList(List<String> days) {
        List<Byte> infoBytes = new ArrayList<>();
        if (days == null) {
            infoBytes.add((byte) 0);
            return infoBytes;
        }
        byte weekDaysByte = 0;
        for (String str :
                days) {
            weekDaysByte |= (byte) (1 << (Integer.valueOf(str) - 1));
        }
        infoBytes.add(weekDaysByte);
        return infoBytes;
    }

    //电梯编号
    private static List<Byte> getLiftNoByteList(List<String> liftNos) {
        List<Byte> infoBytes = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            infoBytes.add(i < liftNos.size() ? Integer.valueOf(liftNos.get(i)).byteValue() : (byte) 0);
        }
        Collections.reverse(infoBytes);
        return infoBytes;
    }

    //电梯编号
    private static List<Byte> getLiftNoByteListWithSize(List<String> liftNos) {
        List<Byte> infoBytes = new ArrayList<>();
        for (int i = 0; i < liftNos.size(); i++) {
            infoBytes.add(Integer.valueOf(liftNos.get(i)).byteValue());
        }
        Collections.reverse(infoBytes);
        return infoBytes;
    }

    //二进制整形数组转字节数组
    private static List<Byte> getIntArrByteList(int[] intArr) {
        List<Byte> infoBytes = new ArrayList<>();
        int count = intArr.length / 8;
        for (int i = 0; i < count; i++) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.insert(0, intArr[i * 8]);
            stringBuilder.insert(0, intArr[i * 8 + 1]);
            stringBuilder.insert(0, intArr[i * 8 + 2]);
            stringBuilder.insert(0, intArr[i * 8 + 3]);
            stringBuilder.insert(0, intArr[i * 8 + 4]);
            stringBuilder.insert(0, intArr[i * 8 + 5]);
            stringBuilder.insert(0, intArr[i * 8 + 6]);
            stringBuilder.insert(0, intArr[i * 8 + 7]);
            infoBytes.add((byte) Integer.parseInt(stringBuilder.toString(), 2));
        }
        return infoBytes;
    }

    //电梯楼层数据，单开门
    private static List<Byte> getOneDoorFloorByteList(String[] floors) {
        List<Byte> infoBytes = new ArrayList<>();
        if (floors != null) {
            int[] intArr = new int[192];
            for (String floor :
                    floors) {
                int floorInt = Integer.valueOf(floor);
                if (floorInt > 0) {
                    intArr[Integer.valueOf(floorInt - 1)] = 1;
                }
            }
            infoBytes.addAll(getIntArrByteList(intArr));
        }
        return infoBytes;
    }

    //卡号
    private static List<Byte> getHexCardNo(Long cardNo) {
        List<Byte> infoBytes = new ArrayList<>();
        byte[] bytes = new byte[5];
        bytes[0] = (byte) (cardNo & 0xFF);
        bytes[1] = (byte) (cardNo >> 8 & 0xFF);
        bytes[2] = (byte) (cardNo >> 16 & 0xFF);
        bytes[3] = (byte) (cardNo >> 24 & 0xFF);
        infoBytes.addAll(Bytes.asList(bytes));
        return infoBytes;
    }

    //ip 4B
    private static List<Byte> getHexIp(String ip) {
        List<Byte> infoBytes = new ArrayList<>();
        String[] item = ip.split("\\.");
        for (int i = 0; i < item.length; i++) {
            infoBytes.add(Integer.valueOf(item[i]).byteValue());
        }
        return infoBytes;
    }
}
