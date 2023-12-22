package com.aurine.cloudx.estate.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.aurine.cloudx.estate.constant.enums.PassRightTokenStateEnum;
import com.aurine.cloudx.estate.entity.ProjectFingerprints;
import com.aurine.cloudx.estate.mapper.ProjectFingerprintsMapper;
import com.aurine.cloudx.estate.service.ProjectFingerprintsService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 记录项目辖区内允许通行的指纹信息，供辖区内已开放通行权限的指纹识别设备下载
 * </p>
 *
 * @author 王良俊
 * @date 2020-05-22 11:20:22
 */
@Service
public class ProjectFingerprintsServiceImpl extends ServiceImpl<ProjectFingerprintsMapper, ProjectFingerprints> implements ProjectFingerprintsService {

    @Override
    public List<ProjectFingerprints> listByPersonId(String personId) {
        return this.list(new QueryWrapper<ProjectFingerprints>().lambda()
                .eq(ProjectFingerprints::getPersonId, personId).eq(ProjectFingerprints::getStatus, PassRightTokenStateEnum.USED.code));
    }

    @Override
    public List<ProjectFingerprints> listByPersonId(List<String> personIdList) {
        if (CollUtil.isNotEmpty(personIdList)) {
            return this.list(new QueryWrapper<ProjectFingerprints>().lambda().in(ProjectFingerprints::getPersonId, personIdList));
        }
        return new ArrayList<>();
    }

    /**
     * 根据第三方id和项目编号，获取对象
     *
     * @param code
     * @param projectId
     * @return
     * @author: 王伟
     * @since 2020-08-20
     */
    @Override
    public ProjectFingerprints getByCode(String code, int projectId) {
        return this.baseMapper.getByCode(projectId,code);
    }

}
