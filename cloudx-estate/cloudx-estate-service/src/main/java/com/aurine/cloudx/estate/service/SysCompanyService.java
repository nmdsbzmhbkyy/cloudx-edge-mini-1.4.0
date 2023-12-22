package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.entity.SysCompany;
import com.aurine.cloudx.estate.vo.SysCompanyFormVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 集团管理
 *
 * @author xull@aurine.cn
 * @date 2020-04-29 16:23:11
 */
public interface SysCompanyService extends IService<SysCompany> {

    /**
     * 分页查询集团
     *
     * @param page
     * @param sysCompany
     * @return
     */
    Page<SysCompany> pageCompany(Page page, SysCompany sysCompany);

    /**
     * 保存集团
     *
     * @param sysCompany
     * @return
     */
    Integer saveReturnId(SysCompanyFormVo sysCompany);

    /**
     * 更新集团集团
     *
     * @param sysCompanyFormVo
     * @return
     */
    boolean updateCompanyAndUser(SysCompanyFormVo sysCompanyFormVo);

    /**
     * 根据项目或者项目组获取父级集团信息
     * @param id
     * @return
     */
    List<SysCompany> findByGroupOrProjectId(Integer id);
}
