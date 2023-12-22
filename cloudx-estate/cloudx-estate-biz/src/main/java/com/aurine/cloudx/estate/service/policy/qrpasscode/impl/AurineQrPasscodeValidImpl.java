package com.aurine.cloudx.estate.service.policy.qrpasscode.impl;

import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.common.core.constant.enums.DeviceTypeEnum;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.entity.ProjectDeviceInfo;
import com.aurine.cloudx.estate.entity.ProjectEntityLevelCfg;
import com.aurine.cloudx.estate.service.ProjectDeviceInfoService;
import com.aurine.cloudx.estate.service.ProjectDeviceParamInfoService;
import com.aurine.cloudx.estate.service.ProjectEntityLevelCfgService;
import com.aurine.cloudx.estate.service.policy.qrpasscode.IValidQrPasscode;
import com.aurine.cloudx.estate.service.policy.qrpasscode.QrPasscodeTempMethods;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.model.RemoteOpenDoorResultModel;
import com.aurine.cloudx.estate.util.AurineQrCodeUtil;
import com.aurine.cloudx.estate.util.Sm3Utils;
import com.aurine.cloudx.estate.vo.DeviceParamDataVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

@Component("aurineQrPasscodeValid")
@Slf4j
@RequiredArgsConstructor
public class AurineQrPasscodeValidImpl extends QrPasscodeTempMethods implements IValidQrPasscode {

    private final ProjectEntityLevelCfgService projectEntityLevelCfgService;
    private final RedisTemplate<String, Object> redisTemplate;
    private final ProjectDeviceInfoService deviceInfoService;
    private final ProjectDeviceParamInfoService deviceParamInfoService;

    @Override
    public void valid(ProjectDeviceInfo deviceInfo, String passcode, RemoteOpenDoorResultModel resultModel) {

        resultModel.setResult(RemoteOpenDoorResultModel.ResultType.PASS_RESULT_TYPE_VALID.getCode());
        if (Objects.isNull(deviceInfo)) {
            log.warn("【冠林自有二维码校验】设备不存在");
            resultModel.setResult(RemoteOpenDoorResultModel.ResultType.PASS_RESULT_TYPE_INVALID.getCode());
        } else {
            List<ProjectEntityLevelCfg> cfgList = projectEntityLevelCfgService
                    .list(new QueryWrapper<ProjectEntityLevelCfg>()
                            .lambda()
                            .eq(ProjectEntityLevelCfg::getProjectId, ProjectContextHolder.getProjectId())
                            .le(ProjectEntityLevelCfg::getLevel, 3)
                            .orderByDesc(ProjectEntityLevelCfg::getLevel));

            if (CollectionUtils.isEmpty(cfgList) || cfgList.size() != 3) {
                log.info("【冠林自有二维码校验】 框架号配置异常");
                resultModel.setResult(RemoteOpenDoorResultModel.ResultType.PASS_RESULT_TYPE_INVALID.getCode());
            } else {
                StringBuilder rule = new StringBuilder();
                for (ProjectEntityLevelCfg cfg : cfgList) {
                    if (Objects.equals("0", cfg.getIsDisable()) && cfg.getLevel() <= 3) {
                        rule.append(cfg.getCodeRule());
                    }
                }
                //进行二维码解密
                AurineQrCodeUtil.QrCode qrAnalysis = AurineQrCodeUtil.analysis(passcode, rule.toString(), 2);
                if (Objects.isNull(qrAnalysis)) {
                    log.info("【冠林自有二维码校验】 二维码解析异常：{}", passcode);
                    resultModel.setResult(RemoteOpenDoorResultModel.ResultType.PASS_RESULT_TYPE_INVALID.getCode());
                    return;
                }
                log.info("【冠林自有二维码校验】 二维码解析数据：{}", qrAnalysis);
                DeviceTypeEnum deviceTypeEnum = DeviceTypeEnum.getByCode(deviceInfo.getDeviceType());
                log.info("【冠林自有二维码校验】 设备数据：{}", deviceInfo);
                switch (deviceTypeEnum) {//校验社区ID | 房屋权限
                    case LADDER_WAY_DEVICE:
                        log.info("【冠林自有二维码校验】 梯口设备校验");
                        if (StringUtils.isBlank(deviceInfo.getDeviceCode()) || CollectionUtils.isEmpty(qrAnalysis.getRoomNos())) {
                            log.info("【冠林自有二维码校验】 设备编号/解析权限房间号异常deviceInfo:{}", deviceInfo);
                            resultModel.setResult(RemoteOpenDoorResultModel.ResultType.PASS_RESULT_TYPE_INVALID.getCode());
                            return;

                        }
                        String deviceCode = deviceInfo.getDeviceCode();
                        AtomicInteger buildingLen = new AtomicInteger(2);
                        cfgList.stream().filter(c -> c.getLevel().equals(3)).findAny().ifPresent(c -> {
                            buildingLen.set(c.getCodeRule());
                        });
                        AtomicInteger unitLen = new AtomicInteger(2);
                        cfgList.stream().filter(c -> c.getLevel().equals(2)).findAny().ifPresent(c -> {
                            unitLen.set(c.getCodeRule());
                        });

                        String buildingNo = deviceCode.substring(0, buildingLen.get());
                        String unitNo = deviceCode.substring(buildingLen.get(), buildingLen.get() + unitLen.get());

                        AurineQrCodeUtil.Room room = qrAnalysis.getRoomNos().stream().filter(r -> r.getBuildingNo().equals(buildingNo) && r.getUnitNo().equals(unitNo)).findAny().orElse(null);
                        if (Objects.isNull(room)) {
                            log.info("【冠林自有二维码校验】 楼栋单元号不匹配->buildingNo:{},unitNo:{}", buildingNo, unitNo);
                            resultModel.setResult(RemoteOpenDoorResultModel.ResultType.PASS_RESULT_TYPE_INVALID.getCode());
                            return;
                        } else {
                            resultModel.setRoomNo(room.toString());
                        }
                    case GATE_DEVICE:
                        log.info("【冠林自有二维码校验】 区口校验");
                        String communityId;
                        try {
                            ProjectDeviceInfo ha80Driver = deviceInfoService.getDriverByDeviceName("HA80");
                            DeviceParamDataVo paramByDeviceId = deviceParamInfoService.getParamByDeviceId(ha80Driver.getDeviceId(), ha80Driver.getProductId());
                            JSONObject jsonObject = JSONObject.parseObject(paramByDeviceId.getParamData());
                            JSONObject edgeSysParamObj = jsonObject.getJSONObject("EdgeSysParamObj");
                            communityId = edgeSysParamObj.getString("cloudAreaId");
                        } catch (Exception e) {
                            log.info("【冠林自有二维码校验】 未查询到云社区ID");
                            communityId="";
                        }
                        //先计算出社区编号
                        String communityCode = Sm3Utils.encrypt(communityId);
                        communityCode = String.valueOf(Integer.parseInt(communityCode.substring(communityCode.length() - 6), 16));
                        if (!Objects.equals(communityCode, qrAnalysis.getCommunityCode())) {
                            log.info("【冠林自有二维码校验】 社区编码不匹配->sourceCommunityCode:{},analysisCommunityCode:{}", communityCode, qrAnalysis.getCommunityCode());
                            resultModel.setResult(RemoteOpenDoorResultModel.ResultType.PASS_RESULT_TYPE_INVALID.getCode());
                            return;
                        }
                }
                //校验时间
                long now = System.currentTimeMillis() / 1000;
                //如果是二维码带时间信息 或者 有效时间为空或者有效时间为0 则进入时间判断
                long overdueTime = 0L;
                if ((Objects.equals(qrAnalysis.getType(), "1") || Objects.equals(qrAnalysis.getType(), "2") || Objects.equals(qrAnalysis.getType(), "5"))
                        || Objects.isNull(qrAnalysis.getEffectiveTime())
                        || qrAnalysis.getEffectiveTime() != 0) {
                    log.info("【冠林自有二维码校验】 时间校验");
                    Long startTime = qrAnalysis.getStartTime();//秒级时间戳
                    Long toleranceTime = 5 * 60L;
                    if (Objects.isNull(startTime) || startTime == 0 || now < (startTime - toleranceTime)) {//五分钟容错时间
                        log.info("【冠林自有二维码校验】 未达开始时间->startTime:{},currentTime:{}", startTime - toleranceTime, now);
                        resultModel.setResult(RemoteOpenDoorResultModel.ResultType.PASS_RESULT_TYPE_INVALID.getCode());
                        return;
                    }
                    Long effectiveTime = qrAnalysis.getEffectiveTime() * 60L;
                    if (Objects.equals(qrAnalysis.getType(), "1")) {
                        effectiveTime = 60L;
                    }
                    if (now > startTime + effectiveTime + toleranceTime) {//五分钟容错时间
                        log.info("【冠林自有二维码校验】 二维码过期->expireTime:{},currentTime:{}", startTime + effectiveTime + toleranceTime, now);
                        resultModel.setResult(RemoteOpenDoorResultModel.ResultType.PASS_RESULT_TYPE_EXPIRE.getCode());
                        return;
                    }
                    overdueTime = startTime + effectiveTime + toleranceTime - now;
                }

                if (Objects.equals(qrAnalysis.getType(), "3") || Objects.equals(qrAnalysis.getType(), "5")) {
                    log.info("【冠林自有二维码校验】 通行次数校验");
                    if (Objects.isNull(qrAnalysis.getTimes())) {
                        log.info("【冠林自有二维码校验】 3/5类型二维码通行次数解析错误");
                        resultModel.setResult(RemoteOpenDoorResultModel.ResultType.PASS_RESULT_TYPE_INVALID.getCode());
                        return;
                    }
                    //读取缓存 校验通行次数
                    String passcodeData = passcode.replaceAll(":", "");
                    Long passTimes = redisTemplate.opsForValue().increment(getRedisKey(passcodeData), 1);
                    if (Objects.nonNull(passTimes) && Integer.parseInt(passTimes.toString()) > qrAnalysis.getTimes()) {
                        log.info("【冠林自有二维码校验】 通行次数到达上限->allowTimes:{},passedTimes:{}", qrAnalysis.getTimes(), passTimes);
                        resultModel.setResult(RemoteOpenDoorResultModel.ResultType.PASS_RESULT_TYPE_INVALID.getCode());
                        return;
                    }
                    log.info("【冠林自有二维码校验】 当前通行次数:{}", passTimes);
                    if (Objects.equals(qrAnalysis.getType(), "5") && overdueTime >= 0) {
                        redisTemplate.expire(getRedisKey(passcodeData), overdueTime, TimeUnit.SECONDS);
                    }
                }
            }
        }
    }

    @Override
    protected String getValidType() {
        return "aurine";
    }
}
