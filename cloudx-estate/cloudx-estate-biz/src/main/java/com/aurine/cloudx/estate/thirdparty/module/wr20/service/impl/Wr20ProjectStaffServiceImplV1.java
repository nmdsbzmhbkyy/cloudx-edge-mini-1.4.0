package com.aurine.cloudx.estate.thirdparty.module.wr20.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.constant.PersonConstant;
import com.aurine.cloudx.estate.dto.ProjectStaffDTO;
import com.aurine.cloudx.estate.entity.ProjectStaff;
import com.aurine.cloudx.estate.service.ProjectPersonAttrService;
import com.aurine.cloudx.estate.service.ProjectStaffService;
import com.aurine.cloudx.estate.service.impl.ProjectStaffServiceImpl;
import com.aurine.cloudx.estate.thirdparty.business.platform.BusinessBaseService;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.PlatformEnum;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.VersionEnum;
import com.aurine.cloudx.estate.thirdparty.module.wr20.entity.dto.WR20WorkObj;
import com.aurine.cloudx.estate.thirdparty.module.wr20.entity.enums.WR20CredentialTypeEnum;
import com.aurine.cloudx.estate.thirdparty.module.wr20.entity.enums.WR20GenderEnum;
import com.codingapi.tx.annotation.TxTransaction;
import com.pig4cloud.pigx.admin.api.dto.CxUserDTO;
import com.pig4cloud.pigx.admin.api.feign.RemoteUserService;
import com.pig4cloud.pigx.common.core.constant.CommonConstants;
import com.pig4cloud.pigx.common.core.util.R;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.UUID;

/**
 * (WebProjectPersonDeviceServiceImpl)
 *
 * @author xull
 * @version 1.0.0
 * @date 2020/12/8 17:48
 */
@Service("wr20ProjectStaffServiceImplV1")
@Slf4j
public class Wr20ProjectStaffServiceImplV1 extends ProjectStaffServiceImpl implements ProjectStaffService, BusinessBaseService {

    @Resource
    private RemoteUserService userRemote;
    @Resource
    private ProjectPersonAttrService projectPersonAttrService;
    @Resource
    private WR20RemoteService wr20RemoteService;


    /**
     * 保存员工
     *
     * @param entity
     * @return
     */
    @Override
    @TxTransaction(isStart = true)
    @Transactional(rollbackFor = Exception.class)
    public boolean saveStaff(ProjectStaffDTO entity) {
        String staffId = UUID.randomUUID().toString().replaceAll("-", "");
        entity.setStaffId(staffId);
        log.info("[WR20] 开始保存员工:{}", entity);
        CxUserDTO user = parseUser(entity);

        // 如果用户不存在则新增用户，如果用户已存在则只更新用户信息
        if (entity.getUserId() == null) {
            user.setPassword(PersonConstant.PASSWORD);
            user.setDeptId(entity.getDepartmentId());
            user.setUsername(entity.getMobile());
            user.setPhone(entity.getMobile());

            R<Integer> r = userRemote.saveUserRole(user);

            if (r.getCode() == CommonConstants.SUCCESS) {
                entity.setUserId(r.getData());
                this.save(entity);
            }
        } else {
            try {
                user.setRoleId(user.getNewRoleId());
                R<Boolean> r = userRemote.editUserRole(user);

                if (r.getCode() == CommonConstants.SUCCESS) {
                    this.save(entity);
                }
            } catch (Exception e) {
                log.error("[WR20] 员工账号修改出现异常，通常是由于原员工删除后再添加导致，该异常可忽略");
                this.save(entity);
            }

        }


        //WR20存储员工
        WR20WorkObj workObj = new WR20WorkObj();
        workObj.setGuardID(-1L);
        workObj.setName(entity.getStaffName());
        workObj.setCredentialType(WR20CredentialTypeEnum.getByCloudCode(entity.getCredentialType()).wr20Code);
        workObj.setCredentialID(entity.getCredentialNo());
        workObj.setGender(WR20GenderEnum.getByCloudCode(entity.getSex()).wr20Code);
        workObj.setTelephone(entity.getMobile());

        wr20RemoteService.saveStaff(ProjectContextHolder.getProjectId(), JSON.parseObject(JSONObject.toJSONString(workObj)), staffId);
//        ProjectPersonAttrFormVo projectPersonAttrFormVo = new ProjectPersonAttrFormVo();
//        projectPersonAttrFormVo.setProjectPersonAttrList(entity.getProjectPersonAttrListVos());
//        projectPersonAttrFormVo.setPersonId(entity.getStaffId());
//        projectPersonAttrFormVo.setProjectId(entity.getProjectId());
//        projectPersonAttrFormVo.setType(PersonTypeEnum.STAFF.code);
//        return projectPersonAttrService.updatePersonAttrList(projectPersonAttrFormVo);

        return true;
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @TxTransaction(isStart = true)
    public boolean removeStaff(String id) {
        ProjectStaff staff = this.getById(id);
        log.info("[WR20] 开始删除员工:{}", staff);

        R<Boolean> r = userRemote.removeUserRole(staff.getUserId(), staff.getRoleId());
        if (r.getCode() == CommonConstants.SUCCESS) {
//            /**
//             * 删除员工也一并删除权限，凭证
//             * @author: 王伟
//             * @since : 2020-09-21
//             */
//            //迁出介质权限
//            projectRightDeviceService.removeCertmediaDeviceAuthorize(id);
//
//            //更新权限
//            projectPersonDeviceService.refreshByPersonId(id, PersonTypeEnum.STAFF);
            // 删除拓展属性值
            projectPersonAttrService.removePersonAttrList(id);

            //WR20删除员工

            if (StringUtils.isNotEmpty(staff.getStaffCode())) {
                WR20WorkObj workObj = new WR20WorkObj();
                workObj.setGuardID(Long.valueOf(staff.getStaffCode()));

                wr20RemoteService.removeStaff(ProjectContextHolder.getProjectId(), JSON.parseObject(JSONObject.toJSONString(workObj)), staff.getStaffId());
            } else {
                log.error("[WR20] 员工不存在第三方id，直接删除 {}", staff);
            }


            return this.removeById(id);
        } else {
            throw new RuntimeException(r.getMsg());
//            return false;
        }
    }

    @Override
    protected Class<ProjectStaff> currentModelClass() {
        return ProjectStaff.class;
    }

    /**
     * 获取版本
     *
     * @return
     */
    @Override
    public String getVersion() {
        return VersionEnum.V1.code;
    }

    /**
     * 获取平台类型
     *
     * @return
     */
    @Override
    public String getPlatform() {
        return PlatformEnum.BUSINESS_WR20.code;
    }
}
