

package com.aurine.cloudx.open.origin.service;

import com.aurine.cloudx.open.origin.vo.ProjectDeviceAttrFormVo;
import com.aurine.cloudx.open.origin.vo.ProjectDeviceAttrListVo;
import com.aurine.cloudx.open.origin.entity.ProjectDeviceAttr;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 设备拓展属性表
 *
 * @author xull@aurine.cn
 * @date 2020-07-03 15:18:23
 */
public interface ProjectDeviceAttrService extends IService<ProjectDeviceAttr> {

    List<ProjectDeviceAttrListVo> getDeviceAttrListVo(Integer projectId, String type, String deviceId);

    boolean updateDeviceAttrList(ProjectDeviceAttrFormVo projectDeviceAttrFormVo);
}
