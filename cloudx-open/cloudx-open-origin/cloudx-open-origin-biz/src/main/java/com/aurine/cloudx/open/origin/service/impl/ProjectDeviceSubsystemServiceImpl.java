
package com.aurine.cloudx.open.origin.service.impl;

import com.aurine.cloudx.common.core.constant.DataConstants;
import com.aurine.cloudx.common.core.util.TreeUtil;
import com.aurine.cloudx.open.origin.vo.ProjectDeviceSubsystemTreeVo;
import com.aurine.cloudx.open.origin.mapper.ProjectDeviceSubsystemMapper;
import com.aurine.cloudx.open.origin.constant.DeviceSubsystemModelIdConstant;
import com.aurine.cloudx.open.origin.constant.enums.SubSystemLevelEnum;
import com.aurine.cloudx.open.origin.entity.ProjectDeviceSubsystem;
import com.aurine.cloudx.open.origin.service.ProjectDeviceSubsystemService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 子系统
 *
 * @author xull@aurine.cn
 * @date 2020-05-20 10:39:47
 */
@SuppressWarnings("AlibabaTransactionMustHaveRollback")
@Service
public class ProjectDeviceSubsystemServiceImpl extends ServiceImpl<ProjectDeviceSubsystemMapper, ProjectDeviceSubsystem> implements ProjectDeviceSubsystemService {

    @Resource
    ProjectDeviceSubsystemMapper projectDeviceSubsystemMapper;

//    private final String subsystem = "门禁系统、安防系统";

    @Override
    public List<ProjectDeviceSubsystemTreeVo> findTree() {
        return getTree(baseMapper.selectList(Wrappers.query()));
    }

    /**
     * 创建项目时预设子系统
     *
     * @return
     */
    @Override
    @Transactional
    public boolean initDeviceSubSysTem(Integer projectId, Integer tenantId) {
//        List<ProjectDeviceSubsystem> defaultSubsystem = projectDeviceSubsystemMapper.defaultSubsystem(projectId, this.subsystem);
        List<ProjectDeviceSubsystem> result = projectDeviceSubsystemMapper.selectByTemplate();

        /*
        * leve1
        * */

        // 门禁系统ID
        String entranceGuardId = UUID.randomUUID().toString().replace("-", "");
        // 安防系统ID
        String securitySysId = UUID.randomUUID().toString().replace("-", "");
        // 检测系统ID
        String monitoringSysId = UUID.randomUUID().toString().replace("-", "");
        // 照明系统ID
        String lightingSysId = UUID.randomUUID().toString().replace("-", "");

        /*
        * level2
        * */

        // 区域门禁ID
        String accessId = UUID.randomUUID().toString().replace("-", "");
        // 视频监控ID
        String monitorId = UUID.randomUUID().toString().replace("-", "");
        // 周界报警ID
        String perimeterId = UUID.randomUUID().toString().replace("-", "");
        // 计量设备ID
        String meteringEquipmentId = UUID.randomUUID().toString().replace("-", "");
        // 检测设备ID
        String testingEquipmentId = UUID.randomUUID().toString().replace("-", "");
        // 室外照明ID
        String outdoorLightingId = UUID.randomUUID().toString().replace("-", "");

        for (ProjectDeviceSubsystem po : result) {
            po.setProjectId(projectId);
            if (po.getLevel().equals(SubSystemLevelEnum.LEVEL_ONE.code)) {
                switch (po.getSubsystemId()) {
                    case DeviceSubsystemModelIdConstant.ENTRANCE_ID:
                        po.setSubsystemId(entranceGuardId);
                        break;
                    case DeviceSubsystemModelIdConstant.SECURITY_ID:
                        po.setSubsystemId(securitySysId);
                        break;
                    case DeviceSubsystemModelIdConstant.MONITORING_SYS_ID:
                        po.setSubsystemId(monitoringSysId);
                        break;
                    case DeviceSubsystemModelIdConstant.LIGHTING_SYS_ID:
                        po.setSubsystemId(lightingSysId);
                        break;
                    default:
                }
            } else {
                switch (po.getSubsystemId()) {
                    case DeviceSubsystemModelIdConstant.ACCESS_ID:
                        /*
                        判断模板的ID值,保存为  区域门禁
                         */
                        po.setPid(entranceGuardId);
                        po.setSubsystemId(accessId);
                        break;
                    case DeviceSubsystemModelIdConstant.MONITOR_ID:
                        /*
                        判断模板的ID值,保存为  视频监控
                         */
                        po.setPid(securitySysId);
                        po.setSubsystemId(monitorId);
                        break;
                    case DeviceSubsystemModelIdConstant.PERIMETER_ID:
                        /*
                        判断模板的ID值,保存为  周界报警
                         */
                        po.setPid(securitySysId);
                        po.setSubsystemId(perimeterId);
                        break;
                    case DeviceSubsystemModelIdConstant.METERING_EQUIPMENT_ID:
                         /*
                        判断模板的ID值,保存为  计量设备
                         */
                        po.setPid(monitoringSysId);
                        po.setSubsystemId(meteringEquipmentId);
                        break;
                    case DeviceSubsystemModelIdConstant.TESTING_EQUIPMENT_ID:
                        /*
                        判断模板的ID值,保存为  检测设备
                         */
                        po.setPid(monitoringSysId);
                        po.setSubsystemId(testingEquipmentId);
                        break;
                    case DeviceSubsystemModelIdConstant.OUTDOOR_LIGHTING_ID:
                        /*
                        判断模板的ID值,保存为  室外照明
                         */
                        po.setPid(lightingSysId);
                        po.setSubsystemId(outdoorLightingId);
                        break;
                    default:
                }

                switch (po.getPid()) {
                    case DeviceSubsystemModelIdConstant.ACCESS_ID:
                        /*
                        判断模板的上级ID,保存为  门禁子级
                         */
                        po.setPid(accessId);
                        po.setSubsystemId(UUID.randomUUID().toString().replace("-", ""));
                        break;
                    case DeviceSubsystemModelIdConstant.MONITOR_ID:
                        /*
                        判断模板的上级ID,保存为  监控子级
                         */
                        po.setPid(monitorId);
                        po.setSubsystemId(UUID.randomUUID().toString().replace("-", ""));
                        break;
                    case DeviceSubsystemModelIdConstant.PERIMETER_ID:
                        /*
                        判断模板的上级ID,保存为  周界报警子级
                         */
                        po.setPid(perimeterId);
                        po.setSubsystemId(UUID.randomUUID().toString().replace("-", ""));
                        break;
                    case DeviceSubsystemModelIdConstant.METERING_EQUIPMENT_ID:
                        /*
                        判断模板的上级ID,保存为  周界报警子级
                         */
                        po.setPid(meteringEquipmentId);
                        po.setSubsystemId(UUID.randomUUID().toString().replace("-", ""));
                        break;
                    case DeviceSubsystemModelIdConstant.TESTING_EQUIPMENT_ID:
                        /*
                        判断模板的上级ID,保存为  周界报警子级
                         */
                        po.setPid(testingEquipmentId);
                        po.setSubsystemId(UUID.randomUUID().toString().replace("-", ""));
                        break;
                    case DeviceSubsystemModelIdConstant.OUTDOOR_LIGHTING_ID:
                        /*
                        判断模板的上级ID,保存为  周界报警子级
                         */
                        po.setPid(outdoorLightingId);
                        po.setSubsystemId(UUID.randomUUID().toString().replace("-", ""));
                        break;
                    default:
                }
            }
            projectDeviceSubsystemMapper.initInsert(po, tenantId);
        }
        return true;
    }

    /**
     * 构建结构树
     *
     * @param projectDeviceSubsystems 子系统列表
     * @return
     */
    private List<ProjectDeviceSubsystemTreeVo> getTree(List<ProjectDeviceSubsystem> projectDeviceSubsystems) {
        List<ProjectDeviceSubsystemTreeVo> treeList = projectDeviceSubsystems.stream()
                .filter(subsystem -> !subsystem.getSubsystemId().equals(subsystem.getPid()))
                .map(subsystem -> {
                    ProjectDeviceSubsystemTreeVo node = new ProjectDeviceSubsystemTreeVo();
                    node.setId(subsystem.getSubsystemId());
                    node.setParentId(subsystem.getPid());
                    node.setName(subsystem.getSubsystemName());
                    node.setCode(subsystem.getSubsystemCode());
                    node.setLevel(subsystem.getLevel());
                    return node;
                }).collect(Collectors.toList());
        return TreeUtil.build(treeList, DataConstants.ROOT);
    }
}
