package com.aurine.cloudx.estate.service.policy.qrpasscode.impl;

import com.aurine.cloudx.common.core.util.BeanUtil;
import com.aurine.cloudx.common.data.entity.BaseEntity;
import com.aurine.cloudx.estate.entity.ProjectDeviceInfo;
import com.aurine.cloudx.estate.entity.ProjectQrPasscodeRecord;
import com.aurine.cloudx.estate.service.ProjectQrPasscodeRecordService;
import com.aurine.cloudx.estate.service.ProjectQrPasscodeUseFlowService;
import com.aurine.cloudx.estate.service.policy.qrpasscode.IValidQrPasscode;
import com.aurine.cloudx.estate.service.policy.qrpasscode.QrPasscodeTempMethods;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.model.RemoteOpenDoorResultModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Component("scanTableQrPasscodeValid")
@Slf4j
@RequiredArgsConstructor
public class ScanTableQrPasscodeValidImpl extends QrPasscodeTempMethods implements IValidQrPasscode {

    private final ProjectQrPasscodeRecordService projectQrPasscodeRecordService;
    private final RedisTemplate<String, Object> redisTemplate;
    private final ProjectQrPasscodeUseFlowService projectQrPasscodeUseFlowService;


    @Override
    public void valid(ProjectDeviceInfo deviceInfo, String passcode, RemoteOpenDoorResultModel resultModel) {
        resultModel.setResult(RemoteOpenDoorResultModel.ResultType.PASS_RESULT_TYPE_VALID.getCode());
        ProjectQrPasscodeRecord record = projectQrPasscodeRecordService.lambdaQuery().eq(ProjectQrPasscodeRecord::getUniqueCode, passcode).one();
        if (Objects.isNull(record) || StringUtils.isBlank(record.getUniqueCode())) {
            throw new RuntimeException("未查询到二维码信息");
        }
        Long startTime = record.getStartTime();
        Long endTime = record.getEndTime();
        long now = System.currentTimeMillis() / 1000;
        // *************************校验时间*************************** //
        if (Objects.nonNull(startTime) && startTime != 0 && now < startTime) {
            log.info("【扫表二维码校验】 未达开始时间->startTime:{},currentTime:{}", startTime, now);
            resultModel.setResult(RemoteOpenDoorResultModel.ResultType.PASS_RESULT_TYPE_INVALID.getCode());
            return;
        }
        long overdueTime=0L;
        if (Objects.nonNull(endTime) && endTime != 0 ) {
            if (now > endTime){
                log.info("【扫表二维码校验】 二维码过期->expireTime:{},currentTime:{}", endTime, now);
                resultModel.setResult(RemoteOpenDoorResultModel.ResultType.PASS_RESULT_TYPE_EXPIRE.getCode());
                return;
            }
            overdueTime=endTime-now;
        }
        // *************************校验通行次数*************************** //
        Integer times = record.getTimes();
        String passcodeData = record.getUniqueCode();
        Long passTimes = redisTemplate.opsForValue().increment(getRedisKey(passcodeData),1);
        if (Objects.nonNull(times) &&
                times != 0 &&
                Objects.nonNull(passTimes) &&
                Integer.parseInt(passTimes.toString()) > times) {
            log.info("【扫表二维码校验】 通行次数到达上限->allowTimes:{},passedTimes:{}",times,passTimes);
            resultModel.setResult(RemoteOpenDoorResultModel.ResultType.PASS_RESULT_TYPE_INVALID.getCode());
            return;
        }
        log.info("【扫表二维码校验】 当前通行次数:{}",passTimes);
        if (overdueTime>0){
            redisTemplate.expire(getRedisKey(passcodeData),overdueTime, TimeUnit.SECONDS);
        }
        try {
            projectQrPasscodeUseFlowService.insert(record,deviceInfo,resultModel.getResult());
        }catch (Exception ignore){}

    }

    @Override
    protected String getValidType() {
        return "scanTable";
    }
}
