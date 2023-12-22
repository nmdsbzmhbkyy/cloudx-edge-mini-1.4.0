package com.aurine.cloudx.open.origin.service;

import com.aurine.cloudx.open.common.entity.vo.PassPlanVo;
import com.aurine.cloudx.open.origin.constant.enums.PassMacroIdEnum;
import com.aurine.cloudx.open.origin.constant.enums.PersonTypeEnum;
import com.aurine.cloudx.open.origin.entity.ProjectPassPlan;
import com.aurine.cloudx.open.origin.vo.ProjectPassDeviceVo;
import com.aurine.cloudx.open.origin.vo.ProjectPassPlanVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>通行方案接口</p>
 *
 * @ClassName: ProjectPassPlanService
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020/5/20 15:18
 * @Copyright:
 */
public interface ProjectPassPlanService extends IService<ProjectPassPlan> {


    //    @Transactional(rollbackFor = Exception.class)
    boolean update(ProjectPassPlanVo projectPassPlanVo);

    /**
     * 保存
     *
     * @param projectPassPlanVo
     * @return
     */
    boolean save(ProjectPassPlanVo projectPassPlanVo);


    /**
     * 根据默认配置生成方案
     *
     * @return
     */
    boolean createByDefault(int projectId, int tenantId);

    /**
     * 清空默认方案
     *
     * @return
     */
    boolean clearDefaultPlan();

    /**
     * 获取当前项目的默认方案
     *
     * @param personTypeEnum
     * @return
     */
    ProjectPassPlan getDefaultPlan(PersonTypeEnum personTypeEnum);


    /**
     * 获取通行方案VO
     *
     * @param id
     * @return
     */
    ProjectPassPlanVo getVo(String id);

    /**
     * 根据方案,获取该方案下所有可以使用的设备
     *
     * @param planId
     * @param buildingId
     * @param unitId
     * @return
     */
    List<ProjectPassDeviceVo> listDeviceByPlanId(String planId, String buildingId, String unitId);


    /**
     * 根据方案类型（方案对象） 获取方案列表，默认方案在前，其他的按照时间排序
     *
     * @param planObject
     * @return
     */
    List<ProjectPassPlan> listByType(String planObject);

    /**
     * 获取默认通行方案(微信相关接口)
     *
     * @return
     */
    ProjectPassPlan getdefaultPass();

    /**
     * 删除方案
     *
     * @param planId
     * @return
     */
    boolean delete(String planId);

    /**
     * 获取使用了指定逻辑策略宏的通行方案
     *
     * @param macroEnum
     * @return
     */
    List<ProjectPassPlan> listByMacro(PassMacroIdEnum macroEnum);

    /**
     * 分页查询
     *
     * @param page
     * @param vo
     * @return
     */
    Page<PassPlanVo> page(Page page, PassPlanVo vo);

}
