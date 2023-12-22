package com.aurine.cloudx.estate.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.constant.enums.device.DeviceRegAbnormalEnum;
import com.aurine.cloudx.estate.entity.ProjectDeviceAbnormal;
import com.aurine.cloudx.estate.entity.ProjectDeviceInfo;
import com.aurine.cloudx.estate.entity.ProjectDeviceModifyLog;
import com.aurine.cloudx.estate.mapper.ProjectDeviceModifyLogMapper;
import com.aurine.cloudx.estate.service.ProjectDeviceAbnormalService;
import com.aurine.cloudx.estate.service.ProjectDeviceInfoService;
import com.aurine.cloudx.estate.service.ProjectDeviceModifyLogService;
import com.aurine.cloudx.estate.vo.ProjectDeviceCheckParamVo;
import com.aurine.cloudx.estate.vo.ProjectDeviceModifyLogVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.admin.api.feign.RemoteUserService;
import com.pig4cloud.pigx.admin.api.vo.UserVO;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.security.util.SecurityUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * 设备修改记录表
 *
 * @author 邹宇
 * @date 2021-9-26 16:05:25
 */
@Service
public class ProjectDeviceModifyLogServiceImpl extends ServiceImpl<ProjectDeviceModifyLogMapper, ProjectDeviceModifyLog> implements ProjectDeviceModifyLogService {


    @Resource
    private RemoteUserService remoteUserService;

    @Resource
    private ProjectDeviceInfoService projectDeviceInfoService;

    @Resource
    private ProjectDeviceAbnormalService projectDeviceAbnormalService;

    /**
     * 根据设备ID查询更新记录
     *
     * @param deviceId
     * @return
     */
    @Override
    public List<ProjectDeviceModifyLogVo> getUpdateRecordByDeviceId(String deviceId, Integer count) {
        List<ProjectDeviceModifyLogVo> deviceModifyLogVos = baseMapper.getUpdateRecordByDeviceid(deviceId, count);
        //设置参数
        deviceModifyLogVos.forEach(modifyLogVo -> {
            JSONObject jsonObject = JSONObject.parseObject(modifyLogVo.getValueAfter());
            modifyLogVo.setIpv4(JSONObject.toJSONString(jsonObject.get("ipv4")));
            modifyLogVo.setMac(JSONObject.toJSONString(jsonObject.get("mac")));
            modifyLogVo.setDeviceCode(JSONObject.toJSONString(jsonObject.get("deviceCode")));
        });

        return deviceModifyLogVos;
    }


    /**
     * 保存设备修改记录
     *
     * @param projectDeviceInfo
     * @return
     */
    @Override
    public boolean saveDeviceModifyLog(String deviceId, ProjectDeviceInfo projectDeviceInfo) {

        String valueAfter = checkParam(deviceId, projectDeviceInfo);
        if (StrUtil.isEmpty(valueAfter)) {
            return false;
        }

        String uuid = UUID.randomUUID().toString().replace("-", "");
        ProjectDeviceModifyLog projectDeviceModifyLog = new ProjectDeviceModifyLog();

        projectDeviceModifyLog.setId(uuid);
        projectDeviceModifyLog.setDeviceId(deviceId);
        projectDeviceModifyLog.setValueAfter(valueAfter);
        projectDeviceModifyLog.setModifyTime(LocalDateTime.now());
       /* projectDeviceModifyLog.setOperatorUserId(SecurityUtils.getUser().getId());

        R<UserVO> user = remoteUserService.user(SecurityUtils.getUser().getId());
        projectDeviceModifyLog.setOperatorName(user.getData().getTrueName());*/
        return this.save(projectDeviceModifyLog);
    }


    private String checkParam(String deviceId, ProjectDeviceInfo projectDeviceInfo) {
        ProjectDeviceInfo deviceInfo = projectDeviceInfoService.getOne(Wrappers.lambdaQuery(ProjectDeviceInfo.class)
                .eq(ProjectDeviceInfo::getDeviceId, deviceId));

        String ipv4 = Optional.of(deviceInfo).map(ProjectDeviceInfo::getIpv4).orElse("1");
        String deviceCode = Optional.of(deviceInfo).map(ProjectDeviceInfo::getDeviceCode).orElse("1");
        String mac = Optional.of(deviceInfo).map(ProjectDeviceInfo::getMac).orElse("1");

        String toIpv4 = Optional.of(projectDeviceInfo).map(ProjectDeviceInfo::getIpv4).orElse("1");
        String toDeviceCode = Optional.of(projectDeviceInfo).map(ProjectDeviceInfo::getDeviceCode).orElse("1");
        String toMac = Optional.of(projectDeviceInfo).map(ProjectDeviceInfo::getMac).orElse("1");

        JSONObject jsonObject = new JSONObject();

        if (!ipv4.equals(toIpv4)) {
            jsonObject.put("ipv4", toIpv4);
        }
        if (!deviceCode.equals(toDeviceCode)) {
            jsonObject.put("deviceCode", toDeviceCode);
        }
        if (!mac.equals(toMac)) {
            jsonObject.put("mac", toMac);
        }
        if (jsonObject.size() > 0) {
            return JSONObject.toJSONString(jsonObject);
        }
        return null;
    }

    /**
     * 校验设备ip4和设备编号以及mac
     *
     * @param deviceId
     * @return
     */
    @Override
    public ProjectDeviceCheckParamVo checkDeviceIp4AndDeviceCode(String deviceId) {
        ProjectDeviceCheckParamVo projectDeviceCheckParamVo = new ProjectDeviceCheckParamVo();
        //设置默认值
        projectDeviceCheckParamVo.setIpv4(true);
        projectDeviceCheckParamVo.setDeviceCode(true);
        projectDeviceCheckParamVo.setMac(true);

        ProjectDeviceInfo deviceInfo = projectDeviceInfoService.getOne(Wrappers.lambdaQuery(ProjectDeviceInfo.class).eq(ProjectDeviceInfo::getDeviceId, deviceId));


        if (StrUtil.isNotEmpty(deviceInfo.getIpv4())) {
            List<ProjectDeviceInfo> ipv4List = projectDeviceInfoService.list(Wrappers.lambdaQuery(ProjectDeviceInfo.class)
                    .eq(ProjectDeviceInfo::getIpv4, deviceInfo.getIpv4())
                    .eq(ProjectDeviceInfo::getProjectId, ProjectContextHolder.getProjectId()));
            if (ipv4List.size() > 1) {
                projectDeviceCheckParamVo.setIpv4(false);
            }
        }

        if (StrUtil.isNotEmpty(deviceInfo.getDeviceCode())) {
            List<ProjectDeviceInfo> deviceCodeList = projectDeviceInfoService.list(Wrappers.lambdaQuery(ProjectDeviceInfo.class)
                    .eq(ProjectDeviceInfo::getDeviceCode, deviceInfo.getDeviceCode())
                    .eq(ProjectDeviceInfo::getProjectId, ProjectContextHolder.getProjectId()));
            if (deviceCodeList.size() > 1) {
                projectDeviceCheckParamVo.setDeviceCode(false);
            }
        }

        if (StrUtil.isNotEmpty(deviceInfo.getMac())) {
            List<ProjectDeviceInfo> macList = projectDeviceInfoService.list(Wrappers.lambdaQuery(ProjectDeviceInfo.class)
                    .eq(ProjectDeviceInfo::getMac, deviceInfo.getMac())
                    .eq(ProjectDeviceInfo::getProjectId, ProjectContextHolder.getProjectId()));
            if (macList.size() > 1) {
                projectDeviceCheckParamVo.setMac(false);
            }
        }
        return projectDeviceCheckParamVo;
    }


    @Override
    public R<Boolean> checkDeviceParam(String ipv4, String deviceCode, String mac, String deviceId) {
        StringBuilder adCode = new StringBuilder();
        StringBuilder abDesc = new StringBuilder();
        Integer result = 0;
        if (StrUtil.isNotEmpty(ipv4)) {
            LambdaQueryWrapper<ProjectDeviceInfo> ipv4Wrappers = new LambdaQueryWrapper<>();
            ipv4Wrappers.eq(ProjectDeviceInfo::getIpv4, ipv4);
            if (StrUtil.isNotEmpty(deviceId)) {
                ipv4Wrappers.ne(ProjectDeviceInfo::getDeviceId, deviceId);
            }
            int count = projectDeviceInfoService.count(ipv4Wrappers);
            if (count > 0) {
                abDesc.append(DeviceRegAbnormalEnum.AURINE_IPV4_REPEAT.desc);
                adCode.append(DeviceRegAbnormalEnum.AURINE_IPV4_REPEAT.systemCode);
                result++;
            }
        }
        if (StrUtil.isNotEmpty(deviceCode)) {
            LambdaQueryWrapper<ProjectDeviceInfo> deviceCodeWrappers = new LambdaQueryWrapper<>();
            deviceCodeWrappers.eq(ProjectDeviceInfo::getDeviceCode, deviceCode);
            if (StrUtil.isNotEmpty(deviceId)) {
                deviceCodeWrappers.ne(ProjectDeviceInfo::getDeviceId, deviceId);
            }
            int count = projectDeviceInfoService.count(deviceCodeWrappers);
            if (count > 0) {
                abDesc.append(result == 0 ? "" : ",").append(DeviceRegAbnormalEnum.DEVICE_NO_REPEAT.desc);
                adCode.append(result == 0 ? "" : ",").append(DeviceRegAbnormalEnum.DEVICE_NO_REPEAT.systemCode);
                result++;
            }
        }
        if (StrUtil.isNotEmpty(mac)) {
            LambdaQueryWrapper<ProjectDeviceInfo> macWrappers = new LambdaQueryWrapper<>();
            macWrappers.eq(ProjectDeviceInfo::getMac, mac);
            if (StrUtil.isNotEmpty(deviceId)) {
                macWrappers.ne(ProjectDeviceInfo::getDeviceId, deviceId);
            }
            int count = projectDeviceInfoService.count(macWrappers);
            if (count > 0) {
                abDesc.append(result == 0 ? "" : ",").append(DeviceRegAbnormalEnum.AURINE_MAC_REPEAT.desc);
                adCode.append(result == 0 ? "" : ",").append(DeviceRegAbnormalEnum.AURINE_MAC_REPEAT.systemCode);
                result++;
            }
        }
        if (result > 0) {
            String pj = abDesc + "/" + adCode;
            return R.ok(Boolean.FALSE, pj);
        }
        return R.ok(Boolean.TRUE);
    }

    /**
     * @return com.pig4cloud.pigx.common.core.util.R<java.lang.Boolean>
     * @description 新增校验逻辑 校验第三方编码
     * @author cyw
     * @time 2023-04-26 16:01
     */
    @Override
    public R<Boolean> checkDeviceParam(String ipv4, String deviceCode, String mac, String deviceId, String sn) {
        R<Boolean> result = this.checkDeviceParam(ipv4, deviceCode, mac, deviceId);
        if (result.getData() && StringUtils.isNotBlank(sn)) {
            Integer count = projectDeviceInfoService.lambdaQuery()
                    .ne(StringUtils.isNotBlank(deviceId),ProjectDeviceInfo::getDeviceId,deviceId)//增加判断是否为当前设备
                    .eq( ProjectDeviceInfo::getSn, sn).count();//与当前设备SN相同的 进入HINT弹窗提醒
            if(count>0){
                return R.ok(Boolean.FALSE);
            }
            return R.ok(Boolean.TRUE);
        } else {
            return result;
        }
    }
}
