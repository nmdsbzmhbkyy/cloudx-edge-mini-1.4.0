package com.aurine.cloudx.open.origin.service.impl;

import cn.hutool.core.codec.Base64;
import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.symmetric.AES;
import com.aurine.cloudx.common.core.exception.BaseException;
import com.aurine.cloudx.common.core.exception.SystemErrorType;
import com.aurine.cloudx.common.core.util.StringUtil;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.open.origin.mapper.ProjectConfigMapper;
import com.aurine.cloudx.open.origin.entity.ProjectConfig;
import com.aurine.cloudx.open.origin.service.ProjectConfigService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.admin.api.dto.CxUserDTO;
import com.pig4cloud.pigx.admin.api.dto.UserInfo;
import com.pig4cloud.pigx.admin.api.entity.SysRole;
import com.pig4cloud.pigx.admin.api.feign.RemoteRoleService;
import com.pig4cloud.pigx.admin.api.feign.RemoteUserService;
import com.pig4cloud.pigx.common.core.constant.SecurityConstants;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.security.util.SecurityUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 项目参数设置
 *
 * @author guhl.@aurine.cn
 * @date 2020-07-10 10:06:39
 */
@Service
public class ProjectConfigServiceImpl extends ServiceImpl<ProjectConfigMapper, ProjectConfig> implements ProjectConfigService {
    @Resource
    private RemoteUserService userService;
    @Resource
    private RemoteRoleService roleService;

    private static final PasswordEncoder ENCODER = new BCryptPasswordEncoder();

    private static final String PASSWORD = "password";
    private static final String KEY_ALGORITHM = "AES";
    private static final String encodeKey = "pigxpigxpigxpigx";


    @Override
    public ProjectConfig getConfig() {
        ProjectConfig projectConfig = super.getOne(Wrappers.lambdaQuery(ProjectConfig.class)
                .eq(ProjectConfig::getProjectId, ProjectContextHolder.getProjectId()).last("LIMIT 1"));
        if (projectConfig == null) {
            baseMapper.insert(new ProjectConfig());
        }
        return super.getOne(Wrappers.lambdaQuery(ProjectConfig.class)
                .eq(ProjectConfig::getProjectId, ProjectContextHolder.getProjectId()).last("LIMIT 1"));
    }

    @Override
    public void initDefaultData(Integer projectId, Integer tenantId) {
        ProjectConfig projectConfig = new ProjectConfig();
        projectConfig.setProjectId(projectId);
        projectConfig.setVisitorAudit("1");
        projectConfig.setAlarmTimeLimit(30);
        projectConfig.setAuthAudit("2");
        projectConfig.setServiceInitalStatus("1");
        projectConfig.setFloorNoLen(2);
        projectConfig.setOperator(SecurityUtils.getUser().getId());
        baseMapper.init(projectConfig);
    }

    @Override
    public ProjectConfig getByProjectId(Integer projectId) {
        return baseMapper.getByProjectId(projectId);
    }

    @Override
    public void updateServiceExpTime(Integer projectId, String serviceExpTime) {
        baseMapper.updateServiceExpTime(projectId, serviceExpTime);
    }

    @Override
    public boolean isEnableMultiCarsPerPlace(Integer projectId) {
        return this.count(new QueryWrapper<ProjectConfig>().lambda().eq(ProjectConfig::getProjectId, projectId).eq(ProjectConfig::getMultiCarsPerPlace, 1)) > 0;
    }

    @Override
    public void updateAliProjectCode(Integer projectId, String aliProjectCode) {
        baseMapper.updateAliProjectCode(projectId,aliProjectCode);
    }

    @Override
    public void updateTotalMonitorDevNo(Integer projectId, Integer totalMonitorDevNo) {
        baseMapper.updateTotalMonitorDevNo(projectId,totalMonitorDevNo);
    }

    @Override
    public void updateOpen(ProjectConfig projectConfig) {
        // 获取项目id
        Integer id = ProjectContextHolder.getProjectId();

        ProjectConfig config = super.getOne(Wrappers.lambdaQuery(ProjectConfig.class)
                .eq(ProjectConfig::getProjectId, id).last("LIMIT 1"));

        String username = config.getClientId();

        // 验证用户信息
        R info = userService.info(projectConfig.getClientId(), SecurityConstants.FROM_IN);

        if (info.getCode() == 1 || info.getData() == null) {
            throw new BaseException(SystemErrorType.ARGUMENT_INVALID, "clientId 关联的用户不存在");
        }

        // 密码校验
        String clientId = projectConfig.getClientId();
        String clientSecret = projectConfig.getClientSecret();
        String password = decode(clientSecret);

        UserInfo userInfo = (UserInfo) info.getData();

        if (ENCODER.matches(password, userInfo.getSysUser().getPassword())) {
            throw new BaseException(SystemErrorType.ARGUMENT_INVALID, "clientId和clientSecret 关联的失败");
        }

        // 更新数据
        baseMapper.updateOpen(id, clientId, clientSecret);

        // 获取角色列表
        R roles = roleService.getByDeptId(id);
        List<SysRole> roleList = (List<SysRole>) roles.getData();
        SysRole mr = roleList.get(0);

        // 删除旧用户角色
        if (StringUtil.isNotEmpty(username) && !username.equals(clientId)) {
            userService.deleteRole(username, mr.getRoleId(), SecurityConstants.FROM_IN);
        }

        // 更新用户角色
        if (StringUtil.isNotEmpty(clientId) && StringUtil.isNotEmpty(clientSecret) && !username.equals(clientId)) {
            CxUserDTO userDTO = new CxUserDTO();
            userDTO.setUsername(clientId);
            userDTO.setPassword(password);
            userDTO.setNewRoleId(mr.getRoleId());

            // 获取部门角色
            userService.saveUserRoleWithFromTo(userDTO, SecurityConstants.FROM_IN);
        }
    }

    private String decode(String r) {
        String password = "";

        try {
            // 构建前端对应解密AES 因子
            AES aes = new AES(Mode.CBC, Padding.NoPadding,
                    new SecretKeySpec(encodeKey.getBytes(), KEY_ALGORITHM),
                    new IvParameterSpec(encodeKey.getBytes()));

            // 获取请求密码并解密
            byte[] result = aes.decrypt(Base64.decode(r.getBytes(StandardCharsets.UTF_8)));
            password = new String(result, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new BaseException(SystemErrorType.ARGUMENT_FORMAT_INVALID, "clientSecret参数异常", e);
        }

        return password;
    }
}
