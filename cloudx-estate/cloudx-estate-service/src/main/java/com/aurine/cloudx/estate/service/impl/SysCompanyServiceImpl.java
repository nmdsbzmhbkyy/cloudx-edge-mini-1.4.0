
package com.aurine.cloudx.estate.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.aurine.cloudx.common.core.constant.enums.DeptTypeEnum;
import com.aurine.cloudx.estate.constant.SysDeptConstant;
import com.aurine.cloudx.estate.entity.SysCompany;
import com.aurine.cloudx.estate.mapper.SysCompanyMapper;
import com.aurine.cloudx.estate.service.SysCompanyService;
import com.aurine.cloudx.estate.service.SysDeptUserService;
import com.aurine.cloudx.estate.vo.SysCompanyFormVo;
import com.aurine.cloudx.estate.vo.SysUserVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.codingapi.tx.annotation.TxTransaction;
import com.pig4cloud.pigx.admin.api.entity.SysDept;
import com.pig4cloud.pigx.admin.api.entity.SysDeptRelation;
import com.pig4cloud.pigx.admin.api.entity.SysRole;
import com.pig4cloud.pigx.admin.api.feign.RemoteDeptService;
import com.pig4cloud.pigx.admin.api.feign.RemoteRoleService;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.data.enums.DataScopeTypeEnum;
import com.pig4cloud.pigx.common.security.util.SecurityUtils;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

/**
 * 集团管理
 *
 * @author xull@aurine.cn
 * @date 2020-04-29 16:23:11
 */
@Service
@AllArgsConstructor
public class SysCompanyServiceImpl extends ServiceImpl<SysCompanyMapper, SysCompany> implements SysCompanyService {

    private final RemoteDeptService remoteDeptService;
    private final RemoteRoleService remoteRoleService;
    private final SysDeptUserService sysDeptUserService;


    @Override
    @TxTransaction(isStart = true)
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Integer saveReturnId(SysCompanyFormVo sysCompanyFormVo) {

        SysCompany entity = new SysCompany();
        BeanUtils.copyProperties(sysCompanyFormVo, entity);
        SysDept sysDept = new SysDept();
        //设置集团默认父级部门为冠林云平台
        if (sysCompanyFormVo.getParentId() == null) {
            sysDept.setParentId(SysDeptConstant.AURINE_ID);
        } else {
            //如果parentId有值则传递
            sysDept.setParentId(sysCompanyFormVo.getParentId());
        }
        sysDept.setName(entity.getCompanyName());
        sysDept.setSort(SysDeptConstant.COMPANY_SORT);
        sysDept.setDeptTypeId(DeptTypeEnum.COMPANY.getId());
        sysDept.setDeptTypeName(DeptTypeEnum.COMPANY.getName());

        Integer deptId = null;
        Integer roleId = null;
        SysUserVo sysUserVo = sysCompanyFormVo.getUser();
        //新增pigx中的部门对应集团
        deptId = remoteDeptService.saveRetId(sysDept).getData();


        //设置集团id与pigx部门同步
        entity.setCompanyId(deptId);
        //设置操作人
        entity.setOperator(SecurityUtils.getUser().getId());

        //新增集团用户角色
        SysRole sysRole = new SysRole();
        sysRole.setDeptId(deptId);
        sysRole.setDsType(DataScopeTypeEnum.OWN_LEVEL.getType());
        sysRole.setRoleName(sysCompanyFormVo.getCompanyName() + SysDeptConstant.COMPANY_NAME);
        sysRole.setRoleCode(SysDeptConstant.COMPANY_CODE + RandomUtil.randomStringUpper(18).replaceAll("\\d+", ""));
        roleId = remoteRoleService.saveRetId(sysRole).getData();
        //更新角色菜单权限
        remoteRoleService.updateRoleMenuByRoleId(DeptTypeEnum.GROUP.getId(), roleId);
        sysUserVo.setDeptId(deptId);
        sysUserVo.setRoleId(roleId);

        //根据用户id是否为空判断是否存在,存在则更新不纯正就新增
        sysDeptUserService.addDeptUser(sysUserVo);
        //新增集团
        baseMapper.insert(entity);


        return entity.getCompanyId();
    }


    @Override
    @TxTransaction(isStart = true)
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean updateCompanyAndUser(SysCompanyFormVo sysCompanyFormVo) {
        SysCompany entity = new SysCompany();
        BeanUtils.copyProperties(sysCompanyFormVo, entity);
        R<SysDept> resultData = remoteDeptService.getById(entity.getCompanyId());
        SysDept sysDept = resultData.getData();
        sysDept.setName(entity.getCompanyName());
        remoteDeptService.update(sysDept);
        if (sysCompanyFormVo.getUserList().size() > 0) {
            sysCompanyFormVo.getUserList().forEach(sysDeptUserService::updateDeptUser);
        }
        return super.updateById(entity);
    }

    @Override
    public List<SysCompany> findByGroupOrProjectId(Integer id) {
        return baseMapper.findByGroupOrProjectId(id);
    }

    @Override
    public boolean removeById(Serializable id) {
        R<List<SysDeptRelation>> descendantList = remoteDeptService.getDescendantList((Integer) id);
        if (descendantList.getData() != null && descendantList.getData().size() > 1) {
            throw new RuntimeException("该集团底下存在项目组,无法删除");
        }
        remoteRoleService.removeByDeptId((Integer) id);
        remoteDeptService.removeById((Integer) id);
        return super.removeById(id);
    }

    @Override
    public  Page<SysCompany> pageCompany(Page page, SysCompany sysCompany) {
        return baseMapper.pageCompany(page, sysCompany);
    }


}
