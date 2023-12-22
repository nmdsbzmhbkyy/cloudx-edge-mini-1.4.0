package com.aurine.cloudx.estate.service.adapter.adapter;

import com.aurine.cloudx.estate.constant.enums.AuditStatusEnum;
import com.aurine.cloudx.estate.entity.ProjectHousePersonRel;
import com.aurine.cloudx.estate.service.ProjectHousePersonRelService;
import com.aurine.cloudx.estate.service.adapter.AbstractProjectHousePersonRelService;
import com.aurine.cloudx.estate.util.DockModuleConfigUtil;
import com.aurine.cloudx.estate.vo.ProjectHousePersonRelVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;

/**
 * WebProjectHousePersonRelServiceImpl 适配器
 * 用于根据配置适配合适的业务实现，并允许多个业务实现顺序执行
 *
 * @description:
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-12-14
 * @Copyright:
 */
@Service("adapterProjectHousePersonRelServiceImpl")
public class AdapterProjectHousePersonRelServiceImpl extends AbstractProjectHousePersonRelService {
    @Resource
    ProjectHousePersonRelService wr20ProjectHousePersonRelServiceImplV1;
    @Resource
    DockModuleConfigUtil dockModuleConfigUtil;

    /**
     * 审核
     *
     * @param projectHousePersonRel
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ProjectHousePersonRel verify(ProjectHousePersonRelVo projectHousePersonRel) {
        ProjectHousePersonRel resultHousePersonRel = super.verify(projectHousePersonRel);
        //根据配置 调用对应实现类
        if (dockModuleConfigUtil.isWr20()) {
            if (projectHousePersonRel.getAuditStatus().equals(AuditStatusEnum.pass.code)) {
                //审核成功，直接保存一份到wr20
                //WR20添加的住户默认设置状态为 添加中
                resultHousePersonRel.setAuditStatus(AuditStatusEnum.reissue.code);
                wr20ProjectHousePersonRelServiceImplV1.save(resultHousePersonRel);
            }

        } else {

        }

        return resultHousePersonRel;
    }

    /**
     * 保存住户信息
     *
     * @param projectHousePersonRel
     * @return
     */
    @Override
    public boolean save(ProjectHousePersonRel projectHousePersonRel) {
        //获取配置信息

        //根据配置 调用对应实现类
        if (dockModuleConfigUtil.isWr20()) {
            wr20ProjectHousePersonRelServiceImplV1.save(projectHousePersonRel);
            //WR20添加的住户默认设置状态为 添加中
            projectHousePersonRel.setAuditStatus(AuditStatusEnum.reissue.code);

            return super.save(projectHousePersonRel);
        } else {

        }

        return super.save(projectHousePersonRel);
    }


    /**
     * 根据 ID 删除
     *
     * @param id 主键ID
     */
    @Override
    public boolean removeById(Serializable id) {

        //根据配置 调用对应实现类
        if (dockModuleConfigUtil.isWr20()) {
            return wr20ProjectHousePersonRelServiceImplV1.removeById(id);
        } else {

        }
        return super.removeById(id);
    }


    /**
     * 重新迁入住户
     *
     * @param housePersonRelId
     * @return
     */
    @Override
    public boolean reSaveRel(String housePersonRelId) {

        //根据配置 调用对应实现类
        if (dockModuleConfigUtil.isWr20()) {
            return wr20ProjectHousePersonRelServiceImplV1.reSaveRel(housePersonRelId);
        } else {

        }
        return super.reSaveRel(housePersonRelId);
    }

}
