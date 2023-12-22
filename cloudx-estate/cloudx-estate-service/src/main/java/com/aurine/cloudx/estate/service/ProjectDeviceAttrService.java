

package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.vo.ProjectDeviceAttrFormVo;
import com.aurine.cloudx.estate.vo.ProjectDeviceAttrListVo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aurine.cloudx.estate.entity.ProjectDeviceAttr;

import java.util.List;

/**
 * 设备拓展属性表
 *
 * @author xull@aurine.cn
 * @date 2020-07-03 15:18:23
 */
public interface ProjectDeviceAttrService extends IService<ProjectDeviceAttr> {

    List<ProjectDeviceAttrListVo> getDeviceAttrListVo(Integer projectId,String  type, String deviceId);

    boolean updateDeviceAttrList(ProjectDeviceAttrFormVo projectDeviceAttrFormVo);
}
