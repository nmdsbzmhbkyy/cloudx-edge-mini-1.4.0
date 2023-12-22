package com.aurine.cloudx.estate.service.impl;

import cn.hutool.core.util.StrUtil;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.config.SyncConfig;
import com.aurine.cloudx.estate.constant.CascadeStatusConstants;
import com.aurine.cloudx.estate.constant.enums.IntoCloudStatusEnum;
import com.aurine.cloudx.estate.entity.*;
import com.aurine.cloudx.estate.mapper.EdgeCascadeConfMapper;
import com.aurine.cloudx.estate.service.*;
import com.aurine.cloudx.estate.util.PercentageUtil;
import com.aurine.cloudx.estate.util.bean.BeanPropertyUtil;
import com.aurine.cloudx.estate.vo.EdgeCascadeConfVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.common.core.util.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.UUID;

/**
 * <p>边缘网关级联入云配置表</p>
 *
 * @author : 王良俊
 * @date : 2021-12-17 09:19:58
 */
@Slf4j
@Service
public class EdgeCascadeConfServiceImpl extends ServiceImpl<EdgeCascadeConfMapper, EdgeCascadeConf> implements EdgeCascadeConfService {

    @Resource
    EdgeCascadeConfMapper edgeCascadeConfMapper;
    @Resource
    EdgeCascadeRequestSlaveService edgeCascadeRequestSlaveService;
    @Resource
    EdgeCloudRequestService edgeCloudRequestService;
    @Resource
    EdgeCascadeRequestMasterService edgeCascadeRequestMasterService;
    @Resource
    ProjectInfoService projectInfoService;
    @Resource
    private SyncConfig syncConfig;
    @Resource
    private EdgeCascadeProcessMasterService edgeCascadeProcessMasterService;
    @Resource
    private EdgeSyncLogService edgeSyncLogService;
    @Resource
    private ProjectDeviceInfoService projectDeviceInfoService;

    @Override
    public R enableNetwork(Integer projectId) {
        EdgeCascadeConfVo conf = this.getConf(projectId);
        if (!conf.isCanEnableNetwork()) {
            return R.failed(conf.getCannotEnableNetworkReason());
        }
        conf.setIsSyncCloud('1');
        this.saveOrUpdate(conf);
        return R.ok(true);
    }

    @Override
    public R disableNetwork(Integer projectId) {
        // 大于0代表当前项目或从边缘网关项目存在入云申请或是已入云无法关闭
        Boolean result = edgeCloudRequestService.canDisableNetwork(projectId);
        if (!result) {
            return R.failed("无法禁用联网，存在已入云或是申请中的项目");
        }
        edgeCloudRequestService.updateCloudStatus(projectId, IntoCloudStatusEnum.NOT_INTO_CLOUD);
        this.update(new LambdaUpdateWrapper<EdgeCascadeConf>().eq(EdgeCascadeConf::getProjectId, projectId).set(EdgeCascadeConf::getIsSyncCloud, '0'));
        return R.ok(true);
    }

    @Override
    public R enableCascade(Integer projectId) {
        EdgeCascadeConf conf = this.getConf(projectId);
        conf.setIsCascade('1');
        this.saveOrUpdate(conf);
        return R.ok(true);
    }

    @Override
    public R disableCascade(Integer projectId) {
        EdgeCascadeConfVo conf = this.getConf(projectId);
        if (!conf.isCanDisableCascade()) {
            return R.failed(conf.getCannotDisableCascadeReason());
        }
        this.update(new LambdaUpdateWrapper<EdgeCascadeConf>().eq(EdgeCascadeConf::getProjectId, projectId).set(EdgeCascadeConf::getIsCascade, '0'));
        return R.ok(true);
    }

    @Override
    public EdgeCascadeConfVo getConf(Integer projectId) {
        EdgeCascadeConfVo confVo = baseMapper.getConfVo(projectId);
        if (confVo == null) {
            EdgeCascadeConf conf = new EdgeCascadeConf();
            conf.setConfId(UUID.randomUUID().toString().replaceAll("-", ""));
            conf.setIsCascade('0');
            conf.setIsSyncCloud('0');
            conf.setProjectId(projectId);
            boolean master = edgeCascadeRequestMasterService.isMaster(projectId);
            if (master) {
                conf.setConnectCode(Integer.toUnsignedString(projectId, 36).toUpperCase());
            }
            this.save(conf);
            confVo = baseMapper.getConfVo(projectId);
        }

        Integer originProjectId = edgeCascadeRequestMasterService.getOriginProjectId();
        confVo.setOriginProjectId(originProjectId);

        boolean isMaster = edgeCascadeRequestMasterService.isMaster(projectId);
        Boolean canDisableNetwork = edgeCloudRequestService.canDisableNetwork(projectId);

        confVo.setCanEnableNetwork(isMaster);
        confVo.setCanDisableNetwork(canDisableNetwork);

        confVo.setCannotEnableNetworkReason(isMaster ? "" : "由于已级联或是提交级联申请无法开启联网");
        confVo.setCannotDisableNetworkReason(canDisableNetwork ? "" : "无法禁用联网，存在已入云或是申请中的项目");
        // 级联开启没限制
        confVo.setCanEnableCascade(true);
        boolean canDisableCascade = true;
        if (isMaster) {
            // 是否有级联其他边缘网关设备
            int countSlave = edgeCascadeRequestMasterService.countSlave();
            confVo.setSlaveNum(countSlave);
            if (countSlave > 0) {
                confVo.setCannotDisableCascadeReason("存在从边缘网关，无法禁用级联");
                canDisableCascade = false;
            }

        } else {
            // 判断是否级联
            int isCascade = edgeCascadeRequestSlaveService.count(new LambdaQueryWrapper<EdgeCascadeRequestSlave>()
                    .eq(EdgeCascadeRequestSlave::getCascadeStatus, CascadeStatusConstants.CASCADE));
            if (isCascade > 0) {
                confVo.setCannotDisableCascadeReason("已级联主边缘网关，无法禁用级联");
                canDisableCascade = false;
            } else {
                int inAudit = edgeCascadeRequestSlaveService.count(new LambdaQueryWrapper<EdgeCascadeRequestSlave>()
                        .eq(EdgeCascadeRequestSlave::getCascadeStatus, CascadeStatusConstants.PENDING_AUDIT));
                if (inAudit > 0) {
                    confVo.setCannotDisableCascadeReason("已提交级联申请，无法禁用级联");
                    canDisableCascade = false;
                }
            }
        }
        confVo.setCanDisableCascade(canDisableCascade);
        confVo.setMaster(isMaster);

        EdgeCloudRequest cloudRequest = edgeCloudRequestService.getOne(Wrappers.lambdaQuery(EdgeCloudRequest.class)
                .eq(EdgeCloudRequest::getProjectId, projectId)
                .in(EdgeCloudRequest::getCloudStatus, IntoCloudStatusEnum.INTO_CLOUD.code, IntoCloudStatusEnum.UNBINDING.code)
                .orderByDesc(EdgeCloudRequest::getCreateTime).last("limit 1"));
        confVo.setIsSync(cloudRequest != null ? cloudRequest.getIsSync() : '0');

        //同步进度
        EdgeSyncLog syncLog = edgeSyncLogService.getOne(new LambdaQueryWrapper<EdgeSyncLog>().eq(EdgeSyncLog::getProjectId, projectId).orderByDesc(EdgeSyncLog::getSeq).last("limit 1"));
        if(syncLog!= null && cloudRequest != null && cloudRequest.getIsSync().equals('1')){
            int successCount = edgeCascadeProcessMasterService.list(Wrappers.lambdaQuery(EdgeCascadeProcessMaster.class)
                    .eq(EdgeCascadeProcessMaster::getSyncId, syncLog.getSyncId())
                    .eq(EdgeCascadeProcessMaster::getStatus, "1")).size();
//            int size = cloudRequest.getSyncType().equals('1') ? syncConfig.getCloudxNum() + syncConfig.getParkingNum() : syncConfig.getCloudxNum();
            Integer syncNum = syncLog.getSyncNum();
            //进度
//            int i = (int) (successCount / (size * 0.01));
            int i = (int) PercentageUtil.percentage(successCount, syncNum, 0);

            confVo.setCloudSyncProcess(new BigDecimal(i));
        }else{
            confVo.setCloudSyncProcess(new BigDecimal(0));
        }

        int count = projectDeviceInfoService.count();
        confVo.setResult(count <= 0);
        return confVo;
    }

    @Override
    public R updateCascadeConfInfo(EdgeCascadeConf conf) {
        if (conf == null || StrUtil.isEmpty(conf.getConfId())) {
            return R.failed("缺少关键信息，无法修改级联配置");
        }
        return R.ok(this.updateById(conf));
    }

    @Override
    public String getConnectCode() {
        // 这里是因为一台边缘网关，这个级联配置表只会有一条数据（自己的级联码）
        EdgeCascadeConf conf = this.getOne(new LambdaQueryWrapper<EdgeCascadeConf>().last("limit 1"));
        if (conf != null) {
            return conf.getConnectCode();
        }
        return "";
    }

    @Override
    public boolean checkConnectCode(String connectCode) {
        if (StrUtil.isNotEmpty(connectCode)) {
            int count = this.count(new LambdaQueryWrapper<EdgeCascadeConf>().eq(EdgeCascadeConf::getConnectCode, connectCode));
            return count > 0;
        }
        return false;
    }

    @Override
    public void initCascadeConf(Integer projectId) {
        EdgeCascadeConfVo confVo = this.getConf(projectId);
        this.update(new LambdaUpdateWrapper<EdgeCascadeConf>().eq(EdgeCascadeConf::getConfId, confVo.getConfId())
                .set(EdgeCascadeConf::getIsCascade, '1')
                .set(EdgeCascadeConf::getIsSyncCloud, '1'));
    }

}
