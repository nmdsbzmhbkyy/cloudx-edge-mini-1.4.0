package com.aurine.cloudx.estate.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.entity.ProjectDeviceAbnormal;
import com.aurine.cloudx.estate.entity.ProjectDeviceInfo;
import com.aurine.cloudx.estate.mapper.ProjectDeviceAbnormalMapper;
import com.aurine.cloudx.estate.service.EdgeCascadeRequestMasterService;
import com.aurine.cloudx.estate.service.ProjectDeviceAbnormalService;
import com.aurine.cloudx.estate.service.ProjectDeviceInfoService;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.exception.entity.DeviceAbnormalHandleInfo;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.exception.handle.AbnormalHandleChain;
import com.aurine.cloudx.estate.util.bean.BeanPropertyUtil;
import com.aurine.cloudx.estate.util.bean.FieldMapping;
import com.aurine.cloudx.estate.vo.ProjectDeviceAbnormalSearchCondition;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ProjectDeviceAbnormalServiceImpl extends ServiceImpl<ProjectDeviceAbnormalMapper, ProjectDeviceAbnormal> implements ProjectDeviceAbnormalService {

    @Resource
    AbnormalHandleChain abnormalHandleChain;
    @Resource
    ProjectDeviceInfoService projectDeviceInfoService;
    @Resource
    EdgeCascadeRequestMasterService edgeCascadeRequestMasterService;

    @Override
    public Page<ProjectDeviceAbnormal> fetchList(Page page, ProjectDeviceAbnormalSearchCondition condition) {
        return baseMapper.fetchList(page, condition);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Boolean checkAbnormal(DeviceAbnormalHandleInfo handleInfo) {
        Integer originProjectId = edgeCascadeRequestMasterService.getOriginProjectId();
        if (!originProjectId.equals(handleInfo.getProjectId())) {
            // 如果不是当前边缘网关服务器直连的设备，这里不做异常判断
            log.info("[冠林边缘网关] 级联设备-跳过设备异常事件流程");
            return false;
        }

        if (handleInfo.getProjectId() != null) {
            ProjectContextHolder.setProjectId(handleInfo.getProjectId());
        }
        if (StringUtils.isNotEmpty(handleInfo.getThirdpartyCode())) {
            ProjectDeviceInfo deviceInfo = projectDeviceInfoService.getOne(new LambdaQueryWrapper<ProjectDeviceInfo>().eq(ProjectDeviceInfo::getThirdpartyCode, handleInfo.getThirdpartyCode()).last("limit 1"));
            handleInfo.setDeviceId(Optional.ofNullable(deviceInfo).map(deviceTemp -> deviceTemp.getDeviceId()).orElse(null));
        }
        List<ProjectDeviceAbnormal> abnormalList = null;
        boolean existIdInfo = StringUtils.isNotEmpty(handleInfo.getDeviceId()) || StringUtils.isNotEmpty(handleInfo.getSn()) || StringUtils.isNotEmpty(handleInfo.getThirdpartyCode());
        if (existIdInfo) {
            abnormalList = list(new LambdaQueryWrapper<ProjectDeviceAbnormal>()
                    .eq(StringUtils.isNotEmpty(handleInfo.getSn()), ProjectDeviceAbnormal::getSn, handleInfo.getSn())
                    .or()
                    .eq(StringUtils.isNotEmpty(handleInfo.getDeviceId()), ProjectDeviceAbnormal::getDeviceId, handleInfo.getDeviceId())
                    .or()
                    .eq(StringUtils.isNotEmpty(handleInfo.getThirdpartyCode()), ProjectDeviceAbnormal::getThirdpartyCode, handleInfo.getThirdpartyCode())
                    .last("limit 2")
            );
        }
        ProjectDeviceAbnormal abnormal = null;
        if (CollUtil.isNotEmpty(abnormalList)) {
            abnormal = abnormalList.get(0);
            if (abnormalList.size() >= 2) {
                log.error("获取到多条异常设备数据，{}", abnormalList);
            }
        }
//        ProjectDeviceAbnormal abnormal = this.getOne(new LambdaQueryWrapper<ProjectDeviceAbnormal>()
//                .eq(ProjectDeviceAbnormal::getSn, handleInfo.getSn()).or().eq(ProjectDeviceAbnormal::getDeviceId, handleInfo.getDeviceId()));
        if (abnormal == null) {
            abnormal = new ProjectDeviceAbnormal();
            if (StrUtil.isNotEmpty(handleInfo.getDeviceId())) {
                ProjectDeviceInfo deviceInfo = projectDeviceInfoService.getOne(new LambdaQueryWrapper<ProjectDeviceInfo>().eq(ProjectDeviceInfo::getDeviceId, handleInfo.getDeviceId()));
                abnormal.setDeviceId(deviceInfo.getDeviceId());
            } else {
                abnormal.setDeviceId(handleInfo.getDeviceId());
            }
        }
        abnormal.setDevAddTime(LocalDateTime.now());
        BeanPropertyUtil.copyProperty(abnormal, handleInfo, new FieldMapping<ProjectDeviceAbnormal, DeviceAbnormalHandleInfo>()
                .add(ProjectDeviceAbnormal::getAccessMethod, DeviceAbnormalHandleInfo::getAccessMethod)
                .add(ProjectDeviceAbnormal::getDeviceType, DeviceAbnormalHandleInfo::getDeviceType)
                .add(ProjectDeviceAbnormal::getDStatus, DeviceAbnormalHandleInfo::getDStatus)
                .add(ProjectDeviceAbnormal::getDeviceDesc, DeviceAbnormalHandleInfo::getDeviceDesc)
                .add(ProjectDeviceAbnormal::getDeviceCodeReg, DeviceAbnormalHandleInfo::getDeviceCodeReg)
                .add(ProjectDeviceAbnormal::getSn, DeviceAbnormalHandleInfo::getSn)
                .add(ProjectDeviceAbnormal::getThirdpartyCode, DeviceAbnormalHandleInfo::getThirdpartyCode)
                .add(ProjectDeviceAbnormal::getIpv4, DeviceAbnormalHandleInfo::getIpv4)
                .add(ProjectDeviceAbnormal::getMac, DeviceAbnormalHandleInfo::getMac)
                .add(ProjectDeviceAbnormal::getDeviceCode, DeviceAbnormalHandleInfo::getDeviceCode)
        );
        log.info("进入异常判断责任链之前的abnormal：{}", JSON.toJSONString(abnormal));
        abnormalHandleChain.handle(handleInfo, abnormal);
        log.info("进入异常判断责任链之后的abnormal：{}", JSON.toJSONString(abnormal));
        // 能拿到设备信息就拿，拿不到就算了
        if (StrUtil.isNotEmpty(handleInfo.getSn()) || StrUtil.isNotEmpty(handleInfo.getDeviceId())) {
            ProjectDeviceInfo deviceInfo = projectDeviceInfoService.getOne(new LambdaQueryWrapper<ProjectDeviceInfo>()
                    .eq(StrUtil.isNotEmpty(handleInfo.getSn()), ProjectDeviceInfo::getSn, handleInfo.getSn())
                    .eq(StrUtil.isNotEmpty(handleInfo.getDeviceId()), ProjectDeviceInfo::getDeviceId, handleInfo.getDeviceId()));
            if (deviceInfo != null) {
                // 这里应该获取到完整的设备描述（带楼栋那些信息）
                abnormal.setDeviceDesc(deviceInfo.getDeviceName());
                abnormal.setDeviceType(deviceInfo.getDeviceType());
            }
        }

        if (StrUtil.isNotEmpty(abnormal.getAbnormalCode()) && abnormal.getAbnormalCode().contains("2")) {
            //根据设备信息查询记录
            List<ProjectDeviceAbnormal> deviceAbnormalList =  list(Wrappers.lambdaQuery(ProjectDeviceAbnormal.class)
                    .eq(StrUtil.isNotEmpty(abnormal.getSn()),ProjectDeviceAbnormal::getSn, abnormal.getSn())
                    .eq(StrUtil.isNotEmpty(abnormal.getMac()),ProjectDeviceAbnormal::getMac, abnormal.getMac())
                    .eq(StrUtil.isNotEmpty(abnormal.getIpv4()),ProjectDeviceAbnormal::getIpv4, abnormal.getIpv4())
                    .or().eq(StrUtil.isNotEmpty(abnormal.getDeviceId()),ProjectDeviceAbnormal::getDeviceId, abnormal.getDeviceId())
                    .orderByDesc(ProjectDeviceAbnormal::getCreateTime)
                    .last("limit 1"));
            if (CollUtil.isNotEmpty(deviceAbnormalList)) {
                //如果存在并且异常不一致做更新操作
                ProjectDeviceAbnormal projectDeviceAbnormal = deviceAbnormalList.get(0);
                if (!projectDeviceAbnormal.getAbnormalCode().equals(abnormal.getAbnormalCode())) {
                    abnormal.setSeq(projectDeviceAbnormal.getSeq());
                    this.updateById(abnormal);
                }
            } else {
                //不存在就添加
                this.save(abnormal);
            }
            return true;
        } else if (abnormal.getSeq() != null) {
            this.removeById(abnormal.getSeq());
        }
        return false;
    }

    @Override
    public void removeAbnormalByDeviceId(String deviceId) {
        this.remove(new LambdaQueryWrapper<ProjectDeviceAbnormal>().eq(ProjectDeviceAbnormal::getDeviceId, deviceId));
    }

}
