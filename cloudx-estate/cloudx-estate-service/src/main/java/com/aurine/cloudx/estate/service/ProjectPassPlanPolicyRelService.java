

package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.constant.enums.PassMacroIdEnum;
import com.aurine.cloudx.estate.entity.ProjectPassPlan;
import com.aurine.cloudx.estate.entity.ProjectPassPlanPolicyRel;
import com.aurine.cloudx.estate.vo.ProjectPassDeviceVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 策略方案关联
 *
 * @author pigx code generator
 * @date 2020-05-20 15:54:21
 */
public interface ProjectPassPlanPolicyRelService extends IService<ProjectPassPlanPolicyRel> {

    /**
     * 根据方案,获取该方案下所有可以使用的设备
     *
     * @param projectPassPlan
     * @param buildingId
     * @param unitId
     * @return
     */
    List<ProjectPassDeviceVo> listDeviceByPlan(ProjectPassPlan projectPassPlan, String buildingId, String unitId);

    /**
     * 通过方案ID，获取该方案的逻辑策略的宏类型数组
     *
     * @param planId
     * @return
     */
    String[] listMacroIdArrayByPlanId(String planId);

    /**
     * 通过方案id获取方案宏列表
     *
     * @param planId
     * @return
     */
    List<String> listMacroIdListByPlanId(String planId);

    /**
     * 通过personId获取该用户使用方案的宏列表
     *
     * @param personId
     * @return
     */
    List<String> listMacroIdListByPersonId(String personId);

    /**
     * 通过方案ID，获取该方案的物理策略的设备ID数组
     *
     * @param planId
     * @return
     */
    String[] listPhysicalDeviceIdArrayByPlanId(String planId);

    /**
     * 通过逻辑宏名称，获取使用该宏策略的方案ID
     *
     * @param macroIdEnum
     * @return
     */
    String[] listPlanIdByMacro(PassMacroIdEnum macroIdEnum);

    /**
     * 逻辑方案策略与关联表批量存储
     *
     * @param macroIdArray
     * @param planId
     * @return
     */
    boolean saveBatchLogic(String[] macroIdArray, String planId);

    /**
     * 物理策略与关联表批量存储
     *
     * @param deviceIdArray
     * @param planId
     * @return
     */
    boolean saveBatchPhysical(String[] deviceIdArray, String planId);

    /**
     * 删除逻辑策略与关联
     *
     * @param planId
     * @return
     */
    boolean removeBathLogic(String planId);

    /**
     * 删除物理策略与关联
     *
     * @param planId
     * @return
     */
    boolean removeBathPhysical(String planId);

}
