package com.aurine.cloudx.estate.service.adapter.adapter;

import com.aurine.cloudx.estate.constant.enums.AuditStatusEnum;
import com.aurine.cloudx.estate.service.ProjectVisitorService;
import com.aurine.cloudx.estate.service.adapter.AbstractProjectVisitorService;
import com.aurine.cloudx.estate.util.DockModuleConfigUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * AdapterWebProjectVisitorServiceImpl 适配器
 * 用于根据配置适配合适的业务实现，并允许多个业务实现顺序执行
 *
 * @description:
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-12-29 9:59
 * @Copyright:
 */
@Service("adapterProjectVisitorServiceImpl")
public class AdapterProjectVisitorServiceImpl extends AbstractProjectVisitorService {
    @Resource
    ProjectVisitorService wr20ProjectVisitorServiceImplV1;
    @Resource
    DockModuleConfigUtil dockModuleConfigUtil;


    /**
     * 通过授权
     *
     * @param visitId
     * @param auditStatus
     * @return
     */
    @Override
    public Boolean passAudit(String visitId, AuditStatusEnum auditStatus) {
        //根据配置 调用对应实现类
        if (dockModuleConfigUtil.isWr20()) {
            if (auditStatus == AuditStatusEnum.pass) {
                return wr20ProjectVisitorServiceImplV1.passAudit(visitId, auditStatus);
            }
        } else {

        }
        return super.passAudit(visitId, auditStatus);
    }

    /**
     * 迁离
     *
     * @param visitId
     * @return
     */
    @Override
    public boolean signOff(String visitId) {
        //根据配置 调用对应实现类
        if (dockModuleConfigUtil.isWr20()) {
            return wr20ProjectVisitorServiceImplV1.signOff(visitId);
        } else {

        }
        return super.signOff(visitId);
    }


}
