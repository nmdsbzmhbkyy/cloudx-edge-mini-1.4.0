
package com.aurine.cloudx.estate.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.func.Func1;
import cn.hutool.core.util.StrUtil;
import com.aurine.cloudx.estate.entity.ProjectParkBillingRule;
import com.aurine.cloudx.estate.entity.ProjectParkRegion;
import com.aurine.cloudx.estate.entity.ProjectParkingInfo;
import com.aurine.cloudx.estate.entity.SysServiceCfg;
import com.aurine.cloudx.estate.mapper.ProjectParkingInfoMapper;
import com.aurine.cloudx.estate.service.*;
import com.aurine.cloudx.estate.vo.ProjectParkingInfoVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.common.data.tenant.TenantContextHolder;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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

    @Resource
    private ProjectEntryExitLaneService projectEntryExitLaneService;
    private final ProjectParkingInfoMapper projectParkingInfoMapper;
    private final ProjectParkRegionService projectParkRegionService;
    private final ProjectParkBillingRuleService projectParkBillingRuleService;
    @Resource
    private SysServiceCfgService sysServiceCfgService;

    @Resource
    private final RedisTemplate redisTemplateAurine;

    @Override
    public IPage<ProjectParkingInfoVo> findPage(IPage<ProjectParkingInfoVo> page, ProjectParkingInfoVo projectParkingInfoVo) {
        /**
         * 修正查询车场列表会展现所有项目 车厂的问题
         * @since 2020-07-29
         * @author: 王伟
         */
//        projectParkingInfoVo.setProjectId(ProjectContextHolder.getProjectId());
        projectParkingInfoVo.setTenantId(TenantContextHolder.getTenantId());

        IPage<ProjectParkingInfoVo> select = projectParkingInfoMapper.select(page, projectParkingInfoVo, projectParkingInfoVo.getProjectId());
        List<ProjectParkingInfoVo> records = select.getRecords();
        if (records.size() != 0) {
            for (ProjectParkingInfoVo record : records) {
                if (!StrUtil.hasEmpty(record.getCompany())) {
                    SysServiceCfg SysServiceCfgOne = sysServiceCfgService.getOne(new LambdaQueryWrapper<SysServiceCfg>().eq(SysServiceCfg::getServiceCode, record.getCompany()));
                    if (SysServiceCfgOne != null) {
                        record.setManufacturer(SysServiceCfgOne.getManufacturer());
                    }
                }
            }
        }
        select.setRecords(records);
        return select;
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
        projectParkBillingRuleService.initByCompany(planId, parkingInfo.getCompany(), parkingInfo.getProjectId(), parkingInfo);
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
        boolean result = this.updateById(parkingInfo);
        if (result && projectParkBillingRuleService.count(new QueryWrapper<ProjectParkBillingRule>().lambda().eq(ProjectParkBillingRule::getParkId, parkingInfo.getParkId())) == 0) {
            projectParkBillingRuleService.initByCompany(parkingInfo.getParkId(), parkingInfo.getCompany(), parkingInfo.getProjectId(), parkingInfo);
        }

        return result;
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
            return false;
        }
        return this.removeById(parkId);
//        return true;
    }

    @Override
    public void init(String projectName, Integer projectId, Integer tenantId) {
        ProjectParkingInfo parkingInfo = new ProjectParkingInfo();
        parkingInfo.setProjectId(projectId);
        parkingInfo.setParkName(projectName + "停车场");
        this.save(parkingInfo);
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

    /**
     * 更新车道
     *
     * @param parkName
     * @param projectId
     * @return
     */
    @Override
    public boolean syncPark(String parkName, Integer projectId) {
        ProjectParkingInfo one = this.getOne(new LambdaQueryWrapper<ProjectParkingInfo>()
                .eq(ProjectParkingInfo::getParkName, parkName)
                .eq(ProjectParkingInfo::getProjectId, projectId)
                .eq(ProjectParkingInfo::getTenantId, TenantContextHolder.getTenantId()));

        if (one != null) {
            String parkId = one.getParkId();
            boolean b = projectEntryExitLaneService.syncLane(parkId);
            if (b == true) {
//                throw new RuntimeException("添加失败，请在车场管理中更新车道");
                return true;
            }
        }
        return false;

    }

    @Override
    public String getParkIdByParkName(String redisKey, String parkName, Integer projectId) {
        if (redisTemplateAurine.hasKey(redisKey)) {
            return (String) redisTemplateAurine.opsForHash().get(redisKey, parkName);
        } else {
            this.initParkCache(redisKey, projectId);
            return this.getParkIdByParkName(redisKey, parkName, projectId);
        }
    }

    @Override
    public void deleteParkTmpCache(String redisKey) {
        redisTemplateAurine.delete(redisKey);
    }

    private void initParkCache(String redisKey, Integer projectId) {
        List<ProjectParkingInfo> parkingInfoList = this.list(new QueryWrapper<ProjectParkingInfo>().lambda().eq(ProjectParkingInfo::getProjectId, projectId));
        Map<String, String> parkDictMap;
        if (CollUtil.isNotEmpty(parkingInfoList)) {
            parkDictMap = parkingInfoList.stream().filter(projectParkingInfo -> StrUtil.isNotEmpty(projectParkingInfo.getParkName()))
                    .collect(Collectors.toMap(ProjectParkingInfo::getParkName, ProjectParkingInfo::getParkId, (s, s2) -> s2));
        } else {
            parkDictMap = new HashMap<>();
            parkDictMap.put("1", "1");
        }
        redisTemplateAurine.delete(redisKey);
        redisTemplateAurine.opsForHash().putAll(redisKey, parkDictMap);
        redisTemplateAurine.expire(redisKey, 2, TimeUnit.HOURS);
    }
}
