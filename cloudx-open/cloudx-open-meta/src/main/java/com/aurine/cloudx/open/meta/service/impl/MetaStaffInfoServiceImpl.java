package com.aurine.cloudx.open.meta.service.impl;

import com.aurine.cloudx.open.common.core.exception.CloudxOpenErrorEnum;
import com.aurine.cloudx.open.common.core.response.Result;
import com.aurine.cloudx.open.meta.service.MetaStaffInfoService;
import com.aurine.cloudx.open.origin.entity.ProjectStaff;
import com.aurine.cloudx.open.origin.service.ProjectStaffService;
import com.pig4cloud.pigx.admin.api.feign.RemoteUserService;
import com.pig4cloud.pigx.common.core.constant.CommonConstants;
import com.pig4cloud.pigx.common.core.constant.SecurityConstants;
import com.pig4cloud.pigx.common.core.util.R;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * open平台-员工信息管理实现
 *
 * @author : Qiu
 * @date : 2021 12 28 9:55
 */

@Service
@Slf4j
public class MetaStaffInfoServiceImpl implements MetaStaffInfoService {

    @Resource
    private ProjectStaffService projectStaffService;
    @Resource
    private RemoteUserService userRemote;

    @Override
    public R<ProjectStaff> save(ProjectStaff po) {
        boolean result = projectStaffService.saveOrUpdate(po);
        if (!result) return Result.fail(po, CloudxOpenErrorEnum.SYSTEM_ERROR);

        return R.ok(po);
    }

    @Override
    public R<ProjectStaff> update(ProjectStaff po) {
        po.setRoleId(null);
        boolean result = projectStaffService.saveOrUpdate(po);
        if (!result) return Result.fail(po, CloudxOpenErrorEnum.EMPTY_RESULT);


        return R.ok(po);
    }

    @Override
    @GlobalTransactional
    public R<Boolean> delete(String id) {
        ProjectStaff staff = projectStaffService.getById(id);
        if(staff == null) {
            return R.ok(true);
        }
        boolean result = false;
        if(staff.getUserId() == null){
            result =  projectStaffService.removeById(id);
        }else {
            R<Boolean> r = userRemote.removeUserRoleInner(staff.getUserId(), staff.getRoleId(), SecurityConstants.FROM_IN);
            log.info("删除pigx关系{}",r);

            if(r.getCode() == CommonConstants.SUCCESS) {
                result = projectStaffService.removeById(id);
            }
        }

        if (!result) return Result.fail(false, CloudxOpenErrorEnum.EMPTY_RESULT);

        return R.ok(true);
    }
}
