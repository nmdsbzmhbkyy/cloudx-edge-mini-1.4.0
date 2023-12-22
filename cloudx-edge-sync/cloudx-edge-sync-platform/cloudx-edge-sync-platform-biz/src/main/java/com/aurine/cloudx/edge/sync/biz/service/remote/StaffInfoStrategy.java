package com.aurine.cloudx.edge.sync.biz.service.remote;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.edge.sync.common.config.BaseStrategy;
import com.aurine.cloudx.edge.sync.common.config.BaseStrategyFactory;
import com.aurine.cloudx.edge.sync.common.entity.vo.OpenRespVo;
import com.aurine.cloudx.open.api.inner.feign.RemoteMetaStaffInfoService;
import com.aurine.cloudx.open.api.feign.RemoteOpenSysDeptService;
import com.aurine.cloudx.open.api.feign.RemoteOpenSysRoleService;
import com.aurine.cloudx.open.common.core.constant.enums.ServiceNameEnum;
import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.common.entity.vo.SysDeptVo;
import com.aurine.cloudx.open.common.entity.vo.SysRoleVo;
import com.aurine.cloudx.open.origin.entity.ProjectStaff;
import com.pig4cloud.pigx.common.core.util.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author: wrm
 * @Date: 2022/02/11 10:17
 * @Package: com.aurine.cloudx.edge.sync.biz.service.remote
 * @Version: 1.0
 * @Remarks: 员工信息管理
 **/
@Slf4j
@Component
public class StaffInfoStrategy implements BaseStrategy {

    @Resource
    private RemoteMetaStaffInfoService remoteMetaStaffInfoService;

    @Resource
    private RemoteOpenSysRoleService remoteOpenSysRoleService;

    @Resource
    private RemoteOpenSysDeptService remoteOpenSysDeptService;

    @Override
    public OpenRespVo doAdd(OpenApiModel<JSONObject> requestObj) {
        ProjectStaff projectStaff = getProjectStaff(requestObj);
        log.info("intoCloud -> openApi add req= {}", JSON.toJSONString(requestObj));
        OpenApiModel openApiModel = new OpenApiModel();
        openApiModel.setData(projectStaff);
        openApiModel.setHeader(requestObj.getHeader());
        R r = remoteMetaStaffInfoService.save(openApiModel);
        log.info("intoCloud -> openApi add res = {}", JSON.toJSONString(r));
        if (r.getCode() != 0) {
            return new OpenRespVo(null, r);
        }
        JSONObject object = JSONObject.parseObject(JSONObject.toJSONString(r.getData()));
        String id = object.getString("staffId");
        return new OpenRespVo(id, r);
    }

    @Override
    public OpenRespVo doUpdate(OpenApiModel<JSONObject> requestObj, String uuid) {
        ProjectStaff projectStaff = getProjectStaff(requestObj);
        log.info("intoCloud -> openApi update req= {}", JSON.toJSONString(requestObj));
        JSONObject data = requestObj.getData();
        data.put("staffId", uuid);
        OpenApiModel openApiModel = new OpenApiModel();
        openApiModel.setData(projectStaff);
        openApiModel.setHeader(requestObj.getHeader());
        R r = remoteMetaStaffInfoService.update(openApiModel);
        log.info("intoCloud -> openApi update res = {}", JSON.toJSONString(r));
        return new OpenRespVo(null, r);
    }

    @Override
    public OpenRespVo doDelete(String appId, String projectUUID, Integer tenantId, String uuid) {
        log.info("intoCloud -> openApi delete req= {}", uuid);
        R r = remoteMetaStaffInfoService.delete(appId, projectUUID, tenantId, uuid);
        log.info("intoCloud -> openApi delete res = {}", JSON.toJSONString(r));
        return new OpenRespVo(null, r);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        BaseStrategyFactory.registerStrategy(ServiceNameEnum.STAFF_INFO.name, this);
    }

    /**
     * 处理员工信息
     *
     * @param requestObj
     * @return
     */
    private ProjectStaff getProjectStaff(OpenApiModel<JSONObject> requestObj) {
        ProjectStaff projectStaff = JSONObject.toJavaObject(requestObj.getData(), ProjectStaff.class);
        R<SysRoleVo> sysRoleVo = remoteOpenSysRoleService.getFirstByProject(requestObj.getHeader().getAppId(), requestObj.getHeader().getProjectUUID(), requestObj.getHeader().getTenantId());
        R<SysDeptVo> sysDeptVo = remoteOpenSysDeptService.getFirstByProject(requestObj.getHeader().getAppId(), requestObj.getHeader().getProjectUUID(), requestObj.getHeader().getTenantId());

        projectStaff.setDepartmentId(sysDeptVo.getData() != null ? sysDeptVo.getData().getDeptId() : null);
        projectStaff.setRoleId(sysRoleVo.getData() != null ? sysRoleVo.getData().getRoleId() : null);
        projectStaff.setUserId(null);
        return projectStaff;
    }
}
