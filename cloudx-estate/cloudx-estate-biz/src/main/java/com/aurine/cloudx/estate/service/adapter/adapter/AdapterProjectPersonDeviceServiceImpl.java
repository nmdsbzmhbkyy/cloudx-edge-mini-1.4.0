package com.aurine.cloudx.estate.service.adapter.adapter;

import com.aurine.cloudx.estate.dto.ProjectPersonDeviceDTO;
import com.aurine.cloudx.estate.service.ProjectPersonDeviceService;
import com.aurine.cloudx.estate.service.adapter.AbstractProjectPersonDeviceService;
import com.aurine.cloudx.estate.util.DockModuleConfigUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * WebProjectPersonDeviceServiceImpl 适配器
 * 用于根据配置适配合适的业务实现，并允许多个业务实现顺序执行
 *
 * @description:
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-12-15
 * @Copyright:
 */
@Service("adapterWebProjectPersonDeviceServiceImpl")
public class AdapterProjectPersonDeviceServiceImpl extends AbstractProjectPersonDeviceService {
    @Resource
    ProjectPersonDeviceService wr20ProjectPersonDeviceServiceImplV1;
    @Resource
    DockModuleConfigUtil dockModuleConfigUtil;

    /**
     * 保存权限变更
     * @param personDeviceDTO
     * @return
     * @author: 王伟
     */
    @Override
    public boolean savePersonDevice(ProjectPersonDeviceDTO personDeviceDTO) {
        //获取配置信息

        //根据配置 调用对应实现类
        if (dockModuleConfigUtil.isWr20()) {
            return wr20ProjectPersonDeviceServiceImplV1.savePersonDevice(personDeviceDTO);
        } else {

        }

        return super.savePersonDevice(personDeviceDTO);
    }

    /**
     * 开启通行权限
     *
     * @param personType
     * @param personId
     * @return
     */
    @Override
    public boolean enablePassRight(String personType, String personId) {
        //获取配置信息

        //根据配置 调用对应实现类
        if (dockModuleConfigUtil.isWr20()) {
            return wr20ProjectPersonDeviceServiceImplV1.enablePassRight(personType, personId);
        } else {

        }

        return super.enablePassRight(personType, personId);
    }

    /**
     * 关闭通行权限
     *
     * @param personType 人员类型
     * @param personId   人员ID
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean disablePassRight(String personType, String personId) {
        //获取配置信息

        //根据配置 调用对应实现类
        if (dockModuleConfigUtil.isWr20()) {
            return wr20ProjectPersonDeviceServiceImplV1.disablePassRight(personType, personId);
        } else {

        }

        return super.disablePassRight(personType, personId);
    }

}
