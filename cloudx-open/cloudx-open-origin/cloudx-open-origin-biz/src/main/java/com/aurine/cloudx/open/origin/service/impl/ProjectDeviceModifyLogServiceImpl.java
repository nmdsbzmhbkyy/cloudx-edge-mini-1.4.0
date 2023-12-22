package com.aurine.cloudx.open.origin.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.open.origin.vo.ProjectDeviceCheckParamVo;
import com.aurine.cloudx.open.origin.vo.ProjectDeviceModifyLogVo;
import com.aurine.cloudx.open.origin.mapper.ProjectDeviceModifyLogMapper;
import com.aurine.cloudx.open.origin.entity.ProjectDeviceAbnormal;
import com.aurine.cloudx.open.origin.entity.ProjectDeviceInfo;
import com.aurine.cloudx.open.origin.entity.ProjectDeviceModifyLog;
import com.aurine.cloudx.open.origin.service.ProjectDeviceAbnormalService;
import com.aurine.cloudx.open.origin.service.ProjectDeviceInfoService;
import com.aurine.cloudx.open.origin.service.ProjectDeviceModifyLogService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.admin.api.feign.RemoteUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
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

        List<ProjectDeviceAbnormal> deviceAbnormals = projectDeviceAbnormalService.list(Wrappers.lambdaQuery(ProjectDeviceAbnormal.class)
                .eq(ProjectDeviceAbnormal::getDeviceId, deviceId));

        ProjectDeviceInfo deviceInfo = projectDeviceInfoService.getOne(Wrappers.lambdaQuery(ProjectDeviceInfo.class).eq(ProjectDeviceInfo::getDeviceId, deviceId));

        if(deviceAbnormals.size()<1){
            Integer count = baseMapper.getAbnormalParam(deviceId);
            if(count <= 0){
                projectDeviceCheckParamVo.setIpv4(true);
                projectDeviceCheckParamVo.setDeviceCode(true);
                projectDeviceCheckParamVo.setMac(true);
            }
        }

        if(StrUtil.isNotEmpty(deviceInfo.getIpv4())){
            List<ProjectDeviceInfo> ipv4List = projectDeviceInfoService.list(Wrappers.lambdaQuery(ProjectDeviceInfo.class)
                    .eq(ProjectDeviceInfo::getIpv4, deviceInfo.getIpv4())
                    .eq(ProjectDeviceInfo::getProjectId, ProjectContextHolder.getProjectId()));
            if(ipv4List.size()>1){
                projectDeviceCheckParamVo.setIpv4(false);
            }else{
                projectDeviceCheckParamVo.setIpv4(true);
            }
        }

        if(StrUtil.isNotEmpty(deviceInfo.getDeviceCode())){
            List<ProjectDeviceInfo> ipv4List = projectDeviceInfoService.list(Wrappers.lambdaQuery(ProjectDeviceInfo.class)
                    .eq(ProjectDeviceInfo::getDeviceCode, deviceInfo.getDeviceCode())
                    .eq(ProjectDeviceInfo::getProjectId, ProjectContextHolder.getProjectId()));
            if(ipv4List.size()>1){
                projectDeviceCheckParamVo.setDeviceCode(false);
            }else{
                projectDeviceCheckParamVo.setDeviceCode(true);
            }
        }

        if(StrUtil.isNotEmpty(deviceInfo.getMac())){
            List<ProjectDeviceInfo> ipv4List = projectDeviceInfoService.list(Wrappers.lambdaQuery(ProjectDeviceInfo.class)
                    .eq(ProjectDeviceInfo::getMac, deviceInfo.getMac())
                    .eq(ProjectDeviceInfo::getProjectId, ProjectContextHolder.getProjectId()));
            if(ipv4List.size()>1){
                projectDeviceCheckParamVo.setMac(false);
            }else{
                projectDeviceCheckParamVo.setMac(true);
            }
        }
        return projectDeviceCheckParamVo;
    }
}
