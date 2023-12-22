package com.aurine.cloudx.open.origin.service.impl;

import com.aurine.cloudx.open.origin.mapper.ProjectDeviceAbnormalMapper;
import com.aurine.cloudx.open.origin.entity.ProjectDeviceAbnormal;
import com.aurine.cloudx.open.origin.vo.ProjectDeviceAbnormalSearchCondition;
import com.aurine.cloudx.open.origin.service.ProjectDeviceAbnormalService;
import com.aurine.cloudx.open.origin.service.ProjectDeviceInfoService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@Slf4j
public class ProjectDeviceAbnormalServiceImpl extends ServiceImpl<ProjectDeviceAbnormalMapper, ProjectDeviceAbnormal> implements ProjectDeviceAbnormalService {

//    @Resource
//    AbnormalHandleChain abnormalHandleChain;
    @Resource
ProjectDeviceInfoService projectDeviceInfoService;

    @Override
    public Page<ProjectDeviceAbnormal> fetchList(Page page, ProjectDeviceAbnormalSearchCondition condition) {
        return baseMapper.fetchList(page, condition);
    }

//    @Override
//    public void checkAbnormal(DeviceAbnormalHandleInfo handleInfo) {
//        ProjectContextHolder.setProjectId(handleInfo.getProjectId());
//        if (StringUtils.isNotEmpty(handleInfo.getThirdpartyCode())) {
//            ProjectDeviceInfo deviceInfo = projectDeviceInfoService.getOne(new LambdaQueryWrapper<ProjectDeviceInfo>().eq(ProjectDeviceInfo::getThirdpartyCode, handleInfo.getThirdpartyCode()).last("limit 1"));
//            handleInfo.setDeviceId(deviceInfo.getDeviceId());
//        }
//        List<ProjectDeviceAbnormal> abnormalList = list(new LambdaQueryWrapper<ProjectDeviceAbnormal>()
//                .eq(StringUtils.isNotEmpty(handleInfo.getSn()), ProjectDeviceAbnormal::getSn, handleInfo.getSn())
//                .or()
//                .eq(StringUtils.isNotEmpty(handleInfo.getDeviceId()), ProjectDeviceAbnormal::getDeviceId, handleInfo.getDeviceId()));
//
//        ProjectDeviceAbnormal abnormal = null;
//        if (CollUtil.isNotEmpty(abnormalList)) {
//            abnormal = abnormalList.get(0);
//            if (abnormalList.size() >= 2) {
//                log.error("获取到多条异常设备数据，{}", abnormalList);
//            }
//        }
////        ProjectDeviceAbnormal abnormal = this.getOne(new LambdaQueryWrapper<ProjectDeviceAbnormal>()
////                .eq(ProjectDeviceAbnormal::getSn, handleInfo.getSn()).or().eq(ProjectDeviceAbnormal::getDeviceId, handleInfo.getDeviceId()));
//        if (abnormal == null) {
//            abnormal = new ProjectDeviceAbnormal();
//            if (StrUtil.isNotEmpty(handleInfo.getDeviceId())) {
//                ProjectDeviceInfo deviceInfo = projectDeviceInfoService.getOne(new LambdaQueryWrapper<ProjectDeviceInfo>().eq(ProjectDeviceInfo::getDeviceId, handleInfo.getDeviceId()));
//                abnormal.setDevAddTime(deviceInfo.getCreateTime());
//                abnormal.setDeviceId(deviceInfo.getDeviceId());
//            } else {
//                abnormal.setDevAddTime(LocalDateTime.now());
//                abnormal.setDeviceId(handleInfo.getDeviceId());
//            }
//
//            abnormal.setAccessMethod(handleInfo.getAccessMethod());
//            abnormal.setDeviceType(handleInfo.getDeviceType());
////            abnormal.setDeviceCodeReg();
//            abnormal.setDStatus(handleInfo.getDStatus());
//            abnormal.setDeviceDesc(handleInfo.getDeviceDesc());
//            abnormal.setDeviceCodeReg(handleInfo.getDeviceCodeReg());
//            abnormal.setSn(handleInfo.getSn());
//        }
//
//        abnormalHandleChain.handle(handleInfo, abnormal);
//        // 能拿到设备信息就拿，拿不到就算了
//        if (StrUtil.isNotEmpty(handleInfo.getSn()) || StrUtil.isNotEmpty(handleInfo.getDeviceId())) {
//            ProjectDeviceInfo deviceInfo = projectDeviceInfoService.getOne(new LambdaQueryWrapper<ProjectDeviceInfo>()
//                    .eq(StrUtil.isNotEmpty(handleInfo.getSn()), ProjectDeviceInfo::getSn, handleInfo.getSn())
//                    .eq(StrUtil.isNotEmpty(handleInfo.getDeviceId()), ProjectDeviceInfo::getDeviceId, handleInfo.getDeviceId()));
//            if (deviceInfo != null) {
//                // 这里应该获取到完整的设备描述（带楼栋那些信息）
//                abnormal.setDeviceDesc(deviceInfo.getDeviceName());
//                abnormal.setDeviceType(deviceInfo.getDeviceType());
//            }
//        }
//
//        if (StrUtil.isNotEmpty(abnormal.getAbnormalCode())) {
//            this.saveOrUpdate(abnormal);
//        } else if (abnormal.getSeq() != null) {
//            this.removeById(abnormal.getSeq());
//        }
//    }

    @Override
    public void removeAbnormalByDeviceId(String deviceId) {
        this.remove(new LambdaQueryWrapper<ProjectDeviceAbnormal>().eq(ProjectDeviceAbnormal::getDeviceId, deviceId));
    }

}
