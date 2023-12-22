package com.aurine.cloudx.estate.service.adapter.adapter;

import com.aurine.cloudx.estate.entity.ProjectFaceResources;
import com.aurine.cloudx.estate.service.ProjectFaceResourcesService;
import com.aurine.cloudx.estate.service.adapter.AbstractProjectFaceResourcesService;
import com.aurine.cloudx.estate.util.DockModuleConfigUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 用于根据配置适配合适的业务实现，并允许多个业务实现顺序执行
 *
 * @description:
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-12-14
 * @Copyright:
 */
@Service("adapterWebProjectFaceResourcesServiceImpl")
public class AdapterProjectFaceResourcesServiceImpl extends AbstractProjectFaceResourcesService {
    @Resource
    ProjectFaceResourcesService wr20ProjectFaceResourcesServiceImplV1;
    @Resource
    DockModuleConfigUtil dockModuleConfigUtil;


    /**
     * 保存面部
     *
     * @param faceResources
     * @return
     * @author: 王伟
     */
    @Override
    public boolean saveFace(ProjectFaceResources faceResources){
        //根据配置 调用对应实现类
        if (dockModuleConfigUtil.isWr20()) {
            return wr20ProjectFaceResourcesServiceImplV1.saveFace(faceResources);
        } else {

        }

        return super.saveFace(faceResources);
    }

    /**
     * 删除人脸
     *
     * @param faceId
     * @return
     */
    @Override
    public boolean removeFace(String faceId) {

        //根据配置 调用对应实现类
        if (dockModuleConfigUtil.isWr20()) {
            return wr20ProjectFaceResourcesServiceImplV1.removeFace(faceId);
        } else {

        }

        return super.removeFace(faceId);
    }

}
