

package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.entity.ProjectPersonDevice;
import com.aurine.cloudx.estate.vo.ProjectStaffDeviceRecordVo;
import com.aurine.cloudx.estate.vo.ProjectStaffDeviceSearchConditionVo;
import com.aurine.cloudx.estate.vo.ProjectStaffDeviceVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>员工设备权限 代理接口</p>
 *
 * @ClassName: ProjectProprietorDeviceProxyService
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020/5/22 8:40
 * @Copyright:
 */
public interface ProjectStaffDeviceProxyService extends IService<ProjectPersonDevice> {


    /**
     * 获取员工权限配置
     *
     * @param personId
     * @return
     */
    ProjectStaffDeviceVo getVo(String personId);


    /**
     * 员工门禁权限查询
     *
     * @param page
     * @param searchCondition
     * @return
     */
    IPage<ProjectStaffDeviceRecordVo> findPage(IPage<ProjectStaffDeviceRecordVo> page, ProjectStaffDeviceSearchConditionVo searchCondition);



}
