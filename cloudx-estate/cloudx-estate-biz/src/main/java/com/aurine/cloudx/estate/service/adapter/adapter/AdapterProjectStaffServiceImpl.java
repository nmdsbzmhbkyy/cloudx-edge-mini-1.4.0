package com.aurine.cloudx.estate.service.adapter.adapter;

import com.aurine.cloudx.estate.dto.ProjectStaffDTO;
import com.aurine.cloudx.estate.service.ProjectStaffService;
import com.aurine.cloudx.estate.service.adapter.AbstractProjectStaffService;
import com.aurine.cloudx.estate.util.DockModuleConfigUtil;
import com.codingapi.tx.annotation.TxTransaction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * AdapterWebProjectCardServiceImpl 适配器
 * 用于根据配置适配合适的业务实现，并允许多个业务实现顺序执行
 *
 * @description:
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-12-14
 * @Copyright:
 */
@Service("abstractWebProjectStaffService")
public class AdapterProjectStaffServiceImpl extends AbstractProjectStaffService {
    @Resource
    ProjectStaffService wr20ProjectStaffServiceImplV1;
    @Resource
    DockModuleConfigUtil dockModuleConfigUtil;


    /**
     * 保存员工
     *
     * @param entity
     * @return
     * @author: 王伟
     */
    @Override
    public boolean saveStaff(ProjectStaffDTO entity) {
        //根据配置 调用对应实现类
        if (dockModuleConfigUtil.isWr20()) {
            return wr20ProjectStaffServiceImplV1.saveStaff(entity);
        } else {

        }
        return super.saveStaff(entity);
    }

    /**
     * 修改员工
     *
     * @param entity
     * @return
     */
    @Override
    @TxTransaction(isStart = true)
    @Transactional(rollbackFor = Exception.class)
    public boolean editStaff(ProjectStaffDTO entity) {
        //根据配置 调用对应实现类
        if (dockModuleConfigUtil.isWr20()) {
//            return wr20WebProjectStaffServiceImplV1.editStaff(entity);
        } else {

        }
        return super.editStaff(entity);
    }


    /**
     * 删除员工
     * @param id
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @TxTransaction(isStart = true)
    public boolean removeStaff(String id) {
        //根据配置 调用对应实现类
        if (dockModuleConfigUtil.isWr20()) {
            return wr20ProjectStaffServiceImplV1.removeStaff(id);
        } else {

        }
        return super.removeStaff(id);
    }


}
