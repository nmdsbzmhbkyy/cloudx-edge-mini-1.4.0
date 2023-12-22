
package com.aurine.cloudx.open.origin.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.common.core.util.StringUtil;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.open.origin.vo.ProjectDeviceNoRule;
import com.aurine.cloudx.open.origin.mapper.ProjectEntityLevelCfgMapper;
import com.aurine.cloudx.open.origin.constant.BuildingConstant;
import com.aurine.cloudx.open.origin.constant.enums.FrameTypeEnum;
import com.aurine.cloudx.open.origin.entity.ProjectConfig;
import com.aurine.cloudx.open.origin.entity.ProjectEntityLevelCfg;
import com.aurine.cloudx.open.origin.entity.ProjectFrameInfo;
import com.aurine.cloudx.open.origin.vo.ProjectEntityLevelCfgVo;
import com.aurine.cloudx.open.origin.service.ProjectConfigService;
import com.aurine.cloudx.open.origin.service.ProjectDeviceInfoService;
import com.aurine.cloudx.open.origin.service.ProjectEntityLevelCfgService;
import com.aurine.cloudx.open.origin.service.ProjectFrameInfoService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.data.tenant.TenantContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 配置项目区域层级
 *
 * @author pigx code generator
 * @date 2020-05-06 13:49:41
 */
@Service
public class ProjectEntityLevelCfgServiceImpl extends ServiceImpl<ProjectEntityLevelCfgMapper, ProjectEntityLevelCfg> implements ProjectEntityLevelCfgService {

    @Autowired
    ProjectFrameInfoService projectFrameInfoService;
    @Autowired
    ProjectConfigService projectConfigService;
    @Autowired
    ProjectDeviceInfoService projectDeviceInfoService;
    @Override
    public IPage<ProjectEntityLevelCfg> page(Page page, ProjectEntityLevelCfg searchProjectEntityLevelCfg) {
        int projectId = searchProjectEntityLevelCfg.getProjectId();
        Page returnPage = getBaseMapper().selectPage(page, Wrappers.query(searchProjectEntityLevelCfg));

        //首次进入框架配置时，如果找不到配置信息，则从默认配置复制一份
        if (returnPage.getRecords().size() == 0) {
            searchProjectEntityLevelCfg.setProjectId(BuildingConstant.DEFAULT_PROJECT_ID);
            /**
             * 修正多租户情况下，无法正常获取默认组团数据的问题
             * @author: 王伟 2020-07-07
             */
            searchProjectEntityLevelCfg.setTenantId(BuildingConstant.DEFAULT_TENANT_ID);

            List<ProjectEntityLevelCfg> cfgList = getBaseMapper().selectList(
                    Wrappers.query(searchProjectEntityLevelCfg).orderByAsc("level"));

            for (ProjectEntityLevelCfg cfg : cfgList) {
                cfg.setProjectId(projectId);
                cfg.setTenantId(TenantContextHolder.getTenantId());
            }
            this.saveBatch(cfgList);

            //重新查询当前项目配置数据
            searchProjectEntityLevelCfg.setProjectId(projectId);
            returnPage = getBaseMapper().selectPage(page, Wrappers.query(searchProjectEntityLevelCfg).orderByAsc("level"));

        }
        return returnPage;
    }

    @Override
    public boolean swithFrame(int id, int val) {
        ProjectEntityLevelCfg cfg = this.getById(id);
        if (val == 0) {// 启动
            cfg.setIsDisable("0");
        } else { //禁用

            //如果当前层级存在数据，禁止禁用
            int count = projectFrameInfoService.countByLevel(cfg.getLevel());
            if (count >= 1) {
                return false;
            }

            cfg.setIsDisable("1");
        }
        return this.updateById(cfg);
    }

    /**
     * 初始化组团信息
     *
     * @return
     */
    @Override
    public boolean initEntityLevelCfg(Integer projectId, Integer tenantId) {
        //检查是否已经存在组团信息
        if (this.count(new QueryWrapper<ProjectEntityLevelCfg>().lambda().eq(ProjectEntityLevelCfg::getProjectId, projectId).eq(ProjectEntityLevelCfg::getTenantId, tenantId)) == 0) {
            ProjectEntityLevelCfg searchProjectEntityLevelCfg = new ProjectEntityLevelCfg();

            searchProjectEntityLevelCfg.setProjectId(BuildingConstant.DEFAULT_PROJECT_ID);
            searchProjectEntityLevelCfg.setTenantId(BuildingConstant.DEFAULT_TENANT_ID);

            List<ProjectEntityLevelCfg> cfgList = getBaseMapper().selectList(Wrappers.query(searchProjectEntityLevelCfg).orderByAsc("level"));

            for (ProjectEntityLevelCfg cfg : cfgList) {
                cfg.setProjectId(projectId);
                cfg.setTenantId(TenantContextHolder.getTenantId());
            }
            this.saveBatch(cfgList);

        }
        return false;
    }


    @Override
    public Map<String, Object> getFrameList() {
        Page page = new Page();
        page.setSize(100);
        page.setCurrent(1);
        Integer[] levels = {4, 5, 6, 7};
        IPage<ProjectEntityLevelCfg> projectEntityLevelCfgIPage = this.page(page,
                new QueryWrapper<ProjectEntityLevelCfg>().lambda().eq(ProjectEntityLevelCfg::getTenantId, TenantContextHolder.getTenantId()).eq(ProjectEntityLevelCfg::getProjectId, ProjectContextHolder.getProjectId()).in(ProjectEntityLevelCfg::getLevel, levels));
        //获取所有本项目的层级列表
        List<ProjectEntityLevelCfg> records = projectEntityLevelCfgIPage.getRecords();
        Map<String, Object> frameMap = new HashMap<>();
        for (ProjectEntityLevelCfg entity :records) {
            List<ProjectFrameInfo> data = projectFrameInfoService.list(new QueryWrapper<ProjectFrameInfo>().lambda()
                    .eq(ProjectFrameInfo::getLevel, entity.getLevel()));
            if (data.size() == 0) {
                continue;
            }
            //这里的l是因为前段直接用数字无法从map中点出数据，得要是字符串才行，根据组团的四个级别存储到l1 l2 l3 l4 方便前端取出
            frameMap.put('l' + data.get(0).getLevel().toString(), data);
        }
        return frameMap;
    }

    /**
     * 开启当前项目下的层级，如果如开启LV6,则开启LV1-6
     * 如果当前项目下存在LV4-LV6的组团实体，则禁止修改
     *
     * @param level
     * @return
     * @author: 王伟
     */
    @Override
    public R activeLevel(int level) {
        //检查要开启的层级以内是否存在数据
        int count = this.projectFrameInfoService.count(new QueryWrapper<ProjectFrameInfo>().lambda().ge(ProjectFrameInfo::getLevel, 4));
        if (count >= 1) {
            return R.failed("已存在组团数据，无法变更组团层级");
        }
        //切换开关状态（层级4-N）
        List<ProjectEntityLevelCfg> levelCfgList = list(new QueryWrapper<ProjectEntityLevelCfg>().lambda().eq(ProjectEntityLevelCfg::getProjectId, ProjectContextHolder.getProjectId()));
        for (ProjectEntityLevelCfg levelCfg : levelCfgList) {
            //跳过 楼栋 房屋 单元
            if (levelCfg.getLevel() <= 3){
                continue;
            }

            if (level >= levelCfg.getLevel()) {
                levelCfg.setIsDisable("0");
            } else {
                levelCfg.setIsDisable("1");
            }
        }

        updateBatchById(levelCfgList);

        return R.ok();
    }

    /**
     * 关闭组团功能，并清空数据
     * 如果已经挂接楼栋，禁止删除
     *
     * @return
     * @author: 王伟
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R disableAllLevel() {
        //检查楼栋是否关联组团
        /**
         * 只要项目内存在楼栋，就禁止修改组团数据
         */
//        int countBiuldingHaveGroup = this.projectFrameInfoService.count(new QueryWrapper<ProjectFrameInfo>().lambda().eq(ProjectFrameInfo::getLevel, FrameTypeEnum.BUILDING.code).ne(ProjectFrameInfo::getPuid, ""));
        int countBiuldingHaveGroup = this.projectFrameInfoService.count(new QueryWrapper<ProjectFrameInfo>().lambda().eq(ProjectFrameInfo::getLevel, FrameTypeEnum.BUILDING.code));


        if (countBiuldingHaveGroup >= 1) {
            return R.failed("项目已创建楼栋，无法关闭组团");
        } else {
            ProjectEntityLevelCfg searchProjectEntityLevelCfg = new ProjectEntityLevelCfg();

            searchProjectEntityLevelCfg.setProjectId(BuildingConstant.DEFAULT_PROJECT_ID);
            searchProjectEntityLevelCfg.setTenantId(BuildingConstant.DEFAULT_TENANT_ID);

            List<ProjectEntityLevelCfg> cfgList = getBaseMapper().selectList(Wrappers.query(searchProjectEntityLevelCfg).orderByAsc("level"));


            //关闭组团（层级4-N）
            List<ProjectEntityLevelCfg> levelCfgList = list(new QueryWrapper<ProjectEntityLevelCfg>().lambda()
                    .eq(ProjectEntityLevelCfg::getProjectId, ProjectContextHolder.getProjectId())
                    .eq(ProjectEntityLevelCfg::getTenantId,TenantContextHolder.getTenantId()));
            for (ProjectEntityLevelCfg levelCfg : levelCfgList) {
                //跳过 楼栋 房屋 单元
                if (levelCfg.getLevel() <= 3){
                    continue;
                }
                levelCfg.setIsDisable("1");
                List<ProjectEntityLevelCfg> collect = cfgList.stream().filter(item -> item.getLevel().intValue() == levelCfg.getLevel().intValue()).collect(Collectors.toList());
                levelCfg.setCodeRule(collect.get(0).getCodeRule());
                levelCfg.setLevelDesc(collect.get(0).getLevelDesc());
            }

            updateBatchById(levelCfgList);
            this.projectFrameInfoService.remove(new QueryWrapper<ProjectFrameInfo>().lambda().gt(ProjectFrameInfo::getLevel, FrameTypeEnum.BUILDING.code));

            //关闭组团后通行方案要自动改为所有区口门禁、
            Integer projectId = ProjectContextHolder.getProjectId();
            baseMapper.putMacroId(projectId);

            return R.ok();
        }
    }

    @Override
    public boolean checkIsEnabled() {
        List<ProjectEntityLevelCfg> list = this.list(new QueryWrapper<ProjectEntityLevelCfg>().lambda()
                .eq(ProjectEntityLevelCfg::getLevel, 4).eq(ProjectEntityLevelCfg::getIsDisable, 0).eq(ProjectEntityLevelCfg::getTenantId, TenantContextHolder.getTenantId()).eq(ProjectEntityLevelCfg::getProjectId, ProjectContextHolder.getProjectId()));
        return CollUtil.isNotEmpty(list);
    }

    @Override
    public int getCodeRuleByLevel(String level) {
        ProjectEntityLevelCfg cfg = this.getOne(new LambdaQueryWrapper<ProjectEntityLevelCfg>()
                .eq(ProjectEntityLevelCfg::getLevel, level)
                .eq(ProjectEntityLevelCfg::getProjectId, ProjectContextHolder.getProjectId()));
        return cfg != null ? cfg.getCodeRule() : 0;
    }

    @Override
    public ProjectDeviceNoRule getProjectSubSection(Integer projectId) {
        return this.baseMapper.getProjectSubSection(projectId);
    }

    @Override
    public String getProjectSubDesc(Integer projectId) {
        try {
            List<ProjectEntityLevelCfg> entityLevelCfgList = this.list(new QueryWrapper<ProjectEntityLevelCfg>().lambda()
                    .eq(ProjectEntityLevelCfg::getProjectId, ProjectContextHolder.getProjectId())
                    .select(ProjectEntityLevelCfg::getLevelDesc)
                    .eq(ProjectEntityLevelCfg::getIsDisable, "0")
                    .orderByDesc(ProjectEntityLevelCfg::getLevel));
            return new ObjectMapper().writeValueAsString(entityLevelCfgList.stream().map(ProjectEntityLevelCfg::getLevelDesc).collect(Collectors.toList()));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R updateFrame(ProjectEntityLevelCfgVo projectEntityLevelCfgVo) {
        int countBiuldingHaveGroup = this.projectFrameInfoService.count(new QueryWrapper<ProjectFrameInfo>().lambda().gt(ProjectFrameInfo::getLevel, FrameTypeEnum.BUILDING.code));

        if (countBiuldingHaveGroup >= 1) {
            return R.failed("已存在组团或楼栋，不可修改");
        }else {
            this.update(new UpdateWrapper<ProjectEntityLevelCfg>().lambda()
                    .eq(ProjectEntityLevelCfg::getLevel, 1)
                    .eq(ProjectEntityLevelCfg::getProjectId, ProjectContextHolder.getProjectId())
                    .set(ProjectEntityLevelCfg::getCodeRule, projectEntityLevelCfgVo.getHouseRule())
                    .set(ProjectEntityLevelCfg::getLevelDesc, projectEntityLevelCfgVo.getHouseDesc())
            );
            this.update(new UpdateWrapper<ProjectEntityLevelCfg>().lambda()
                    .eq(ProjectEntityLevelCfg::getLevel, 2)
                    .eq(ProjectEntityLevelCfg::getProjectId, ProjectContextHolder.getProjectId())
                    .set(ProjectEntityLevelCfg::getCodeRule, projectEntityLevelCfgVo.getUnitRule())
                    .set(ProjectEntityLevelCfg::getLevelDesc, projectEntityLevelCfgVo.getUnitDesc())
            );
            this.update(new UpdateWrapper<ProjectEntityLevelCfg>().lambda()
                    .eq(ProjectEntityLevelCfg::getLevel, 3)
                    .eq(ProjectEntityLevelCfg::getProjectId, ProjectContextHolder.getProjectId())
                    .set(ProjectEntityLevelCfg::getCodeRule, projectEntityLevelCfgVo.getBuildingRule())
                    .set(ProjectEntityLevelCfg::getLevelDesc, projectEntityLevelCfgVo.getBuildingDesc())
            );
      /*      if(ObjectUtil.isNull(projectEntityLevelCfgVo.getMaxLevel())){
                return R.failed("请选择组团");
            }*/
            if( null!=projectEntityLevelCfgVo.getMaxLevel() && projectEntityLevelCfgVo.getMaxLevel()>3) {
              /*  this.update(new UpdateWrapper<ProjectEntityLevelCfg>().lambda()
                        .eq(ProjectEntityLevelCfg::getProjectId, ProjectContextHolder.getProjectId())
                        .eq(ProjectEntityLevelCfg::getLevel, projectEntityLevelCfgVo.getMaxLevel())
                        .set(ProjectEntityLevelCfg::getCodeRule, projectEntityLevelCfgVo.getGroupRule())
                        .set(ProjectEntityLevelCfg::getLevelDesc, projectEntityLevelCfgVo.getGroupDesc())
                );*/
                JSONObject groupDescArray = projectEntityLevelCfgVo.getGroupDescArray();
                JSONObject groupRuleArray = projectEntityLevelCfgVo.getGroupRuleArray();
                for (int i = 4; i <= projectEntityLevelCfgVo.getMaxLevel() ; i++) {
                    // 获取前端传来的组团规则与描述
                    String groupDesc = groupDescArray.getString(i+"");
                    String groupRule = groupRuleArray.getString(i+"");
                    if(StringUtil.isEmpty(groupDesc)){
                       return R.failed(i+"级组团描述不能为空");

                    }
                    // 循环更新
                    this.update(new UpdateWrapper<ProjectEntityLevelCfg>().lambda()
                            .eq(ProjectEntityLevelCfg::getProjectId, ProjectContextHolder.getProjectId())
                            .eq(ProjectEntityLevelCfg::getLevel, i)
                            .set(ProjectEntityLevelCfg::getCodeRule, groupRule)
                            .set(ProjectEntityLevelCfg::getLevelDesc, groupDesc)
                    );
                }
            }
            int count = projectConfigService.count(new QueryWrapper<ProjectConfig>().lambda().eq(ProjectConfig::getProjectId, ProjectContextHolder.getProjectId()));
            if( count == 0 ) {
                ProjectConfig config = new ProjectConfig();
                config.setProjectId(ProjectContextHolder.getProjectId());
                config.setFloorNoLen(projectEntityLevelCfgVo.getCellRule());
                projectConfigService.save(config);
            }else {
                projectConfigService.update(new UpdateWrapper<ProjectConfig>().lambda()
                        .eq(ProjectConfig::getProjectId, ProjectContextHolder.getProjectId())
                        .set(ProjectConfig::getFloorNoLen, projectEntityLevelCfgVo.getCellRule())

                );
            }

        }
        return R.ok();

    }
}
