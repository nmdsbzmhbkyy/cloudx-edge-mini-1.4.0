
package com.aurine.cloudx.open.origin.service.impl;

import com.aurine.cloudx.common.core.util.StringUtil;
import com.aurine.cloudx.open.origin.mapper.ProjectDeviceCollectMapper;
import com.aurine.cloudx.open.origin.constant.DeviceCollectConstant;
import com.aurine.cloudx.open.origin.constant.enums.DeviceCollectTypeEnum;
import com.aurine.cloudx.open.origin.entity.ProjectDeviceCollect;
import com.aurine.cloudx.open.origin.vo.ProjectDeviceCollectFormVo;
import com.aurine.cloudx.open.origin.vo.ProjectDeviceCollectListVo;
import com.aurine.cloudx.open.origin.service.ProjectDeviceCollectService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 项目设备采集参数
 *
 * @author xull@aurine.cn
 * @date 2020-06-12 11:43:43
 */
@Service
public class ProjectDeviceCollectServiceImpl extends ServiceImpl<ProjectDeviceCollectMapper, ProjectDeviceCollect> implements ProjectDeviceCollectService {


    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public boolean updateDeviceCollectList(ProjectDeviceCollectFormVo projectDeviceCollectFormVo) {
        //先删除属性值
        remove(Wrappers.lambdaUpdate(ProjectDeviceCollect.class)
                .eq(ProjectDeviceCollect::getDeviceType, projectDeviceCollectFormVo.getType())
                .eq(ProjectDeviceCollect::getProjectId, projectDeviceCollectFormVo.getProjectId()));
        //再插入数据
        List<ProjectDeviceCollect> projectDeviceCollectList = projectDeviceCollectFormVo.getProjectDeviceCollectList();
        projectDeviceCollectList.forEach(projectDeviceCollect -> {
                    projectDeviceCollect.setDeviceType(projectDeviceCollectFormVo.getType());
                    projectDeviceCollect.setProjectId(projectDeviceCollectFormVo.getProjectId());
                }
        );
        saveBatch(projectDeviceCollectList);
        return true;
    }

    @Override
    public List<ProjectDeviceCollectListVo> getDeviceCollectListVo(Integer projectId, String type) {
        return baseMapper.getDeviceCollectListVo(projectId, type, null);
    }

    @Override
    public List<ProjectDeviceCollectListVo> getDeviceCollectListVo(Integer projectId, String type, String param) {
        return baseMapper.getDeviceCollectListVo(projectId, type, param);
    }

    @Override
    public String getPoliceEnable(Integer projectId) {
        //获取公安对接接口判断是否启用
        List<ProjectDeviceCollectListVo> ProjectDeviceCollectListVos = baseMapper
                .getDeviceCollectListVo(projectId, DeviceCollectTypeEnum.POLICE.code, DeviceCollectConstant.POLICE_PARAM_NAME);
        String policeStatus = "0";
        if (ProjectDeviceCollectListVos != null && ProjectDeviceCollectListVos.size() > 0) {
            String status= ProjectDeviceCollectListVos.get(0).getAttrValue();
            if (StringUtil.isNotBlank(status)) {
                policeStatus = status;
            }
        }
        return policeStatus;
    }
}
