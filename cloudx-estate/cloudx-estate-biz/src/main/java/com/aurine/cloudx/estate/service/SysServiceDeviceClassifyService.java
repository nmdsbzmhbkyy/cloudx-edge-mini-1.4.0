

package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.entity.SysServiceDeviceClassify;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 增值服务关联设施
 *
 * @author guhl@aurine.cn
 * @date 2020-06-04 11:17:42
 */
public interface SysServiceDeviceClassifyService extends IService<SysServiceDeviceClassify> {
    /**
     * 删除当前增值服务所有关联设施
     * @param serviceId
     * @return
     */
    Boolean deleteByServiceDeviceClassifys(String serviceId);
}
