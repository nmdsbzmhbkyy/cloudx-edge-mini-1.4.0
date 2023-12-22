
package com.aurine.cloudx.estate.service.impl;

import com.aurine.cloudx.estate.entity.SysServiceDeviceClassify;
import com.aurine.cloudx.estate.mapper.SysServiceDeviceClassifyMapper;
import com.aurine.cloudx.estate.service.SysServiceDeviceClassifyService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 增值服务关联设施
 *
 * @author guhl@aurine.cn
 * @date 2020-06-04 11:17:42
 */
@Service
public class SysServiceDeviceClassifyServiceImpl extends ServiceImpl<SysServiceDeviceClassifyMapper, SysServiceDeviceClassify> implements SysServiceDeviceClassifyService {
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Boolean deleteByServiceDeviceClassifys(String  serviceId) {
        baseMapper.delete(Wrappers.lambdaUpdate(SysServiceDeviceClassify.class).eq(SysServiceDeviceClassify::getServiceId, serviceId));
        return true;
    }
}
