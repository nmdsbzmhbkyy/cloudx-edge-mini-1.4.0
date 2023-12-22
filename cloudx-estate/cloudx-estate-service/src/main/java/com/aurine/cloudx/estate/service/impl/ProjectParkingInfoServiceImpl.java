
package com.aurine.cloudx.estate.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.entity.ProjectParkRegion;
import com.aurine.cloudx.estate.entity.ProjectParkingInfo;
import com.aurine.cloudx.estate.mapper.ProjectParkingInfoMapper;
import com.aurine.cloudx.estate.service.ProjectParkBillingRuleService;
import com.aurine.cloudx.estate.service.ProjectParkRegionService;
import com.aurine.cloudx.estate.service.ProjectParkingInfoService;
import com.aurine.cloudx.estate.vo.ProjectParkingInfoVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.common.data.tenant.TenantContextHolder;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * 停车区域
 *
 * @author 王良俊
 * @date 2020-05-07 09:13:25
 */
@Service
@AllArgsConstructor
public class ProjectParkingInfoServiceImpl extends ServiceImpl<ProjectParkingInfoMapper, ProjectParkingInfo>
        implements ProjectParkingInfoService {

    private final ProjectParkingInfoMapper projectParkingInfoMapper;
    private final ProjectParkRegionService projectParkRegionService;
    private final ProjectParkBillingRuleService projectParkBillingRuleService;

    @Override
    public IPage<ProjectParkingInfoVo> findPage(IPage<ProjectParkingInfoVo> page, ProjectParkingInfoVo projectParkingInfoVo) {
        /**
         * 修正查询车场列表会展现所有项目 车厂的问题
         * @since 2020-07-29
         * @author: 王伟
         */
//        projectParkingInfoVo.setProjectId(ProjectContextHolder.getProjectId());
        projectParkingInfoVo.setTenantId(TenantContextHolder.getTenantId());

        return projectParkingInfoMapper.select(page, projectParkingInfoVo);
    }

    @Override
    public boolean saveParkInfo(ProjectParkingInfo parkingInfo) {

        //核对是否重名
        boolean isExist = this.checkExist(parkingInfo.getParkName(), parkingInfo.getProjectId());
        if (isExist) {
            throw new RuntimeException("车场名称已存在");
        }

        /**
         * 车厂的第三方编号不可重复
         * @author: 王伟
         * @since 2020-07-29
         */
        if (getByThirdCode(parkingInfo.getParkCode()) != null) {
            throw new RuntimeException("请勿重复绑定车场第三方编号");
        }

        parkingInfo.setParkNum(0);

        String planId = UUID.randomUUID().toString().replace("-", "");
        parkingInfo.setParkId(planId);
        projectParkBillingRuleService.initByCompany(planId, parkingInfo.getCompany(), parkingInfo.getProjectId());
        return this.save(parkingInfo);
    }

    @Override
    public boolean updateParkInfo(ProjectParkingInfo parkingInfo) {

        //核对是否重名
        boolean isExist = this.checkExist(parkingInfo.getParkId(), parkingInfo.getParkName(), parkingInfo.getProjectId());
        if (isExist) {
            throw new RuntimeException("车场名称已存在");
        }

        /**
         * 车厂的第三方编号不可重复
         * @author: 王伟
         * @since 2020-07-29
         */
        if (checkThirdCodeRepeat(parkingInfo.getParkId(), parkingInfo.getParkCode())) {
            throw new RuntimeException("请勿重复绑定车场第三方编号");
        }

        return this.updateById(parkingInfo);
    }

    /**
     * 根据第三方编号，获取车场信息
     *
     * @param code
     * @return
     * @author: 王伟
     * @since 2020-07-29
     */
    @Override
    public ProjectParkingInfo getByThirdCode(String code) {

        if (StringUtils.isEmpty(code)) {
            return null;
        }

        List<ProjectParkingInfo> parkingInfoList = this.list(new QueryWrapper<ProjectParkingInfo>().lambda()
                .eq(ProjectParkingInfo::getParkCode, code));

        if (CollectionUtil.isNotEmpty(parkingInfoList)) {
            return parkingInfoList.get(0);
        } else {
            return null;
        }
    }

    /**
     * 验证第三方编号是否重复
     *
     * @param parkingId
     * @param code
     * @return
     */
    @Override
    public boolean checkThirdCodeRepeat(String parkingId, String code) {

        if (StringUtils.isEmpty(code)) {
            return false;
        }

        int count = this.count(new QueryWrapper<ProjectParkingInfo>().lambda()
                .eq(ProjectParkingInfo::getParkCode, code)
                .notLike(StringUtils.isNotEmpty(parkingId), ProjectParkingInfo::getParkId, parkingId));

        return count >= 1;
    }

    @Override
    public boolean removeParkById(String parkId) {
        int count = projectParkRegionService.count(new QueryWrapper<ProjectParkRegion>().lambda()
                .eq(ProjectParkRegion::getParkId, parkId));
        if (count != 0) {
            throw new RuntimeException("车场已对接无法删除");
        }
        return this.removeById(parkId);
    }

    private boolean checkExist(String parkName, Integer projectId) {
        int count = this.count(new QueryWrapper<ProjectParkingInfo>().lambda()
                .eq(ProjectParkingInfo::getParkName, parkName)
                .eq(ProjectParkingInfo::getProjectId, projectId)
                .eq(ProjectParkingInfo::getTenantId, TenantContextHolder.getTenantId()));
        return count > 0;
    }

    private boolean checkExist(String id, String parkName, Integer projectId) {
        int count = this.count(new QueryWrapper<ProjectParkingInfo>().lambda().notIn(ProjectParkingInfo::getParkId, id)
                .eq(ProjectParkingInfo::getParkName, parkName)
                .eq(ProjectParkingInfo::getProjectId, projectId)
                .eq(ProjectParkingInfo::getTenantId, TenantContextHolder.getTenantId())
        );
        return count > 0;
    }
}
