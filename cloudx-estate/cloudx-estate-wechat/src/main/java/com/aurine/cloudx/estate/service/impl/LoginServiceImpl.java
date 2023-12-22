package com.aurine.cloudx.estate.service.impl;

import cn.hutool.json.JSONObject;
import com.aurine.cloudx.estate.entity.ProjectPersonInfo;
import com.aurine.cloudx.estate.entity.ProjectStaff;
import com.aurine.cloudx.estate.entity.ProjectVisitor;
import com.aurine.cloudx.estate.service.LoginService;
import com.aurine.cloudx.estate.service.ProjectPersonInfoService;
import com.aurine.cloudx.estate.service.ProjectStaffService;
import com.aurine.cloudx.estate.service.impl.ProjectVisitorServiceImpl;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.codingapi.tx.annotation.TxTransaction;
import com.pig4cloud.pigx.admin.api.feign.RemoteSocialDetailsService;
import com.pig4cloud.pigx.admin.api.vo.ChangeInfoVo;
import com.pig4cloud.pigx.admin.api.vo.RegisterVo;
import com.pig4cloud.pigx.admin.api.vo.WeChatRegisterVo;
import com.pig4cloud.pigx.common.core.constant.SecurityConstants;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * (LoginServiceImpl)
 *
 * @author xull
 * @version 1.0.0
 * @date 2020/11/18 17:25
 */
@Service
@Primary
public class LoginServiceImpl implements LoginService {


    @Resource
    private RemoteSocialDetailsService remoteSocialDetailsService;

    @Resource
    private ProjectStaffService projectStaffService;

    @Resource
    private ProjectPersonInfoService projectPersonInfoService;

    @Resource
    private ProjectVisitorServiceImpl projectVisitorService;


    @Override
    @TxTransaction(isStart = true)
    @Transactional(rollbackFor = Exception.class)
    public R<RegisterVo> register(WeChatRegisterVo weChatRegisterVo) {
        R<RegisterVo> r = remoteSocialDetailsService.register(weChatRegisterVo, SecurityConstants.FROM_IN);
        //当注册成功根据手机号查询并变更ProjectPersonInfo表格，ProjectStaff表格因为已经设置userId故无需变更
        if (0 == r.getCode() && r.getData().getChange()) {
            JSONObject jsonObject = r.getData().getInfo();
            projectVisitorService.updateUserIdByPhone(jsonObject.getStr("phone"), jsonObject.getInt("userId"));
            projectPersonInfoService.updateUserIdByPhone(jsonObject.getStr("phone"), jsonObject.getInt("userId"));
        }
        return r;
    }

    @Override
    @TxTransaction(isStart = true)
    @Transactional(rollbackFor = Exception.class)
    public R<Integer> change(RegisterVo registerVo) {
        R<ChangeInfoVo> info = remoteSocialDetailsService.change(registerVo, SecurityConstants.FROM_IN);
        if (info.getCode() == 0) {
            if (registerVo.getChange()) {
                //如果选择true，则为修改当前用手机号
                projectPersonInfoService.updatePhoneByUserId(info.getData().getPhone(), info.getData().getUserId());
                //更新员工手机

                projectStaffService.updatePhoneByUserId(info.getData().getPhone(), info.getData().getUserId());
                //更新访客手机
                projectVisitorService.updatePhoneByUserId(info.getData().getPhone(), info.getData().getUserId());

            } else {
                //如果为false，则为变更当前手机号用户的userId
                projectPersonInfoService.updateUserIdByPhone(info.getData().getPhone(), info.getData().getUserId());

                //更新员工的userId
                projectStaffService.updateUserIdByPhone(info.getData().getPhone(), info.getData().getUserId());

                //更新访客的userId
                projectVisitorService.updateUserIdByPhone(info.getData().getPhone(), info.getData().getUserId());
            }

            return R.ok();
        } else {
            return R.failed("更新异常");
        }
    }

}
