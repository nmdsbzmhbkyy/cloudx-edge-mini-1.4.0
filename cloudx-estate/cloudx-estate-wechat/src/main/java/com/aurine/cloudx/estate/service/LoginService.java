package com.aurine.cloudx.estate.service;

import com.pig4cloud.pigx.admin.api.vo.RegisterVo;
import com.pig4cloud.pigx.admin.api.vo.WeChatRegisterVo;
import com.pig4cloud.pigx.common.core.util.R;

/**
 * (LoginService)
 *
 * @author xull
 * @version 1.0.0
 * @date 2020/11/18 17:26
 */
public interface LoginService {
    /**
     *  微信注册或校验微信用户账号更新用户数据
     * @param weChatRegisterVo
     * @return
     */
    R<RegisterVo> register(WeChatRegisterVo weChatRegisterVo);

    /**
     * 账号有出入修改方法
     * @param registerVo
     * @return
     */
    R<Integer> change(RegisterVo registerVo);
}
