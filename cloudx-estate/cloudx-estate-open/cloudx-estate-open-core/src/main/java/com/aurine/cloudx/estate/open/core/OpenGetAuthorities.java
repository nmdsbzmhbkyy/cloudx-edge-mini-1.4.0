package com.aurine.cloudx.estate.open.core;

import cn.hutool.core.util.ArrayUtil;
import com.aurine.open.admin.dto.UserInfo;
import com.aurine.open.admin.feign.RemoteOpenApiService;
import com.pig4cloud.pigx.admin.api.feign.RemoteUserService;
import com.pig4cloud.pigx.admin.api.vo.UserDeptVo;
import com.pig4cloud.pigx.common.core.constant.CommonConstants;
import com.pig4cloud.pigx.common.core.constant.SecurityConstants;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.security.component.GetAuthorities;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;

@Primary
@Component
public class OpenGetAuthorities implements GetAuthorities {
    @Resource
    private RemoteOpenApiService remoteOpenApiService;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(Map<String, ?> map) {
        Map<String, ?> userInfo = (Map<String, ?>) map.get("user_info");
        String license = (String) map.get("license");

        R<UserInfo> r = remoteOpenApiService.getPermissions((Integer) userInfo.get("id"), userInfo.get("id") + "_" + license, SecurityConstants.FROM_IN);

        if (r.getCode() == CommonConstants.SUCCESS && r.getData() != null) {
            UserInfo info = r.getData();

            Set<String> dbAuthsSet = new HashSet<>();
            if (ArrayUtil.isNotEmpty(info.getPermissions())) {
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
