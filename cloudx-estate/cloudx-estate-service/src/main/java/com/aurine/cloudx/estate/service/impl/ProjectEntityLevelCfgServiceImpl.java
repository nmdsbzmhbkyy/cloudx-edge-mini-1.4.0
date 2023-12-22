
package com.aurine.cloudx.estate.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.constant.BuildingConstant;
import com.aurine.cloudx.estate.constant.enums.FrameTypeEnum;
import com.aurine.cloudx.estate.entity.ProjectEntityLevelCfg;
import com.aurine.cloudx.estate.entity.ProjectFrameInfo;
import com.aurine.cloudx.estate.mapper.ProjectEntityLevelCfgMapper;
import com.aurine.cloudx.estate.service.ProjectEntityLevelCfgService;
import com.aurine.cloudx.estate.service.ProjectFrameInfoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.data.tenant.TenantContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        List<ProjectEntityLevelCfg> levelCfgList = list(new QueryWrapper<ProjectEntityLevelCfg>().lambda().eq(ProjectEntityLevelCfg::getProjectId,ProjectContextHolder.getProjectId()));
        for (ProjectEntityLevelCfg levelCfg : levelCfgList) {
            //跳过 楼栋 房屋 单元
            if (levelCfg.getLevel() <= 3) continue;

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
        int countBiuldingHaveGroup = this.projectFrameInfoService.count(new QueryWrapper<ProjectFrameInfo>().lambda().eq(ProjectFrameInfo::getLevel, FrameTypeEnum.BUILDING.code).ne(ProjectFrameInfo::getPuid, ""));
        if (countBiuldingHaveGroup >= 1) {
            return R.failed("组团已关联楼栋，无法关闭");
        } else {

            //关闭组团（层级4-N）
            List<ProjectEntityLevelCfg> levelCfgList = list(new QueryWrapper<ProjectEntityLevelCfg>().lambda()
                    .eq(ProjectEntityLevelCfg::getProjectId,ProjectContextHolder.getProjectId())
                    .eq(ProjectEntityLevelCfg::getTenantId,TenantContextHolder.getTenantId()));
            for (ProjectEntityLevelCfg levelCfg : levelCfgList) {
                //跳过 楼栋 房屋 单元
                if (levelCfg.getLevel() <= 3) continue;
                levelCfg.setIsDisable("1");
            }

            updateBatchById(levelCfgList);
            this.projectFrameInfoService.remove(new QueryWrapper<ProjectFrameInfo>().lambda().gt(ProjectFrameInfo::getLevel, FrameTypeEnum.BUILDING.code));
            return R.ok();
        }
    }

    @Override
    public boolean checkIsEnabled() {
        List<ProjectEntityLevelCfg> list = this.list(new QueryWrapper<ProjectEntityLevelCfg>().lambda()
                .eq(ProjectEntityLevelCfg::getLevel, 4).eq(ProjectEntityLevelCfg::getIsDisable, 0).eq(ProjectEntityLevelCfg::getTenantId, TenantContextHolder.getTenantId()).eq(ProjectEntityLevelCfg::getProjectId, ProjectContextHolder.getProjectId()));
        return CollUtil.isNotEmpty(list);
    }


}
