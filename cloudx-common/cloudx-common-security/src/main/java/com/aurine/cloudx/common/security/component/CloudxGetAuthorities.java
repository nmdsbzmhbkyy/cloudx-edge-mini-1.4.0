package com.aurine.cloudx.common.security.component;

import cn.hutool.core.util.ArrayUtil;
import com.pig4cloud.pigx.admin.api.dto.UserInfo;
import com.pig4cloud.pigx.admin.api.feign.RemoteUserService;
import com.pig4cloud.pigx.common.core.constant.CommonConstants;
import com.pig4cloud.pigx.common.core.constant.SecurityConstants;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.security.component.GetAuthorities;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Priority;
import javax.annotation.Resource;
import java.util.*;

@Priority(5)
@Component
public class CloudxGetAuthorities implements GetAuthorities {
    @Resource
    private RemoteUserService userService;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(Map<String, ?> map) {
        Map<String, ?> userInfo = (Map<String, ?>) map.get("user_info");
        String license = (String) map.get("license");

        R r = userService.getPermissions((Integer) userInfo.get("id"), userInfo.get("id") + "_" + license, SecurityConstants.FROM_IN);

        if (r.getCode() == CommonConstants.SUCCESS && r.getData() != null) {
            UserInfo info = (UserInfo) r.getData();

            Set<String> dbAuthsSet = new HashSet<>();
            if (ArrayUtil.isNotEmpty(info.getRoles())) {
                // 获取角色
                Arrays.stream(info.getRoles()).forEach(roleId -> dbAuthsSet.add(SecurityConstants.ROLE + roleId));
                // 获取资源
                dbAuthsSet.addAll(Arrays.asList(info.getPermissions()));
            }
            Collection<? extends GrantedAuthority> authorities
                    = AuthorityUtils.createAuthorityList(dbAuthsSet.toArray(new String[0]));

            return authorities;
        }

        // 没有操作权限
        return new ArrayList<>();
    }
}
