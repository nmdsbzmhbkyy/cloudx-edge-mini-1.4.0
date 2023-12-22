package com.aurine.cloudx.open.biz.service;

import com.aurine.cloudx.open.common.entity.vo.PasswordInfoVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;

/**
 * open平台-密码信息管理
 *
 * @author : Qiu
 * @date : 2021 12 28 9:49
 */

public interface OpenPasswordInfoService {

    /**
     * 根据ID获取密码信息
     *
     * @param id
     * @return
     */
    R<PasswordInfoVo> getById(String id);

    /**
     * 根据ID获取密码信息
     *
     * @param page
     * @param vo
     * @return
     */
    R<Page<PasswordInfoVo>> page(Page page, PasswordInfoVo vo);

    /**
     * 新增密码信息
     *
     * @param vo
     * @return
     */
    R<PasswordInfoVo> save(PasswordInfoVo vo);

    /**
     * 修改密码信息
     *
     * @param vo
     * @return
     */
    R<PasswordInfoVo> update(PasswordInfoVo vo);

    /**
     * 删除密码信息
     *
     * @param id
     * @return
     */
    R<Boolean> delete(String id);

}
