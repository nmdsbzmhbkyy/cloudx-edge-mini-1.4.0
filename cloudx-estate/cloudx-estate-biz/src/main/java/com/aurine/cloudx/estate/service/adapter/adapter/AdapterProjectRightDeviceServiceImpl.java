package com.aurine.cloudx.estate.service.adapter.adapter;

import com.aurine.cloudx.estate.entity.ProjectRightDevice;
import com.aurine.cloudx.estate.service.ProjectRightDeviceService;
import com.aurine.cloudx.estate.service.adapter.AbstractProjectRightDeviceService;
import com.aurine.cloudx.estate.util.DockModuleConfigUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * AWebProjectRightDeviceServiceImpl 适配器
 * 用于根据配置适配合适的业务实现，并允许多个业务实现顺序执行
 *
 * @description:
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-12-14
 * @Copyright:
 */
@Service("adapterWebProjectRightDeviceServiceImpl")
public class AdapterProjectRightDeviceServiceImpl extends AbstractProjectRightDeviceService {
    @Resource
    ProjectRightDeviceService wr20ProjectRightDeviceServiceImplV1;

    @Resource
    DockModuleConfigUtil dockModuleConfigUtil;

    /**
     * 执行下发凭证
     *
     * @param rightDeviceList
     * @return
     * @author: 王伟
     */
    @Override
    public boolean remoteInterfaceByDevices(List<ProjectRightDevice> rightDeviceList, boolean isAdd) {
        //获取配置信息

        //根据配置 调用对应实现类
        if (dockModuleConfigUtil.isWr20()) {
            return wr20ProjectRightDeviceServiceImplV1.remoteInterfaceByDevices(rightDeviceList, isAdd);
        } else {
            return super.remoteInterfaceByDevices(rightDeviceList, isAdd);
        }
    }


}
