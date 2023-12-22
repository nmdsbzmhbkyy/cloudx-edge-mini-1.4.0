

package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.entity.ProjectPersonDevice;
import com.aurine.cloudx.estate.vo.ProjectProprietorDeviceRecordVo;
import com.aurine.cloudx.estate.vo.ProjectProprietorDeviceSearchCondition;
import com.aurine.cloudx.estate.vo.ProjectProprietorDeviceVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

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
