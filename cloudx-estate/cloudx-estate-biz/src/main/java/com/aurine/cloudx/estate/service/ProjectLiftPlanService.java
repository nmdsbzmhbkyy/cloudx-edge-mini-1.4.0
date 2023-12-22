package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.constant.enums.PassMacroIdEnum;
import com.aurine.cloudx.estate.constant.enums.PersonTypeEnum;
import com.aurine.cloudx.estate.entity.ProjectLiftPlan;
import com.aurine.cloudx.estate.entity.ProjectPassPlan;
import com.aurine.cloudx.estate.vo.ProjectDeviceVo;
import com.aurine.cloudx.estate.vo.ProjectPassDeviceVo;
import com.aurine.cloudx.estate.vo.ProjectLiftPlanVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>电梯通行方案接口</p>
 *
 * @ClassName: ProjectLiftPlanService
 * @author: 电梯 <chenz@aurine.cn>
 * @date: 2022/2/22 15:18
 * @Copyright:
 */
public interface ProjectLiftPlanService extends IService<ProjectLiftPlan> {


//    @Transactional(rollbackFor = Exception.class)
    boolean update(ProjectLiftPlanVo projectLiftPlanVo);

    /**
     * 保存
     *
     * @param projectLiftPlanVo
     * @return
     */
    boolean save(ProjectLiftPlanVo projectLiftPlanVo);


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
    ProjectLiftPlan getDefaultPlan(PersonTypeEnum personTypeEnum);



    /**
     * 获取通行方案VO
     *
     * @param id
     * @return
     */
    ProjectLiftPlanVo getVo(String id);

//    /**
//     * 根据方案,获取该方案下所有可以使用的设备
//     *
//     * @param planId
//     * @param buildingId
//     * @param unitId
//     * @return
//     */
//    List<ProjectLiftPlan> listDeviceByPlanId(String planId, String buildingId, String unitId);


    /**
     * 根据方案类型（方案对象） 获取方案列表，默认方案在前，其他的按照时间排序
     *
     * @param planObject
     * @return
     */
    List<ProjectLiftPlan> listByType(String planObject);

    /**
     * 获取默认通行方案(微信相关接口)
     * @return
     */
    ProjectLiftPlan getdefaultPass();
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
    List<ProjectLiftPlan> listByMacro(PassMacroIdEnum macroEnum);

}
