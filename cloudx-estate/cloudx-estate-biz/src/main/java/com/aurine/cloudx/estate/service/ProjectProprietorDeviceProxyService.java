package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.entity.ProjectPersonDevice;
import com.aurine.cloudx.estate.entity.ProjectPersonLiftRel;
import com.aurine.cloudx.estate.vo.ProjectProprietorDeviceRecordVo;
import com.aurine.cloudx.estate.vo.ProjectProprietorDeviceSearchCondition;
import com.aurine.cloudx.estate.vo.ProjectProprietorDeviceVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>住户设备权限 代理接口</p>
 *
 * @ClassName: ProjectProprietorDeviceProxyService
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020/5/22 8:40
 * @Copyright:
 */
public interface ProjectProprietorDeviceProxyService extends IService<ProjectPersonDevice> {

    /**
     * 保存住户权限配置
     *
     * @param projectProprietorDeviceVo
     * @return
     */
    boolean save(ProjectProprietorDeviceVo projectProprietorDeviceVo);


    @Transactional(rollbackFor = Exception.class)
    boolean saveWithLift(ProjectProprietorDeviceVo projectProprietorDeviceVo);

    /**
     * 获取住户权限配置
     *
     * @param personId
     * @return
     */
    ProjectProprietorDeviceVo getVo(String personId);


    List<ProjectPersonLiftRel> getPersonLift(String personId);

    /**
     * 关闭通行权限
     *
     * @param personId 人员ID
     * @return
     */
    boolean disablePassRight(String personId);
    /**
     * 开启通行权限
     *
     * @param personId
     * @return
     */
    boolean enablePassRight(String personId);


    /**
     * 住户门禁权限查询
     *
     * @param page
     * @param searchCondition
     * @return
     */
    IPage<ProjectProprietorDeviceRecordVo> findPage(IPage<ProjectProprietorDeviceRecordVo> page, ProjectProprietorDeviceSearchCondition searchCondition);


//
//    /**
//     * 根据住户ID保存住户权限配置,用于住户通行凭证的重置
//     *
//     * @param personId
//     * @return
//     */
//    boolean saveByPersonId(String personId);
}
