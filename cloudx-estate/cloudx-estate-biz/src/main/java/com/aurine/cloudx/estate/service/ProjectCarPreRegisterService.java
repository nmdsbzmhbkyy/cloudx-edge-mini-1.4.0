package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.entity.ProjectCarPreRegister;
import com.aurine.cloudx.estate.vo.CarPreRegisterSearchCondition;
import com.aurine.cloudx.estate.vo.ProjectCarPreRegisterAuditVo;
import com.aurine.cloudx.estate.vo.ProjectCarPreRegisterVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.common.core.util.R;

/**
 * 车辆登记表
 */
public interface ProjectCarPreRegisterService extends IService<ProjectCarPreRegister> {

    /**
    * <p>
    * 分页查询车辆预登记
    * </p>
    *
    * @param page 分页信息
    * @param query 查询条件
    * @author: 王良俊
    */
    Page<ProjectCarPreRegisterVo> fetchList(Page page, CarPreRegisterSearchCondition query);

    /**
     * <p>
     * 车辆登记申请
     * </p>
     *
     * @param carPreRegister 车辆登记申请（手机号、车牌号为必填）
     * @author: 王良俊
     */
    R application(ProjectCarPreRegister carPreRegister);

    /**
     * <p>
     * 拒绝车辆登记申请
     * </p>
     *
     * @param preRegisterId 所要拒绝的申请ID
     * @param reason        拒绝的原因
     * @author: 王良俊
     */
    boolean rejectAudit(String preRegisterId, String reason);

    /**
     * <p>
     * 通过申请
     * </p>
     *
     * @param carPreRegisterAuditVo 包含车辆预登记ID的车辆登记信息
     * @author: 王良俊
     */
    R passAudit(ProjectCarPreRegisterAuditVo carPreRegisterAuditVo);

    /**
     * <p>
     * 获取车辆预登记信息(审核用)
     * </p>
     *
     * @param preRegId 车辆预登记ID
     * @author: 王良俊
     */
    R getObj(String preRegId);

    /**
     * <p>
     * 获取车辆预登记信息(查看用-已审核通过)
     * </p>
     *
     * @param preRegId 车辆预登记ID
     * @author: 王良俊
     */
    R getPreRegisterInfo(String preRegId);

    /**
    * <p>
    * 检查车牌号是否已有未通过申请
    * </p>
    *
    * @param plateNumber 车牌号
    * @author: 王良俊
    */
    String checkHasBeenApplied(String plateNumber);


    Integer countByOff();
}
