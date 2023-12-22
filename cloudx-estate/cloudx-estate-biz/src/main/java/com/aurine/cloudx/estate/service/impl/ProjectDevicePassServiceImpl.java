package com.aurine.cloudx.estate.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.aurine.cloudx.estate.constant.enums.PersonTypeEnum;
import com.aurine.cloudx.estate.dto.ProjectDevicePassQRDTO;
import com.aurine.cloudx.estate.dto.ProjectHouseDTO;
import com.aurine.cloudx.estate.entity.*;
import com.aurine.cloudx.estate.mapper.ProjectDeviceInfoMapper;
import com.aurine.cloudx.estate.mapper.ProjectDeviceParamInfoMapper;
import com.aurine.cloudx.estate.service.*;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.PlatformEnum;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.aurine.util.AurineQRCodeUnit;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.util.HuaweiQRCodeUnit;
import com.aurine.cloudx.estate.util.RedisUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 费用设置(ProjectFeeConf)表服务实现类
 *
 * @author makejava
 * @since 2020-07-20 16:43:48
 */
@Service
public class ProjectDevicePassServiceImpl extends ServiceImpl<ProjectDeviceInfoMapper, ProjectDeviceInfo> implements ProjectDevicePassService {

    @Resource
    private SysDeviceTypeThirdPartyConfigService sysDeviceTypeThirdPartyConfigService;
    @Resource
    private ProjectHousePersonRelService projectHousePersonRelService;
    @Resource
    private ProjectHouseInfoService projectHouseInfoService;
    @Resource
    private ProjectFrameInfoService projectFrameInfoService;
    @Resource
    private ProjectVisitorHisService projectVisitorHisService;


    /**
     * 获取 QR码
     *
     * @param qrDto
     * @return
     */
    @Override
    public String getQRCode(ProjectDevicePassQRDTO qrDto) {

        SysThirdPartyInterfaceConfig thirdPartyInterfaceConfig = sysDeviceTypeThirdPartyConfigService.getConfigByProjectId(qrDto.getProjectId());

        List<String> houseCodeStringList = new ArrayList<>();

        if (qrDto.getPersonType() == PersonTypeEnum.PROPRIETOR) {
            //如果是住户，获取住户的所有房屋信息
            List<ProjectHouseDTO> houseDTOList = projectHousePersonRelService.listHouseByPersonId(qrDto.getPersonId());

            if (CollUtil.isEmpty(houseDTOList)) {
                return "1000";//住户无房屋
            }

            for (ProjectHouseDTO house : houseDTOList) {
                if (StringUtils.isNotEmpty(house.getHouseCode())) {
                    houseCodeStringList.add(house.getHouseCode());
                }
            }

            //无有效房屋时
            if (CollUtil.isEmpty(houseCodeStringList)) {
                return "1001";//住户房屋获取框架号异常
            }

        } else if (qrDto.getPersonType() == PersonTypeEnum.VISITOR) {
            //如果是访客，获取访客的信息
            ProjectVisitorHis visitorHis = projectVisitorHisService.getById(qrDto.getPersonId());
            if (visitorHis == null) {
                return "2000";//无访客信息
            }

            //如果访客有被访人，获取被访人（被访房屋）的房屋信息
            if (StringUtils.isNotEmpty(visitorHis.getVisitHouseId())) {

                ProjectHouseInfo houseInfo = projectHouseInfoService.getById(visitorHis.getVisitHouseId());
                if (houseInfo != null && StringUtils.isNotEmpty(houseInfo.getHouseCode())) {
                    houseCodeStringList.add(houseInfo.getHouseCode());
                }
            }

        } else {
            return "3000";//暂不支持该人员类型
        }


        /**
         * 临时使用V1版二维码
         */
        return AurineQRCodeUnit.getInstance().getQrcode(qrDto.getPersonName(), qrDto.getPersonType().code, qrDto.getProjectId().toString(), houseCodeStringList, qrDto.getBeginTime(), qrDto.getEffectiveTime(), qrDto.getTimes());

        //冠林中台（中台2.0） 使用版本1
//        if (thirdPartyInterfaceConfig!= null && StringUtils.equals(thirdPartyInterfaceConfig.getName(), PlatformEnum.AURINE_MIDDLE.value)) {
//            return AurineQRCodeUnit.getInstance().getQrcode(qrDto.getPersonName(), qrDto.getPersonType().code, qrDto.getProjectId().toString(), houseCodeStringList, qrDto.getBeginTime(), qrDto.getEffectiveTime(), qrDto.getTimes());
//        } else {
//            //其他环境使用版本２
//            String userNo = this.createQRV2SerialNo();
//            if (userNo == null) return "5000";
//
//            //生成8位序列号，存储在redis中，存储时间应该为失效时间+C分钟
//            RedisUtil.set("QR_" + userNo, qrDto, (qrDto.getEffectiveTime() + 0) * 60);
//
//            return HuaweiQRCodeUnit.getInstance().getQrcodeV2(qrDto.getPersonType().code, qrDto.getProjectId(), userNo, houseCodeStringList, qrDto.getBeginTime(), qrDto.getEffectiveTime(), qrDto.getTimes(), "0", "");
//        }

    }

    /**
     * 生成QR码 V2版本所需要的8位10进制 不重复序列号
     * 基于该序列号生命周期较短的前提
     *
     * @return
     */
    private String createQRV2SerialNo() {
        boolean isLock = false;
        int serialNo = 0;
        String serialNoStr = null;

        //同步上锁
        while (!isLock) {
            isLock = RedisUtil.lock("DEVICE_PASS_QR_SERIAL_NO_LOCK", 1, 2);
        }

        //查询数据库是否存在序列号

        if (RedisUtil.hasKey("DEVICE_PASS_QR_SERIAL_NO")) {
            //获取序列号，+1以后，返回redis
            serialNo = Integer.valueOf(String.valueOf(RedisUtil.get("DEVICE_PASS_QR_SERIAL_NO")));
            if (serialNo >= 99999999){
                serialNo = 0;
            }
            serialNo++;

            serialNoStr = parchLeftZero(String.valueOf(serialNo), 8);
            RedisUtil.set("DEVICE_PASS_QR_SERIAL_NO", serialNoStr);
        } else {
            //如果不存在redis数据，创建首个数据
            serialNoStr = parchLeftZero("1", 8);
            RedisUtil.set("DEVICE_PASS_QR_SERIAL_NO", serialNoStr);
        }


        //解锁
        RedisUtil.deleteLock("DEVICE_PASS_QR_SERIAL_NO_LOCK");


        return serialNoStr;

    }


    //位数不足往左补0
    private static String parchLeftZero(String str, Integer len) {

        if (str.length() >= len) {
            return str;
        }

        for (int i = 0, size = len - str.length(); i < size; i++) {
            str = "0" + str;
        }
        return str;
    }

//    public static void main(String[] args) {
//        String projectId = "1000000523";
//
//        ProjectDevicePassQRDTO qrDto = new ProjectDevicePassQRDTO();
//        qrDto.setProjectId(1000000523);
//        qrDto.setPersonType(PersonTypeEnum.PROPRIETOR);
//        qrDto.setBeginTime(System.currentTimeMillis());
//        qrDto.setTimes(1);
//        qrDto.setPersonName("TEST");
//        List<String> houseCodeStringList = new ArrayList<>();
//        houseCodeStringList.add("01010101");
//        houseCodeStringList.add("02020101");
//
//        System.out.println(AurineQRCodeUnit.getInstance().getQrcode(qrDto.getPersonName(), qrDto.getPersonType().code, qrDto.getProjectId().toString(), houseCodeStringList, qrDto.getBeginTime(), 5, null));
//        System.out.println(HuaweiQRCodeUnit.getInstance().getQrcodeV2(qrDto.getPersonType().code, qrDto.getProjectId(), "11111111", houseCodeStringList, qrDto.getBeginTime(), qrDto.getEffectiveTime(), qrDto.getTimes(), "0", ""));
//    }
}