
package com.aurine.cloudx.estate.service.impl;

import com.aurine.cloudx.common.core.constant.DataConstants;
import com.aurine.cloudx.common.core.constant.enums.DeviceTypeEnum;
import com.aurine.cloudx.common.core.util.TreeUtil;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.constant.enums.SubSystemLevelEnum;
import com.aurine.cloudx.estate.entity.ProjectDeviceSubsystem;
import com.aurine.cloudx.estate.entity.ProjectDeviceType;
import com.aurine.cloudx.estate.mapper.ProjectDeviceSubsystemMapper;
import com.aurine.cloudx.estate.service.ProjectDeviceSubsystemService;
import com.aurine.cloudx.estate.vo.ProjectDeviceSubsystemTreeVo;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.common.data.tenant.TenantContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 子系统
 *
 * @author xull@aurine.cn
 * @date 2020-05-20 10:39:47
 */
@Service
public class ProjectDeviceSubsystemServiceImpl extends ServiceImpl<ProjectDeviceSubsystemMapper, ProjectDeviceSubsystem> implements ProjectDeviceSubsystemService {

    @Resource
    ProjectDeviceSubsystemMapper projectDeviceSubsystemMapper;

    private final String entranceId = "100";
    private final String securityId = "109";
    private final String accessId = "101";
    private final String monitorId = "102";
//    private final String subsystem = "门禁系统、安防系统";

    @Override
    public List<ProjectDeviceSubsystemTreeVo> findTree() {
        return getTree(baseMapper.selectList(Wrappers.query()));
    }

    /**
     * 创建项目时预设子系统
     * @return
     */
    @Override
    @Transactional
    public boolean initDeviceSubSysTem(Integer projectId,Integer tenantId) {
//        List<ProjectDeviceSubsystem> defaultSubsystem = projectDeviceSubsystemMapper.defaultSubsystem(projectId, this.subsystem);
        List<ProjectDeviceSubsystem> result =projectDeviceSubsystemMapper.selectByTemplate();

        String entranceGuardId = UUID.randomUUID().toString().replace("-", "");;
        String securityId = UUID.randomUUID().toString().replace("-", "");
        String accessId = "";
        String monitorId = "";
        for (ProjectDeviceSubsystem po : result) {
            po.setProjectId(projectId);

            if (po.getLevel().equals(SubSystemLevelEnum.LEVEL_ONE.code)) {
//                String uid =
                if (po.getSubsystemId().equals(this.entranceId)) {
                    po.setSubsystemId(entranceGuardId);
                }
                if (po.getSubsystemId().equals(this.securityId)) {
                    po.setSubsystemId(securityId);
                }
            }

            if (po.getSubsystemId().equals(this.accessId)) {
                /*
                判断模板的ID值,保存为  区域门禁
                 */
                po.setPid(entranceGuardId);
                accessId = UUID.randomUUID().toString().replace("-", "");
                po.setSubsystemId(accessId);
            }

            if (po.getSubsystemId().equals(this.monitorId)) {
                /*
                判断模板的ID值,保存为  视频监控
                 */
                po.setPid(securityId);
                monitorId = UUID.randomUUID().toString().replace("-", "");
                po.setSubsystemId(monitorId);
            }

            if (po.getPid().equals(this.accessId)) {
                /*
                判断模板的上级ID,保存为  门禁子级
                 */
                po.setPid(accessId);
                po.setSubsystemId(UUID.randomUUID().toString().replace("-", ""));
            }

            if (po.getPid().equals(this.monitorId)) {
                /*
                判断模板的上级ID,保存为  监控子级
                 */
                po.setPid(monitorId);
                po.setSubsystemId(UUID.randomUUID().toString().replace("-", ""));
            }
            projectDeviceSubsystemMapper.initInsert(po, tenantId);
        }
        return true;
    }

    /**
     * 构建结构树
     *
     * @param projectDeviceSubsystems
     *         子系统列表
     *
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
