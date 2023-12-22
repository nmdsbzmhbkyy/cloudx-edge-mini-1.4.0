

package com.aurine.cloudx.open.origin.service;

import com.aurine.cloudx.open.origin.vo.ProjectDeviceNoRule;
import com.aurine.cloudx.open.origin.entity.ProjectEntityLevelCfg;
import com.aurine.cloudx.open.origin.vo.ProjectEntityLevelCfgVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.common.core.util.R;

import java.util.Map;

/**
 * 配置项目区域层级
 *
 * @author pigx code generator
 * @date 2020-05-06 13:49:41
 */
public interface ProjectEntityLevelCfgService extends IService<ProjectEntityLevelCfg> {

    IPage<ProjectEntityLevelCfg> page(Page page, ProjectEntityLevelCfg projectEntityLevelCfg);

    boolean swithFrame(int id, int val);

    /**
     * 初始化组团信息
     * @return
     */
    boolean initEntityLevelCfg(Integer projectId, Integer tenantId);

    /**
     * <p>
     * 获取level1-4的组团列表
     * </p>
     *
     * @author:王良俊
     */
    Map<String, Object> getFrameList();

    /**
     * 开启当前项目下的层级，如果如开启LV6,则开启LV1-6
     * 如果当前项目下存在LV4-LV6的组团实体，则禁止修改
     *
     * @param level
     * @return
     * @author: 王伟
     */
    R activeLevel(int level);

    /**
     * 关闭组团功能，并清空数据
     * 如果已经挂接楼栋，禁止删除
     *
     * @param
     * @return
     * @author: 王伟
     */
    R disableAllLevel();

    boolean checkIsEnabled();

    R updateFrame(ProjectEntityLevelCfgVo projectEntityLevelCfgVo);

    int getCodeRuleByLevel(String level);

    /**
    * <p>
    * 获取项目分段参数
    * </p>
    *
    * @author: 王良俊
    */
    ProjectDeviceNoRule getProjectSubSection(Integer projectId);

    String getProjectSubDesc(Integer projectId);

}
